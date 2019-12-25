package unii.draft.mtg.parings.view.fragments.settings;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.annotation.PairingModeNotation;
import unii.draft.mtg.parings.buisness.algorithm.base.PairingMode;
import unii.draft.mtg.parings.buisness.sittings.SittingsMode;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

public class DraftSettingsFragment extends BaseFragment {

    private Unbinder mUnbinder;

    @Nullable
    @BindView(R.id.settings_sittingsButton)
    Button mSittingsOptionsTextView;
    @Nullable
    @BindView(R.id.settings_createPairingsChooserButton)
    Button mCreateParingChooserTextView;
    @Nullable
    @BindView(R.id.settings_saveResultsAfterDraftButton)
    Button mSaveDraftResultButton;
    @Inject
    ISharedPreferences mSharedPreferenceManager;


    @Nullable
    @BindView(R.id.settings_pointsMatchWinButton)
    Button mMatchWinTextView;
    @Nullable
    @BindView(R.id.settings_pointsMatchDrawButton)
    Button mMatchDrawTextView;
    @Nullable
    @BindView(R.id.settings_pointsGameWinButton)
    Button mGameWinTextView;
    @Nullable
    @BindView(R.id.settings_pointsGameDrawButton)
    Button mGameDrawTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_draft, container, false);
        mUnbinder = ButterKnife.bind(this, view);
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

        mMatchWinTextView.setText(getString(R.string.button_match_winning_points, Integer.toString(mSharedPreferenceManager.getPointsForMatchWinning())));
        mMatchDrawTextView.setText(getString(R.string.button_match_draw_points, Float.toString(mSharedPreferenceManager.getPointsForMatchDraws())));
        mGameWinTextView.setText(getString(R.string.button_game_winning_points, Integer.toString(mSharedPreferenceManager.getPointsForGameWinning())));
        mGameDrawTextView.setText(getString(R.string.button_game_draw_points, Float.toString(mSharedPreferenceManager.getPointsForGameDraws())));
    }

    @Override
    protected void initFragmentData() {

    }

    @OnClick(R.id.settings_sittingsButton)
    void onSittingsTextViewClicked() {
        List<String> sittingsOptionList = new ArrayList<>();
        sittingsOptionList.add(getString(R.string.settings_sittings_none));
        sittingsOptionList.add(getString(R.string.settings_sittings_random));
        sittingsOptionList.add(getString(R.string.settings_sittings_tournament));
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
        pairingsOptionList.add(getString(R.string.settings_pairing_mode_round_robin));
        pairingsOptionList.add(getString(R.string.settings_pairing_mode_knock_out));
        showRadioButtonListDialog(getActivity(), getString(R.string.settings_pairing_mode_dialog_title), pairingsOptionList,
                getString(R.string.dialog_positive), getString(R.string.dialog_negative),
                mSharedPreferenceManager.getPairingType(), mPairingTypeCallback);


    }


    @OnClick(R.id.settings_pointsMatchWinButton)
    void onChangeMatchWinPointClicked() {
        showEditTextDialog(getContext(),
                getString(R.string.dialog_title_match_win_points),
                getString(R.string.dialog_body_match_win_points),
                getString(R.string.dialog_win_points_hint),
                Integer.toString(mSharedPreferenceManager.getPointsForMatchWinning()),
                new UpdateData() {
                    @Override
                    public void updateView() {
                        String newValue = Integer.toString(mSharedPreferenceManager.getPointsForMatchWinning());
                        mMatchWinTextView.setText(getString(R.string.button_match_winning_points, newValue));
                    }

                    @Override
                    public void updateSharedPreferences(String newData) {
                        int newValue = Integer.parseInt(newData);
                        if (newValue != mSharedPreferenceManager.getPointsForMatchWinning()) {
                            mSharedPreferenceManager.setPointsForMatchWinning(newValue);
                        }
                    }
                }
        );
    }

    @OnClick(R.id.settings_pointsMatchDrawButton)
    void onChangeMatchDrawPointClicked() {
        float points = -1;
        try {
            points = mSharedPreferenceManager.getPointsForMatchDraws();
        } catch (NumberFormatException e) {
            points = 1;
        }
        showEditTextDialogWithDecimalValues(getContext(),
                getString(R.string.dialog_title_match_draw_points),
                getString(R.string.dialog_title_match_draw_points),
                getString(R.string.dialog_draw_points_hint),
                Float.toString(points),
                new UpdateData() {
                    @Override
                    public void updateView() {
                        String newValue = Float.toString(mSharedPreferenceManager.getPointsForMatchDraws());
                        mMatchDrawTextView.setText(getString(R.string.button_match_draw_points, newValue));
                    }

                    @Override
                    public void updateSharedPreferences(String newData) {

                        float newValue = convertSafety(newData);
                        if (newValue != mSharedPreferenceManager.getPointsForMatchDraws()) {
                            mSharedPreferenceManager.setPointsForMatchDraws(newValue);
                        }
                    }
                }
        );
    }

    @OnClick(R.id.settings_pointsGameWinButton)
    void onChangeGameWinPointClicked() {
        showEditTextDialog(getContext(),
                getString(R.string.dialog_title_game_win_points),
                getString(R.string.dialog_title_game_win_points),
                getString(R.string.dialog_win_points_hint),
                Integer.toString(mSharedPreferenceManager.getPointsForGameWinning()),
                new UpdateData() {
                    @Override
                    public void updateView() {
                        String newValue = Integer.toString(mSharedPreferenceManager.getPointsForGameWinning());
                        mGameWinTextView.setText(getString(R.string.button_game_winning_points, newValue));
                    }

                    @Override
                    public void updateSharedPreferences(String newData) {
                        int newValue = Integer.parseInt(newData);
                        if (newValue != mSharedPreferenceManager.getPointsForGameWinning()) {
                            mSharedPreferenceManager.setPointsForGameWinning(newValue);
                        }
                    }
                }
        );
    }

    @OnClick(R.id.settings_pointsGameDrawButton)
    void onChangeGameDrawPointClicked() {
        showEditTextDialogWithDecimalValues(getContext(),
                getString(R.string.dialog_title_game_draw_points),
                getString(R.string.dialog_title_game_draw_points),
                getString(R.string.dialog_draw_points_hint),
                Float.toString(mSharedPreferenceManager.getPointsForGameDraws()),
                new UpdateData() {
                    @Override
                    public void updateView() {
                        String newValue = Float.toString(mSharedPreferenceManager.getPointsForGameDraws());
                        mGameDrawTextView.setText(getString(R.string.button_game_draw_points, newValue));
                    }

                    @Override
                    public void updateSharedPreferences(String newData) {

                        float newValue = convertSafety(newData);
                        if (newValue != mSharedPreferenceManager.getPointsForGameDraws()) {
                            mSharedPreferenceManager.setPointsForGameDraws(newValue);
                        }
                    }
                }
        );
    }

    private float convertSafety(String value) {
        float numericalValue = 0f;
        try {
            numericalValue = Float.parseFloat(value);
        } catch (NumberFormatException e) {
            numericalValue = Integer.parseInt(value) * 1.0f;
        }
        return numericalValue;
    }

    @OnClick(R.id.settings_saveResultsAfterDraftButton)
    void onSaveDraftResultsViewClicked() {
        List<String> options = new ArrayList<>();
        options.add(getString(R.string.negative));
        options.add(getString(R.string.positive));

        showRadioButtonListDialog(getActivity(), getString(R.string.settings_save_draft_result_dialog_title), options,
                getString(R.string.dialog_positive), getString(R.string.dialog_negative),
                mSharedPreferenceManager.getSaveDraftResults(), mSaveDraftResultCallback);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

    private void setSaveDraftResults(int mode) {
        String option;
        if (mode == 0) {
            option = getString(R.string.negative);
        } else {
            option = getString(R.string.positive);
        }
        mSaveDraftResultButton.setText(getString(R.string.text_save_results_after_draft, option));
    }

    private void setSittingsOptionsText(int mode) {
        String sittingsOptionName;
        if (mode == SittingsMode.NO_SITTINGS) {
            sittingsOptionName = getString(R.string.settings_sittings_none);

        } else if (mode == SittingsMode.SITTINGS_RANDOM) {
            sittingsOptionName = getString(R.string.settings_sittings_random);
        } else {
            sittingsOptionName = getString(R.string.settings_sittings_tournament);
        }
        mSittingsOptionsTextView.setText(getString(R.string.settings_sittings_option, sittingsOptionName));
    }

    private void setPairingOptionText(@PairingModeNotation int mode) {
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
            case PairingMode.PAIRING_ROUND_ROBIN:
                pairingOptionName = getString(R.string.settings_pairing_mode_round_robin);
                break;
            case PairingMode.PAIRING_ROUND_KNOCK_OUT:
                pairingOptionName = getString(R.string.settings_pairing_mode_knock_out);
                break;
            default:
                pairingOptionName = getString(R.string.settings_pairing_mode_automatic_with_repeats);
                break;
        }
        mCreateParingChooserTextView.setText(getString(R.string.text_personal_parings, pairingOptionName));
    }

    @NonNull
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

    @NonNull
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
                case SittingsMode.SITTINGS_TOURNAMENT:
                    mSharedPreferenceManager.setGeneratedSittingMode(SittingsMode.SITTINGS_TOURNAMENT);
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

    @NonNull
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
                case PairingMode.PAIRING_ROUND_ROBIN:
                    mSharedPreferenceManager.setPairingType(PairingMode.PAIRING_ROUND_ROBIN);
                    break;
                case PairingMode.PAIRING_ROUND_KNOCK_OUT:
                    mSharedPreferenceManager.setPairingType(PairingMode.PAIRING_ROUND_KNOCK_OUT);
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
