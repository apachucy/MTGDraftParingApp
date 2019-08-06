package unii.draft.mtg.parings.logic.dagger;


import androidx.annotation.NonNull;

import unii.draft.mtg.parings.buisness.algorithm.base.IParingAlgorithm;
import unii.draft.mtg.parings.buisness.share.scoreboard.IShareData;
import unii.draft.mtg.parings.buisness.sittings.ISittingGenerator;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;

interface IApplicationModule {
    @NonNull
    ISharedPreferences provideSharedPreferencesManager();

    @NonNull
    IParingAlgorithm provideManualAlgorithmManager();

    @NonNull
    IParingAlgorithm provideAutomaticAlgorithmManager();

    @NonNull
    IParingAlgorithm provideSuddenDeathAlgorithmManager();

    @NonNull
    AlgorithmChooser provideAlgorithmChooser();

    @NonNull
    ISittingGenerator provideSittingGenerator();

    @NonNull
    IShareData provideShareDataContent();

    @NonNull
    IDatabaseHelper provideDatabaseHelper();


}
