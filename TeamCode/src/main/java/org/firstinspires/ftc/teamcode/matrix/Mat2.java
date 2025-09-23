package org.firstinspires.ftc.teamcode.matrix;

/**
 * Represents an immutable 2x2 matrix of double-precision floating point numbers.
 * Commonly used to express 2D rotations.
 */
public final class Mat2 {
    // CSOFF:MagicNumber
    /**
     * The underlying array storing matrix elements.
     */
    private final double[] mat;

    /**
     * Constructs a Mat2.
     *
     * @param m00 - the element in the 0th row and 0th column
     * @param m10 - the element in the 1st row and 0th column
     * @param m01 - the element in the 0th row and 1st column
     * @param m11 - the element in the 1st row and 1st column
     */
    public Mat2(double m00, double m10, double m01, double m11) {
        mat = new double[] {m00, m10, m01, m11};
    }

    /**
     * Creates a 2D rotation matrix for a given angle.
     *
     * @param angle an angle in radians. Positive values indicates counterclockwise rotation.
     * @return The created rotation matrix.
     */
    public static Mat2 fromAngle(double angle) {
        return new Mat2(Math.cos(angle), -Math.sin(angle), Math.sin(angle), Math.cos(angle));
    }

    /**
     * Returns a matrix which is the product of this and the given matrix.
     * If the matrices represent transformations, their product is the transformation matrix
     * equivalent to applying the first factor's transformation and then the second one's.
     *
     * @param other the matrix to postmultiply by.
     * @return A new matrix that is the product of the multiplication.
     */
    public Mat2 mul(Mat2 other) {
        return new Mat2(
            mat[0] * other.mat[0] + mat[1] * other.mat[2],
            mat[0] * other.mat[1] + mat[1] * other.mat[3],
            mat[2] * other.mat[0] + mat[3] * other.mat[2],
            mat[2] * other.mat[1] + mat[3] * other.mat[3]
        );
    }

    /**
     * Returns the product of this matrix and the given vector.
     * If the matrix represents a rotation, their product is the vector rotated about the origin by
     * this rotation. This product could also represent the transformation of a 1D point, but this
     * has no obvious use on our robot.
     *
     * @param other the vector factor.
     * @return A new vector that is the product of the multiplication.
     */
    public Vec2 mul(Vec2 other) {
        return new Vec2(
            mat[0] * other.getX() + mat[1] * other.getY(),
            mat[2] * other.getX() + mat[3] * other.getY()
        );
    }

    /**
     * Returns a matrix which is the elementwise product of this matrix and a scalar.
     *
     * @param other the scalar factor.
     * @return A new matrix that is the product of the multiplication.
     */
    public Mat2 mul(double other) {
        return new Mat2(
            mat[0] * other,
            mat[1] * other,
            mat[2] * other,
            mat[3] * other
        );
    }

    /**
     * Calculates the determinant of the matrix.
     *
     * @return The matrix's determinant.
     */
    public double det() {
        return mat[0] * mat[3] - mat[1] * mat[2];
    }

    /**
     * Computes the inverse of the matrix.
     *
     * @return the inverse of this matrix. If {@link #det} returns 0, this may return a matrix where
     * {@link #isFinite} is false.
     */
    public Mat2 inv() {
        double d = det();
        return new Mat2(
            mat[3] / d,
            -mat[2] / d,
            -mat[1] / d,
            mat[0] / d
        );
    }

    /**
     * Returns whether all matrix elements {@link Double#isFinite are finite}.
     *
     * @return whether all matrix elements are a valid, real, nonexceptional number.
     */
    public boolean isFinite() {
        return Double.isFinite(mat[0])
            && Double.isFinite(mat[1])
            && Double.isFinite(mat[2])
            && Double.isFinite(mat[3]);
    }

    /**
     * Returns a column of the matrix as a vector.
     *
     * @param num the index of column to return. 0 indicates the left column and 1 the right.
     * @return The requested column as a new vector.
     * @throws IllegalArgumentException If the index is out of bounds.
     */
    public Vec2 col(int num) {
        if (num == 0) {
            return new Vec2(mat[0], mat[2]);
        } else if (num == 1) {
            return new Vec2(mat[1], mat[3]);
        } else {
            throw new IllegalArgumentException("Bad column number " + num);
        }
    }

    /**
     * Returns a row of the matrix as a vector.
     *
     * @param num the index of row to return. 1 indicates the top row and 0 the bottom.
     * @return The requested row as a new vector.
     * @throws IllegalArgumentException If the index is out of bounds.
     */
    public Vec2 row(int num) {
        if (num == 0) {
            return new Vec2(mat[0], mat[1]);
        } else if (num == 1) {
            return new Vec2(mat[2], mat[3]);
        } else {
            throw new IllegalArgumentException("Bad row number " + num);
        }
    }

    /**
     * Gets an element of the matrix.
     *
     * @param x the column index of the element to get.
     * @param y the row index of the element to get.
     * @return The requested element.
     */
    public double elem(int x, int y) {
        return mat[y * 2 + x];
    }
}
