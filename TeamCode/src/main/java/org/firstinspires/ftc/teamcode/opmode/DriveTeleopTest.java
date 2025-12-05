package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;
import java.util.Arrays;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.layer.input.GamepadInputGenerator;
import org.firstinspires.ftc.teamcode.layer.input.mapping.JoystickHoloDriveMapping;

@TeleOp(name="Drive")
public final class DriveTeleopTest extends AbstractLayerOpMode{

    public DriveTeleopTest() { }

    @Override
    protected List<Layer> getLayers() {
        return Arrays.asList(
            new MecanumDrive(),
            new JoystickHoloDriveMapping(),
            new GamepadInputGenerator()
        );
    }
}