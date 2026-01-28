package org.firstinspires.ftc.teamcode.layer;

import java.util.Iterator;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.TeleopAgitatorTask;

import org.firstinspires.ftc.teamcode.task.TeleopFeederTask;
import org.firstinspires.ftc.teamcode.task.TeleopShooterTask;
import org.firstinspires.ftc.teamcode.task.AutoShooterTask;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ShooterIntakeLayer implements Layer {
    private AprilTagProcessor aprilTagProcessor;

    private VisionPortal visionPortal;

    private List<AprilTagDetection> detectedTags = new ArrayList<>();

    private static final String flywheelMotorName = "flywheel";

    private static final String intakeMotorName = "bandy";

    private static final String agitatorName = "servo 1";

    private ElapsedTime timer;

    private double startTime = 0;

    private DcMotor flywheel;

    private DcMotor bandy;

    private CRServo agitator;

    private boolean isFinished;

    public ShooterIntakeLayer() {
    }

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        flywheel = setupInfo.getHardwareMap().get(DcMotor.class, flywheelMotorName);
        bandy = setupInfo.getHardwareMap().get(DcMotor.class, intakeMotorName);
        agitator = setupInfo.getHardwareMap().get(CRServo.class, agitatorName);
        timer = new ElapsedTime();
        isFinished = true;


        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(setupInfo.getHardwareMap().get(WebcamName.class, "Webcam 1")); // This is a placeholder for the webcam name
        // builder.setCameraResolution(new Size(640, 480));
        builder.addProcessor(aprilTagProcessor);

        visionPortal = builder.build();

    }

    @Override
    public boolean isTaskDone() {
        return isFinished;
    }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
        return null;
    }

    @Override
    public void acceptTask(Task task) {
        if (task instanceof AutoShooterTask) {
            detectedTags = aprilTagProcessor.getDetections();
            AutoShooterTask castedTask = (AutoShooterTask) task;
            if (castedTask.getShoot()) {
                
                flywheel.setPower(1.0);
            } else if (castedTask.getIntake()) {
                bandy.setPower(0.75);
            } else if (castedTask.getEject()) {
                bandy.setPower(-1.0);
            } else if (castedTask.getShooterEject()) {
                flywheel.setPower(-1.0);
            } else {
                flywheel.setPower(0);
                bandy.setPower(0);
            }
        }
    }

    public List<AprilTagDetection> getDetectedTags() {
        return detectedTags;
    }

    public AprilTagDetection getTagBySpecificId(int id) {
        for(AprilTagDetection detection : detectedTags) {
            if(detection.id == id) {
                return detection;
            }
        }
        return null;
    }
     public void stop() {
        if(visionPortal != null) {
            visionPortal.close();
        }
     }
}

