package unii.draft.mtg.parings;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import unii.draft.mtg.parings.logic.dagger.ApplicationComponent;
import unii.draft.mtg.parings.logic.dagger.ApplicationModule;
import unii.draft.mtg.parings.logic.dagger.DaggerApplicationComponent;
import unii.draft.mtg.parings.logic.dagger.HasComponent;

public class MTGDraftParingsApplication extends Application implements HasComponent<ApplicationComponent> {

    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this, this)).build();
 }

    @Override
    public ApplicationComponent getComponent() {
        return mComponent;
    }
}
