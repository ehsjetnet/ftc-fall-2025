package org.firstinspires.ftc.teamcode.layer.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.firstinspires.ftc.teamcode.Units;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.localization.RobotLocalizer;
import org.firstinspires.ftc.teamcode.matrix.Mat2;
import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Vec2;
import org.firstinspires.ftc.teamcode.task.HolonomicDriveTask;
import org.firstinspires.ftc.teamcode.task.MoveToFieldTask;
import org.firstinspires.ftc.teamcode.task.Task;

/**
 * Computes holonomic drive powers to pathfind around obstacles to a goal transform.
 */
public final class PathfindingLayer implements Layer {
    /**
     * The distance in meters and offset in radians the robot's transform may be from the goal
     * before the robot is considered arrived.
     */
    private static final double GOAL_COMPLETE_EPSILON = 0.01;

    /**
     * The trajectory parameter increment to use between trajectories when numerically maximizing.
     */
    private static final double TRAJECTORY_SEARCH_INCREMENT = 0.01;

    /**
     * The coefficient of the target angle term in the objective function.
     */
    private static final double TARGET_ANGLE_COEFF = 0.5;

    /**
     * The coefficient of the clearance term in the objective function.
     */
    private static final double CLEARANCE_COEFF = 1.2;

    /**
     * The coefficient of the speed term in the objective function.
     */
    private static final double SPEED_COEFF = 0.5;

    /**
     * How often to recalculate the optimal trajectory in seconds.
     */
    private static final double CALCULATE_INTERVAL = 0.25;

    /**
     * A constant in the smoothing function used on the target angle term of the objective function.
     * $\frac{c}{k}$ = maximum score of target angle term.
     */
    private static final double TARGET_ANGLE_SMOOTHING_C = 1000;

    /**
     * A constant in the smoothing function used on the target angle term of the objective function.
     * Controls speed of decay as target angle moves away from 0.
     *
     * @see #TARGET_ANGLE_SMOOTHING_C
     */
    private static final double TARGET_ANGLE_SMOOTHING_K = 1;

    /**
     * The fraction of a trajectory that should be between successive points checked for the
     * clearance term.
     */
    private static final double CLEARANCE_STEP = 0.05;

    /**
     * The goal field transform the robot should pathfind to.
     */
    private Mat3 goal;

    /**
     * The list of obstacles to consider in the clearance objective function term and dynamic window
     * culling.
     */
    private List<Obstacle> obstacles;

    /**
     * The current trajectory the robot should take.
     */
    private Trajectory currentTrajectory;

    /**
     * The timestamp in nanoseconds the current trajectory was calculated.
     */
    private long lastCalcTime;

    /**
     * The field transform of the robot before the current trajectory was applied.
     */
    private Mat3 initialTransform;

    /**
     * The robot space velocity of the robot before the current trajectory was applied.
     */
    private Mat3 initialVelocity;

    /**
     * The localizer used to determine the robot's current field-space transform.
     */
    private RobotLocalizer localizer;

    /**
     * Constructs a PathfindingLayer.
     */
    public PathfindingLayer() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        obstacles = new ArrayList<>();
        localizer = setupInfo.getLocalizer();
    }

    @Override
    public boolean isTaskDone() {
        Mat3 delta = getTransform().inv().mul(goal);
        return delta.getTranslation().len() < GOAL_COMPLETE_EPSILON
            && delta.getDirection().getAngle() < GOAL_COMPLETE_EPSILON;
    }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
        long nowNano = System.nanoTime();
        if (nowNano - lastCalcTime > (long)Units.convert(CALCULATE_INTERVAL, Units.Time.SEC,
            Units.Time.NANO)) {
            calculatePath();
            lastCalcTime = nowNano;
        }
        return Collections.singleton((Task)(new HolonomicDriveTask(
            currentTrajectory.getAxial(),
            -currentTrajectory.getLateral(),
            currentTrajectory.getYaw()
        ))).iterator();
    }

    @Override
    public void acceptTask(Task task) {
        if (task instanceof MoveToFieldTask) {
            MoveToFieldTask castedTask = (MoveToFieldTask)task;
            goal = castedTask.getGoalTransform();
            lastCalcTime = System.nanoTime() - (long)Units.convert(CALCULATE_INTERVAL,
                Units.Time.SEC, Units.Time.NANO);
        }
    }

    /**
     * Computes a comparable score for a trajectory considering three factors.
     * This is the objective function the dynamic window approach optimizes.
     *
     * @param t - the trajectory to evaluate.
     * @return A comparable score for the trajectory.
     */
    private double evaluateTrajectory(Trajectory t) {
        double weightedTargetAngle = evaluateTargetAngle(t) * TARGET_ANGLE_COEFF;
        double weightedClearance = evaluateClearence(t) * CLEARANCE_COEFF;
        double weightedSpeed = evaluateSpeed(t) * SPEED_COEFF;
        return weightedTargetAngle + weightedClearance + weightedSpeed;
    }

    /**
     * Computes a comparable score for a trajectory on the grounds of final angle to target.
     *
     * @param t - the trajectory to evaluate.
     * @return A comparable score for the trajectory which is higher the more directly the robot
     * would face the goal at the end of the evaluated trajectory.
     */
    private double evaluateTargetAngle(Trajectory t) {
        Mat3 finalTransform = getTrajectoryTransform(t, 1);
        Vec2 finalDirection = finalTransform.getDirection();
        Vec2 finalDelta = finalTransform.getTranslation().mul(-1).add(goal.getTranslation());
        double angle = finalDirection.angleWith(finalDelta);
        return TARGET_ANGLE_SMOOTHING_C / (angle + TARGET_ANGLE_SMOOTHING_K);
    }

    /**
     * Computes a comparable score for a trajectory on the grounds of minimum clearance to
     * obstacles.
     *
     * @param t - the trajectory to evaluate.
     * @return A comparable score for the trajectory which is higher the greater the minimum
     * clearance the robot has to any obstacle at any point during the evaluated trajectory.
     */
    private double evaluateClearence(Trajectory t) {
        double minClearence = Double.POSITIVE_INFINITY;
        for (double frac = 0; frac < 1; frac += CLEARANCE_STEP) {
            Vec2 translation = getTrajectoryTransform(t, frac).getTranslation();
            for (Obstacle obstacle : obstacles) {
                double clearanceToObstacle = obstacle.getDistanceTo(translation);
                if (minClearence > clearanceToObstacle) {
                    minClearence = clearanceToObstacle;
                }
            }
        }
        return minClearence;
    }

    /**
     * Computes a comparable score for a trajectory on the grounds of final speed of the robot.
     *
     * @param t - the trajectory to evaluate.
     * @return A comparable score for the trajectory which is higher the greater the robot's
     * translational velocity at the end of the evaluated trajectory..
     */
    private double evaluateSpeed(Trajectory t) {
        return getTrajectoryVelocity(t, 1).getTranslation().len();
    }

    /**
     * Maximizes the objective function inside the dynamic window, setting
     * {@link #currentTrajectory} to the result.
     * Searches for the trajectory with the highest score from the objective function within a small
     * rectangular region. Trajectories outside the dynamic window are culled before calling the
     * objective function.
     */
    private void calculatePath() {
        // Keeps track of the best-scored trajectory and the score it had.
        Trajectory bestTrajectory = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        // Represents bounds of the search space.
        Trajectory maxBounds = new Trajectory(1, 1, 1);
        Trajectory minBounds = new Trajectory(-1, -1, -1);

        // Veeeeeeery slow search loop.
        for (double a = minBounds.getAxial(); a < maxBounds.getAxial(); a += TRAJECTORY_SEARCH_INCREMENT) {
            for (double l = minBounds.getLateral(); l < maxBounds.getLateral(); l += TRAJECTORY_SEARCH_INCREMENT) {
                for (double y = minBounds.getYaw(); y < maxBounds.getYaw(); y += TRAJECTORY_SEARCH_INCREMENT) {
                    Trajectory t = new Trajectory(a, l, y);
                    if (!checkDynamicWindow(t)) {
                        continue;
                    }
                    double score = evaluateTrajectory(t);
                    if (score > bestScore) {
                        bestScore = score;
                        bestTrajectory = t;
                    }
                }
            }
        }
        if (bestScore == Double.NEGATIVE_INFINITY) {
            // Dynamic window was empty of trajectories (all valid ones interesct obstacles)
            // Spin until the dynamic window isn't empty
            bestTrajectory = new Trajectory(0, 0, 1);
        }
        currentTrajectory = bestTrajectory;
    }

    /**
     * Checks if a trajectory is within the dynamic window.
     * During optimization, trajectories not within the dynamic window may be culled from the
     * search.
     *
     * @param t - the trajectory to check.
     * @return Whether the checked trajectory is achievable by the robot and the trajectory would
     * not cause the robot to crash into an obstacle.
     */
    private boolean checkDynamicWindow(Trajectory t) {
        for (double frac = 0; frac < 1; frac += CLEARANCE_STEP) {
            Vec2 translation = getTrajectoryTransform(t, frac).getTranslation();
            for (Obstacle obstacle : obstacles) {
                double clearanceToObstacle = obstacle.getDistanceTo(translation);
                if (clearanceToObstacle < 0) {
                    return false;
                }
            }
        }
        return Math.abs(t.getAxial()) + Math.abs(t.getLateral()) + Math.abs(t.getYaw()) < 1;
    }

    /**
     * Adds an obstacle to the internal list of obstacles.
     *
     * @param obstacle - the obstacle to add.
     */
    private void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    /**
     * Gets the predicted robot transform after applying the given set of accelerations (trajectory)
     * over the given fraction of the time interval.
     *
     * <p>To derive this hairy bit of math, first express the velocity over the course of the
     * trajectory in terms of the initial velocity, trajectory taken (expressed as a vector
     * \(\vec{z}=\langle z_a,z_l,z_\theta\rangle\) where the components represent constant axial,
     * lateral, and rotational accelerations), and duration spent on the trajectory:
     * \[\vec{v}(\vec{z} ,t_f) = \vec{v}(0, 0) + t_f\vec{z}\]
     * Adding the initial angle and integrating the \(\theta\) component with respect to \(t\) gives:
     * \[\theta(\vec{z}, t_f) = \theta(0,0) + v_\theta(0, 0) t_f + \frac{z_\theta t_f^2}{2}\]
     * which is reminiscient of a motion equation with constant acceleration. This result is
     * directly used to find the rotation component of the transform.
     *
     * <p>The constant axial and lateral accelerations \(z_a\) and \(z_l\) are easily converted to
     * accelerations along the field axes, though they vary with \(\theta\):
     * \[z_x = z_a\cos(\theta) - z_l\sin(\theta)\]
     * \[z_y = z_a\sin(\theta) + z_l\cos(\theta)\]
     * Now the field space coordinates of the robot may be found given using the initial position,
     * velocity, and integrating the corrosponding field axis accelerations:
     * \[x(\vec{z},t_f)=x(0,0)+\int_0^{t_f}\left(v_x(0,0)+\int_0^{t_f}z_xdt\right)dt\]
     * which, after substituting above values (\(\theta(\vec{z}, t)\) left unsubstituted for brevity)
     * and evaluating the definite integrals, yields:
     * \[x(\vec{z},t_f)=x(0,0)+v_x(0,0)t_f+t_f\frac{z_a(\sin(\theta(\vec{z},t_f))-\sin(\theta(0,0)))
     * +z_l(\cos(\theta(\vec{z},t_f))-\cos(\theta(0,0)))}{\sqrt{v_\theta^2(0,0)-2z_\theta\theta(0,0)
     * }}\]
     * \(y(\vec{z},t_f)\) is similarly found:
     * \[y(\vec{z},t_f)=y(0,0)+v_y(0,0)t_f+t_f\frac{-z_a(\cos(\theta(\vec{z},t_f))-\cos(\theta(0,0))
     * )+z_l(\sin(\theta(\vec{z},t_f))-\sin(\theta(0,0)))}{\sqrt{v_\theta^2(0,0)-2z_\theta\theta(0,0
     * )}}\]
     * These values are combined with the earlier result from \(\theta(\vec{z}, t_f)\) to produce
     * the returned transformation.
     *
     * @param t - the trajectory to simulate applying.
     * @param frac - the fraction of the trajectory along which the robot is to have traveled.
     * @return The predicted transform of the robot.
     */
    private Mat3 getTrajectoryTransform(Trajectory t, double frac) {
        double za = t.getAxial();
        double zl = t.getLateral();
        double zth = t.getYaw();
        double tf = frac * CALCULATE_INTERVAL;
        double x0 = initialTransform.getTranslation().getX();
        double vx0 = initialVelocity.getTranslation().getX();
        double y0 = initialTransform.getTranslation().getY();
        double vy0 = initialVelocity.getTranslation().getY();
        double vth0 = initialVelocity.getDirection().getAngle();
        double th0 = initialTransform.getDirection().getAngle();

        double x = x0 + vx0 * tf
            + tf * (
                za * (Math.sin(th0 + vth0 * tf + zth * tf * tf / 2) - Math.sin(th0))
                + zl * (Math.cos(th0 + vth0 * tf + zth * tf * tf / 2) - Math.cos(th0)))
            / Math.sqrt(vth0 * vth0 - 2 * zth * th0);
        double y = y0 + vy0 * tf
            + tf * (
                -za * (Math.cos(th0 + vth0 * tf + zth * tf * tf / 2) - Math.cos(th0))
                + zl * (Math.sin(th0 + vth0 * tf + zth * tf * tf / 2) - Math.sin(th0)))
            / Math.sqrt(vth0 * vth0 - 2 * zth * th0);
        double th = th0 + vth0 * tf + zth * tf * tf / 2;

        return Mat3.fromTransform(Mat2.fromAngle(th), new Vec2(x, y));
    }

    /**
     * Gets the predicted field space robot translation and rotational velocity after applying the
     * given set of accelerations (trajectory) over the given fraction of the time interval.
     *
     * @param t - the trajectory to simulate applying.
     * @param frac - the fraction of the trajectory along which the robot is to have traveled.
     * @return The predicted velocities of the robot, encoded as a transformation matrix where the
     * translation component is the translational velocity and the rotation component is the
     * rotational velocity.
     */
    private Mat3 getTrajectoryVelocity(Trajectory t, double frac) {
        double tf = frac * CALCULATE_INTERVAL;
        return Mat3.fromTransform(
            Mat2.fromAngle(t.getYaw() * tf + initialVelocity.getDirection().getAngle()),
            new Vec2(t.getAxial(), t.getLateral()).mul(tf).add(initialVelocity.getTranslation())
        );
    }

    /**
     * Gets the current field transform of the robot.
     *
     * @return The robot's current field space transform.
     */
    private Mat3 getTransform() {
        return localizer.resolveTransform();
    }

    /**
     * Gets the current field space velocity of the robot.
     *
     * @return The robot's current field space velocity encoded as a transformation matrix.
     */
    private Mat3 getVelocity() {
        // TODO: implement
        return new Mat3(
            1, 0, 0,
            0, 1, 0,
            0, 0, 1
        );
    }

    /**
     * A set of accelerations that the robot can take.
     * Alternately, a 3D point in the search space of trajectories.
     */
    private static class Trajectory {
        /**
         * The axial acceleration to apply to the robot.
         * Positive values indicate forward acceleration.
         */
        private double axial;

        /**
         * The lateral acceleration to apply to the robot.
         * Positive values indicate leftward acceleration.
         */
        private double lateral;

        /**
         * The rotational acceleration to apply to the robot.
         * Positive value indicate counterclockwise acceleration.
         */
        private double yaw;

        /**
         * Constructs a Trajectory.
         *
         * @param axial - the axial acceleration to apply to the robot.
         * @param lateral - the lateral acceleration to apply to the robot.
         * @param yaw - the rotational acceleration to apply to the robot.
         */
        Trajectory(double axial, double lateral, double yaw) {
            this.axial = axial;
            this.lateral = lateral;
            this.yaw = yaw;
        }

        /**
         * The axial acceleration to apply to the robot.
         *
         * @return The axial acceleration specified by this trajectory.
         */
        public double getAxial() {
            return axial;
        }

        /**
         * The lateral acceleration to apply to the robot.
         *
         * @return The lateral acceleration specified by this trajectory.
         */
        public double getLateral() {
            return lateral;
        }

        /**
         * The rotational acceleration to apply to the robot.
         *
         * @return The rotational acceleration specified by this trajectory.
         */
        public double getYaw() {
            return yaw;
        }
    }

    /**
     * Represents an impassable obstacle on the field.
     */
    private interface Obstacle {
        /**
         * Returns the signed distance in to the given field space.
         *
         * @param point - the field point to calculate the distance to, given in units of meters.
         * @return The signed distance to the closest surface of the obstacle in meters.
         */
        double getDistanceTo(Vec2 point);
    }

    /**
     * Represents an obstacle detected by a distance sensor.
     * These obstacles are represented by line segments that are oriented to face the robot and have
     * length dependent on the distance from the robot, both at the time of detection.
     */
    private static final class DynamicObstacle implements Obstacle {
        /**
         * The transform of the center of the segment, given in units of meters.
         */
        private Mat3 transform;

        /**
         * The length of the segment in meters.
         */
        private double size;

        /**
         * Constructs a DynamicObstacle.
         *
         * @param transform - the transform of the center of the segment, given in units of meters.
         * @param size - the length of the segment in meters.
         */
        DynamicObstacle(Mat3 transform, double size) {
            this.transform = transform;
            this.size = size;
        }

        @Override
        public double getDistanceTo(Vec2 point) {
            Vec2 delta = transform.getTranslation().add(point.mul(-1));
            double axialProj = transform.getDirection().proj(delta).len();
            double lateralProj = transform.getDirection().getPerpendicular().proj(delta).len();
            if (lateralProj < size / 2) {
                return axialProj;
            }
            Vec2 ep1 = transform.mul(new Vec2(0, size / 2));
            Vec2 ep2 = transform.mul(new Vec2(0, -size / 2));
            return Math.min(ep1.add(point.mul(-1)).len(), ep2.add(point.mul(-1)).len());
        }
    }

    /**
     * Represents a rectangular obstacle whose transform and size is definitely known.
     * The robot is prepopulated with these obstacles to represent impassible parts of the field.
     */
    private static class StaticObstacle implements Obstacle {
        /**
         * The transform of the center of the segment, given in units of meters.
         */
        private Mat3 transform;

        /**
         * The width and height of the rectangle contained in the x and y components of a 2D vector.
         */
        private Vec2 size;

        /**
         * Constructs a DynamicObstacle.
         *
         * @param transform - the transform of the center of the segment, given in units of meters.
         * @param size - the width and height of the rectangle expressed as a 2D vector.
         */
        StaticObstacle(Mat3 transform, Vec2 size) {
            this.transform = transform;
            this.size = size;
        }

        @Override
        public double getDistanceTo(Vec2 point) {
            Vec2 p = transform.inv().mul(point);
            double m1 = size.getY() / size.getX();
            double m2 = -m1;
            boolean useHeight = Math.signum(p.getY() - m1) == Math.signum(p.getY() - m2);
            double dim = useHeight ? size.getY() : size.getX();
            return p.len() - dim / (2 * Math.cos(p.getAngle()));
        }
    }
}
