package unii.draft.mtg.parings.view.fragments.settings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.base.PairingMode;
import unii.draft.mtg.parings.buisness.sittings.SittingsMode;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

public class DraftSettingsFragment extends BaseFragment {


    @Bind(R.id.settings_sittingsButton)
    Button mSittingsOptionsTextView;
    @Bind(R.id.settings_createPairingsChooserButton)
    Button mCreateParingChooserTextView;
    @Bind(R.id.settings_saveResultsAfterDraftButton)
    Button mSaveDraftResultButton;
    @Inject
    ISharedPreferences mSharedPreferenceManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_draft, container, false);
        ButterKnife.bind(this, view);
        injectDependencies();
        initFragmentView();
        initFragmentData();
        return view;
    }

    @Override
    protected void initFragmentView() {
        setSittingsOptionsText(mSharedPreferenceManager.getGeneratedSittingMode());
        setPairingOptionText(mSharedPreferenceManager.getPairingType());
        setSaveDraftResults(mSharedPreferenceManager.getSaveDraftResults());
    }

    @Override
    protected void initFragmentData() {

    }

    @OnClick(R.id.settings_sittingsButton)
    void onSittingsTextViewClicked() {
        List<String> sittingsOptionList = new ArrayList<>();
        sittingsOptionList.add(getString(R.string.settings_sittings_none));
        sittingsOptionList.add(getString(R.string.settings_sittings_random));
        showRadioButtonListDialog(getActivity(), getString(R.string.settings_sittings_dialog_title), sittingsOptionList,
                getString(R.string.dialog_positive), getString(R.string.dialog_negative),
                mSharedPreferenceManager.getGeneratedSittingMode(), mSittingsCallback);

    }

    @OnClick(R.id.settings_createPairingsChooserButton)
    void onCreatePairingsTextViewClicked() {
        List<String> pairingsOptionList = new ArrayList<>();
        pairingsOptionList.add(getString(R.string.settings_pairing_mode_automatic_with_repeats));
        pairingsOptionList.add(getString(R.string.settings_pairing_mode_manual));
        pairingsOptionList.add(getString(R.string.settings_pairing_mode_tournament));
        showRadioButtonListDialog(getActivity(), getString(R.string.settings_pairing_mode_dialog_title), pairingsOptionList,
                getString(R.string.dialog_positive), getString(R.string.dialog_negative),
                mSharedPreferenceManager.getPairingType(), mPairingTypeCallback);


    }

    @OnClick(R.id.settings_saveResultsAfterDraftButton)
    void onSaveDraftResultsViewClicked() {
        List<String> options = new ArrayList<>();
        options.add(getString(R.string.negative));
        options.add(getString(R.string.possitive));

        showRadioButtonListDialog(getActivity(), getString(R.string.settings_save_draft_result_dialog_title), options,
                getString(R.string.dialog_positive), getString(R.string.dialog_negative),
                mSharedPreferenceManager.getSaveDraftResults(), mSaveDraftResultCallback);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

    private void setSaveDraftResults(int mode) {
        String option;
        if (mode == 0) {
            option = getString(R.string.negative);
        } else {
            option = getString(R.string.possitive);
        }
        mSaveDraftResultButton.setText(getString(R.string.text_save_results_after_draft, option));
    }

    private void setSittingsOptionsText(int mode) {
        String sittingsOptionName;
        if (mode == SittingsMode.NO_SITTINGS) {
            sittingsOptionName = getString(R.string.settings_sittings_none);
        } else {
            sittingsOptionName = getString(R.string.settings_sittings_random);
        }
        mSittingsOptionsTextView.setText(getString(R.string.settings_sittings_option, sittingsOptionName));
    }

    private void setPairingOptionText(int mode) {
        String pairingOptionName;
        switch (mode) {
            case PairingMode.PAIRING_AUTOMATIC_CAN_REPEAT_PAIRINGS:
                pairingOptionName = getString(R.string.settings_pairing_mode_automatic_with_repeats);
                break;
            case PairingMode.PAIRING_MANUAL:
                pairingOptionName = getString(R.string.settings_pairing_mode_manual);
                break;
            case PairingMode.PAIRING_TOURNAMENT:
                pairingOptionName = getString(R.string.settings_pairing_mode_tournament);
                break;
            default:
                pairingOptionName = getString(R.string.settings_pairing_mode_automatic_with_repeats);
                break;
        }
        mCreateParingChooserTextView.setText(getString(R.string.text_personal_parings, pairingOptionName));
    }

    private MaterialDialog.ListCallbackSingleChoice mSaveDraftResultCallback = new MaterialDialog.ListCallbackSingleChoice() {
        @Override
        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
            /**
             * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
             * returning false here won't allow the newly selected radio button to actually be selected.
             **/
            switch (which) {
                case 0:
                    mSharedPreferenceManager.setSaveDraftResults(0);
                    break;
                case 1:
                default:
                    mSharedPreferenceManager.setSaveDraftResults(1);
                    break;
            }
            setSaveDraftResults(mSharedPreferenceManager.getSaveDraftResults());
            return true;
        }
    };

    private MaterialDialog.ListCallbackSingleChoice mSittingsCallback = new MaterialDialog.ListCallbackSingleChoice() {
        @Override
        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
            /**
             * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
             * returning false here won't allow the newly selected radio button to actually be selected.
             **/
            switch (which) {
                case SittingsMode.SITTINGS_RANDOM:
                    mSharedPreferenceManager.setGeneratedSittingMode(SittingsMode.SITTINGS_RANDOM);
                    break;
                case SittingsMode.NO_SITTINGS:
                default:
                    mSharedPreferenceManager.setGeneratedSittingMode(SittingsMode.NO_SITTINGS);
                    break;
            }
            setSittingsOptionsText(mSharedPreferenceManager.getGeneratedSittingMode());
            return true;
        }
    };

    private MaterialDialog.ListCallbackSingleChoice mPairingTypeCallback = new MaterialDialog.ListCallbackSingleChoice() {
        @Override
        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
            switch (which) {
                case PairingMode.PAIRING_MANUAL:
                    mSharedPreferenceManager.setPairingType(PairingMode.PAIRING_MANUAL);
                    break;
                case PairingMode.PAIRING_AUTOMATIC_CAN_REPEAT_PAIRINGS:
                    mSharedPreferenceManager.setPairingType(PairingMode.PAIRING_AUTOMATIC_CAN_REPEAT_PAIRINGS);
                    break;
                case PairingMode.PAIRING_TOURNAMENT:
                    mSharedPreferenceManager.setPairingType(PairingMode.PAIRING_TOURNAMENT);
                    break;
                default:
                    mSharedPreferenceManager.setPairingType(PairingMode.PAIRING_AUTOMATIC_CAN_REPEAT_PAIRINGS);
                    break;
            }
            setPairingOptionText(mSharedPreferenceManager.getPairingType());
            return true;
        }
    };
}
