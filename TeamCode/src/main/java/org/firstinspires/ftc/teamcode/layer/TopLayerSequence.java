package org.firstinspires.ftc.teamcode.layer;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Contains a sequence of top-level layers.
 * When each contained layer is exhausted of subtasks, the next layer in the sequence is used. This
 * layer is completed after reaching and completing the last contained layer.
 *
 * <p>A "top-level layer" is a Layer that never accepts a superior task during the program's lifetime.
 * An OpMode whose layer stack contains one top-level layer ends when that layer is asked for
 * another subtask but it has none to supply.
 */
public final class TopLayerSequence implements Layer {
    /**
     * The list of contained layers.
     */
    private List<Layer> layers;

    /**
     * The iterator of contained layers, progressed as they each run to completion.
     */
    private Iterator<Layer> layerIter;

    /**
     * The top-level layer currently being operated on.
     */
    private Layer layer;

    /**
     * Constructs a TopLayerSequence.
     *
     * @param layers - the list of top-level layers to iterate through.
     */
    public TopLayerSequence(List<Layer> layers) {
        this.layers = layers;
    }

    @Override
    public boolean isTaskDone() {
        return layer.isTaskDone() && !layerIter.hasNext();
    }

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        String name = "TopLayerSequence[" + layers.stream()
            .map(Object::getClass)
            .map(Class<?>::getSimpleName)
            .collect(Collectors.joining()) + "]";
        for (Layer l : layers) {
            l.setup(setupInfo);
        }
        this.layerIter = layers.iterator();
        layer = layerIter.next();
    }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
        Iterator<Task> subtasks = layer.update(completed);
        if (layer.isTaskDone() && layerIter.hasNext()) {
            layer = layerIter.next();
        }
        return subtasks;
    }

    @Override
    public void acceptTask(Task task) {
        throw new UnsupportedTaskException(this, task);
    }
}
