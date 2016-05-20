package unii.draft.mtg.parings.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unii.draft.mtg.parings.ManualPlayerPairingActivity;
import unii.draft.mtg.parings.ParingDashboardActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.SittingsActivity;
import unii.draft.mtg.parings.algorithm.IAlgorithmConfigure;
import unii.draft.mtg.parings.algorithm.ManualParingAlgorithm;
import unii.draft.mtg.parings.algorithm.ParingAlgorithm;
import unii.draft.mtg.parings.sharedprefrences.SettingsPreferencesFactory;
import unii.draft.mtg.parings.sharedprefrences.SettingsSharedPreferences;
import unii.draft.mtg.parings.sittings.SittingsMode;
import unii.draft.mtg.parings.validation.ValidationHelper;
import unii.draft.mtg.parings.view.custom.IActivityHandler;
import unii.draft.mtg.parings.view.custom.IPlayerList;

/**
 * Created by apachucy on 2015-09-25.
 */
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

    @OnClick(R.id.init_addPlayerButton)
    void onAddPlayerClick(View view) {
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
    void onStartGameClick(View view) {
        if (!ValidationHelper.isTextInputFieldEmpty(mRoundsTextInput,
                getString(R.string.warning_empty_field))) {
            if (mPlayerNameList.getPlayerList().isEmpty() || mPlayerNameList.getPlayerList().size() < 2) {
                Toast.makeText(mActivity,
                        getString(R.string.warning_need_players),
                        Toast.LENGTH_LONG).show();
                // if number of player where bigger than
                // round ask user to change it
            } else if (Integer.parseInt(mRoundsTextInput.getEditText().getText()
                    .toString()) >= mPlayerNameList.getPlayerList().size()) {
                mActivityHandler.showInfoDialog(getString(R.string.dialog_warning_title),
                        getString(R.string.dialog_warning_message),
                        getString(R.string.dialog_start_button));
            } else {
                mActivityHandler.showInfoDialog(getString(R.string.dialog_start_title),
                        getString(R.string.dialog_start_message, mPlayerNameList.getPlayerList().size(), Integer.parseInt(mRoundsTextInput.getEditText().getText().toString())),
                        getString(R.string.start_game), mStartGameDialogOnClickListener);

            }
        }
    }

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


        mListAdapter = new ArrayAdapter<String>(mActivity, R.layout.row_player_name,
                mPlayerNameList.getPlayerList());

        mPlayerNameTextInput.setHint(getString(R.string.hint_player_name));
        mRoundsTextInput.setHint(getString(R.string.hint_rounds));
        mRoundsTextInput.setErrorEnabled(true);
        mPlayerNameTextInput.setErrorEnabled(true);
        View header = inflater.inflate(R.layout.header_names, null);

        mPlayerList.addHeaderView(header);
        mPlayerList.setAdapter(mListAdapter);

        return view;
    }

    //todo: open sittingsPlayerActivity
    private MaterialDialog.SingleButtonCallback mStartGameDialogOnClickListener = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(MaterialDialog dialog, DialogAction which) {
            if (dialog != null && mRoundsTextInput != null && mRoundsTextInput.getEditText() != null) {
                dialog.dismiss();
                Intent intent = null;
                IAlgorithmConfigure algorithmConfigure = (IAlgorithmConfigure) mActivity.getApplication();
                //set parings Factory
                if (SettingsPreferencesFactory.getInstance().areManualParings()) {
                    algorithmConfigure.setAlgorithm(new ManualParingAlgorithm(mPlayerNameList.getPlayerList(), Integer.parseInt(mRoundsTextInput.getEditText().getText().toString())));
                } else {
                    algorithmConfigure.setAlgorithm(new ParingAlgorithm(mPlayerNameList.getPlayerList(), Integer.parseInt(mRoundsTextInput.getEditText().getText().toString())));
                }
                //set started activity
                if (SettingsPreferencesFactory.getInstance().getGeneratedSittingMode() == SittingsMode.SITTINGS_RANDOM) {
                    intent = new Intent(mActivity, SittingsActivity.class);
                } else if (SettingsPreferencesFactory.getInstance().areManualParings()) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
