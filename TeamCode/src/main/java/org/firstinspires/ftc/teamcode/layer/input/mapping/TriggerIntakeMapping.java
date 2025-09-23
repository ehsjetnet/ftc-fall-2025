package org.firstinspires.ftc.teamcode.layer.input.mapping;

import org.firstinspires.ftc.teamcode.layer.AbstractFunctionLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.IntakeTeleopTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Mapping for gamepad input that uses the left and right triggers to acquire and eject samples from
 * the intake.
 */
public final class TriggerIntakeMapping extends AbstractFunctionLayer {
    /**
     * Constructs a TriggerIntakeMapping.
     */
    public TriggerIntakeMapping() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Task map(Task task) {
        if (task instanceof GamepadInputTask) {
            GamepadInputTask castedTask = (GamepadInputTask)task;
            boolean intake = castedTask.gamepad0.triggers.left;
            boolean eject = castedTask.gamepad0.triggers.right;
            return new IntakeTeleopTask(false, false, (intake ? 1 : 0) - (eject ? 1 : 0));
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}
