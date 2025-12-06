package org.firstinspires.ftc.teamcode.task;

import org.firstinspires.ftc.teamcode.task.Task;

/**
 * Controls the intake in teleop.
 */
public class AutoShooterTask implements Task {

    private final boolean autoBankShot;

    private final boolean autoFarShot;
    
    public AutoShooterTask(boolean autoBankShot, boolean autoFarShot) {
        this.autoBankShot = autoBankShot;
        this.autoFarShot = autoFarShot;
    }

    public final boolean getAutoBankShot() {
        return this.autoBankShot;
    }

    public final boolean getAutoFarShot(){
        return this.autoFarShot;
    }

    public final double getShootingPower(){
        if (this.autoBankShot) {
            return 1.0;
        } else if (this.autoFarShot) {
            return 1.0;
        } else {
            return 0;
        }
    }
}