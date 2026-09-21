package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * A wrapper for the rumbling functionality of the gamepad.
 *
 * <p>
 *     This wrapper contains support for:
 *     <ul>
 *         <li>Mono messages (Vibrations)</li>
 *         <li>Stereo messages (Vibrations)</li>
 *         <li>Custom patterns</li>
 *         <li>Preprogrammed messages</li>
 *         <li>Message cooldown</li>
 *     </ul>
 * </p>
 * */
public class DriverNotifier {

    /**
     * <p>Maps the controller's vibration motors to channels.</p>
     *
     * <p>Left -> rumble1</p>
     * <p>Right -> rumble2</p>
     * */
    public enum Channel {
        LEFT, RIGHT
    }

    /**
     * Preprogrammed controller messages for specific purposes.
     * */
    public enum MessageLevel {
        /**Shall be used when an action/command has been completed.*/
        ACTION_COMPLETED(new Gamepad.RumbleEffect.Builder().addStep(1, 1, 100).addStep(1, 1, 200).build()),
        /**Shall be used when an action/command is queued.*/
        ACTION_QUEUED(new Gamepad.RumbleEffect.Builder().addStep(1, 1, 200).build()),
        /**Shall be used when communicating internal warnings.*/
        WARNING(new Gamepad.RumbleEffect.Builder().addStep(1, 0, 300).addStep(0, 1, 300).build()),
        /**Shall be used when communicating internal errors.*/
        ERROR(new Gamepad.RumbleEffect.Builder().addStep(0, 1, 600).addStep(1, 0, 600).build()),
        /**Shall be used when communicating internal critical errors.*/
        CRITICAL(new Gamepad.RumbleEffect.Builder()
                .addStep(1, 0, 300)
                .addStep(0, 1, 300)
                .addStep(1, 0, 300)
                .addStep(0, 1, 300)
                .build());

        MessageLevel(Gamepad.RumbleEffect pattern) {
            this.pattern = pattern;
        }

        public final Gamepad.RumbleEffect pattern;
    }

    private final Gamepad gamepad;

    /**The notification intensity.*/
    public double notificationVolume = 0.5;
    /**The notification cooldown time. (Milliseconds)*/
    public int rumblePaddingMS = 300;

    private long previousNotificationTime = 0;
    private int minimumDeltaTime = rumblePaddingMS;


    /**
     * Creates a notifier instance with a specified gamepad.
     *
     * @param gamepad the gamepad linked to this instance of the notifier
     * */
    public DriverNotifier(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    /**
     * <p>Creates a notification on both vibration channels for a specified duration.</p>
     * <p>
     *     After a notification is queued the method will wait the notification's duration plus the class
     *     specified cooldown time util it responds again.
     * </p>
     *
     * @param durationMS the duration of the notification. (Milliseconds)
     * */
    public void createStereoNotification(int durationMS) {
        validateInput(durationMS);

        if ((long) (System.currentTimeMillis() - previousNotificationTime) < minimumDeltaTime) {
            return;
        }

        minimumDeltaTime = rumblePaddingMS + durationMS;
        previousNotificationTime = System.currentTimeMillis();

        gamepad.rumble(notificationVolume, notificationVolume, durationMS);
    }

    /**
     * <p>Creates a notification on a specified vibration channel for a specified duration.</p>
     * <p>
     *     After a notification is queued the method will wait the notification's duration plus the class
     *     specified cooldown time util it responds again.
     * </p>
     *
     * @param durationMS the duration of the notification. (Milliseconds)
     * @param notificationChannel the specified channel of the notification.
     * */
    public void createMonoNotification(Channel notificationChannel, int durationMS) {
        validateInput(durationMS);

        if (System.currentTimeMillis() - previousNotificationTime < minimumDeltaTime)
            return;

        minimumDeltaTime = rumblePaddingMS + durationMS;
        previousNotificationTime = System.currentTimeMillis();

        gamepad.rumble(
                notificationChannel == Channel.LEFT ? notificationVolume : 0,
                notificationChannel == Channel.RIGHT ? notificationVolume : 0,
                durationMS
        );
    }

    /**
     * <p>Creates a notification of a specified pattern (effect).</p>
     * <p>
     *     After a notification is queued the method will wait the pattern's duration plus the class
     *     specified cooldown time util it responds again.
     * </p>
     *
     * @param pattern the pattern (effect) of the notification.
     * @see Gamepad.RumbleEffect
     * */
    public void createPatternNotification(Gamepad.RumbleEffect pattern) {
        if (System.currentTimeMillis() - previousNotificationTime < minimumDeltaTime)
            return;

        minimumDeltaTime = rumblePaddingMS;

        for (Gamepad.RumbleEffect.Step patternStep : pattern.steps) {
            validateInput(patternStep.duration);
            minimumDeltaTime += patternStep.duration;
        }

        previousNotificationTime = System.currentTimeMillis();

        gamepad.runRumbleEffect(pattern);
    }

    /**
     * <p>Creates a notification of a specified message (effect).</p>
     * <p>
     *     After a notification is queued the method will wait the message's duration plus the class
     *     specified cooldown time util it responds again.
     * </p>
     *
     * @param messageLevel the message level of the notification.
     * @see MessageLevel
     * */
    public void sendMessage(MessageLevel messageLevel) {
        createPatternNotification(messageLevel.pattern);
    }

    private void validateInput(int input) {
        if (input <= 0) {
            throw new IllegalArgumentException("Input must be a positive integer.");
        }
    }

    private void validateStepDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Step duration must be a positive integer.");
        }
    }

}
