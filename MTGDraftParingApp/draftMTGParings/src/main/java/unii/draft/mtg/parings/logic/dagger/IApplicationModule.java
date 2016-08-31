package unii.draft.mtg.parings.logic.dagger;


import unii.draft.mtg.parings.buisness.algorithm.IParingAlgorithm;
import unii.draft.mtg.parings.buisness.sittings.ISittingGenerator;
import unii.draft.mtg.parings.database.model.DaoSession;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.util.config.SetPreferencesOnFirstRun;

public interface IApplicationModule {
    ISharedPreferences provideSharedPreferencesManager();

    IParingAlgorithm provideManualAlgorithmManager();

    IParingAlgorithm provideAutomaticAlgorithmManager();

    DaoSession provideDatabaseManager();

    AlgorithmChooser provideAlgorithmChooser();

    ISittingGenerator provideSittingGenerator();


}
