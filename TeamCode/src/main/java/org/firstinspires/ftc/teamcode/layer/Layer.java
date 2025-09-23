package org.firstinspires.ftc.teamcode.layer;

import java.util.Iterator;

import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * A modular unit of robot functionality.
 * Layers interact with other layers by accepting tasks from the above layer and submitting tasks to
 * the below layer. This represents the breaking down of a complex or abstract task into more simple
 * and concrete ones. (A task is an instruction passed from a layer to its subordinate. Tasks range
 * widely in their concreteness and can be as vague as
 * {@link org.firstinspires.ftc.teamcode.task.WinTask "win the game"} or as specific as
 * {@link org.firstinspires.ftc.teamcode.task.AxialMovementTask "move forward 2 meters."})
 *
 * <p>See {@link org.firstinspires.ftc.teamcode.RobotController} for detailed information about
 * layer processing.
 */
public interface Layer {
    /**
     * Performs layer setup that requires access to hardware and the RobotController.
     *
     * @param setup - LayerSetupInfo provided to set up the layer.
     */
    void setup(LayerSetupInfo setup);

    /**
     * Returns whether the layer is ready to accept a new task.
     * This method should be free of any side effects; move code mutating state to update or
     * acceptTask.
     *
     * @return true if the layer has finished processing the last accepted task, if any.
     */
    boolean isTaskDone();

    /**
     * Returns the next subordinate tasks produced from this layer's current task.
     * Calculates the next subordinate tasks that should be submitted to the below layer. If the
     * returned iterator contains more than one task, all are offered to the lower layer.
     *
     * @param completed - an iterable of tasks completed since the last call to update.
     * @return The next task that the lower layer should run. Must not be null unless this is the
     * bottommost layer.
     */
    Iterator<Task> update(Iterable<Task> completed);

    /**
     * Sets the layer's current task.
     * Accepts a task from the above layer.
     * Behavior is only defined if {@link Layer#isTaskDone} returns true.
     *
     * @param task - the task this layer should start processing.
     * @throws UnsupportedTaskException - this layer does not support the given task.
     * Implementations must not change the internal state of the layer if this is thrown; isTaskDone
     * should still return true.
     */
    void acceptTask(Task task);
}
