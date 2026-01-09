package org.firstinspires.ftc.teamcode.task;

import org.firstinspires.ftc.teamcode.task.Task;

/**
 * Controls the intake in teleop.
 */
public class TeleopAgitatorTask implements Task {

    private final boolean runServo;

    public TeleopAgitatorTask(boolean runServo) {
        this.runServo = runServo;
    }

    public final boolean getRunServo() {
        return runServo;
}
