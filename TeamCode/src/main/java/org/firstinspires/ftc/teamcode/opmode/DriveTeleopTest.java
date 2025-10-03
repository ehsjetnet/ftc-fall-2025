package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode;

import java.util.list;
import java.util.Arrays;

import org.firstinspires.ftc.layer.GamepadInputGenerator;
import org.firstinspires.ftc.layer.JoystickHoloDriveMapping;
import org.firstinspires.ftc.layer.Layer;
import org.firstinspires.ftc.layer.drive.MecanumDrive;

@Teleop(name="Test Drive")
public final class DriveTeleopTest implements AbstractLayerOpMode{

    public DriveTeleopTest() { }

    @Override
    public List<Layer> getLayers() {
        return Arrays.asList(
            new MecanumDrive(),
            new JoystickHoloDriveMapping(),
            new GamepadInputGenerator()
        );
    }
}