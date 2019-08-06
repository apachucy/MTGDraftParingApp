package unii.draft.mtg.parings.logic.dagger;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Module;


@Module
public class ActivityModule implements IActivityModule{

    private final AppCompatActivity mActivity;
    private final HasComponent<ActivityComponent> mComponent;

    public ActivityModule(AppCompatActivity activity, HasComponent<ActivityComponent> component) {
        this.mActivity = activity;
        mComponent = component;
    }

}
