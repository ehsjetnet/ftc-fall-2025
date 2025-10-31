package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;
import java.util.Arrays;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.MultiplexLayer;
import org.firstinspires.ftc.teamcode.layer.TopLayerSequence;
import org.firstinspires.ftc.teamcode.layer.ShooterIntakeLayer;
import org.firstinspires.ftc.teamcode.layer.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.layer.input.GamepadInputGenerator;
import org.firstinspires.ftc.teamcode.layer.input.mapping.JoystickHoloDriveMapping;
import org.firstinspires.ftc.teamcode.layer.input.mapping.TeleopAgitatorMapping;
import org.firstinspires.ftc.teamcode.layer.input.mapping.TeleopFeederMapping;
import org.firstinspires.ftc.teamcode.layer.input.mapping.TeleopShooterMapping;
import org.firstinspires.ftc.teamcode.layer.input.mapping.AutoShooterMapping;

@TeleOp(name="Base Kit Teleop")
public final class BasekitBotOpMode extends AbstractLayerOpMode{

    public BasekitBotOpMode() { }

    @Override
    protected List<Layer> getLayers() {
        return Arrays.asList(
            new MultiplexLayer(Arrays.asList(
                new MecanumDrive(),
                new ShooterIntakeLayer()
            )),
            new MultiplexLayer(Arrays.asList(
                new JoystickHoloDriveMapping(),
                new TeleopAgitatorMapping(),
                new TeleopFeederMapping(),
                new TeleopShooterMapping().
                new AutoShooterMapping()
            )),
            new TopLayerSequence(Arrays.asList(
                new GamepadInputGenerator()
            ))
        );
    }
}