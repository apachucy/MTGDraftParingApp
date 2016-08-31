package unii.draft.mtg.parings.sharedprefrences;

import android.content.Context;
import android.content.SharedPreferences;

import unii.draft.mtg.parings.buisness.sittings.SittingsMode;
import unii.draft.mtg.parings.util.config.BaseConfig;
import unii.draft.mtg.parings.util.config.SettingsPreferencesConst;

public class SharedPreferencesManager implements ISharedPreferences {

    private SharedPreferences mSharedPreferences;
    private final Context mContext;
    private static final String SHARED_PREFERENCES_NAME = SettingsPreferencesConst.SETTINGS_SHARED_PREFRENCES;

    public SharedPreferencesManager(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean useVibration() {
        return mSharedPreferences.getBoolean(
                SettingsPreferencesConst.USE_VIBRATION,
                BaseConfig.DEFAULT_USE_VIBRATION);
    }

    @Override
    public void setUseVibration(boolean useVibration) {
        mSharedPreferences
                .edit()
                .putBoolean(SettingsPreferencesConst.USE_VIBRATION,
                        useVibration).apply();

    }

    @Override
    public boolean displayCounterRound() {

        return mSharedPreferences.getBoolean(
                SettingsPreferencesConst.DISPALY_COUNTER_ROUND,
                BaseConfig.DEFAULT_DISPLAY_COUNTER_ROUND);
    }

    @Override
    public void setDisplayCounterRound(boolean displayCounter) {
        mSharedPreferences
                .edit()
                .putBoolean(SettingsPreferencesConst.DISPALY_COUNTER_ROUND,
                        displayCounter).apply();

    }

    @Override
    public long getTimePerRound() {
        return mSharedPreferences.getLong(
                SettingsPreferencesConst.TIME_PER_ROUND,
                BaseConfig.DEFAULT_TIME_PER_ROUND);
    }

    @Override
    public void setTimePerRound(long roundTime) {
        mSharedPreferences.edit()
                .putLong(SettingsPreferencesConst.TIME_PER_ROUND, roundTime)
                .apply();

    }

    @Override
    public long getFirstVibration() {
        return mSharedPreferences.getLong(
                SettingsPreferencesConst.FIRST_VIBRATION,
                BaseConfig.DEFAULT_FIRST_VIBRATION);
    }

    @Override
    public void setFirstVibration(long timeBeforeEnd) {
        mSharedPreferences
                .edit()
                .putLong(SettingsPreferencesConst.FIRST_VIBRATION,
                        timeBeforeEnd).apply();

    }

    @Override
    public long getSecondVibration() {

        return mSharedPreferences.getLong(
                SettingsPreferencesConst.SECOND_VIBRATION,
                BaseConfig.DEFAULT_SECOND_VIBRATION);
    }

    @Override
    public void setSecondVibration(long timeBeforeEnd) {
        mSharedPreferences
                .edit()
                .putLong(SettingsPreferencesConst.SECOND_VIBRATION,
                        timeBeforeEnd).apply();

    }

    @Override
    public void setVibrationDuration(long duration) {
        mSharedPreferences.edit()
                .putLong(SettingsPreferencesConst.VIBRATION_DURATION, duration)
                .apply();

    }

    @Override
    public long getVibrationDuration() {
        return mSharedPreferences.getLong(
                SettingsPreferencesConst.VIBRATION_DURATION,
                BaseConfig.DEFAULT_VIBRATION_DURATION);
    }

    @Override
    public boolean isFirstRun() {

        return mSharedPreferences.getBoolean(
                SettingsPreferencesConst.FIRST_RUN,
                BaseConfig.DEFAULT_FIRST_RUN);
    }

    @Override
    public void setFirstRun(boolean isFirstRun) {
        mSharedPreferences.edit().putBoolean(
                SettingsPreferencesConst.FIRST_RUN,
                isFirstRun).apply();

    }

    @Override
    public boolean areManualParings() {
        return mSharedPreferences.getBoolean(SettingsPreferencesConst.MANUAL_PARINGS, BaseConfig.DEFAULT_MANUAL_PARINGS);
    }

    @Override
    public void setManualParings(boolean areManualParings) {
        mSharedPreferences.edit().putBoolean(SettingsPreferencesConst.MANUAL_PARINGS, areManualParings).apply();
    }

    @Override
    public void setGeneratedSittingMode(int sittingMode) {
        mSharedPreferences.edit().putInt(SettingsPreferencesConst.SITTINGS, sittingMode).apply();

    }

    @Override
    public int getGeneratedSittingMode() {
        return mSharedPreferences.getInt(SettingsPreferencesConst.SITTINGS, SittingsMode.NO_SITTINGS);
    }

    @Override
    public void resetGuideTour() {
        mSharedPreferences.edit().putBoolean(SettingsPreferencesConst.SHOW_GUIDE_TOUR_ON_MAIN_SCREEN, true)
                .putBoolean(SettingsPreferencesConst.SHOW_GUIDE_TOUR_ON_PARING_SCREEN, true)
                .putBoolean(SettingsPreferencesConst.SHOW_GUIDE_TOUR_ON_SCOREBOARD_SCREEN, true)
                .apply();

    }

    @Override
    public boolean showGuideTourOnMainScreen() {
        return mSharedPreferences.getBoolean(SettingsPreferencesConst.SHOW_GUIDE_TOUR_ON_MAIN_SCREEN, true);
    }

    @Override
    public void setGuideTourOnMainScreen(boolean isVisible) {
        mSharedPreferences.edit().putBoolean(SettingsPreferencesConst.SHOW_GUIDE_TOUR_ON_MAIN_SCREEN, isVisible).apply();
    }

    @Override
    public boolean showGuideTourOnParingScreen() {
        return mSharedPreferences.getBoolean(SettingsPreferencesConst.SHOW_GUIDE_TOUR_ON_PARING_SCREEN, true);
    }

    @Override
    public void setGuideTourOnMainParingScreen(boolean isVisible) {
        mSharedPreferences.edit().putBoolean(SettingsPreferencesConst.SHOW_GUIDE_TOUR_ON_PARING_SCREEN, isVisible).apply();

    }

    @Override
    public boolean showGuideTourOnScoreBoardScreen() {
        return mSharedPreferences.getBoolean(SettingsPreferencesConst.SHOW_GUIDE_TOUR_ON_SCOREBOARD_SCREEN, true);
    }

    @Override
    public void setGuideTourOnScoreBoardScreen(boolean isVisible) {
        mSharedPreferences.edit().putBoolean(SettingsPreferencesConst.SHOW_GUIDE_TOUR_ON_SCOREBOARD_SCREEN, isVisible).apply();

    }

}
