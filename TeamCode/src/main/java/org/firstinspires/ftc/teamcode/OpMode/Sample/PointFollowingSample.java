package org.firstinspires.ftc.teamcode.OpMode.Sample;

import com.github.bouyio.cyancore.debugger.Debuggers;
import com.github.bouyio.cyancore.geomery.Point;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.TankDrive;

@TeleOp()
public class PointFollowingSample extends OpMode {

    private TankDrive tankDrive;

    private Point point = new Point(130, 130);

    @Override
    public void init() {
        tankDrive = new TankDrive(hardwareMap, telemetry, TankDrive.DriveMode.FIELD_CENTRIC);
    }

    @Override
    public void loop() {
        telemetry.addData(">", tankDrive.getCurrentPosition().toString());

        if (gamepad1.left_bumper) {
            tankDrive.followPoint(point);
        } else {
            tankDrive.driveRobotCentric(0, 0);
        }

    }
}
