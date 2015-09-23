package unii.draft.mtg.parings.sharedprefrences;

import unii.draft.mtg.parings.config.BaseConfig;
import unii.draft.mtg.parings.config.SettingsPreferencesConst;
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
						useVibration).commit();

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
						displayCounter).commit();

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
				.commit();

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
						timeBeforeEnd).commit();

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
						timeBeforeEnd).commit();

	}

	@Override
	public void setVibrationDuration(long duration) {
		mSharedPreferences.edit()
				.putLong(SettingsPreferencesConst.VIBRATION_DURATION, duration)
				.commit();

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
				isFirstRun).commit();
		
	}

	@Override
	public boolean areManualParings() {
		return mSharedPreferences.getBoolean(SettingsPreferencesConst.MANUAL_PARINGS, BaseConfig.DEFAULT_MANUAL_PARINGS);
	}

	@Override
	public void setManualParings(boolean areManualParings) {
		mSharedPreferences.edit().putBoolean(SettingsPreferencesConst.MANUAL_PARINGS,areManualParings).commit();
	}

}
