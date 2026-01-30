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

    private final boolean experimental;
    
    public AutoShooterTask(boolean shoot, boolean shooterEject, boolean intake, boolean eject, boolean experimental) {
        this.shoot = shoot;
        this.shooterEject = shooterEject;
        this.intake = intake;
        this.eject = eject;
        this.experimental = experimental;
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

    public final boolean getExperimental(){
        return this.experimental;
    }
}