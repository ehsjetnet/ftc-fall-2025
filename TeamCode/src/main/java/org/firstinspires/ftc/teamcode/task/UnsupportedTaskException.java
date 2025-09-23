package org.firstinspires.ftc.teamcode.task;

import org.firstinspires.ftc.teamcode.layer.Layer;

/**
 * Exception raised by layers when accepting a task type they don't support.
 */
public class UnsupportedTaskException extends IllegalArgumentException {
    /**
     * Constructs an UnsupportedTaskException for a layer that does not support a task with a
     * standard error message.
     *
     * @param layer - the Layer throwing the exception.
     * @param task - the Task that the layer rejected.
     */
    public UnsupportedTaskException(Layer layer, Task task) {
        super("Layer '" + layer.getClass().getSimpleName() + "' does not support task of type '"
            + task.getClass().getSimpleName() + "'.");
    }

    /**
     * Constructs an UnsupportedTaskException with a custom error message.
     *
     * @param msg - the exception message.
     */
    public UnsupportedTaskException(String msg) {
        super(msg);
    }
}
