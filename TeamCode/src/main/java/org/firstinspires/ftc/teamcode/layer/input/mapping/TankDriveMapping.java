package org.firstinspires.ftc.teamcode.layer.input.mapping;

import org.firstinspires.ftc.teamcode.layer.AbstractFunctionLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.TankDriveTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Mapping for gamepad input that uses the y components of each joystick to
 * control power to the corresponding side of the robot.
 */
public final class TankDriveMapping extends AbstractFunctionLayer {
    /**
     * Constructs a TankDriveMapping.
     */
    public TankDriveMapping() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Task map(Task task) {
        if (task instanceof GamepadInputTask) {
            GamepadInputTask castedTask = (GamepadInputTask)task;
            return new TankDriveTask(
                castedTask.gamepad0.joysticks.left.y,
                castedTask.gamepad0.joysticks.right.y
            );
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}
