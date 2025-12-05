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
import org.firstinspires.ftc.teamcode.task.AutoShooterTask;

public final class ShooterIntakeLayer implements Layer {

    private static final String flywheelMotorName = "flywheel";

    private static final String intakeMotorName = "bandy";

    private DcMotor flywheel;

    private DcMotor bandy;

    private boolean isFinished;

    public ShooterIntakeLayer() {}

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        flywheel = setupInfo.getHardwareMap().get(DcMotor.class, flywheelMotorName);
        bandy = setupInfo.getHardwareMap().get(DcMotor.class, intakeMotorName);
        flywheel.setDirection(DcMotor.Direction.REVERSE);
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
        if (task instanceof AutoShooterTask) {
            AutoShooterTask castedTask = (AutoShooterTask) task;
            ((DcMotorEx) flywheel).setVelocity(castedTask.getShootingVelocity());
            if (((DcMotorEx) flywheel).getVelocity() >= castedTask.getShootingVelocity()){
                bandy.setPower(0.5);
            } else {
                bandy.setPower(0);
            }
        }
    }
}
