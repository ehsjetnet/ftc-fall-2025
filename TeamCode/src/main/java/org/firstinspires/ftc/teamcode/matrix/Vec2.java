package org.firstinspires.ftc.teamcode.matrix;

/**
 * Represents a 2D vector.
 */
public final class Vec2 {
    /**
     * The x component of the vector.
     */
    private final double x;

    /**
     * The y component of the vector.
     */
    private final double y;

    /**
     * Constructs a Vec2.
     *
     * @param x the x component of the vector.
     * @param y the y component of the vector.
     */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x component of the vector.
     *
     * @return The x component of the vector.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y component of the vector.
     *
     * @return The y component of the vector.
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the sum of this and a given vector.
     *
     * @param other the addend.
     * @return A new vector that is the sum of this and the given vector.
     */
    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    /**
     * Gets the produce of this vector with a given scalar.
     *
     * @param scalar the scalar factor.
     * @return A new vector that is the scalar product of this vector and the given scalar.
     */
    public Vec2 mul(double scalar) {
        return new Vec2(x * scalar, y * scalar);
    }

    /**
     * Calculates the dot product of this and a given vector.
     *
     * @param other the factor to compute the dot product with.
     * @return The dot product.
     */
    public double dot(Vec2 other) {
        return x * other.x + y * other.y;
    }

    /**
     * Finds the length of this vector.
     *
     * @return The length of this vector.
     */
    public double len() {
        return Math.sqrt(dot(this));
    }

    /**
     * Gets the angle between this vector and the positive x axis.
     *
     * @return The positive angle between this vector and the positive x axis in the range [0, 2pi).
     */
    public double getAngle() {
        return Math.atan2(y, x);
    }

    /**
     * Normalizes the vector.
     *
     * @return A new vector pointing in the same direction from the origin as this one but
     * guarenteed to have a length of 1.
     */
    public Vec2 unit() {
        return mul(1 / len());
    }

    /**
     * Returns the positive angle made with a given vector.
     *
     * @param other the vector to calculate the angle with.
     * @return The positive angle made with another vector in the range [0, pi].
     */
    public double angleWith(Vec2 other) {
        return Math.acos(unit().dot(other.unit()));
    }

    /**
     * Checks whether both components of the vector are finite.
     *
     * @return Whether both components of this vector are finite.
     */
    public boolean isFinite() {
        return Double.isFinite(x) && !Double.isNaN(x) && Double.isFinite(y) && !Double.isNaN(y);
    }

    /**
     * Returns the projection of a given vector onto this one.
     *
     * @param projectee the vector to project.
     * @return A new vector that is the projection of the given vector onto this one.
     */
    public Vec2 proj(Vec2 projectee) {
        return mul(dot(projectee) / dot(this));
    }

    /**
     * Returns an arbitrary vector that is perpendicular to this one.
     *
     * @return A new vector with arbitrary length. The only guarenteed properties are that it
     * {@link #isFinite} and the dot product between it and this vector is zero.
     */
    public Vec2 getPerpendicular() {
        return new Vec2(1, -y / x);
    }
}
