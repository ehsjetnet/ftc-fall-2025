package org.firstinspires.ftc.teamcode.localization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.firstinspires.ftc.teamcode.matrix.Mat2;
import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Vec2;

/**
 * Combines localization data by using Newton's method to numerically approximate the transform with
 * highest probability.
 * This implementation allows position and rotation to be resolved independently, while only
 * consulting sources relevant to the required attribute.
 */
public final class NewtonRobotLocalizer implements RobotLocalizer {
    /**
     * The number of steps to take when finding a root of the probability sum derivative.
     * The greater this number, the greater the likelihood that each identified "root" is actually
     * near an extremum of the probability sum.
     */
    private static final int MAX_NEWTON_STEPS = 40;

    /**
     * The number of roots of the probability sum derivative that will be found during maximization.
     * The greater this number, the greater the number of local extrema that may be filtered out.
     */
    private static final int MAX_NEWTON_ROOTS = 10;

    /**
     * The size of the random disturbance that is added to the working root when it reaches an
     * extremum or saddle point.
     * The intended effect of this is to nudge the root off of saddle points. While this could also
     * "nudge" the working root away from a true extremum, this does not affect the result because
     * the root with minimum error is used from each root search instead of the final value of the
     * working root.
     */
    private static final double NEWTON_DISTURBANCE_SIZE = 1;

    /**
     * The list of sources of localization data to collect data from.
     */
    private ArrayList<LocalizationSource> sources;

    /**
     * Holds localization data cached from sources so repeated calls within the same invocation of a
     * resolve method return the same values.
     */
    private Map<LocalizationSource, LocalizationData> cachedData;

    /**
     * Holds the last computed position of the robot so repeated calls to resolve methods return the
     * same values until {@link #invalidateCache} is called.
     * May be null if none is computed yet.
     */
    private Vec2 cachedPos;

    /**
     * Holds the last computed rotation of the robot so repeated calls to resolve methods return the
     * same values until {@link #invalidateCache} is called.
     * May be null if none is computed yet.
     */
    private Double cachedRot;

    /**
     * Constructs a NewtonRobotLocalizer.
     */
    public NewtonRobotLocalizer() {
        sources = new ArrayList<>();
        cachedData = new HashMap<>();
    }

    @Override
    public void invalidateCache() {
        cachedData.clear();
        cachedPos = null;
        cachedRot = null;
    }

    @Override
    public void registerSource(LocalizationSource source) {
        sources.add(source);
    }

    @Override
    public Mat3 resolveTransform() {
        resolve(cachedPos == null, cachedRot == null);
        return Mat3.fromTransform(Mat2.fromAngle(cachedRot), cachedPos);
    }

    @Override
    public Vec2 resolvePosition() {
        resolve(cachedPos == null, false);
        return cachedPos;
    }

    @Override
    public double resolveRotation() {
        resolve(false, cachedRot == null);
        return cachedRot;
    }

    /**
     * Resolves the robot's position, rotation, or both.
     * The requested values are stored in {@link #cachedPos} and {@link #cachedRot} as
     * appropriate.
     *
     * @param pos whether the resolve the robot's position.
     * @param rot whether the resolve the robot's rotation.
     */
    private void resolve(boolean pos, boolean rot) {
        if (pos && cachedPos == null) {
            List<LocalizationSource> posSources = sources
                .stream()
                .filter(LocalizationSource::canLocalizePosition)
                .collect(Collectors.toList());
            List<Vec2> roots = new ArrayList<>();
            for (int i = 0; i < MAX_NEWTON_ROOTS; ++i) {
                Vec2 xy = new Vec2(0, 0);
                Vec2 xyMinErr = xy;
                double minErr = Double.POSITIVE_INFINITY;
                for (int j = 0; j < MAX_NEWTON_STEPS + 1; ++j) {
                    Vec2 curXy = xy;
                    double err = posSources
                        .stream()
                        .mapToDouble(src
                            -> getData(src).getPositionProbabilityDx(curXy, roots)
                            + getData(src).getPositionProbabilityDy(curXy, roots))
                        .sum();
                    if (err < minErr) {
                        xyMinErr = xy;
                        minErr = err;
                    }
                    if (j < MAX_NEWTON_STEPS) {
                        Vec2 grad = posSources
                            .stream()
                            .map(src
                                -> getData(src).getPositionProbabilityDxGradient(curXy, roots)
                                .add(getData(src).getPositionProbabilityDyGradient(curXy, roots)))
                            .reduce(new Vec2(0, 0), Vec2::add);
                        Vec2 delta = grad.mul(-err / grad.len());
                        if (!delta.isFinite()) {
                            double dir = Math.random() * 2 * Math.PI;
                            delta = new Vec2(
                                Math.cos(dir) * NEWTON_DISTURBANCE_SIZE,
                                Math.sin(dir) * NEWTON_DISTURBANCE_SIZE
                            );
                        }
                        xy = xy.add(delta);
                    }
                }
                roots.add(xyMinErr);
            }
            // Also contains saddle points and points where we ran out of steps even without
            // reaching an extremum. We're going to take the maximum of the function at every
            // combination, though, so we don't care.
            Map<Vec2, Double> extrema = new HashMap<>();
            roots.forEach(root -> {
                extrema.put(root, posSources
                    .stream()
                    .mapToDouble(src -> getData(src).getPositionProbability(root))
                    .sum()
                );
            });
            cachedPos = Collections.max(extrema.entrySet(), Map.Entry.comparingByValue())
                .getKey();
        }
        if (rot && cachedRot == null) {
            List<LocalizationSource> rotSources = sources
                .stream()
                .filter(LocalizationSource::canLocalizeRotation)
                .collect(Collectors.toList());
            List<Double> roots = new ArrayList<>();
            for (int i = 0; i < MAX_NEWTON_ROOTS; ++i) {
                double x = 0;
                double xMinErr = x;
                double minErr = Double.POSITIVE_INFINITY;
                for (int j = 0; j < MAX_NEWTON_STEPS + 1; ++j) {
                    double curX = x;
                    double err = rotSources
                        .stream()
                        .mapToDouble(src -> getData(src).getRotationProbabilityDx(curX, roots))
                        .sum();
                    if (err < minErr) {
                        xMinErr = x;
                        minErr = err;
                    }
                    if (j < MAX_NEWTON_STEPS) {
                        double slope = rotSources
                            .stream()
                            .mapToDouble(src -> getData(src).getRotationProbabilityDx2(curX, roots))
                            .sum();
                        double delta = -err / slope;
                        if (!Double.isFinite(delta)) {
                            // Randomly disturb
                            delta = Math.signum(Math.random() - 1.0 / 2) * NEWTON_DISTURBANCE_SIZE;
                        }
                        x += delta;
                    }
                }
                roots.add(xMinErr);
            }
            Map<Double, Double> extrema = new HashMap<>();
            roots.forEach(x -> extrema.put(x, rotSources
                .stream()
                .mapToDouble(src -> getData(src).getRotationProbability(x))
                .sum()
            ));
            cachedRot = Collections.max(extrema.entrySet(), Map.Entry.comparingByValue())
                .getKey();
        }
    }

    /**
     * Collects data from a localization source, caching the result for that source.
     *
     * @param source the source to collect from.
     * @return The cached data from the source, cleared when {@link #invalidateCache} is called.
     *
     */
    private LocalizationData getData(LocalizationSource source) {
        if (!cachedData.containsKey(source)) {
            cachedData.put(source, source.collectData());
        }
        return cachedData.get(source);
    }
}
