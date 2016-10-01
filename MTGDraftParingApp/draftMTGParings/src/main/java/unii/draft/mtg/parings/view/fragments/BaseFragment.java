package unii.draft.mtg.parings.view.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import unii.draft.mtg.parings.BaseActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;


public abstract class BaseFragment extends Fragment {
    public ActivityComponent getActivityComponent() {
        return ((BaseActivity) getActivity()).getComponent();
    }

    protected abstract void initFragmentView();
    protected abstract void initFragmentData();



    protected void showRadioButtonListDialog(Context context, String title, List<String> list, String buttonPositive, String buttonNegative,
                                           int defaultSelectedValue, MaterialDialog.ListCallbackSingleChoice listCallbackSingleChoice) {
        new MaterialDialog.Builder(context)
                .title(title).items(list).itemsCallbackSingleChoice(defaultSelectedValue, listCallbackSingleChoice)
                .positiveText(buttonPositive).backgroundColorRes(R.color.windowBackground).negativeText(buttonNegative)
                .show();
    }

}
