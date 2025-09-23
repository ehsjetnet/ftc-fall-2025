package org.firstinspires.ftc.teamcode.task;

/**
 * Holds a snapshot of input from all connected gamepads.
 */
public class GamepadInputTask implements Task {
    /**
     * Input captured from the gamepad connected to the first slot, or null if none is connected.
     */
    public final GamepadInput gamepad0;

    /**
     * Input captured from the gamepad connected to the second slot, or null if none is connected.
     */
    public final GamepadInput gamepad1;

    /**
     * Constructs a GamepadInputTask with given info about gamepad inputs.
     *
     * @param gamepad0 Input captured from the gamepad connected to the first slot, or null if none
     * is connected.
     * @param gamepad1 Input captured from the gamepad connected to the second slot, or null if none
     * is connected.
     */
    public GamepadInputTask(GamepadInput gamepad0, GamepadInput gamepad1) {
        this.gamepad0 = gamepad0;
        this.gamepad1 = gamepad1;
    }

    /**
     * Carries information about the axes of a joystick on a gamepad.
     */
    public static final class Joystick {
        /**
         * The horizontal axis of the joystick.
         * Positive values correspond to the rightward direction.
         */
        public final float x;

        /**
         * The vertical axis of the joystick.
         * Positive values correspond to the upward direction.
         */
        public final float y;

        /**
         * Constructs a Joystick.
         *
         * @param x - the joystick's horizontal axis input.
         * @param y - the joystick's vertical axis input.
         */
        private Joystick(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Carries information about the pair of joysticks on a gamepad.
     */
    public static final class Joysticks {
        /**
         * The left joystick.
         */
        public final Joystick left;

        /**
         * The right joystick.
         */
        public final Joystick right;

        /**
         * Constructs a Joysticks.
         *
         * @param left - the object representing the left joystick.
         * @param right - the object representing the right joystick.
         */
        private Joysticks(Joystick left, Joystick right) {
            this.left = left;
            this.right = right;
        }
    }

    /**
     * Carries information about a pair of symmetrical buttons on a gamepad, such as the triggers or
     * bumpers.
     */
    public static final class ButtonPair {
        /**
         * Whether the left button of the pair is pressed.
         */
        public final boolean left;

        /**
         * Whether the right button of the pair is pressed.
         */
        public final boolean right;

        /**
         * Constructs a ButtonPair.
         *
         * @param left - whether the left button of the pair is pressed.
         * @param right - whether the right button of the pair is pressed.
         */
        private ButtonPair(boolean left, boolean right) {
            this.left = left;
            this.right = right;
        }
    }

    /**
     * Caries information about a gamepad's directional pad (four buttons arranged to indicate
     * movement along x and y axes).
     */
    public static final class DirectionalPad {
        /**
         * Whether the up button of the dpad is pressed.
         */
        public final boolean up;

        /**
         * Whether the right button of the dpad is pressed.
         */
        public final boolean right;

        /**
         * Whether the down button of the dpad is pressed.
         */
        public final boolean down;

        /**
         * Whether the left button of the dpad is pressed.
         */
        public final boolean left;

        /**
         * Constructs a DirectionalPad.
         *
         * @param up - whether the up button of the dpad is pressed.
         * @param right - whether the right button of the dpad is pressed.
         * @param down - whether the down button of the dpad is pressed.
         * @param left - whether the left button of the dpad is pressed.
         */
        private DirectionalPad(boolean up, boolean right, boolean down, boolean left) {
            this.up = up;
            this.right = right;
            this.down = down;
            this.left = left;
        }
    }

    /**
     * Carries information about miscellanous buttons on a gamepad.
     */
    public static final class Buttons {
        /**
         * Whether the A gamepad button is pressed.
         */
        public final boolean a;

        /**
         * Whether the B gamepad button is pressed.
         */
        public final boolean b;

        /**
         * Whether the X gamepad button is pressed.
         */
        public final boolean x;

        /**
         * Whether the Y gamepad button is pressed.
         */
        public final boolean y;

        /**
         * Constructs a Buttons.
         *
         * @param a - whether the A gamepad button is pressed.
         * @param b - whether the B gamepad button is pressed.
         * @param x - whether the X gamepad button is pressed.
         * @param y - whether the Y gamepad button is pressed.
         */
        private Buttons(boolean a, boolean b, boolean x, boolean y) {
            this.a = a;
            this.b = b;
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Carries information about a single connected gamepad.
     */
    public static class GamepadInput {
        /**
         * The minimum value reported by the trigger for it to be considered "pressed".
         * For the sake of symmetry with the PiE API, triggers are considered digital buttons
         * and not as real-valued inputs.
         */
        private static final float TRIGGER_MIN = 0.3f;

        /**
         * The pair of joysticks on the gamepad.
         */
        public final Joysticks joysticks;

        /**
         * The pair of bumpers on the gamepad.
         */
        public final ButtonPair bumpers;

        /**
         * The pair of triggers on the gamepad.
         */
        public final ButtonPair triggers;

        /**
         * The directional pad on the gamepad.
         */
        public final DirectionalPad dpad;

        /**
         * The miscellanious buttons on the gamepad.
         */
        public final Buttons buttons;

        /**
         * Constructs a GamepadInput.
         *
         * @param joystickLeftX - the horizontal axis of the left joystick.
         * @param joystickLeftY - the vertical axis of the left joystick.
         * @param bumperLeft - whether the left bumper is pressed.
         * @param triggerLeft - the value of the left trigger.
         * @param joystickRightX - the horizontal axis of the right joystick.
         * @param joystickRightY - the vertical axis of the right joystick.
         * @param bumperRight - whether the right bumper is pressed.
         * @param triggerRight - the value of the right trigger.
         * @param dpadUp - whether the up dpad button is pressed.
         * @param dpadRight - whether the right dpad button is pressed.
         * @param dpadDown - whether the down dpad button is pressed.
         * @param dpadLeft - whether the left dpad button is pressed.
         * @param buttonA - whether the A gamepad button is pressed.
         * @param buttonB - whether the B gamepad button is pressed.
         * @param buttonX - whether the X gamepad button is pressed.
         * @param buttonY - whether the Y gamepad button is pressed.
         */
        public GamepadInput(
            float joystickLeftX,
            float joystickLeftY,
            boolean bumperLeft,
            float triggerLeft,
            float joystickRightX,
            float joystickRightY,
            boolean bumperRight,
            float triggerRight,
            boolean dpadUp,
            boolean dpadRight,
            boolean dpadDown,
            boolean dpadLeft,
            boolean buttonA,
            boolean buttonB,
            boolean buttonX,
            boolean buttonY
        ) {
            joysticks = new Joysticks(
                new Joystick(joystickLeftX, joystickLeftY),
                new Joystick(joystickRightX, joystickRightY)
            );
            bumpers = new ButtonPair(bumperLeft, bumperRight);
            triggers = new ButtonPair(triggerLeft >= TRIGGER_MIN, triggerRight >= TRIGGER_MIN);
            dpad = new DirectionalPad(dpadUp, dpadRight, dpadDown, dpadLeft);
            buttons = new Buttons(buttonA, buttonB, buttonX, buttonY);
        }
    }
}
