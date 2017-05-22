package unii.draft.mtg.parings.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Lazy;
import unii.draft.mtg.parings.MainActivity;
import unii.draft.mtg.parings.ParingDashboardActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.base.BaseAlgorithm;
import unii.draft.mtg.parings.buisness.algorithm.base.PairingMode;
import unii.draft.mtg.parings.buisness.algorithm.tournament.TournamentRounds;
import unii.draft.mtg.parings.buisness.sittings.SittingsMode;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.util.validation.ValidationHelper;
import unii.draft.mtg.parings.view.activities.options.ManualPlayerPairingActivity;
import unii.draft.mtg.parings.view.activities.options.SittingsActivity;
import unii.draft.mtg.parings.view.custom.IActivityHandler;
import unii.draft.mtg.parings.view.custom.IPlayerList;


public class GameMenuFragment extends BaseFragment {

    private IPlayerList mPlayerNameList;
    private IActivityHandler mActivityHandler;
    private ArrayAdapter<String> mListAdapter;
    private Activity mActivity;

    @Bind(R.id.init_playerList)
    ListView mPlayerList;

    @Bind(R.id.init_playerNameTextInput)
    TextInputLayout mPlayerNameTextInput;

    @Bind(R.id.init_roundsTextInput)
    TextInputLayout mRoundsTextInput;


    @Bind(R.id.init_addPlayerFromHistoryButton)
    Button mAddPlayerFromHistoryButton;

    @Inject
    Lazy<ISharedPreferences> mSharedPreferenceManager;
    @Inject
    Lazy<AlgorithmChooser> mAlgorithmChooser;
    @Inject
    Lazy<IDatabaseHelper> mDatabaseHelper;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mPlayerNameList = (IPlayerList) mActivity;
        if (!(mActivity instanceof IActivityHandler)) {
            throw new RuntimeException("Activity should implement IActivityHandler");
        }
        mActivityHandler = (IActivityHandler) mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_menu, container, false);
        ButterKnife.bind(this, view);
        injectDependencies();
        initFragmentData();
        initFragmentView(inflater);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.init_addPlayerButton)
    void onAddPlayerClick() {
        // add new player
        if (!ValidationHelper.isTextInputFieldEmpty(mPlayerNameTextInput,
                getString(R.string.warning_empty_field)) && !isNameAddedBefore(mPlayerNameTextInput.getEditText().getText()
                .toString())) {
            mPlayerNameList.getPlayerList().add(mPlayerNameTextInput.getEditText().getText()
                    .toString());
            mPlayerNameTextInput.getEditText().setText("");
            mListAdapter.notifyDataSetChanged();

        } else if (isNameAddedBefore(mPlayerNameTextInput.getEditText().getText()
                .toString())) {
            mPlayerNameTextInput.setError(getResources().getString(R.string.warning_player_name_added));
        }
    }

    @OnClick(R.id.init_roundsButton)
    void onStartGameClick() {
        if (isValidRoundEditText()) {
            if (mPlayerNameList.getPlayerList().isEmpty() || mPlayerNameList.getPlayerList().size() < 2) {
                Toast.makeText(mActivity,
                        getString(R.string.warning_need_players),
                        Toast.LENGTH_LONG).show();
                // if number of player where bigger than
                // round ask user to change it
            } else if (mRoundsTextInput.getVisibility() == View.VISIBLE && Integer.parseInt(mRoundsTextInput.getEditText().getText()
                    .toString()) >= mPlayerNameList.getPlayerList().size()) {
                mActivityHandler.showInfoDialog(getString(R.string.dialog_warning_title),
                        getString(R.string.dialog_warning_message),
                        getString(R.string.dialog_start_button));
            } else {
                int rounds = 0;
                if (mRoundsTextInput.getVisibility() == View.VISIBLE && mRoundsTextInput != null && mRoundsTextInput.getEditText() != null) {
                    rounds = Integer.parseInt(mRoundsTextInput.getEditText().getText().toString());
                } else if (mRoundsTextInput.getVisibility() != View.VISIBLE) {
                    rounds = new TournamentRounds().getMaxRound(mPlayerNameList.getPlayerList().size());
                }
                mActivityHandler.showInfoDialog(getString(R.string.dialog_start_title),
                        getString(R.string.dialog_start_message, mPlayerNameList.getPlayerList().size(), rounds),
                        getString(R.string.start_game), mStartGameDialogOnClickListener);

            }
        }
    }

    @OnClick(R.id.init_addPlayerFromHistoryButton)
    public void onAddPlayersFromHistoryClicked() {
        if (playersNotExistInHistory()) {
            Toast.makeText(getContext(), getString(R.string.no_players_from_history), Toast.LENGTH_SHORT).show();
            return;
        }
        final List<String> playersNameFromHistory = mDatabaseHelper.get().getAllPlayersNames();

        MaterialDialog.ListCallbackMultiChoice listCallbackMultiChoice = new MaterialDialog.ListCallbackMultiChoice() {
            @Override
            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

                for (Integer aWhich : which) {
                    String playerName = playersNameFromHistory.get(aWhich);
                    if (!isNameAddedBefore(playerName)) {
                        mPlayerNameList.getPlayerList().add(playerName);
                        mListAdapter.notifyDataSetChanged();
                    }
                }
                return true;
            }
        };

        showMultipleChoiceListDialog(getContext(), getString(R.string.add_players_from_history), playersNameFromHistory,
                listCallbackMultiChoice, getString(R.string.add_item));
    }


    protected void initFragmentView(LayoutInflater inflater) {
        mListAdapter = new ArrayAdapter<>(mActivity, R.layout.row_player_name,
                mPlayerNameList.getPlayerList());

        mPlayerNameTextInput.setHint(getString(R.string.hint_player_name));
        mRoundsTextInput.setHint(getString(R.string.hint_rounds));
        mRoundsTextInput.setErrorEnabled(true);
        mPlayerNameTextInput.setErrorEnabled(true);
        View header = inflater.inflate(R.layout.header_names, null);
        if (mSharedPreferenceManager.get().getPairingType() == PairingMode.PAIRING_TOURNAMENT) {
            mRoundsTextInput.setVisibility(View.GONE);
        }

        mAddPlayerFromHistoryButton.setEnabled(!playersNotExistInHistory());
        mPlayerList.addHeaderView(header);
        mPlayerList.setAdapter(mListAdapter);
    }

    @Override
    protected void initFragmentView() {
        //Not-implemented
    }

    @Override
    protected void initFragmentData() {

    }

    private boolean isValidRoundEditText() {
        return (mRoundsTextInput.getVisibility() != View.VISIBLE) || (mRoundsTextInput.getVisibility() == View.VISIBLE && !ValidationHelper.isTextInputFieldEmpty(mRoundsTextInput,
                getString(R.string.warning_empty_field)));
    }


    private boolean playersNotExistInHistory() {
        final List<String> playersNameFromHistory = mDatabaseHelper.get().getAllPlayersNames();
        return playersNameFromHistory.isEmpty();
    }

    private MaterialDialog.SingleButtonCallback mStartGameDialogOnClickListener = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            int rounds = 0;
            if (mRoundsTextInput.getVisibility() == View.VISIBLE && mRoundsTextInput != null && mRoundsTextInput.getEditText() != null) {
                rounds = Integer.parseInt(mRoundsTextInput.getEditText().getText().toString());
            } else if (mRoundsTextInput.getVisibility() != View.VISIBLE) {
                rounds = new TournamentRounds().getMaxRound(mPlayerNameList.getPlayerList().size());
            }

            if (rounds != 0) {
                dialog.dismiss();
                Intent intent;
                //set parings Factory
                mAlgorithmChooser.get().getCurrentAlgorithm().startAlgorithm(mPlayerNameList.getPlayerList(), rounds);
                if (mAlgorithmChooser.get().getCurrentAlgorithm() instanceof BaseAlgorithm) {
                    BaseAlgorithm baseAlgorithm = (BaseAlgorithm) mAlgorithmChooser.get().getCurrentAlgorithm();
                    if (!baseAlgorithm.cacheDraft()) {
                        displayErrorDialog();
                        return;
                    }
                }
                //set started activity
                if (mSharedPreferenceManager.get().getGeneratedSittingMode() == SittingsMode.SITTINGS_RANDOM) {
                    intent = new Intent(mActivity, SittingsActivity.class);
                } else if (mSharedPreferenceManager.get().getPairingType() == PairingMode.PAIRING_MANUAL) {
                    intent = new Intent(mActivity,
                            ManualPlayerPairingActivity.class);
                } else {
                    intent = new Intent(mActivity,
                            ParingDashboardActivity.class);

                }
                startActivity(intent);
                mActivity.finish();
            }
        }
    };


    private boolean isNameAddedBefore(String playerName) {
        boolean isAddedBefore = false;

        for (String name : mPlayerNameList.getPlayerList()) {
            if (playerName.equals(name)) {
                isAddedBefore = true;
                break;
            }

        }
        return isAddedBefore;
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }


    private void displayErrorDialog() {
        ((MainActivity) getActivity()).showInfoDialog(getString(R.string.dialog_error_algorithm_title),
                getString(R.string.dialog_error_algorithm__message),
                getString(R.string.dialog_start_button), mDialogErrorButtonClickListener);
    }

    private MaterialDialog.SingleButtonCallback mDialogErrorButtonClickListener = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(MaterialDialog dialog, DialogAction which) {
            dialog.dismiss();
        }
    };
}
