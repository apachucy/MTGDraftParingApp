package unii.draft.mtg.parings.view.fragments;

import android.support.v4.app.Fragment;

import unii.draft.mtg.parings.BaseActivity;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;


public abstract class BaseFragment extends Fragment {
    public ActivityComponent getActivityComponent() {
        return ((BaseActivity) getActivity()).getComponent();
    }

    protected abstract void initFragmentView();
    protected abstract void initFragmentData();
}
