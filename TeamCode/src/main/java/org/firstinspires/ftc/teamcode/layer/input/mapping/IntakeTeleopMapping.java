package org.firstinspires.ftc.teamcode.layer.input.mapping;

import org.firstinspires.ftc.teamcode.layer.AbstractFunctionLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;
import org.firstinspires.ftc.teamcode.task.IntakeTeleopTask;

public final class IntakeTeleopMapping extends AbstractFunctionLayer {
    public IntakeTeleopMapping() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Task map(Task task) {
        if (task instanceof GamepadInputTask) {
            GamepadInputTask castedTask = (GamepadInputTask) task;
            return new IntakeTeleopTask(castedTask.gamepad0.dpad.left, castedTask.gamepad0.dpad.right,
            castedTask.gamepad0.dpad.up, castedTask.gamepad0.dpad.down);
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}