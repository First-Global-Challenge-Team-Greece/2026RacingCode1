package org.firstinspires.ftc.teamcode.OpMode.Sample;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.TankDrive;

@TeleOp()
public class FieldCentricTankDriveSample extends OpMode {

    private TankDrive tankDrive;

    @Override
    public void init() {
        tankDrive = new TankDrive(hardwareMap, telemetry, TankDrive.DriveMode.FIELD_CENTRIC);
    }


    @Override
    public void loop() {
        tankDrive.driveFieldCentric(gamepad1.left_stick_x, -gamepad1.left_stick_y);
    }
}
