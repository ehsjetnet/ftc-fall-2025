package org.firstinspires.ftc.teamcode.layer.autonomous;

import java.util.ArrayList;

import org.firstinspires.ftc.teamcode.layer.AbstractQueuedLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.WinTask;
import org.firstinspires.ftc.teamcode.task.LinearMovementTask;
import org.firstinspires.ftc.teamcode.Units;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;
import org.firstinspires.ftc.teamcode.task.AutoShooterTask;

public final class FrontAuto extends AbstractQueuedLayer {

    public static final double backupDistance = 1.5;

    private ArrayList<Task> queue;

    public FrontAuto() {
        queue = new ArrayList<>();
    }

    @Override
    public void setup(LayerSetupInfo setupInfo) {
    }

    @Override
    public void acceptTask(Task task) {
        if (task instanceof WinTask) {
            queue.add(new LinearMovementTask(-Units.convert(backupDistance, Units.Distance.TILE, Units.Distance.M), 0));
            // queue.add(new AutoShooterTask(true, false));
	        setSubtasks(queue);
        } else {
            throw new UnsupportedTaskException("Front auto is brokey, please fix");
        }
    }

}
