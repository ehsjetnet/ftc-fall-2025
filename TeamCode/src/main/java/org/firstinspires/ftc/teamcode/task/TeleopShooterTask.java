package org.firstinspires.ftc.teamcode.task;

import org.firstinspires.ftc.teamcode.task.Task;

/**
 * Controls the intake in teleop.
 */
public class TeleopShooterTask implements Task {

    private final boolean manualBankShot;

    private final boolean manualFarShot;

    private final boolean runFlywheelReverse;
    
    public TeleopShooterTask(boolean manualBankShot, boolean manualFarShot, boolean runFlywheelReverse) {
        this.manualBankShot = manualBankShot;
        this.manualFarShot = manualFarShot;
        this.runFlywheelReverse = runFlywheelReverse;
    }

    public final boolean getManualBankShot() {
        return manualBankShot;
    }

    public final boolean getMnaualFarShot(){
        return manualFarShot;
    }

    public final boolean getRunFlywheelMotorReverse() {
        return runFlywheelReverse;
    }

    /** 
     * Returns a velocity to directly give to the core hex motor.
     * 
     * @return a velocity to directly give to the core hex motor.
    */
    public final double getFlywheelVelocity() {
        if (manualBankShot) {
            return 1300;
        } else if (manualFarShot) {
            return 1900;
        } else if (runFlywheelReverse) {
            return -1300;
        } else {
            return 0;
        }
    }
}