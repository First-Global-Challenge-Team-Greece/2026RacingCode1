package org.firstinspires.ftc.teamcode.OpMode.Sample;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.WildfireCamera;
import org.firstinspires.ftc.teamcode.Util.TelemetryDivider;

@TeleOp()
public class WildlifeVisionPortalSample extends OpMode {

    private WildfireCamera wildfireCamera;

    @Override
    public void init() {
        wildfireCamera = new WildfireCamera(hardwareMap, telemetry);

        telemetry.addLine("A sample for the wildfire detection.");
        telemetry.addLine("For live feed of the camera press the three dots on the right-hand corner of the driver station and select Camera Stream.");
        telemetry.addLine("Otherwise start the OpMode and check the live luma values of each section in the telemetry.");
    }

    @Override
    public void start() {
        telemetry.clear();
    }

    @Override
    public void loop() {
        double[] frameLumaValues = wildfireCamera.getLumaValues();

        telemetry.addLine(TelemetryDivider.generate("Unscaled Luma", 10));
        telemetry.addData("Left Lower Luma", frameLumaValues[WildfireCamera.LEFT_LOWER_SECTION_ID]);
        telemetry.addData("Left Upper Luma", frameLumaValues[WildfireCamera.LEFT_UPPER_SECTION_ID]);
        telemetry.addData("Right Lower Luma", frameLumaValues[WildfireCamera.RIGHT_LOWER_SECTION_ID]);
        telemetry.addData("Right Upper Luma", frameLumaValues[WildfireCamera.RIGHT_UPPER_SECTION_ID]);
        telemetry.addLine();

        double[] frameScaledLumaValues = wildfireCamera.getScaledLumaValues();

        telemetry.addLine(TelemetryDivider.generate("Scaled Luma", 10));
        telemetry.addData("Left Lower Luma", frameScaledLumaValues[WildfireCamera.LEFT_LOWER_SECTION_ID]);
        telemetry.addData("Left Upper Luma", frameScaledLumaValues[WildfireCamera.LEFT_UPPER_SECTION_ID]);
        telemetry.addData("Right Lower Luma", frameScaledLumaValues[WildfireCamera.RIGHT_LOWER_SECTION_ID]);
        telemetry.addData("Right Upper Luma", frameScaledLumaValues[WildfireCamera.RIGHT_UPPER_SECTION_ID]);


    }
}
