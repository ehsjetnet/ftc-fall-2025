package org.firstinspires.ftc.teamcode.task;

/**
 * Controls the intake in teleop.
 */
public class IntakeTeleopTask implements Task {
    
    /**
    * Which direction to run the agitator: True being forward, False being reversed.
    */
    private final boolean runServoDirection;

    /**
    * Which direction to run the core hex motor: True being forward, False being reversed.
    */
    private final boolean runCoreHexMotorDirection;

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
     * @param runServoDirection - Which direction to run the agitator: True being forward, False being reversed.
     * @param runCoreHexMotorDirection - which direction to run the core hex motor: True being forward, False being reversed.
     * @param servoPower - a power to directly give to the agitator actuator.
     * @param coreHexPower - a power to directly give to the core hex motor.
     */
    public IntakeTeleopTask(boolean runServoDirection, boolean runCoreHexMotorDirection, double servoPower, double coreHexPower) {
        this.runServo = runServo;
        this.runCoreHexMotor = runCoreHexMotor;
        this.servoPower = servoPower;
        this.coreHexPower = coreHexPower;
    }

    /** 
     * Returns which direction the agitator should go.
     * 
     * @return Returns which direction the agitator should go.
    */
    public final boolean getRunServoDirection() {
        return runServoDirection;
    }

    /** 
     * Returns which direction the core hex motor should go.
     * 
     * @return which direction the agitator should go..
    */
    public final boolean getRunCoreHexMotorDirection() {
        return runCoreHexMotorDirection;
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