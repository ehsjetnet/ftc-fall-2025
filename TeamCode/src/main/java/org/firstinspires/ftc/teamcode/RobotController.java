package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.localization.RobotLocalizer;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Executes a Layer stack.
 * This forms the core of the robot's control logic: layers processing tasks by computing subtasks
 * and then delegating to subordinates.
 * To process a "tick" on the layer stack, RobotController:
 * <ul>
 * <li>Finds the bottommost layer whose {@link Layer#isTaskDone} method returns false, indicating
 * that it has more subtasks to emit.
 * <li>Requests a new subtask from it with the {@link Layer#update} method, which is then given to
 * the layer below it in the stack with {@link Layer#acceptTask} method. A layer may supply more
 * than one subtask in this step, in which case the layer below it is offered each of the emitted
 * subtasks while its isTaskDone method still returns true. If the lower layer's isTaskDone method
 * returns false while there are still tasks to be consumed, an exception is thrown.
 * <li>Applies the preceeding step to each lower layer in turn, "trickling down" the new subtasks.
 * The return value of the bottommost layer's update method is ignored; this is assumed to be a
 * drive layer that does not produce any tasks to delegate.
 * </ul>
 * Through creative Layer implementations such as
 * {@link org.firstinspires.ftc.teamcode.layer.MultiplexLayer}, this system enables complex logic to
 * be described modularly and with loose coupling.
 */
public class RobotController {
    /**
     * The number of unconsumed tasks by a layer to report in the exception message.
     * Prevents an infinite loop if a layer's update method returns an non-terminating iterator.
     */
    private static final int MAX_UNCONSUMED_REPORT_TASKS = 4;

    /**
     * Listeners to fire at the start of each {@link #update}.
     */
    private ArrayList<Runnable> updateListeners;

    /**
     * Listeners to fire during the first {@link #update} the layer stack finishes executing.
     */
    private ArrayList<Runnable> teardownListeners;

    /**
     * The current stack of layers and some metadata needed to execute them.
     */
    private List<LayerInfo> layers;

    /**
     * Constructs a RobotController.
     */
    public RobotController() {
        updateListeners = new ArrayList<>();
        layers = null;
    }

    /**
     * Initializes the controller with the given layers.
     *
     * @param hardwareMap - HardwareMap used to retrieve interfaces for robot hardware.
     * @param robotLocalizer - the RobotLocalizer to get robot transformation info from during the
     * execution.
     * @param layerStack - the layer stack to use.
     * @param gamepad0 - the first connected Gamepad, or null if none is connected or available.
     * @param gamepad1 - the second connected Gamepad, or null if none is connected or available.
     */
    public void setup(
        HardwareMap hardwareMap,
        RobotLocalizer robotLocalizer,
        List<Layer> layerStack,
        Gamepad gamepad0,
        Gamepad gamepad1,
    ) {
        LayerSetupInfo setupInfo = new LayerSetupInfo(
            hardwareMap,
            this,
            robotLocalizer,
            gamepad0,
            gamepad1,
        );
        this.layers = layerStack.stream().map(layer -> {
            layer.setup(setupInfo);
            return new LayerInfo(layer);
        }).collect(Collectors.toList());
    }

    /**
     * Performs incremental work and returns whether layers have completed all tasks.
     * Performs incremental work on the bottommost layer of the configured stack, invoking upper
     * layers as necessary when lower layers complete their current tasks.
     *
     * @return whether the topmost layer (and by extension, the whole stack of layers) is exhausted
     * of tasks. When this happens, update listeners are notified and then unregistered.
     */
    public boolean update() {
        // Call all update listeners
        for (Runnable listener : updateListeners) {
            listener.run();
        }

        // Do work on layers
        if (layers == null) {
            return true;
        }
        LayerInfo layer = null;
        ListIterator<LayerInfo> layerIter = layers.listIterator();
        while (true) {
            layer = layerIter.next();
            if (!layerIter.hasNext()) {
                // No tasks left in any layer, inform all listeners of completion
                for (Runnable listener : teardownListeners) {
                    listener.run();
                }
                updateListeners.clear();
                layers = null;
                return true;
            }
        }
        layerIter.previous(); // Throw away current layer
        Iterator<Task> tasks;
        while (true) {
            if (!layerIter.hasPrevious()) {
                // Discard bottommost layer's return value
                layer.update(Collections.emptyList());
                break;
            }
            LayerInfo oldLayer = layer;
            layer = layerIter.previous();
            tasks = oldLayer.update(layer.getLastTasks());
            if (tasks == null) {
                throw new NullPointerException(
                    String.format(
                        "Layer '%s' returned null from update.",
                        oldLayer.getName()
                    )
                );
            }
            if (!tasks.hasNext()) {
                break; // Nothing to do for now. TODO: hacky fix
            }
            while (tasks.hasNext() && layer.isTaskDone()) {
                Task task = tasks.next();
                if (task == null) {
                    throw new NullPointerException(
                        String.format(
                            "Layer '%s' returned null as a subtask.",
                            oldLayer.getName()
                        )
                    );
                }
                layer.acceptTask(task);
            }
            if (tasks.hasNext()) {
                String errMsg = "Layer '" + layer.getName() + "' did not consume all"
                    + " tasks from upper layer. Remaining tasks: ";
                for (int i = 0; i < MAX_UNCONSUMED_REPORT_TASKS && tasks.hasNext(); ++i) {
                    errMsg += tasks.next().getClass().getSimpleName() + (tasks.hasNext() ? ", " : "");
                }
                if (tasks.hasNext()) {
                    errMsg += " (and more)";
                }
                throw new UnsupportedTaskException(errMsg);
            }
        }
        return false;
    }

    /**
     * Registers a function to be called on every update.
     * Registers a function to be called on every update of the controller before layer work is
     * performed. Listeners are executed in registration order. After teardown,
     * listeners are unregistered.
     *
     * @param listener - the function to be registered as an update listener.
     */
    public void addUpdateListener(Runnable listener) {
        updateListeners.add(listener);
    }

    /**
     * Registers a function to be called when the layer stack finishes executing.
     * On the first update after the topmost layer runs out of tasks, the
     * listeners are called in registration order, then unregistered.
     *
     * @param listener - the function to be registered as an update listener.
     */
    public void addTeardownListener(Runnable listener) {
        teardownListeners.add(listener);
    }

    /**
     * Thinly wraps a Layer while storing its last accepted task.
     */
    private static class LayerInfo {
        /**
         * The contained Layer.
         */
        private Layer layer;

        /**
         * The last tasks the contained Layer accepted at once.
         */
        private ArrayList<Task> lastTasks;

        /**
         * Whether the previous accepted task satisfied the Layer's need for new tasks.
         */
        private boolean lastTaskSaturated;

        /**
         * Constructs a LayerInfo.
         *
         * @param layer - the Layer to contain.
         */
        LayerInfo(Layer layer) {
            this.layer = layer;
            lastTasks = new ArrayList<>();
            lastTaskSaturated = true;
        }

        /**
         * Returns the implementing class name of the contained Layer.
         *
         * @return the contained Layer's concrete class name.
         */
        public String getName() {
            return layer.getClass().getSimpleName();
        }

        /**
         * Calls {@link Layer#isTaskDone} on the contained Layer.
         *
         * @return whether the contained Layer is finished processing its last accepted task.
         */
        public boolean isTaskDone() {
            return layer.isTaskDone();
        }

        /**
         * Calls {@link Layer#update} on the contained Layer.
         *
         * @param completed - an iterable of the tasks emitted by this Layer that have been
         * completed since the last call to this method.
         * @return an iterator of the tasks for the layer below to accept.
         */
        public Iterator<Task> update(Iterable<Task> completed) {
            return layer.update(completed);
        }

        /**
         * Calls {@link Layer#acceptTask} on the contained Layer.
         * Must not be called if {@link #isTaskDone} returns false.
         *
         * @param task - the task the contained layer should be offered.
         */
        public void acceptTask(Task task) {
            if (lastTaskSaturated) {
                lastTasks.clear();
            }
            lastTasks.add(task);
            layer.acceptTask(task);
            lastTaskSaturated = !layer.isTaskDone();
        }

        /**
         * Returns the layer's last accepted tasks.
         *
         * @return an iterable of the tasks last accepted by the layer.
         * @see #acceptTask
         */
        public Iterable<Task> getLastTasks() {
            return lastTasks;
        }
    }
}
