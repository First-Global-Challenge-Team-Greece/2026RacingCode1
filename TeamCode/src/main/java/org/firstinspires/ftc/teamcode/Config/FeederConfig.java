package org.firstinspires.ftc.teamcode.Config;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
public class FeederConfig {
    public static double MAX_MOTOR_POWER = 0.8;
    public static double MAX_CONTINUOUS_SERVO_POWER = 1;

    public static DcMotorSimple.Direction FEEDER_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static DcMotorSimple.Direction LEFT_MIXER_DIRECTION = DcMotorSimple.Direction.REVERSE;
    public static DcMotorSimple.Direction RIGHT_MIXER_DIRECTION = DcMotorSimple.Direction.FORWARD;

}
