package org.firstinspires.ftc.teamcode.layer.input.mapping;

import org.firstinspires.ftc.teamcode.layer.AbstractFunctionLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.LiftTeleopTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Mapping for gamepad input that uses the right bumper and trigger to control a lift.
 */
public final class RightLiftMapping extends AbstractFunctionLayer {
    /**
     * Constructs a RightLiftMapping.
     */
    public RightLiftMapping() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Task map(Task task) {
        if (task instanceof GamepadInputTask) {
            GamepadInputTask castedTask = (GamepadInputTask)task;
            boolean raiseSwing = castedTask.gamepad0.bumpers.right;
            boolean lowerSwing = castedTask.gamepad0.triggers.right;
            boolean extend = castedTask.gamepad0.bumpers.left;
            boolean retract = castedTask.gamepad0.triggers.left;
            return new LiftTeleopTask(
                (raiseSwing ? 1 : 0) - (lowerSwing ? 1 : 0),
                (extend ? 1 : 0) - (retract ? 1 : 0)
            );
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}
