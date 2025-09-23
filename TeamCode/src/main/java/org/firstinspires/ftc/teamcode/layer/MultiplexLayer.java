package org.firstinspires.ftc.teamcode.layer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.firstinspires.ftc.teamcode.logging.Logger;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Acts on behalf on multiple component layers to handle multiple unrelated kinds of tasks from the
 * layers above.
 * Needed because the RobotController reads layers as a stack, not a tree.
 */
public final class MultiplexLayer implements Layer {
    /**
     * The list of component layers.
     */
    private final List<Layer> layers;

    /**
     * The logger.
     */
    private Logger logger;

    /**
     * Constructs a MultiplexLayer.
     *
     * @param layers - the layers this MultiplexLayer will contain.
     */
    public MultiplexLayer(List<Layer> layers) {
        this.layers = layers;
    }

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        String name = "MultiplexLayer[" + layers.stream()
            .map(Object::getClass)
            .map(Class<?>::getSimpleName)
            .collect(Collectors.joining()) + "]";
        logger = setupInfo.getLogger(name);
        for (Layer layer : layers) {
            layer.setup(setupInfo);
        }
    }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
        // Concatenates results of component layer update methods into a single stream, then creates
        // an iterator from the stream
        return layers.stream().flatMap(layer -> {
            if (layer.isTaskDone()) {
                return Stream.of();
            }
            Iterator<Task> tasks = layer.update(completed);
            if (tasks == null) {
                throw new NullPointerException(
                    String.format(
                        "Tasks from layer '%s' is null.",
                        layer.getClass().getSimpleName()
                    )
                );
            }
            // TODO: revert this after debugging; stuffing the tasks into an ArrayList defeats the
            // purpose of returning an iterator
            List<Task> taskList = new ArrayList<>();
            tasks.forEachRemaining(taskList::add);
            if (taskList.contains(null)) {
                throw new NullPointerException(
                    String.format(
                        "Tasks from layer '%s' contains null.",
                        layer.getClass().getSimpleName()
                    )
                );
            }

            return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                    taskList.iterator(), // layer.update(completed),
                    0
                ),
                false
            );
        }).iterator();
    }

    @Override
    public boolean isTaskDone() {
        return layers.stream().anyMatch(Layer::isTaskDone);
    }

    @Override
    public void acceptTask(Task task) {
        boolean anyAccepted = layers.stream().map((layer) -> {
            try {
                layer.acceptTask(task);
            } catch (UnsupportedTaskException e) {
                return false;
            }
            return true;
        }).reduce(false, (a, b) -> a || b); // Prevent short circuiting
        if (!anyAccepted) {
            // Should list component layers, not say MultiplexLayer
            throw new UnsupportedTaskException(this, task);
        }
    }
}
