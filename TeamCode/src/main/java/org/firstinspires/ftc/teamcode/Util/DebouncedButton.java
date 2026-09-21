package org.firstinspires.ftc.teamcode.Util;


/**
 * A debounced button input detector that generates a one-shot pulse.
 */
public class DebouncedButton {

    private final long DEBOUNCE_TIME_MS;

    private long lastUpdateMS = 0;

    /**
     * Creates a new DebouncedButton with the given debounce period.
     *
     * @param debounceMS the minimum time, in milliseconds, that must elapse before
     *                    another pulse can be triggered.
     */
    public DebouncedButton(long debounceMS) {
        DEBOUNCE_TIME_MS = debounceMS;
    }


    /**
     * Updates the button input and returns whether a debounced pulse was detected.
     *
     *
     * @param buttonInput the current raw (unfiltered) state of the button;
     *                    {@code true} if the button is currently pressed
     * @return {@code true} if a debounced button press pulse was detected on this
     *         call, {@code false} otherwise
     */
    public boolean update(boolean buttonInput) {

        if (((System.currentTimeMillis() - lastUpdateMS) > DEBOUNCE_TIME_MS) && buttonInput) {
            lastUpdateMS = System.currentTimeMillis();
            return true;
        }

        return false;
    }
}
