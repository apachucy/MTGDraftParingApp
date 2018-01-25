package unii.draft.mtg.parings.view.fragments.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.shashank.sony.fancytoastlib.FancyToast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.config.BaseConfig;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

public class OtherSettingsFragment extends BaseFragment {

    private Unbinder mUnbinder;

    @Inject
    ISharedPreferences mSharedPreferenceManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_other, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        injectDependencies();
        initFragmentData();
        initFragmentView();

        return view;
    }

    @Override
    protected void initFragmentView() {

    }

    @Override
    protected void initFragmentData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @OnClick(R.id.settings_resetTourGuideButton)
    void onTourGuideReset() {
        showDialogWithTwoOptions(getActivity(), getString(R.string.settings_reset_tour_guide_dialog_title), getString(R.string.settings_reset_tour_guide_dialog_content),
                getString(R.string.dialog_positive), getString(R.string.dialog_negative), mPositiveCallback);
    }

    @OnClick(R.id.settings_rateApplicationButton)
    void onRateMeClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Try Google play
        intent.setData(Uri.parse(BaseConfig.INTENT_OPEN_GOOGLE_PLAY + BaseConfig.INTENT_PACKAGE_DRAFT_MTG));
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            intent.setData(Uri.parse(BaseConfig.INTENT_OPEN_GOOGLE_PLAY_WWW + BaseConfig.INTENT_PACKAGE_DRAFT_MTG));
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                //display error msg
                FancyToast.makeText(getContext(), getString(R.string.settings_rate_me_error), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

            }

        }
    }

    @OnClick(R.id.settings_checkOtherApplicationButton)
    void onCheckOtherApplicationClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(BaseConfig.INTENT_OPEN_DEVELOPER_APPS_WWW));
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            //display error msg
            FancyToast.makeText(getContext(), getString(R.string.settings_developer_account_error), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }
    }

    @OnClick(R.id.settings_ideaBoxButton)
    public void onIdeaBoxClicked() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(BaseConfig.INTENT_OPEN_EMAIL)); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{BaseConfig.INTENT_EMAIL_RECIPIENT});
        intent.putExtra(Intent.EXTRA_SUBJECT, BaseConfig.INTENT_EMAIL_SUBJECT);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            //display error msg
            FancyToast.makeText(getContext(), getString(R.string.settings_idea_box_error), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

        }

    }

    @OnClick(R.id.settings_usedLibrariesButton)
    public void onUsedLibrariesClicked() {
        showDialog(getContext(), getString(R.string.dialog_used_libraries_title), getString(R.string.dialog_used_libraries_body), getString(R.string.positive));
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

    @NonNull
    private MaterialDialog.SingleButtonCallback mPositiveCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            mSharedPreferenceManager.resetGuideTour();
            FancyToast.makeText(getActivity(), getString(R.string.settings_reset_tour_guide_message), FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
        }
    };
}
