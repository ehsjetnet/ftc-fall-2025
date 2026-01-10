package org.firstinspires.ftc.teamcode.layer.input.mapping;

import org.firstinspires.ftc.teamcode.layer.AbstractFunctionLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;
import org.firstinspires.ftc.teamcode.task.AutoShooterTask;

public final class AutoShooterMapping extends AbstractFunctionLayer {
    public AutoShooterMapping() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Task map(Task task) {
        if (task instanceof GamepadInputTask) {
            GamepadInputTask castedTask = (GamepadInputTask) task;
            boolean shoot = castedTask.gamepad0.bumpers.left;
            boolean intake = castedTask.gamepad0.bumpers.right;
            boolean shooterEject = castedTask.gamepad0.buttons.y;
            boolean eject = castedTask.gamepad0.buttons.a;
            return new AutoShooterTask(shoot, shooterEject, intake, eject);
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}