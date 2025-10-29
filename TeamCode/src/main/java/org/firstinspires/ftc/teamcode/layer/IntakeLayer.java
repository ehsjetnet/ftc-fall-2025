package org.firstinspires.ftc.teamcode.layer;

import java.util.Iterator;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.Task;

public final class IntakeLayer implements Layer {

    private static final String coreHexFeederName = "placeholder";

    private static final String agitatorServoName = "placeholder";

    private DcMotor coreHexFeeder;

    private CRServo agitator;

    private boolean isFinished;

    public IntakeLayer() {}

    @Override
    public void setup(LayerSetupInfo initInfo) {
        coreHexFeeder = initInfo.getHardwareMap.get(DcMotor.class, coreHexFeederName);
        agitator = initInfo.getHardwareMap.get(CRServo.class, agitatorServoName);
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
        /*put tasks here once they have been made */
    }


}
