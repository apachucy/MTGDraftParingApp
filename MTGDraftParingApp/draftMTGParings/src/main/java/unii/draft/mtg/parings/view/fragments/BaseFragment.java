package unii.draft.mtg.parings.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import unii.draft.mtg.parings.BaseActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.util.config.BaseConfig;


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

    protected void showDialogWithTwoOptions(Context context, String title, String content, String positiveText, String negativeText, MaterialDialog.SingleButtonCallback positiveCallback) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positiveText)
                .negativeText(negativeText).backgroundColorRes(R.color.windowBackground).onPositive(positiveCallback)
                .show();
    }


    protected void shareAction(String dataForShare) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, dataForShare);
        sendIntent.setType(BaseConfig.INTENT_SHARE_DATA_TYPE);
        startActivity(sendIntent);
    }
}
