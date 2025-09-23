package org.firstinspires.ftc.teamcode.localization;

import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Vec2;

/**
 * Collects data from localization sources to calculate a best guess for the robot's field-space
 * transformation.
 */
public interface RobotLocalizer {
    /**
     * Indicates to the localizer that any cached localization datums or resolution results are no
     * longer valid and should be recalculated.
     */
    void invalidateCache();

    /**
     * Registers a localization source to collect data from during resolution.
     *
     * @param source the source to register.
     */
    void registerSource(LocalizationSource source);

    /**
     * Collects data from all localization sources to determine the robot's transform.
     *
     * @return The possibly cached transform of the robot in field space. To ensure the
     * <i>current</i> transform is returned, call {@link invalidateCache}.
     */
    Mat3 resolveTransform();

    /**
     * Collects data from some localization sources to determine the robot's position.
     * Implementations may optimize this method by excluding some localization sources from the
     * resolution process.
     *
     * @return The possibly cached field position of the robot. To ensure the <i>current</i>
     * position is returned, call {@link invalidateCache}.
     */
    Vec2 resolvePosition();

    /**
     * Collects data from some localization sources to determine the robot's rotation.
     * Implementations may optimize this method by excluding some localization sources from the
     * resolution process.
     *
     * @return The possibly cached field rotation of the robot. To ensure the <i>current</i>
     * rotation is returned, call {@link invalidateCache}.
     */
    double resolveRotation();
}
