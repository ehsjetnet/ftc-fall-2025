package org.firstinspires.ftc.teamcode.task;

/**
 * Tells a robot supporting holonomic drive to move in a straight line without turning.
 */
public class LinearMovementTask implements Task {
    /**
     * The distance in meters to move in the forward direction.
     * Negative values indicate backward movement.
     */
    private double axial;

    /**
     * The distance in meters to move to the robot's rightward direction.
     * Negative values indicate leftward movement.
     */
    private double lateral;

    /**
     * Constructs a LinearMovementTask.
     *
     * @param axial - the distance to move in the forward direction
     * @param lateral - the distance to move in the rightward direction
     */
    public LinearMovementTask(double axial, double lateral) {
        this.axial = axial;
        this.lateral = lateral;
    }

    /**
     * The distance in meters to move in the forward direction.
     *
     * @return the distance in meters to move in the forward direction. Negative values indicate
     * backward movement.
     */
    public double getAxial() {
        return axial;
    }

    /**
     * Returns the distance in meters to move to the robot's rightward direction.
     *
     * @return the distance in meters to move to the robot's rightward direction. Negative values
     * indicate leftward movement.
     */
    public double getLateral() {
        return lateral;
    }
}
