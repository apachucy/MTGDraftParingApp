package unii.draft.mtg.parings.util.config;

import javax.inject.Inject;

import unii.draft.mtg.parings.logic.dagger.ApplicationComponent;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;

public class SetPreferencesOnFirstRun {

    @Inject
    ISharedPreferences mSharedPreferences;

    public SetPreferencesOnFirstRun(ApplicationComponent component) {
        component.inject(this);

    }

    public void defaultSharedPreferencesConfig() {
        if (!mSharedPreferences.isFirstRun()) {
            return;
        }

        mSharedPreferences.setDisplayCounterRound(
                BaseConfig.DEFAULT_DISPLAY_COUNTER_ROUND);
        mSharedPreferences.setFirstVibration(
                BaseConfig.DEFAULT_FIRST_VIBRATION);
        mSharedPreferences.setSecondVibration(
                BaseConfig.DEFAULT_SECOND_VIBRATION);
        mSharedPreferences.setTimePerRound(
                BaseConfig.DEFAULT_TIME_PER_ROUND);
        mSharedPreferences.setUseVibration(
                BaseConfig.DEFAULT_USE_VIBRATION);
        mSharedPreferences.setVibrationDuration(
                BaseConfig.DEFAULT_VIBRATION_DURATION);
        mSharedPreferences.setFirstRun(false);
    }


}
