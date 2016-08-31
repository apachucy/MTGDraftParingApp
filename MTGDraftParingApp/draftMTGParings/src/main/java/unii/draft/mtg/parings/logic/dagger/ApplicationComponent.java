package unii.draft.mtg.parings.logic.dagger;


import javax.inject.Singleton;

import dagger.Component;
import unii.draft.mtg.parings.buisness.algorithm.ManualParingAlgorithm;
import unii.draft.mtg.parings.buisness.algorithm.AutomaticParingAlgorithm;
import unii.draft.mtg.parings.database.model.DaoSession;
import unii.draft.mtg.parings.sharedprefrences.SharedPreferencesManager;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.util.config.SetPreferencesOnFirstRun;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    ActivityComponent plus(ActivityModule activityModule);

    void inject(SharedPreferencesManager provideSharedPreferencesManager);

    void inject(ManualParingAlgorithm provideManualParingAlgorithm);

    void inject(AutomaticParingAlgorithm provideAutomaticAlgorithmManager);

    void inject(DaoSession provideDatabaseManager);

    void inject(AlgorithmChooser provideAlgorithmChooser);

    void inject (SetPreferencesOnFirstRun provideSetPreferencesOnFirstRun);
}
