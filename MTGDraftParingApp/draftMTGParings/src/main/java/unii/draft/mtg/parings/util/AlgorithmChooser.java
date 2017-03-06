package unii.draft.mtg.parings.util;

import javax.inject.Inject;
import javax.inject.Named;

import unii.draft.mtg.parings.buisness.algorithm.base.IParingAlgorithm;
import unii.draft.mtg.parings.buisness.algorithm.base.PairingMode;
import unii.draft.mtg.parings.logic.dagger.ApplicationComponent;
import unii.draft.mtg.parings.logic.dagger.ApplicationModule;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;


public class AlgorithmChooser {


    @Inject
    @Named(ApplicationModule.ALGORITHM_MANUAL)
    IParingAlgorithm mAlgorithmManual;

    @Inject
    @Named(ApplicationModule.ALGORITHM_AUTOMATIC)
    IParingAlgorithm mAlgorithmAutomatic;

    @Inject
    ISharedPreferences mSharedPreferences;

    public AlgorithmChooser(ApplicationComponent component) {
        component.inject(this);
    }

    public IParingAlgorithm getCurrentAlgorithm() {
        if (mSharedPreferences.getPairingType() == PairingMode.PAIRING_MANUAL) {
            return mAlgorithmManual;
        } else {
            return mAlgorithmAutomatic;
        }
    }
}
