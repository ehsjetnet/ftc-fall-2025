package org.firstinspires.ftc.teamcode.layer.input;

import java.util.Collections;
import java.util.Iterator;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.Task;

/**
 * Generates {@link GamepadInputTask}s from snapshots of the gamepad inputs.
 */
public final class GamepadInputGenerator extends AbstractInputGenerator {
    /**
     * The gamepad connected to the first port, or null if none is connected there.
     */
    private Gamepad gamepad0;

    /**
     * The gamepad connected to the first port, or null if none is connected there.
     */
    private Gamepad gamepad1;

    /**
     * Constructs a GamepadInputGenerator.
     */
    public GamepadInputGenerator() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        gamepad0 = setupInfo.getGamepad0();
        gamepad1 = setupInfo.getGamepad1();
        if (gamepad0 == null && gamepad1 == null) {
            throw new IllegalArgumentException("At least one gamepad must be connected to the "
                + "robot.");
        }
    }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
        return Collections.singleton((Task)(new GamepadInputTask(
            gamepad0 == null ? null : new GamepadInputTask.GamepadInput(
                gamepad0.left_stick_x,
                -gamepad0.left_stick_y,
                gamepad0.left_bumper,
                gamepad0.left_trigger,
                gamepad0.right_stick_x,
                -gamepad0.right_stick_y,
                gamepad0.right_bumper,
                gamepad0.right_trigger,
                gamepad0.dpad_up,
                gamepad0.dpad_right,
                gamepad0.dpad_down,
                gamepad0.dpad_left,
                gamepad0.a,
                gamepad0.b,
                gamepad0.x,
                gamepad0.y
            ),
            gamepad1 == null ? null : new GamepadInputTask.GamepadInput(
                -gamepad1.left_stick_x,
                gamepad1.left_stick_y,
                gamepad1.left_bumper,
                gamepad1.left_trigger,
                -gamepad1.right_stick_x,
                gamepad1.right_stick_y,
                gamepad1.right_bumper,
                gamepad1.right_trigger,
                gamepad1.dpad_up,
                gamepad1.dpad_right,
                gamepad1.dpad_down,
                gamepad1.dpad_left,
                gamepad1.a,
                gamepad1.b,
                gamepad1.x,
                gamepad1.y
            )
        ))).iterator();
    }
}
