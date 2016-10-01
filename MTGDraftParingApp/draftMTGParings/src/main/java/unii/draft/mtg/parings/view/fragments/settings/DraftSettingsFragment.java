package unii.draft.mtg.parings.view.fragments.settings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.PairingMode;
import unii.draft.mtg.parings.buisness.sittings.SittingsMode;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

public class DraftSettingsFragment extends BaseFragment {


    @Bind(R.id.settings_sittingsTextView)
    TextView mSittingsOptionsTextView;
    @Bind(R.id.settings_createPairingsChooserTextView)
    TextView mCreateParingChooserTextView;
    @Inject
    ISharedPreferences mSharedPreferenceManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_time, container, false);
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
    }

    @Override
    protected void initFragmentData() {

    }
    @OnClick(R.id.settings_sittingsTextView)
    void onSittingsTextViewClicked(View view){
        List<String> sittingsOptionList = new ArrayList<>();
        sittingsOptionList.add(getString(R.string.settings_sittings_none));
        sittingsOptionList.add(getString(R.string.settings_sittings_random));
        showRadioButtonListDialog(getActivity(), getString(R.string.settings_sittings_dialog_title), sittingsOptionList,
                getString(R.string.dialog_positive), getString(R.string.dialog_negative),
                mSharedPreferenceManager.getGeneratedSittingMode(), mSittingsCallback);

    }
    @OnClick(R.id.settings_createPairingsChooserTextView)
    void onCreatePairingsTextViewClicked(View view){
        List<String> pairingsOptionList = new ArrayList<>();
        pairingsOptionList.add(getString(R.string.settings_pairing_mode_automatic_with_repeats));
        pairingsOptionList.add(getString(R.string.settings_pairing_mode_manual));
        showRadioButtonListDialog(getActivity(), getString(R.string.settings_pairing_mode_dialog_title), pairingsOptionList,
                getString(R.string.dialog_positive), getString(R.string.dialog_negative),
                mSharedPreferenceManager.getPairingType(), mPairingTypeCallback);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
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
        if (mode == PairingMode.PAIRING_AUTOMATIC_CAN_REPEAT_PAIRINGS) {
            pairingOptionName = getString(R.string.settings_pairing_mode_automatic_with_repeats);
        } else {
            pairingOptionName = getString(R.string.settings_pairing_mode_manual);
        }
        mCreateParingChooserTextView.setText(getString(R.string.text_personal_parings, pairingOptionName));
    }


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
                default:
                    mSharedPreferenceManager.setPairingType(PairingMode.PAIRING_AUTOMATIC_CAN_REPEAT_PAIRINGS);
                    break;
            }
            setPairingOptionText(mSharedPreferenceManager.getPairingType());
            return true;
        }
    };
}
