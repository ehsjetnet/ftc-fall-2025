package org.firstinspires.ftc.teamcode.layer;

import java.util.Iterator;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.IntakeTeleopTask;

public final class IntakeLayer implements Layer {

    private static final String coreHexFeederName = "coreHex";

    private static final String agitatorServoName = "servo";

    private DcMotor coreHexFeeder;

    private CRServo agitator;

    private boolean isFinished;

    public IntakeLayer() {}

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        coreHexFeeder = setupInfo.getHardwareMap().get(DcMotor.class, coreHexFeederName);
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
        if (task instanceof IntakeTeleopTask) {
            IntakeTeleopTask castedTask = (IntakeTeleopTask) task;
            coreHexFeeder.setPower(castedTask.getCoreHexPower());
            agitator.setPower(castedTask.getServoPower());
        }
    }


}
