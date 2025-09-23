package org.firstinspires.ftc.teamcode.task;

import org.firstinspires.ftc.teamcode.matrix.Mat3;

/**
 * Instructs the robot to pathfind to a field space transform while avoiding obstacles.
 */
public final class MoveToFieldTask implements Task {
    /**
     * The goal field space transform.
     */
    private Mat3 transform;

    /**
     * Constructs a MoveToFieldTask.
     *
     * @param transform the goal field space transform.
     */
    public MoveToFieldTask(Mat3 transform) {
        this.transform = transform;
    }

    /**
     * Returns the goal transform.
     *
     * @return The goal field space transform.
     */
    public Mat3 getGoalTransform() {
        return transform;
    }
}
