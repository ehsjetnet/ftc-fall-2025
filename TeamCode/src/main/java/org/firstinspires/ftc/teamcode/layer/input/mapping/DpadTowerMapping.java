package org.firstinspires.ftc.teamcode.layer.input.mapping;

import org.firstinspires.ftc.teamcode.layer.AbstractFunctionLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.TowerForearmTask;
import org.firstinspires.ftc.teamcode.task.TowerTeleopTask;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Controls the tower swing using the up and down dpad buttons.
 * Up swings the tower upwards (away from front of robot).
 */
public final class DpadTowerMapping extends AbstractFunctionLayer {
    /**
     * Constructs a DpadTowerMapping.
     */
    public DpadTowerMapping() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Task map(Task task) {
        if (task instanceof GamepadInputTask) {
            GamepadInputTask castedTask = (GamepadInputTask)task;
            return new TowerTeleopTask(
                (castedTask.gamepad0.dpad.up ? 1 : 0) - (castedTask.gamepad0.dpad.down ? 1 : 0),
                (castedTask.gamepad0.dpad.left ? 1 : 0) - (castedTask.gamepad0.dpad.right ? 1 : 0)
            );
        } else if (task instanceof TowerForearmTask) {
            return task;
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}
