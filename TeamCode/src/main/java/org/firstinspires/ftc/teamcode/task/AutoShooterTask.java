package org.firstinspires.ftc.teamcode.task;

import org.firstinspires.ftc.teamcode.task.Task;

/**
 * Controls the intake in teleop.
 */
public class AutoShooterTask implements Task {

    private final boolean shoot;

    private final boolean shooterEject;

    private final boolean intake;

    private final boolean eject;
    
    public AutoShooterTask(boolean shoot, boolean shooterEject, boolean intake, boolean eject) {
        this.shoot = shoot;
        this.shooterEject = shooterEject;
        this.intake = intake;
        this.eject = eject;
    }

    public final boolean getShoot() {
        return this.shoot;
    }

    public final boolean getShooterEject() {
        return this.shooterEject;
    }

    public final boolean getIntake(){
        return this.intake;
    }

    public final boolean getEject(){
        return this.eject;
    }
}