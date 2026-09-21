package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Config.BraceConfig;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;

public class Brace {
    private final DcMotorEx braceMotor;
    private final CRServo leftBraceExtensionServo;
    private final CRServo rightBraceExtensionServo;

    public Brace(HardwareMap hardwareMap) {
        braceMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.BRACE_MOTOR_ID);

        leftBraceExtensionServo = hardwareMap.get(CRServo.class, HardwareMapConfig.LEFT_BRACE_CONTINUOUS_SERVO_ID);
        rightBraceExtensionServo = hardwareMap.get(CRServo.class, HardwareMapConfig.RIGHT_BRACE_CONTINUOUS_SERVO_ID);
        leftBraceExtensionServo.setDirection(BraceConfig.BRACE_MOTOR_DIRECTION);
    }

    public void extend() {
        leftBraceExtensionServo.setPower(BraceConfig.MAX_CONTINUOUS_SERVO_POWER);
        rightBraceExtensionServo.setPower(BraceConfig.MAX_CONTINUOUS_SERVO_POWER);
    }
    public void retract() {
        leftBraceExtensionServo.setPower(-BraceConfig.MAX_CONTINUOUS_SERVO_POWER);
        rightBraceExtensionServo.setPower(-BraceConfig.MAX_CONTINUOUS_SERVO_POWER);
    }
    public void stop() {
        leftBraceExtensionServo.setPower(0);
        rightBraceExtensionServo.setPower(0);
    }

    public void climb() {
        braceMotor.setPower(BraceConfig.MAX_MOTOR_POWER);
    }

    public void stall() {
        braceMotor.setPower(0);
    }
}
