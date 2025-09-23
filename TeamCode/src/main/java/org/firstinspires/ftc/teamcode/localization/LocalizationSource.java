package org.firstinspires.ftc.teamcode.localization;

/**
 * Represents a source of localization data.
 * Often a sensor, but can represent calculated heuristics (such as the tendency of robots to not
 * teleport around the field, making positions closer to previous ones more likely.)
 */
public interface LocalizationSource {
    /**
     * Gets whether this source is capable of detemining the robot's position.
     *
     * @return whether this localization source can localize position. If false, the position
     * component of the data returned by {@link #collectData} is not meaningful.
     */
    boolean canLocalizePosition();

    /**
     * Gets whether this source is capable of detemining the robot's rotation.
     *
     * @return whether this localization source can localize rotation. If false, the rotation
     * component of the data returned by {@link #collectData} is not meaningful.
     */
    boolean canLocalizeRotation();

    /**
     * Collects data from this localization source.
     *
     * @return localization data detailing this source's best guess for the robot's position and/or
     * rotation.
     */
    LocalizationData collectData();
}
