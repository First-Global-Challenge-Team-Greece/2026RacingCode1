package org.firstinspires.ftc.teamcode.OpMode.Sample;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.Config.DriverAlertConfig;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;
import org.firstinspires.ftc.teamcode.Config.TankDriveConfig;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.TagCamera;
import org.firstinspires.ftc.teamcode.Util.DriverNotifier;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.Optional;

@TeleOp()
public class DriverAlertSample extends OpMode {

    private TagCamera tagCamera;
    private DriverNotifier driverNotifier;

    private DcMotorEx leftMotor;
    private DcMotorEx rightMotor;

    private IMU imu;

    @Override
    public void init() {
        tagCamera = new TagCamera(hardwareMap, telemetry);

        driverNotifier = new DriverNotifier(telemetry, gamepad1);

        leftMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.LEFT_DRIVE_MOTOR_ID);
        rightMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.RIGHT_DRIVE_MOTOR_ID);

        leftMotor.setDirection(TankDriveConfig.LEFT_MOTOR_DIRECTION);
        rightMotor.setDirection(TankDriveConfig.RIGHT_MOTOR_DIRECTION);

        imu = hardwareMap.get(IMU.class, HardwareMapConfig.IMU_ID);

        imu.initialize(
                new IMU.Parameters(
                        TankDriveConfig.IMU_ORIENTATION_ON_ROBOT
                )
        );
    }

    @Override
    public void loop() {
        Optional<AprilTagDetection> closestTag = tagCamera.getClosestDetection();
        boolean closestTagExists = closestTag.isPresent();

        int id = closestTagExists ? closestTag.get().id : -1;
        double distance = closestTagExists ? closestTag.get().ftcPose.range : -1;
        double bearing = closestTagExists ? closestTag.get().ftcPose.bearing : 0;

        double robotHeading = imu.getRobotYawPitchRollAngles().getYaw();

        leftMotor.setPower(gamepad1.left_stick_y + gamepad1.right_stick_x);
        rightMotor.setPower(gamepad1.left_stick_y - gamepad1.right_stick_x);

        double actualDistance = -1;

        if (closestTagExists) {
            actualDistance = Math.cos(Math.toRadians(bearing)) * distance;
        }

        if (closestTagExists && actualDistance < DriverAlertConfig.MAXIMUM_DISTANCE
                && (Math.abs(DriverAlertConfig.PARALLEL_TO_GOAL_ROBOT_HEADING - robotHeading) < DriverAlertConfig.BEARING_MARGIN)) {
            driverNotifier.sendMessage(DriverNotifier.MessageLevel.WARNING);
        }

        telemetry.addData("Closest Tag", id);
        telemetry.addData("Distance From Tag", distance);
        telemetry.addData("Tag Bearing", bearing);
    }
}
