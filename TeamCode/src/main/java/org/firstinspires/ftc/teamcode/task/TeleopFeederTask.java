package org.firstinspires.ftc.teamcode.task;

import org.firstinspires.ftc.teamcode.task.Task;

/**
 * Controls the intake in teleop.
 */
public class TeleopFeederTask implements Task {

    private final boolean runCoreHexMotorForward;

    private final boolean runCoreHexMotorReverse;
    
    public TeleopFeederTask(boolean runCoreHexMotorForward, boolean runCoreHexMotorReverse) {
        this.runCoreHexMotorForward = runCoreHexMotorForward;
        this.runCoreHexMotorReverse = runCoreHexMotorReverse;
    }

    public final boolean getRunCoreHexMotorFoward() {
        return runCoreHexMotorForward;
    }

    public final boolean getRunCoreHexMotorReverse() {
        return runCoreHexMotorReverse;
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