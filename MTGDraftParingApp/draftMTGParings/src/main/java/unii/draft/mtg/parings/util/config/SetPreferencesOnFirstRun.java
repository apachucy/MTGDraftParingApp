package unii.draft.mtg.parings.util.config;

import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;

public class SetPreferencesOnFirstRun {


    private SetPreferencesOnFirstRun() {

    }

    public static void defaultSharedPreferencesConfig(ISharedPreferences sharedPreferences) {
        if (!sharedPreferences.isFirstRun()) {
            return;
        }

        sharedPreferences.setDisplayCounterRound(
                BaseConfig.DEFAULT_DISPLAY_COUNTER_ROUND);
        sharedPreferences.setFirstVibration(
                BaseConfig.DEFAULT_FIRST_VIBRATION);
        sharedPreferences.setSecondVibration(
                BaseConfig.DEFAULT_SECOND_VIBRATION);
        sharedPreferences.setTimePerRound(
                BaseConfig.DEFAULT_TIME_PER_ROUND);
        sharedPreferences.setUseVibration(
                BaseConfig.DEFAULT_USE_VIBRATION);
        sharedPreferences.setVibrationDuration(
                BaseConfig.DEFAULT_VIBRATION_DURATION);
        sharedPreferences.setFirstRun(false);
    }


}
