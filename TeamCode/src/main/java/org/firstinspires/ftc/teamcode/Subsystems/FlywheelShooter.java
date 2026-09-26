package org.firstinspires.ftc.teamcode.Subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config.FlywheelShooterConfig;

public abstract class FlywheelShooter {
    public enum ShooterState {
        STALLED, CHARGING, READY, IDLE
    }

    protected ShooterState shooterState = ShooterState.STALLED;

    protected final ShooterBallCounter shooterBallCounter = new ShooterBallCounter(FlywheelShooterConfig.RPM_DROP_COUNT_THRESHOLD);

    protected int shootCount = 0;

    protected void setVelocity(double velocity) {
        double feedForward = (FlywheelShooterConfig.KV * velocity) + FlywheelShooterConfig.KS * Math.signum(velocity);
        double error = velocity - getVelocity();
        double feedBack = error * FlywheelShooterConfig.KP;

        applyPower(feedBack + feedForward);
    }

    abstract void shutdown();
    abstract void applyPower(double power);
    abstract double getVelocity();

    protected boolean isShooterRpmReady() {
        return getVelocity() > FlywheelShooterConfig.RPM_THRESHOLD;
    }

    public ShooterState getShooterState() {
        return shooterState;
    }

    public void shooterStateMachine() {

        updateBallCounts();
        switch (shooterState) {
            case STALLED:
                setVelocity(FlywheelShooterConfig.STALLED_VELOCITY);
                break;
            case IDLE:
                setVelocity(FlywheelShooterConfig.IDLE_VELOCITY);
                break;
            case CHARGING:
                setVelocity(FlywheelShooterConfig.SHOOTING_VELOCITY);
                if (isShooterRpmReady()) {
                    shooterState = ShooterState.READY;
                }
                break;
            case READY:
                setVelocity(FlywheelShooterConfig.SHOOTING_VELOCITY);
                if (!isShooterRpmReady()) {
                    shooterState = ShooterState.CHARGING;
                }
                break;
        }
    }

    public void shoot() {
        if (shooterState != ShooterState.READY) {
            shooterState = ShooterState.CHARGING;
        }
    }

    public void idle() {
        shooterState = ShooterState.IDLE;
    }

    public void stop() {
        shooterState = ShooterState.STALLED;
    }

    private  void updateBallCounts() {
        if (shooterState == ShooterState.READY || shooterState == ShooterState.CHARGING) {
            shooterBallCounter.updateCount(getVelocity());
        }
        shootCount = shooterBallCounter.getBallCount();
    }

    public void debug(Telemetry telemetry) {
        telemetry.addData("Current Velocity", getVelocity());
        telemetry.addData("Is Shooter RPM Ready", isShooterRpmReady());
        telemetry.addData("Shooter State", shooterState);
        telemetry.addData("Shoot Count", shootCount);
    }

    public boolean isReady() {
        return shooterState == ShooterState.READY;
    }

    public void tune() {
        setVelocity(FlywheelShooterConfig.TUNING_VELOCITY);
    }
}
