package org.firstinspires.ftc.teamcode.layer.input.mapping;

import org.firstinspires.ftc.teamcode.layer.AbstractFunctionLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;
import org.firstinspires.ftc.teamcode.task.TeleopShooterTask;

public final class TeleopShooterMapping extends AbstractFunctionLayer {
    public TeleopShooterMapping() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Task map(Task task) {
        if (task instanceof GamepadInputTask) {
            GamepadInputTask castedTask = (GamepadInputTask) task;
            return new TeleopShooterTask(castedTask.gamepad0.buttons.a, castedTask.gamepad0.buttons.y, castedTask.gamepad0.buttons.x);
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}