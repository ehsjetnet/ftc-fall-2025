package org.firstinspires.ftc.teamcode.layer.drive;

import java.util.Iterator;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Units;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.mechanism.Wheel;
import org.firstinspires.ftc.teamcode.task.AxialMovementTask;
import org.firstinspires.ftc.teamcode.task.TankDriveTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.TurnTask;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Drive layer for a two-wheel drive robot.
 */
public final class TwoWheelDrive implements Layer {
    /**
     * Name of the left drive motor in the robot configuration.
     */
    private static final String LEFT_DRIVE_MOTOR_NAME = "left_front_drive";

    /**
     * Name of the right drive motor in the robot configuration.
     */
    private static final String RIGHT_DRIVE_MOTOR_NAME = "right_front_drive";

    /**
     * The radius of the drive wheels in meters.
     */
    private static final double WHEEL_RADIUS = 0.42;

    /**
     * The effective gear ratio of the wheels to the motor drive shafts.
     * Expressed as wheelTeeth / hubGearTeeth, ignoring all intermediate meshing gears as they
     * should cancel out. Differently teethed gears driven by the same axle require more
     * consideration.
     */
    private static final double GEAR_RATIO = 1; // ticks per rot = 28, should get from config

    /**
     * Half the distance between the driving wheels in meters.
     */
    private static final double WHEEL_SPAN_RADIUS = Units.convert(15.0 / 2, Units.Distance.IN, Units.Distance.M);

    /**
     * Unitless, experimentally determined constant (ew) measuring lack of friction.
     * Measures lack of friction between wheels and floor material. Goal delta distances are directly
     * proportional to this.
     */
    private static final double SLIPPING_CONSTANT = 1;

    /**
     * The robot's left wheel.
     */
    private Wheel leftWheel;

    /**
     * The robot's right wheel.
     */
    private Wheel rightWheel;

    /**
     * The position of the left wheel at the start of the currently executing task, in meters.
     */
    private double leftStartPos;

    /**
     * The position of the right wheel at the start of the currently executing task, in meters.
     */
    private double rightStartPos;

    /**
     * The required delta position of the left wheel to complete the currently executing task, in
     * meters.
     */
    private double leftGoalDelta;

    /**
     * The required delta position of the right wheel to complete the currently executing task, in
     * meters.
     */
    private double rightGoalDelta;

    /**
     * Whether the currently executing task has completed.
     */
    private boolean currentTaskDone;

    /**
     * Constructs a TwoWheelDrive layer.
     */
    public TwoWheelDrive() { }

    @Override
    public void setup(LayerSetupInfo initInfo) {
        leftWheel = new Wheel(
            initInfo.getHardwareMap().get(DcMotor.class, LEFT_DRIVE_MOTOR_NAME),
            WHEEL_RADIUS
        );
        rightWheel = new Wheel(
            initInfo.getHardwareMap().get(DcMotor.class, RIGHT_DRIVE_MOTOR_NAME),
            WHEEL_RADIUS
        );

        leftStartPos = 0;
        rightStartPos = 0;
        leftGoalDelta = 0;
        rightGoalDelta = 0;
        currentTaskDone = true;
    }

    @Override
    public boolean isTaskDone() {
        return currentTaskDone;
    }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
        double leftDelta = leftWheel.getDistance() - leftStartPos;
        boolean leftDeltaSignsMatch = (leftDelta < 0) == (leftGoalDelta < 0);
        boolean leftGoalDeltaExceeded = Math.abs(leftDelta) >= Math.abs(leftGoalDelta);
        boolean leftDone = (leftDeltaSignsMatch && leftGoalDeltaExceeded) || leftGoalDelta == 0;

        double rightDelta = rightWheel.getDistance() - rightStartPos;
        boolean rightDeltaSignsMatch = (rightDelta < 0) == (rightGoalDelta < 0);
        boolean rightGoalDeltaExceeded = Math.abs(rightDelta) >= Math.abs(rightGoalDelta);
        boolean rightDone = (rightDeltaSignsMatch && rightGoalDeltaExceeded) || rightGoalDelta == 0;

        boolean isTeleopTask = leftGoalDelta == 0 && rightGoalDelta == 0;
        currentTaskDone = leftDone && rightDone;
        if (currentTaskDone && !isTeleopTask) {
            leftWheel.setVelocity(0);
            rightWheel.setVelocity(0);
        }
        // Adaptive velocity control goes here.
        return null;
    }

    @Override
    public void acceptTask(Task task) {
        if (task instanceof AxialMovementTask) {
            AxialMovementTask castedTask = (AxialMovementTask)task;
            leftGoalDelta = castedTask.getDistance() * GEAR_RATIO * SLIPPING_CONSTANT;
            rightGoalDelta = castedTask.getDistance() * GEAR_RATIO * SLIPPING_CONSTANT;
        } else if (task instanceof TurnTask) {
            TurnTask castedTask = (TurnTask)task;
            leftGoalDelta = -castedTask.getAngle() * WHEEL_SPAN_RADIUS * GEAR_RATIO * SLIPPING_CONSTANT;
            rightGoalDelta = castedTask.getAngle() * WHEEL_SPAN_RADIUS * GEAR_RATIO * SLIPPING_CONSTANT;
        } else if (task instanceof TankDriveTask) {
            // Teleop, set deltas to 0 to pretend we're done
            leftGoalDelta = 0;
            rightGoalDelta = 0;
            TankDriveTask castedTask = (TankDriveTask)task;
            double maxAbsPower = Math.max(
                Math.max(Math.abs(castedTask.getLeft()), Math.abs(castedTask.getRight())),
                1 // Clamp to 1 to prevent upscaling
            );
            leftWheel.setVelocity(castedTask.getLeft() / maxAbsPower);
            rightWheel.setVelocity(castedTask.getRight() / maxAbsPower);
            return;
        } else {
            throw new UnsupportedTaskException(this, task);
        }
        currentTaskDone = false;
        leftStartPos = leftWheel.getDistance();
        rightStartPos = rightWheel.getDistance();
        leftWheel.setVelocity(Math.signum(leftGoalDelta));
        rightWheel.setVelocity(Math.signum(rightGoalDelta));
    }
}
