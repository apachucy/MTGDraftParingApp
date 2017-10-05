package unii.draft.mtg.parings.logic.dagger;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import unii.draft.mtg.parings.buisness.algorithm.automatic.AutomaticParingAlgorithm;
import unii.draft.mtg.parings.buisness.algorithm.base.IParingAlgorithm;
import unii.draft.mtg.parings.buisness.algorithm.manual.ManualParingAlgorithm;
import unii.draft.mtg.parings.buisness.share.scoreboard.IShareData;
import unii.draft.mtg.parings.buisness.share.scoreboard.ShareDataContent;
import unii.draft.mtg.parings.buisness.sittings.ISittingGenerator;
import unii.draft.mtg.parings.buisness.sittings.RandomSittingGenerator;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.sharedprefrences.SharedPreferencesManager;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.util.helper.DatabaseHelper;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;

@Module
public class ApplicationModule implements IApplicationModule {
    public static final String ALGORITHM_MANUAL = "algorithm_manual";
    public static final String ALGORITHM_AUTOMATIC = "algorithm_automatic";

    private final Context mContext;
    private final HasComponent<ApplicationComponent> mHasApplicationComponent;




    public ApplicationModule(Context context, HasComponent<ApplicationComponent> applicationComponent) {
        mContext = context;
        this.mHasApplicationComponent = applicationComponent;
    }

    @NonNull
    @Override
    @Provides
    @Singleton
    public ISharedPreferences provideSharedPreferencesManager() {
        return new SharedPreferencesManager(mContext);
    }

    @NonNull
    @Provides
    @Named(ALGORITHM_MANUAL)
    @Singleton
    @Override
    public IParingAlgorithm provideManualAlgorithmManager() {
        return new ManualParingAlgorithm(mContext);
    }


    @NonNull
    @Provides
    @Named(ALGORITHM_AUTOMATIC)
    @Singleton
    @Override
    public IParingAlgorithm provideAutomaticAlgorithmManager() {
        return new AutomaticParingAlgorithm(mContext);
    }


    @NonNull
    @Override
    @Provides
    @Singleton
    public AlgorithmChooser provideAlgorithmChooser() {
        return new AlgorithmChooser(mHasApplicationComponent.getComponent());
    }

    @NonNull
    @Override
    @Provides
    @Singleton
    public ISittingGenerator provideSittingGenerator() {
        return new RandomSittingGenerator();
    }

    @NonNull
    @Override
    @Provides
    @Singleton
    public IShareData provideShareDataContent() {
        return new ShareDataContent(mContext);
    }

    @NonNull
    @Override
    @Provides
    @Singleton
    public IDatabaseHelper provideDatabaseHelper() {
        return new DatabaseHelper(mContext,mHasApplicationComponent.getComponent());
    }

}
