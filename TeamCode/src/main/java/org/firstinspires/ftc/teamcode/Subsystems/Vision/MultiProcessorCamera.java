package org.firstinspires.ftc.teamcode.Subsystems.Vision;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;
import org.firstinspires.ftc.teamcode.Config.WildfireDetectionConfig;
import org.firstinspires.ftc.teamcode.Util.TelemetryDivider;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.Optional;

import global.first.IgnitingInnovationGameDatabase;

public class MultiProcessorCamera {
    private WebcamName camera;
    private Telemetry telemetry;
    private VisionPortal visionPortal;
    private WildfireVisualProcessorV3 wildfireProcessor;
    private AprilTagProcessor aprilTagProcessor;


    public static final int LEFT_LOWER_SECTION_ID = 0;
    public static final int RIGHT_LOWER_SECTION_ID = 1;
    public static final int LEFT_UPPER_SECTION_ID = 2;
    public static final int RIGHT_UPPER_SECTION_ID = 3;

    public MultiProcessorCamera(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        camera = hardwareMap.get(WebcamName.class, HardwareMapConfig.WEBCAM_ID);

        wildfireProcessor = new WildfireVisualProcessorV3(telemetry);

        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .setTagLibrary(IgnitingInnovationGameDatabase.getIgnitingInnovationTagLibrary())
                .build();


        visionPortal = new VisionPortal.Builder()
                .setCamera(camera)
                .addProcessors(wildfireProcessor, aprilTagProcessor)
                .enableLiveView(true)
                .build();

        telemetry.addData("Camera Status", "Initialized");
    }

    public double[] getLumaValues() {
        return new double[]{
                wildfireProcessor.getLeftLowerLuma(),
                wildfireProcessor.getRightLowerLuma(),
                wildfireProcessor.getLeftUpperLuma(),
                wildfireProcessor.getRightUpperLuma()
        };
    }

    public double[] getScaledLumaValues() {
        return new double[]{
                wildfireProcessor.getLeftLowerLuma() * WildfireDetectionConfig.LOWER_LUMA_GAIN,
                wildfireProcessor.getRightLowerLuma() * WildfireDetectionConfig.LOWER_LUMA_GAIN,
                wildfireProcessor.getLeftUpperLuma() * WildfireDetectionConfig.UPPER_LUMA_GAIN,
                wildfireProcessor.getRightUpperLuma() * WildfireDetectionConfig.UPPER_LUMA_GAIN
        };
    }


    public Optional<AprilTagDetection> getClosestDetection() {

        List<AprilTagDetection> detections = aprilTagProcessor.getDetections();

        if (detections == null) return Optional.empty();

        AprilTagDetection closest = null;
        for (AprilTagDetection detection : detections) {
            if (detection == null) continue;
            if (detection.ftcPose == null) continue;

            if (closest == null) {
                closest = detection;
                continue;
            }

            if (closest.ftcPose.range > detection.ftcPose.range)
                closest = detection;
        }

        if (closest == null) return Optional.empty();

        return Optional.of(closest);
    }

    public Optional<Double> getDistanceFromClosest() {
        Optional<AprilTagDetection> closest = getClosestDetection();
        if (closest.isPresent()) {
            if (closest.get().ftcPose == null) return Optional.empty();
            return Optional.of(closest.get().ftcPose.range);
        }
        return Optional.empty();
    }


    public void debug() {
        telemetry.addLine(TelemetryDivider.generate("April Tag Processor", 10));

        List<AprilTagDetection> detections = aprilTagProcessor.getDetections();
        List<AprilTagDetection> freshDetections = aprilTagProcessor.getFreshDetections();

        telemetry.addData("Detections", detections == null ? "none" : detections);
        telemetry.addData("Fresh Detections", freshDetections == null ? "none" : freshDetections);

        telemetry.addLine(TelemetryDivider.generate("Wildfire Processor", 10));

        double[] scaledWildfireSections = getScaledLumaValues();
        double[] unscaledWildfireSections = getLumaValues();


        telemetry.addLine(TelemetryDivider.generate("Unscaled Luma", 5));
        telemetry.addData("Left Lower Luma", unscaledWildfireSections[WildfireCamera.LEFT_LOWER_SECTION_ID]);
        telemetry.addData("Left Upper Luma", unscaledWildfireSections[WildfireCamera.LEFT_UPPER_SECTION_ID]);
        telemetry.addData("Right Lower Luma", unscaledWildfireSections[WildfireCamera.RIGHT_LOWER_SECTION_ID]);
        telemetry.addData("Right Upper Luma", unscaledWildfireSections[WildfireCamera.RIGHT_UPPER_SECTION_ID]);
        telemetry.addLine();

        telemetry.addLine(TelemetryDivider.generate("Scaled Luma", 5));
        telemetry.addData("Left Lower Luma", scaledWildfireSections[WildfireCamera.LEFT_LOWER_SECTION_ID]);
        telemetry.addData("Left Upper Luma", scaledWildfireSections[WildfireCamera.LEFT_UPPER_SECTION_ID]);
        telemetry.addData("Right Lower Luma", scaledWildfireSections[WildfireCamera.RIGHT_LOWER_SECTION_ID]);
        telemetry.addData("Right Upper Luma", scaledWildfireSections[WildfireCamera.RIGHT_UPPER_SECTION_ID]);
    }

}
