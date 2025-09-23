package org.firstinspires.ftc.teamcode.layer.input.mapping;

import org.firstinspires.ftc.teamcode.layer.AbstractFunctionLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.HolonomicDriveTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Mapping for gamepad input that uses the left joystick for axial and lateral movement (relative to
 * the robot) and the x axis of the right joystick to turn a robot using holonomic drive.
 */
public final class JoystickHoloDriveMapping extends AbstractFunctionLayer {
    /**
     * Constructs a JoystickHoloDriveMapping.
     */
    public JoystickHoloDriveMapping() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Task map(Task task) {
        if (task instanceof GamepadInputTask) {
            GamepadInputTask castedTask = (GamepadInputTask)task;
            return new HolonomicDriveTask(
                castedTask.gamepad0.joysticks.left.y,
                castedTask.gamepad0.joysticks.left.x,
                -castedTask.gamepad0.joysticks.right.x
            );
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}
