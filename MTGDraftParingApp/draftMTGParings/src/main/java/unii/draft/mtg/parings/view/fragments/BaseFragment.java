package unii.draft.mtg.parings.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import unii.draft.mtg.parings.BaseActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.util.config.BaseConfig;
import unii.draft.mtg.parings.view.fragments.settings.TimeSettingsFragment;


public abstract class BaseFragment extends Fragment {
    private MaterialDialog mMaterialDialogInstance;


    public ActivityComponent getActivityComponent() {
        return ((BaseActivity) getActivity()).getComponent();
    }

    protected abstract void initFragmentView();

    protected abstract void initFragmentData();


    @Override
    public void onPause() {
        super.onPause();
        if (mMaterialDialogInstance != null) {
            mMaterialDialogInstance.dismiss();
            mMaterialDialogInstance = null;
        }
    }

    protected void showRadioButtonListDialog(Context context, String title, List<String> list, String buttonPositive, String buttonNegative,
                                             int defaultSelectedValue, MaterialDialog.ListCallbackSingleChoice listCallbackSingleChoice) {
        mMaterialDialogInstance = new MaterialDialog.Builder(context)
                .title(title).items(list).itemsCallbackSingleChoice(defaultSelectedValue, listCallbackSingleChoice)
                .positiveText(buttonPositive).backgroundColorRes(R.color.windowBackground).negativeText(buttonNegative)
                .show();
    }

    protected void showDialogWithTwoOptions(Context context, String title, String content, String positiveText, String negativeText, MaterialDialog.SingleButtonCallback positiveCallback) {
        mMaterialDialogInstance = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positiveText)
                .negativeText(negativeText).backgroundColorRes(R.color.windowBackground).onPositive(positiveCallback)
                .show();
    }

    protected void showMultipleChoiceListDialog(Context context, String title, List<String> data, MaterialDialog.ListCallbackMultiChoice listCallbackMultiChoice, String positiveText) {
        mMaterialDialogInstance = new MaterialDialog.Builder(context)
                .title(title).items(data)
                .itemsCallbackMultiChoice(null, listCallbackMultiChoice)
                .backgroundColorRes(R.color.windowBackground)
                .positiveText(positiveText).show();
    }


    protected void showEditTextDialog(Context context, String title, String content, String hint, String lastValue,
                                      final TimeSettingsFragment.UpdateData updateData) {
        mMaterialDialogInstance =   new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED)
                .backgroundColorRes(R.color.windowBackground)
                .input(hint, lastValue, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        int value = 0;

                        try {
                            value = Integer.parseInt(input.toString());
                        } catch (NumberFormatException e) {
                            value = 0;
                        } finally {
                            if (value < 0) {
                                //noinspection ReturnInsideFinallyBlock
                                return;
                            }
                            updateData.updateSharedPreferences(input.toString());
                            updateData.updateView();
                        }


                    }
                }).show();

    }

    protected void shareAction(String dataForShare) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, dataForShare);
        sendIntent.setType(BaseConfig.INTENT_SHARE_DATA_TYPE);
        startActivity(sendIntent);
    }

    protected interface UpdateData {
        void updateView();

        void updateSharedPreferences(String newData);
    }
}
