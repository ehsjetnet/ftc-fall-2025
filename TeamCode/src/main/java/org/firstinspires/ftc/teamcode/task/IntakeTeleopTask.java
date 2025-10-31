package org.firstinspires.ftc.teamcode.task;

import org.firstinspires.ftc.teamcode.task.Task;

/**
 * Controls the intake in teleop.
 */
public class IntakeTeleopTask implements Task {

    private final boolean runServoLeft;

    private final boolean runServoRight;

    private final boolean runCoreHexMotorForward;

    private final boolean runCoreHexMotorReverse;
    
    public IntakeTeleopTask(boolean runServoLeft, boolean runServoRight, boolean runCoreHexMotorForward, boolean runCoreHexMotorReverse) {
        this.runServoLeft = runServoLeft;
        this.runServoRight = runServoRight;
        this.runCoreHexMotorForward = runCoreHexMotorForward;
        this.runCoreHexMotorReverse = runCoreHexMotorReverse;
    }

    public final boolean getRunServoLeft() {
        return runServoLeft;
    }

    public final boolean getRunServoRight() {
        return runServoRight;
    }

    public final boolean getRunCoreHexMotorFoward() {
        return runCoreHexMotorForward;
    }

    public final boolean getRunCoreHexMotorReverse() {
        return runCoreHexMotorReverse;
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

    /** 
     * Returns a power to directly give to the core hex motor.
     * 
     * @return a power to directly give to the core hex motor.
    */
    public final double getCoreHexPower() {
        if (runCoreHexMotorForward) {
            return 0.5;
        } else if (runCoreHexMotorReverse) {
            return -0.5;
        } else {
            return 0;
        }
    }
}