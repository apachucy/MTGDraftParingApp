package unii.draft.mtg.parings.logic.dagger;


import unii.draft.mtg.parings.buisness.algorithm.base.IParingAlgorithm;
import unii.draft.mtg.parings.buisness.share.scoreboard.IShareData;
import unii.draft.mtg.parings.buisness.sittings.ISittingGenerator;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;

interface IApplicationModule {
    ISharedPreferences provideSharedPreferencesManager();

    IParingAlgorithm provideManualAlgorithmManager();

    IParingAlgorithm provideAutomaticAlgorithmManager();

    AlgorithmChooser provideAlgorithmChooser();

    ISittingGenerator provideSittingGenerator();

    IShareData provideShareDataContent();

    IDatabaseHelper provideDatabaseHelper();


}
