package org.firstinspires.ftc.teamcode.task;

/**
 * Controls the intake in teleop.
 */
public class IntakeTeleopTask implements Task {
    /**
     * If the intake should still be running based on if the button is still being held down.
     */
    private final boolean intake;

    /**
     * A power to directly give to the intake actuator.
     */
    private final double intakePower;

    /**
    * Whether or not to run the agitator.
    */
    private final boolean runServo;

    /**
    * Whether or not to run the core hex motor.
    */
    private final boolean runCoreHexMotor;

    /**
    * A power to directly give to the agitator actuator.
    */
    private final double servoPower;

    /**
    * A power to directly give to the agitator actuator.
    */
    private final double coreHexPower;

    /**
     * Constructs an IntakeTeleopTask.
     *
     * @param intake - whether the intake should run based on if the button is still being pressed.
     * @param intakePower - a power to directly give to the intake actuator.
     * @param runServo - whether the intake agitator should run.
     * @param runCoreHexMotor - whether the core hex motor should run or not.
     * @param servoPower - a power to directly give to the agitator actuator.
     * @param coreHexPower - a power to directly give to the core hex motor.
     */
    public IntakeTeleopTask(boolean intake, double intakePower, boolean runServo, boolean runCoreHexMotor, double servoPower, double coreHexPower) {
        this.intake = intake;
        this.intakePower = intakePower;
        this.runServo = runServo;
        this.runCoreHexMotor = runCoreHexMotor;
        this.servoPower = servoPower;
        this.coreHexPower = coreHexPower;
    }

    /**
     * Returns if the intake should still be running.
     *
     * @return if the intake should still be running.
     */
    public final boolean getIntake() {
        return intake;
    }

    /**
     * Returns a power to directly give to the intake actuator.
     *
     * @return a power to directly give to the intake actuator, which will be in the range [-1, 1].
     */
    public final double getIntakePower() {
        return intakePower;
    }

    /** 
     * Returns whether the agitator should be ran.
     * 
     * @return whether or not the agitator should run.
    */
    public final boolean getRunServo() {
        return runServo;
    }

    /** 
     * Returns whether the core hex motor should run or not.
     * 
     * @return wheteher the core hex motor should run.
    */
    public final boolean getRunCoreHexMotor() {
        return runCoreHexMotor;
    }

    /** 
     * Returns a power to directly give to the agitator actuator.
     * 
     * @return a power to directly give to the agitator actuator.
    */
    public final double getServoPower() {
        return servoPower;
    }

    /** 
     * Returns a power to directly give to the core hex motor.
     * 
     * @return a power to directly give to the core hex motor.
    */
    public final double getCoreHexPower() {
        return coreHexPower;
    }
}