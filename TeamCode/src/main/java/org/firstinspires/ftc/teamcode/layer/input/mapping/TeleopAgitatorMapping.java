package org.firstinspires.ftc.teamcode.layer.input.mapping;

import org.firstinspires.ftc.teamcode.layer.AbstractFunctionLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;
import org.firstinspires.ftc.teamcode.task.TeleopAgitatorTask;

public final class TeleopAgitatorMapping extends AbstractFunctionLayer {
    public TeleopAgitatorMapping() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Task map(Task task) {
        if (task instanceof GamepadInputTask) {
            GamepadInputTask castedTask = (GamepadInputTask) task;
            return new TeleopAgitatorTask(castedTask.gamepad0.dpad.left, castedTask.gamepad0.dpad.right);
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}