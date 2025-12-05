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
        return autoBankShot;
    }

    public final boolean getAutoFarShot(){
        return autoFarShot;
    }

    public final double getShootingVelocity(){
        if (autoBankShot) {
            return 1300;
        } else if (autoFarShot) {
            return 1800;
        } else {
            return 0;
        }
    }
}