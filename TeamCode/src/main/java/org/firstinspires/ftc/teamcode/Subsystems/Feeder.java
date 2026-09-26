package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Config.FeederConfig;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;

public class Feeder {
    private DcMotorEx feederMotor;
    private boolean isShooterReady = false;
    private Telemetry telemetry;

    private CRServo rightMixerServo;
    private CRServo leftMixerServo;

    private boolean isActive = true;

    public Feeder(HardwareMap hardwareMap, Telemetry telemetry) {
        feederMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.FEEDER_MOTOR_ID);
        feederMotor.setDirection(FeederConfig.FEEDER_DIRECTION);

        rightMixerServo = hardwareMap.get(CRServo.class, HardwareMapConfig.RIGHT_MIXER_CONTINUOUS_SERVO_ID);
        leftMixerServo = hardwareMap.get(CRServo.class, HardwareMapConfig.LEFT_MIXER_CONTINUOUS_SERVO_ID);
        rightMixerServo.setDirection(FeederConfig.RIGHT_MIXER_DIRECTION);
        leftMixerServo.setDirection(FeederConfig.LEFT_MIXER_DIRECTION);
        this.telemetry = telemetry;
    }

    public void shooterInterface(boolean isShooterReady) {
        this.isShooterReady = isShooterReady;
    }

    public void feed() {
        if (!isActive) return;
        if (isShooterReady) {
            feederMotor.setPower(FeederConfig.MAX_MOTOR_POWER);
        } else stop();
    }

    public void mix() {
        if (!isActive) return;
        rightMixerServo.setPower(FeederConfig.MAX_CONTINUOUS_SERVO_POWER);
        leftMixerServo.setPower(FeederConfig.MAX_CONTINUOUS_SERVO_POWER);
    }

    public void stop() {
        feederMotor.setPower(0);
    }

    public void shutdown() {
        rightMixerServo.setPower(0);
        leftMixerServo.setPower(0);

        feederMotor.close();
        isActive = false;
    }

    public double[] getMotorCurrents() {
        return new double[] {
                feederMotor.getCurrent(CurrentUnit.AMPS)
        };
    }

    public void debug() {
        telemetry.addLine("|----- Feeder -----|");
        telemetry.addData("Feeder Motor Current", feederMotor.getCurrent(CurrentUnit.AMPS));
        telemetry.addData("Shooter Ready", isShooterReady);
    }
}
