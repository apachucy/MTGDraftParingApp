package unii.draft.mtg.parings.sharedprefrences;

import unii.draft.mtg.parings.util.config.BaseConfig;

public interface ISharedPreferences {


    /**
     * Check if vibrations are turn on
     *
     * @return true if vibration can be used <br>
     * false in other case <br>
     * default - true
     */
     boolean useVibration();

    /**
     * Set if vibration should be used in application
     *
     * @param useVibration
     */
     void setUseVibration(boolean useVibration);

    /**
     * Check if timer should work
     *
     * @return true if counter should be displayed <br>
     * false in other case <br>
     * default- true
     */
     boolean displayCounterRound();

    /**
     * Set if counter should be displayed
     *
     * @param displayCounter
     */
     void setDisplayCounterRound(boolean displayCounter);

    /**
     * @return time per round in ms <br>
     * default time per round: {@link BaseConfig}.DEFAULT_TIME_PER_ROUND
     */
     long getTimePerRound();

    /**
     * Set time in ms per round <br>
     * Time should not be negative
     */
     void setTimePerRound(long roundTime);

    /**
     * @return time before game end when first vibration should occur <br>
     * default {@linkplain BaseConfig}.DEFAULT_FIRST_VIBRATION
     */
     long getFirstVibration();

    /**
     * Set time in ms before game end when vibration should occur<br>
     * <br>
     * Time should not be negative<br>
     *
     * @param timeBeforeEnd
     */
     void setFirstVibration(long timeBeforeEnd);

    /**
     * @return time before game end when second vibration should occur <br>
     * default {@linkplain BaseConfig}.DEFAULT_SECOND_VIBRATION
     */
     long getSecondVibration();

    /**
     * Set time in ms before game end when vibration should occur<br>
     * <br>
     * Time should not be negative<br>
     *
     * @param timeBeforeEnd
     */
     void setSecondVibration(long timeBeforeEnd);

    /**
     * Set vibration duration <br>
     * Time should not be negative<br>
     *
     * @param duration
     */
     void setVibrationDuration(long duration);

    /**
     * @return vibration duration
     */
     long getVibrationDuration();

    /**
     * Check if this application is run first time
     *
     * @return true if app is running first time <br>
     * in other case return false
     */
    @Deprecated
     boolean isFirstRun();

    /**
     * Set if app should behave as app run as first time
     *
     * @param isFirstRun
     */
    @Deprecated
     void setFirstRun(boolean isFirstRun);

    /**
     * @return true if player can set manual parings
     */
     boolean areManualParings();

    /**
     * Choose if parings should be manual or by algorithm
     *
     * @param areManualParings
     */
     void setManualParings(boolean areManualParings);

    /**
     * Set sitting mode
     *
     * @param sittingMode
     */
     void setGeneratedSittingMode(int sittingMode);

    /**
     * get sittings mode
     *
     * @return
     */
     int getGeneratedSittingMode();

     void resetGuideTour();

     boolean showGuideTourOnMainScreen();

     void setGuideTourOnMainScreen(boolean isVisible);

     boolean showGuideTourOnParingScreen();

     void setGuideTourOnMainParingScreen(boolean isVisible);

     boolean showGuideTourOnScoreBoardScreen();

     void setGuideTourOnScoreBoardScreen(boolean isVisible);}
