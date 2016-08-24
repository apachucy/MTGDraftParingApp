package unii.draft.mtg.parings.sharedprefrences;

import unii.draft.mtg.parings.config.BaseConfig;

public interface ISettings {

    /**
     * Check if vibrations are turn on
     *
     * @return true if vibration can be used <br>
     * false in other case <br>
     * default - true
     */
    public boolean useVibration();

    /**
     * Set if vibration should be used in application
     *
     * @param useVibration
     */
    public void setUseVibration(boolean useVibration);

    /**
     * Check if timer should work
     *
     * @return true if counter should be displayed <br>
     * false in other case <br>
     * default- true
     */
    public boolean displayCounterRound();

    /**
     * Set if counter should be displayed
     *
     * @param displayCounter
     */
    public void setDisplayCounterRound(boolean displayCounter);

    /**
     * @return time per round in ms <br>
     * default time per round: {@link BaseConfig}.DEFAULT_TIME_PER_ROUND
     */
    public long getTimePerRound();

    /**
     * Set time in ms per round <br>
     * Time should not be negative
     */
    public void setTimePerRound(long roundTime);

    /**
     * @return time before game end when first vibration should occur <br>
     * default {@linkplain BaseConfig}.DEFAULT_FIRST_VIBRATION
     */
    public long getFirstVibration();

    /**
     * Set time in ms before game end when vibration should occur<br>
     * <br>
     * Time should not be negative<br>
     *
     * @param timeBeforeEnd
     */
    public void setFirstVibration(long timeBeforeEnd);

    /**
     * @return time before game end when second vibration should occur <br>
     * default {@linkplain BaseConfig}.DEFAULT_SECOND_VIBRATION
     */
    public long getSecondVibration();

    /**
     * Set time in ms before game end when vibration should occur<br>
     * <br>
     * Time should not be negative<br>
     *
     * @param timeBeforeEnd
     */
    public void setSecondVibration(long timeBeforeEnd);

    /**
     * Set vibration duration <br>
     * Time should not be negative<br>
     *
     * @param duration
     */
    public void setVibrationDuration(long duration);

    /**
     * @return vibration duration
     */
    public long getVibrationDuration();

    /**
     * Check if this application is run first time
     *
     * @return true if app is running first time <br>
     * in other case return false
     */
    @Deprecated
    public boolean isFirstRun();

    /**
     * Set if app should behave as app run as first time
     *
     * @param isFirstRun
     */
    @Deprecated
    public void setFirstRun(boolean isFirstRun);

    /**
     * @return true if player can set manual parings
     */
    public boolean areManualParings();

    /**
     * Choose if parings should be manual or by algorithm
     *
     * @param areManualParings
     */
    public void setManualParings(boolean areManualParings);

    /**
     * Set sitting mode
     *
     * @param sittingMode
     */
    public void setGeneratedSittingMode(int sittingMode);

    /**
     * get sittings mode
     *
     * @return
     */
    public int getGeneratedSittingMode();

    public void resetGuideTour();

    public boolean showGuideTourOnMainScreen();

    public void setGuideTourOnMainScreen(boolean isVisible);

    public boolean showGuideTourOnParingScreen();

    public void setGuideTourOnMainParingScreen(boolean isVisible);

    public boolean showGuideTourOnScoreBoardScreen();

    public void setGuideTourOnScoreBoardScreen(boolean isVisible);

    public void setBooleanValue(String propertyName, boolean booleanValue);

    public boolean getBooleanValue(String propertyName);

    public void setIntegerValue(String propertyName, int booleanValue);

    public int getIntegerValue(String propertyName);
}

