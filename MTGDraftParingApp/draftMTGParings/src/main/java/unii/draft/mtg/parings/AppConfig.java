package unii.draft.mtg.parings;

import unii.draft.mtg.parings.algorithm.AlgorithmFactory;
import unii.draft.mtg.parings.algorithm.IAlgorithmConfigure;
import unii.draft.mtg.parings.algorithm.IParingAlgorithm;
import unii.draft.mtg.parings.algorithm.ParingAlgorithm;
import unii.draft.mtg.parings.config.BaseConfig;
import unii.draft.mtg.parings.config.SettingsPreferencesConst;
import unii.draft.mtg.parings.sharedprefrences.SettingsPreferencesFactory;
import unii.draft.mtg.parings.sharedprefrences.SettingsSharedPreferences;
import unii.draft.mtg.parings.sharedprefrences.SharedPreferencesManager;
import android.app.Application;

public class AppConfig extends Application  implements IAlgorithmConfigure{

	
	@Override
	public void onCreate() {
		super.onCreate();
	
		SettingsPreferencesFactory.configure(new SettingsSharedPreferences(
				new SharedPreferencesManager().getSharedPreferences(this,
						SettingsPreferencesConst.SETTINGS_SHARED_PREFRENCES)));
		// default config sh preferences

		if (SettingsPreferencesFactory.getInstance().isFirstRun()) {
			defaultSharedPreferencesConfig();
		}
		
		
	}
	

	private void defaultSharedPreferencesConfig() {
		SettingsPreferencesFactory.getInstance().setDisplayCounterRound(
				BaseConfig.DEFAULT_DISPLAY_COUNTER_ROUND);
		SettingsPreferencesFactory.getInstance().setFirstRun(false);
		SettingsPreferencesFactory.getInstance().setFirstVibration(
				BaseConfig.DEFAULT_FIRST_VIBRATION);
		SettingsPreferencesFactory.getInstance().setSecondVibration(
				BaseConfig.DEFAULT_SECOND_VIBRATION);
		SettingsPreferencesFactory.getInstance().setTimePerRound(
				BaseConfig.DEFAULT_TIME_PER_ROUND);
		SettingsPreferencesFactory.getInstance().setUseVibration(
				BaseConfig.DEFAULT_USE_VIBRATION);
		SettingsPreferencesFactory.getInstance().setVibrationDuration(
				BaseConfig.DEFAULT_VIBRATION_DURATION);

	}

	@Override
	public void setAlgorithm(IParingAlgorithm paringAlgorithm) {
		AlgorithmFactory.configure(paringAlgorithm);
	}

	@Override
	public IParingAlgorithm getInstance() {
		return AlgorithmFactory.getInstance();
	}
}
