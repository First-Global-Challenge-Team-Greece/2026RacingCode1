package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Config.BraceConfig;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;

public class Brace {
    private final DcMotorEx braceMotor;
    private final CRServo leftBraceExtensionServo;
    private final CRServo rightBraceExtensionServo;

    private TouchSensor lowerExtensionLimitSensor;
    private TouchSensor upperExtensionLimitSensor;

    private boolean extensionStatus = false;

    public Brace(HardwareMap hardwareMap) {
        braceMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.BRACE_MOTOR_ID);

        leftBraceExtensionServo = hardwareMap.get(CRServo.class, HardwareMapConfig.LEFT_BRACE_CONTINUOUS_SERVO_ID);
        rightBraceExtensionServo = hardwareMap.get(CRServo.class, HardwareMapConfig.RIGHT_BRACE_CONTINUOUS_SERVO_ID);
        rightBraceExtensionServo.setDirection(DcMotorSimple.Direction.REVERSE);

        if (BraceConfig.USE_SENSORS) {
            lowerExtensionLimitSensor = hardwareMap.get(TouchSensor.class, HardwareMapConfig.LOWER_BRACE_EXTENSION_SENSOR_ID);
            upperExtensionLimitSensor = hardwareMap.get(TouchSensor.class, HardwareMapConfig.UPPER_BRACE_EXTENSION_SENSOR_ID);
        }
    }

    public void extend() {
        extensionStatus = true;
    }
    public void retract() {
        extensionStatus = false;
    }

    public void extensionStateMachine() {
        if (extensionStatus && !upperExtensionLimitSensor.isPressed()) {
            leftBraceExtensionServo.setPower(BraceConfig.MAX_CONTINUOUS_SERVO_POWER);
            rightBraceExtensionServo.setPower(BraceConfig.MAX_CONTINUOUS_SERVO_POWER);
            return;
        }

        if (!extensionStatus && !lowerExtensionLimitSensor.isPressed()) {
            leftBraceExtensionServo.setPower(-BraceConfig.MAX_CONTINUOUS_SERVO_POWER);
            rightBraceExtensionServo.setPower(-BraceConfig.MAX_CONTINUOUS_SERVO_POWER);
            return;
        }

        leftBraceExtensionServo.setPower(0);
        rightBraceExtensionServo.setPower(0);
    }

    public void EXTENSION_INTERFACE(double power) {
        leftBraceExtensionServo.setPower(Range.clip(power, -BraceConfig.MAX_CONTINUOUS_SERVO_POWER, BraceConfig.MAX_CONTINUOUS_SERVO_POWER));
        rightBraceExtensionServo.setPower(Range.clip(power, -BraceConfig.MAX_CONTINUOUS_SERVO_POWER, BraceConfig.MAX_CONTINUOUS_SERVO_POWER));
    }

    public void climb() {
        braceMotor.setPower(BraceConfig.MAX_MOTOR_POWER);
    }

    public void stall() {
        braceMotor.setPower(0);
    }
}
