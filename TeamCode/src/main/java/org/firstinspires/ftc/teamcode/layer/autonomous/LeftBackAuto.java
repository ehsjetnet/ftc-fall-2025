package org.firstinspires.ftc.teamcode.layer.autonomous;

import java.util.ArrayList;

import org.firstinspires.ftc.teamcode.layer.AbstractQueuedLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.WinTask;
import org.firstinspires.ftc.teamcode.task.LinearMovementTask;
import org.firstinspires.ftc.teamcode.Units;
import org.firstinspires.ftc.teamcode.task.TurnTask;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

public final class LeftBackAuto extends AbstractQueuedLayer {

	private static final double driveForwardDistance = 1.5;

	private static final double strafingDistance = 1.5;

	private static final double turnAngle = 0.25;

	private ArrayList<Task> queue;

	public LeftBackAuto() {
		queue = new ArrayList<>();
	}

	@Override
	public void setup(LayerSetupInfo setupInfo) {}

	@Override
	public void acceptTask(Task task) {
		if (task instanceof WinTask) {
			queue.add(new LinearMovementTask(Units.convert(driveForwardDistance, Units.Distance.TILE, Units.Distance.M), 0));
			// queue.add(new LinearMovementTask(0, -Units.convert(strafingDistance, Units.Distance.TILE, Units.Distance.M)));
			// queue.add(new TurnTask(Units.convert(turnAngle, Units.Angle.REV, Units.Angle.RAD)));
			setSubtasks(queue);
		} else {
			throw new UnsupportedTaskException("Left back auto is brokey, please fix");
		}
	}


}
