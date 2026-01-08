package org.firstinspires.ftc.teamcode.opmode;

import java.util.Arrays;
import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.autonomous;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.MultiplexLayer;
import org.firstinspires.ftc.teamcode.layer.TopLayerSequence;
import org.firstinspires.ftc.teamcode.layer.WinLayer;
import org.firstinspires.ftc.teamcode.layer.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.layer.ShooterIntakeLayer;
import org.firstinspires.ftc.teamcode.layer.autonomous.FrontAuto;

@Autonomous(name = "Front Auto")
public final class FrontAutoOpMode extends AbstractLayerOpMode {

    public FrontAutoOpMode() {
    }

    @Override
    protected List<Layer> getLayers() {
        return Arrays.asList(
                new MultiplexLayer(Arrays.asList(
                        new MecanumDrive(),
                        new ShooterIntakeLayer())),
                new FrontAuto(),
                new TopLayerSequence(Arrays.asList(
                        new WinLayer())));
    }
}
