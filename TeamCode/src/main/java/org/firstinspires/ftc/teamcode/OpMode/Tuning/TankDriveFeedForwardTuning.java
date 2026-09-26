package org.firstinspires.ftc.teamcode.OpMode.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.TankDrive;

@TeleOp()
public class TankDriveFeedForwardTuning extends OpMode {

    private TankDrive tankDrive;

    @Override
    public void init() {
        tankDrive = new TankDrive(hardwareMap, telemetry, TankDrive.DriveMode.ROBOT_CENTRIC);
    }

    @Override
    public void loop() {
        tankDrive.tune();
    }
}
