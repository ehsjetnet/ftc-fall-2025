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
            boolean shootClose = castedTask.gamepad0.bumpers.left;
            boolean shootFar = castedTask.gamepad0.bumpers.right;
            return new AutoShooterTask(shootClose, shootFar);
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}