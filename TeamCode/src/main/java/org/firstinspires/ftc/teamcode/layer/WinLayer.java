package org.firstinspires.ftc.teamcode.layer;

import java.util.Collections;
import java.util.Iterator;

import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;
import org.firstinspires.ftc.teamcode.task.WinTask;

/**
 * Layer to tell the robot to win.
 * Though seemingly frivolous, required as the top layer on autonomous layer
 * stacks to tell the strategy layers to emit something.
 */
public final class WinLayer implements Layer {
    /**
     * Whether the single WinTask has been emitted yet.
     */
    private boolean emittedWin;

    /**
     * Constructs a WinLayer.
     */
    public WinLayer() {
        emittedWin = false;
    }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public boolean isTaskDone() {
        return emittedWin;
    }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
        emittedWin = true;
        return Collections.singleton((Task)(new WinTask())).iterator();
    }

    @Override
    public void acceptTask(Task task) {
        throw new UnsupportedTaskException(this, task);
    }
}
