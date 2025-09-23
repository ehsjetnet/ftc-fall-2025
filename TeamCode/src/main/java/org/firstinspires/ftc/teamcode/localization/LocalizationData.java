package org.firstinspires.ftc.teamcode.localization;

import java.util.List;

import org.firstinspires.ftc.teamcode.matrix.Vec2;

/**
 * Data suggesting the robot's position and/or rotation collected from a {@link LocalizationSource}.
 */
public interface LocalizationData {
    /**
     * Gets the relative probability of the robot being located at the given field position.
     *
     * @param pos - the field position to test
     * @return the relative probability of the robot being located at the given position. See the
     * package summary for the significance of this value.
     */
    double getPositionProbability(Vec2 pos);

    /**
     * Gets or approximates the partial derivative with respect to x of
     * {@link #getPositionProbability} at the given field position.
     *
     * @param pos the field position to compute the derivative at.
     * @param ignoreRoots a list of roots of this derivative to exclude. This makes the graph of
     * the partial derivative neither approach nor reach zero due to these roots alone.
     * @return the partial derivative wrt x of the probability at the given field position. This is
     * guarenteed to be finite.
     */
    double getPositionProbabilityDx(Vec2 pos, List<Vec2> ignoreRoots);

    /**
     * Gets or approximates the partial derivative with respect to y of
     * {@link #getPositionProbability} at the given field position.
     *
     * @param pos the field position to compute the derivative at.
     * @param ignoreRoots a list of roots of this derivative to exclude. This makes the graph of
     * the partial derivative neither approach nor reach zero due to these roots alone.
     * @return the partial derivative wrt y of the probability at the given field position. This is
     * guarenteed to be finite.
     */
    double getPositionProbabilityDy(Vec2 pos, List<Vec2> ignoreRoots);

    /**
     * Gets or approximates the gradient of the partial derivative with respect to x of
     * {@link #getPositionProbability} at the given field position.
     *
     * @param pos the field position to compute the derivative at.
     * @param ignoreRoots a list of roots of {@link #getPositionProbabilityDx} to exclude. The
     * computed gradient does not suggest the existance of roots in that function at any of these
     * points.
     * @return the gradient of the partial derivative wrt x of the probability at the given field
     * position, returned as a vector: &lt;<code>getPositionProbability</code><sub>xx</sub>(
     * <code>pos.getX()</code>, <code>pos.getY()</code>),
     * <code>getPositionProbabilty</code><sub>xy</sub>(<code>pos.getX()</code>,
     * <code>pos.getY()</code>)&gt;
     * This is guarenteed to be finite.
     */
    Vec2 getPositionProbabilityDxGradient(Vec2 pos, List<Vec2> ignoreRoots);

    /**
     * Gets or approximates the gradient of the partial derivative with respect to x of
     * {@link #getPositionProbability} at the given field position.
     *
     * @param pos the field position to compute the derivative at.
     * @param ignoreRoots a list of roots of {@link #getPositionProbabilityDy} to exclude. The
     * computed gradient does not suggest the existance of roots in that function at any of these
     * points.
     * @return the gradient of the partial derivative wrt y of the probability at the given field
     * position, returned as a vector: &lt;<code>getPositionProbability</code><sub>yx</sub>(
     * <code>pos.getX()</code>, <code>pos.getY()</code>),
     * <code>getPositionProbabilty</code><sub>yy</sub>(<code>pos.getX()</code>,
     * <code>pos.getY()</code>)&gt;
     * This is guarenteed to be finite.
     */
    Vec2 getPositionProbabilityDyGradient(Vec2 pos, List<Vec2> ignoreRoots);

    /**
     * Gets the relative probability of the robot bearing the given field orientation.
     *
     * @param rot the field orientation to test in radians. Zero indicates the positive x
     * direction, and increasing values are counterclockwise.
     * @return the relative probability of the robot bearing the given field orientation. See the
     * package summary for the significance of this value. The probability of the robot having the
     * given rotation. This is guarenteed to be finite.
     */
    double getRotationProbability(double rot);

    /**
     * Gets or approximates the derivative of {@link #getPositionProbability} at the given field
     * orientation.
     *
     * @param rot the field orientation to compute the derivative at in radians. Zero indicates
     * the positive x direction, and increasing values are counterclockwise.
     * @param ignoreRoots a list of roots of this derivative to exclude. This makes the graph of
     * the derivative neither approach nor reach zero due to these roots alone.
     * @return the derivative of the probability at the given field orientation. This is guarenteed
     * to be finite.
     */
    double getRotationProbabilityDx(double rot, List<Double> ignoreRoots);

    /**
     * Gets or approximates the second derivative of {@link #getPositionProbability} at the given
     * field orientation.
     *
     * @param rot the field orientation to compute the derivative at in radians. Zero indicates
     * the positive x direction, and increasing values are counterclockwise.
     * @param ignoreRoots a list of roots of the rotation probability derivative to exclude. The
     * computed second derivative does not suggest the existance of roots in that function at any of
     * these points.
     * @return The second derivative of the probability at the given field orientation.
     */
    double getRotationProbabilityDx2(double rot, List<Double> ignoreRoots);
}
