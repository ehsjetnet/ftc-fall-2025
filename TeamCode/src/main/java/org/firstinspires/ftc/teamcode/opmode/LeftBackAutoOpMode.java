package org.firstinspires.ftc.teamcode.opmode;

import java.util.Arrays;
import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.MultiplexLayer;
import org.firstinspires.ftc.teamcode.layer.TopLayerSequence;
import org.firstinspires.ftc.teamcode.layer.WinLayer;
import org.firstinspires.ftc.teamcode.layer.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.layer.ShooterIntakeLayer;
import org.firstinspires.ftc.teamcode.layer.autonomous.LeftBackAuto;

@Autonomous(name="Left Back Auto")
public final class LeftBackAutoOpMode extends AbstractLayerOpMode {

	public LeftBackAutoOpMode() { }

	@Override
	protected List<Layer> getLayers() {
		return Arrays.asList(
			new MecanumDrive(),
			new LeftBackAuto(),
			new WinLayer()
		);
	}
}
