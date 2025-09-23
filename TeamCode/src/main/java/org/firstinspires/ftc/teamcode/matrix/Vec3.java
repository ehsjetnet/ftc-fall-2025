package org.firstinspires.ftc.teamcode.matrix;

/**
 * Represents a 3D vector.
 */
public final class Vec3 {
    /**
     * An array holding the components of the vector.
     */
    private final double[] vec;

    /**
     * Constructs a Vec3.
     *
     * @param x the x component of the vector.
     * @param y the y component of the vector.
     * @param z the z component of the vector.
     */
    public Vec3(double x, double y, double z) {
        vec = new double[] {x, y, z};
    }

    /**
     * Gets the x component of the vector.
     *
     * @return The x component of the vector.
     */
    public double getX() {
        return vec[0];
    }

    /**
     * Gets the y component of the vector.
     *
     * @return The y component of the vector.
     */
    public double getY() {
        return vec[1];
    }

    /**
     * Gets the z component of the vector.
     *
     * @return The z component of the vector.
     */
    public double getZ() {
        return vec[2];
    }

    /**
     * Calculates the dot product of this and a given vector.
     *
     * @param other the factor to compute the dot product with.
     * @return The dot product.
     */
    public double dot(Vec3 other) {
        return vec[0] * other.vec[0] + vec[1] * other.vec[1] + vec[2] * other.vec[2];
    }

    /**
     * Gets a component of the vector.
     *
     * @param index which component to return: the x coordinate if 0, y if 1, z if 2/
     * @return The indicated component of the vector.
     */
    public double get(int index) {
        if (index < 0 || index > 2) {
            throw new IllegalArgumentException("Bad index " + index);
        }
        return vec[index];
    }
}
