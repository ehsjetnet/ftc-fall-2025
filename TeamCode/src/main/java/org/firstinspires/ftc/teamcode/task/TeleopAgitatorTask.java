package org.firstinspires.ftc.teamcode.task;

import org.firstinspires.ftc.teamcode.task.Task;

/**
 * Controls the intake in teleop.
 */
public class TeleopAgitatorTask implements Task {

    private final boolean runServoLeft;

    private final boolean runServoRight;

    public TeleopAgitatorTask(boolean runServoLeft, boolean runServoRight) {
        this.runServoLeft = runServoLeft;
        this.runServoRight = runServoRight;
    }

    public final boolean getRunServoLeft() {
        return runServoLeft;
    }

    public final boolean getRunServoRight() {
        return runServoRight;
    }

    /** 
     * Returns a power to directly give to the agitator actuator.
     * 
     * @return a power to directly give to the agitator actuator.
    */
    public final double getServoPower() {
        if (runServoLeft) {
            return 1;
        } else if (runServoRight) {
            return -1;
        } else {
            return 0;
        }
    }
}