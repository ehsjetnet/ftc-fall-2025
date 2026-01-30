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
import org.firstinspires.ftc.robotcore.external.Telemetry;
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

    private Telemetry telemetry;

    public ShooterIntakeLayer(Telemetry telemetry) {
        this.telemetry = telemetry;
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
                flywheel.setPower(1);
            } else if(castedTask.getExperimental()) {
                displayDetectionTelemetry(getTagBySpecificId(24));
                ((DcMotorEx) flywheel).setVelocity(1350);
                if(((DcMotorEx) flywheel).getVelocity() >= 1350) {
                    bandy.setPower(1.0);
                }
            }
             else if (castedTask.getIntake()) {
                bandy.setPower(0.75);
            } else if (castedTask.getEject()) {
                bandy.setPower(-1.0);
            } else if (castedTask.getShooterEject()) {
                flywheel.setPower(-1.0);
            } else {
                displayDetectionTelemetry(getTagBySpecificId(24));
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
     public void displayDetectionTelemetry(AprilTagDetection detectedId) {
        if (detectedId == null) {return;}
        if (detectedId.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detectedId.id, detectedId.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detectedId.ftcPose.x, detectedId.ftcPose.y, detectedId.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detectedId.ftcPose.pitch, detectedId.ftcPose.roll, detectedId.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detectedId.ftcPose.range, detectedId.ftcPose.bearing, detectedId.ftcPose.elevation));
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detectedId.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detectedId.center.x, detectedId.center.y));
            }
     }
}

