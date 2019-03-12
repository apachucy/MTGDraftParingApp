package unii.draft.mtg.parings.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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
    @Nullable
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

    protected void showRadioButtonListDialog(@NonNull Context context, @NonNull String title, @NonNull List<String> list, @NonNull String buttonPositive, @NonNull String buttonNegative,
                                             int defaultSelectedValue, @NonNull MaterialDialog.ListCallbackSingleChoice listCallbackSingleChoice) {
        mMaterialDialogInstance = new MaterialDialog.Builder(context)
                .title(title).items(list).itemsCallbackSingleChoice(defaultSelectedValue, listCallbackSingleChoice)
                .positiveText(buttonPositive).backgroundColorRes(R.color.windowBackground).negativeText(buttonNegative)
                .show();
    }

    protected void showDialog(@NonNull Context context, @NonNull String title, @NonNull String content, @NonNull String positiveText) {
        mMaterialDialogInstance = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positiveText)
                .backgroundColorRes(R.color.windowBackground)
                .show();
    }

    protected void showDialogWithLink(@NonNull Context context, @NonNull String title, @StringRes int content, @NonNull String positiveText) {
        mMaterialDialogInstance = new MaterialDialog.Builder(context)
                .title(title)
                .content(content, true)
                .positiveText(positiveText)
                .backgroundColorRes(R.color.windowBackground)
                .show();
    }

    protected void showDialogWithTwoOptions(@NonNull Context context, @NonNull String title, @NonNull String content, @NonNull String positiveText, @NonNull String negativeText, @NonNull MaterialDialog.SingleButtonCallback positiveCallback) {
        mMaterialDialogInstance = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positiveText)
                .negativeText(negativeText).backgroundColorRes(R.color.windowBackground).onPositive(positiveCallback)
                .show();
    }

    protected void showMultipleChoiceListDialog(@NonNull Context context, @NonNull String title, @NonNull List<String> data, @NonNull MaterialDialog.ListCallbackMultiChoice listCallbackMultiChoice, @NonNull String positiveText) {
        mMaterialDialogInstance = new MaterialDialog.Builder(context)
                .title(title).items(data)
                .itemsCallbackMultiChoice(null, listCallbackMultiChoice)
                .backgroundColorRes(R.color.windowBackground)
                .positiveText(positiveText).show();
    }


    protected void showEditTextDialog(@NonNull Context context, @NonNull String title, @NonNull String content, String hint, String lastValue,
                                      @NonNull final TimeSettingsFragment.UpdateData updateData) {
        mMaterialDialogInstance = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED)
                .backgroundColorRes(R.color.windowBackground)
                .input(hint, lastValue, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, @NonNull CharSequence input) {
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

    protected void showEditTextDialogWithAnyValue(@NonNull Context context, @NonNull String title, @NonNull String content, String hint, String lastValue,
                                                  @NonNull final TimeSettingsFragment.UpdateData updateData) {
        mMaterialDialogInstance = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .backgroundColorRes(R.color.windowBackground)
                .input(hint, lastValue, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, @NonNull CharSequence input) {
                        updateData.updateSharedPreferences(input.toString());
                        updateData.updateView();
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
