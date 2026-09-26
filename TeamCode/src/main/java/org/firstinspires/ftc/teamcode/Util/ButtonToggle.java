package org.firstinspires.ftc.teamcode.Util;

/**
 * A debounced toggle switch for button inputs.
 */
public class ButtonToggle {

    private final long DEBOUNCE_TIME_MS;

    private long lastUpdateMS = 0;
    private boolean state = false;

    /**
     * Creates a new ButtonToggle with the given debounce period.
     *
     * @param debounceMS the minimum time, in milliseconds, that must elapse between
     *                    successive toggles. This prevents a single button press
     *                    (or contact bounce) from being registered as multiple toggles.
     */
    public ButtonToggle(long debounceMS) {
        DEBOUNCE_TIME_MS = debounceMS;
    }


    /**
     * Directly sets the toggle state, bypassing the debounce logic.
     *
     * @param state the state to force this toggle into
     */
    public void forceState(boolean state) {
        this.state = state;
    }

    /**
     * Updates the toggle with the latest raw button reading and returns the
     * current toggle state.
     * <p>
     * If {@code buttonInput} is {@code true} and at least {@code DEBOUNCE_TIME_MS}
     * milliseconds have passed since the last toggle, the internal state is
     * flipped and the debounce timer is reset. Otherwise, the state is left
     * unchanged.
     *
     * @param buttonInput the current raw (unfiltered) state of the button;
     *                    {@code true} if the button is currently pressed
     * @return the current toggle state after processing this input
     */
    public boolean update(boolean buttonInput) {

        if ((System.currentTimeMillis() - lastUpdateMS > DEBOUNCE_TIME_MS) && buttonInput) {
            lastUpdateMS = System.currentTimeMillis();
            state = !state;
        }

        return state;
    }
}
