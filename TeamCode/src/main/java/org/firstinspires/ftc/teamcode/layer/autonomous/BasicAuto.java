package org.firstinspires.ftc.teamcode.layer.autonomous;

import java.util.ArrayList;

import org.firstinspires.ftc.teamcode.layer.AbstractQueuedLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.WinTask;
import org.firstinspires.ftc.teamcode.task.LinearMovementTask;
import org.firstinspires.ftc.teamcode.Units;

public final class BasicAuto extends AbstractQueuedLayer {

	private static final double driveForwardDistance = 0.5;

	private ArrayList<Task> queue;

	public BasicAuto() {
		queue = new ArrayList<>();
	}

	@Override
	public void setup(LayerSetupInfo setupInfo) {}

	@Override
	public void acceptTask(Task task) {
		if (task instanceof WinTask) {
			// add stuff here
		}
	}


}
