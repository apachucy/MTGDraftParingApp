package unii.draft.mtg.parings.sharedprefrences;

import unii.draft.mtg.parings.config.BaseConfig;
import unii.draft.mtg.parings.config.SettingsPreferencesConst;
import unii.draft.mtg.parings.sittings.SittingsMode;

import android.content.SharedPreferences;

public class SettingsSharedPreferences implements ISettings {
    private SharedPreferences mSharedPreferences;

    public SettingsSharedPreferences(SharedPreferences shPref) {
        mSharedPreferences = shPref;
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

}
