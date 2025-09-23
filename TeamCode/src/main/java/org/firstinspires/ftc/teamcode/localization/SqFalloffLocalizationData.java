package org.firstinspires.ftc.teamcode.localization;

import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Vec2;

/**
 * LocalizationData suggesting the robot's transform is "near" a position and/or rotation with a
 * square distance falloff.
 */
public final class SqFalloffLocalizationData extends AbstractFinDiffLocalizationData {
    /**
     * The epsilon to give to the superclass AbstractFinDiffLocalizationData to automatically
     * compute the probability derivatives.
     */
    private static final double EPSILON = 0.001;

    /**
     * The best-guess transform of the robot according to this datum.
     */
    private Mat3 transform;

    /**
     * The trustworthiness of the data, directly proportional to the position and rotation
     * probabilities returned.
     */
    private double accuracy;

    /**
     * The precision of the position data, considered in the position probability calculation.
     */
    private double positionPrecision;

    /**
     * The precision of the rotation data, considered in the rotation probability calculation.
     */
    private double rotationPrecision;

    /**
     * Constructs a DiscLocalizationData.
     *
     * @param transform the best-guess transform of the robot according to this datum.
     * @param accuracy the trustworthiness of the data.
     * @param positionPrecision the precision of the position data.
     * @param rotationPrecision the precision of the rotation data.
     */
    public SqFalloffLocalizationData(
        Mat3 transform,
        double accuracy,
        double positionPrecision,
        double rotationPrecision
    ) {
        super(EPSILON);
        this.transform = transform;
        this.accuracy = accuracy;
        this.positionPrecision = positionPrecision;
        this.rotationPrecision = rotationPrecision;
    }

    @Override
    public double getPositionProbability(Vec2 pos) {
        Vec2 diff = transform.getTranslation().mul(-1).add(pos);
        return accuracy / ((diff.dot(diff) * positionPrecision) + 1);
    }

    @Override
    public double getRotationProbability(double rot) {
        double diff = rot - transform.getDirection().getAngle();
        return accuracy / (diff * diff * rotationPrecision + 1);
    }
}
