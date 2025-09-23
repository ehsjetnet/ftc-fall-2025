package org.firstinspires.ftc.teamcode.layer;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RobotController;
import org.firstinspires.ftc.teamcode.localization.RobotLocalizer;
import org.firstinspires.ftc.teamcode.logging.Logger;
import org.firstinspires.ftc.teamcode.logging.LoggerProvider;

/**
 * Contains the information needed to initialize a layer.
 */
public final class LayerSetupInfo {
    /**
     * The HardwareMap for the robot, where peripheral interfaces can be retrieved.
     */
    private final HardwareMap hardwareMap;

    /**
     * The RobotController setting up the layer.
     */
    private final RobotController robotController;

    /**
     * The RobotLocalizer keeping track of the robot's field space transform.
     */
    private final RobotLocalizer robotLocalizer;

    /**
     * The gamepad connected to the first port.
     * Null if none is connected or available (e.g. we're in teleop).
     */
    private final Gamepad gamepad0;

    /**
     * The gamepad connected to the second port.
     * Null if none is connected or available (e.g. we're in teleop).
     */
    private final Gamepad gamepad1;

    /**
     * The base LoggerProvider whose clones are used by layers.
     */
    private final LoggerProvider loggerProvider;

    /**
     * Creates a LayerSetupInfo.
     *
     * @param hardwareMap the source of peripheral interfaces the layer may use to communicate with
     * hardware.
     * @param robotController the RobotController that will run the layer.
     * @param robotLocalizer the RobotLocalizer to get robot transformation info from during the
     * execution.
     * @param gamepad0 the Gamepad connected to the first slot, or null if no such gamepad is
     * available or connected.
     * @param gamepad1 the Gamepad connected to the second slot, or null if no such gamepad is
     * available or connected.
     * @param loggerProvider the base LoggerProvider whose clones should be passed to the layers.
     */
    public LayerSetupInfo(
        HardwareMap hardwareMap,
        RobotController robotController,
        RobotLocalizer robotLocalizer,
        Gamepad gamepad0,
        Gamepad gamepad1,
        LoggerProvider loggerProvider
    ) {
        this.hardwareMap = hardwareMap;
        this.robotController = robotController;
        this.robotLocalizer = robotLocalizer;
        this.gamepad0 = gamepad0;
        this.gamepad1 = gamepad1;
        this.loggerProvider = loggerProvider;
    }

    /**
     * Returns the HardwareMap.
     *
     * @return A HardwareMap that can retrieve peripheral interfaces for devices connected to the
     * robot.
     */
    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    /**
     * Returns the RobotLocalizer.
     *
     * @return A RobotLocalizer keeping track of the robot's field space transform.
     */
    public RobotLocalizer getLocalizer() {
        return robotLocalizer;
    }

    /**
     * Returns the Gamepad connected to the first slot, or null if no such gamepad is available or
     * connected.
     *
     * @return The first gamepad.
     */
    public Gamepad getGamepad0() {
        return gamepad0;
    }

    /**
     * Returns the Gamepad connected to the second slot, or null if no such gamepad is available or
     * connected.
     *
     * @return The second gamepad.
     */
    public Gamepad getGamepad1() {
        return gamepad1;
    }

    /**
     * Returns a LoggerProvider cloned from the base one given to the RobotController.
     *
     * @return A new LoggerProvider with the same configuration as the base one.
     */
    public LoggerProvider getLoggerProvider() {
        return loggerProvider.clone();
    }

    /**
     * Returns a Logger created by the base LoggerProvider.
     *
     * @param label the label of the Logger to create.
     * @return The created logger.
     */
    public Logger getLogger(String label) {
        return loggerProvider.getLogger(label);
    }

    /**
     * Registers a callback to be called on every update of the owning RobotController.
     *
     * @param listener - the callback to be called.
     */
    public void addUpdateListener(Runnable listener) {
        robotController.addUpdateListener(listener);
    }

    /**
     * Registers a callback to be called after the layer stack finishes executing.
     *
     * @param listener - the callback to be called.
     */
    public void addTeardownListener(Runnable listener) {
        robotController.addTeardownListener(listener);
    }
}
