package org.firstinspires.ftc.teamcode.layer;

import java.util.Iterator;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.TeleopAgitatorTask;
import org.firstinspires.ftc.teamcode.task.TeleopFeederTask;
import org.firstinspires.ftc.teamcode.task.TeleopShooterTask;

public final class ShooterIntakeLayer implements Layer {

    private static final String coreHexFeederName = "coreHex";

    private static final String flywheelMotorName = "flywheel";

    private static final String agitatorServoName = "servo";

    private DcMotor coreHexFeeder;

    private DcMotor flywheel;

    private CRServo agitator;

    private boolean isFinished;

    public ShooterIntakeLayer() {}

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        coreHexFeeder = setupInfo.getHardwareMap().get(DcMotor.class, coreHexFeederName);
        flywheel = setupInfo.getHardwareMap().get(DcMotor.class, flywheelMotorName);
        agitator = setupInfo.getHardwareMap().get(CRServo.class, agitatorServoName);
        isFinished = true;
    }
    
    @Override
    public boolean isTaskDone() {
        return isFinished;
    }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {return null;}

    @Override
    public void acceptTask(Task task){
        if (task instanceof TeleopAgitatorTask) {
            TeleopAgitatorTask castedTask = (TeleopAgitatorTask) task;
            agitator.setPower(castedTask.getServoPower());
        } else if (task instanceof TeleopFeederTask) {
            TeleopFeederTask castedTask = (TeleopFeederTask) task;
            coreHexFeeder.setPower(castedTask.getCoreHexPower());
        } else if (task instanceof TeleopShooterTask) {
            TeleopShooterTask castedTask = (TeleopShooterTask) task;
            ((DcMotorEx) flywheel).setVelocity(castedTask.getFlywheelVelocity());
        }
        // else if (task instanceof TeleopShooterTask) {
        //     TeleopShooterTask
        // }
    }
}
