package org.firstinspires.ftc.teamcode.task;

/**
 * Turns the robot in place.
 */
public class TurnTask implements Task {
    /**
     * The angle in radians to turn the robot counterclockwise.
     * Negative values indicate clockwise turns.
     */
    private double angle;

    /**
     * Constructs a TurnTask.
     *
     * @param angle the angle in radians to turn the robot counterclockwise.
     */
    public TurnTask(double angle) {
        this.angle = angle;
    }

    /**
     * Returns the angle in radians to turn the robot counterclockwise.
     *
     * @return the angle in radians to turn the robot counterclockwise. Negative values indicate
     * clockwise turns.
     */
    public double getAngle() {
        return angle;
    }
}
