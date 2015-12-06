package unii.draft.mtg.parings.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unii.draft.mtg.parings.ManualPlayerPairingActivity;
import unii.draft.mtg.parings.ParingDashboardActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.algorithm.IAlgorithmConfigure;
import unii.draft.mtg.parings.algorithm.ManualParingAlgorithm;
import unii.draft.mtg.parings.algorithm.ParingAlgorithm;
import unii.draft.mtg.parings.sharedprefrences.SettingsPreferencesFactory;
import unii.draft.mtg.parings.validation.ValidationHelper;
import unii.draft.mtg.parings.view.IPlayerList;

/**
 * Created by apachucy on 2015-09-25.
 */
public class GameMenuFragment extends BaseFragment {
    private static final String TAG_DIALOG_START_GAME = GameMenuFragment.class
            .getName() + "TAG_DIALOG_START_GAME";
    private static final String TAG_DIALOG_WARNING = GameMenuFragment.class
            .getName() + "TAG_DIALOG_WARNING";

    private CustomDialogFragment mStartGameDialogFragment;
    private CustomDialogFragment mWarningDialogFragment;
    private IPlayerList mPlayerNameList;
    private ArrayAdapter<String> mListAdapter;

    @Bind(R.id.init_playerNameEditText)
    EditText mPlayerNameEditText;
    @Bind(R.id.init_roundsEditText)
    EditText mRoundsEditText;
    @Bind(R.id.init_playerList)
    ListView mPlayerList;

    @OnClick(R.id.init_addPlayerButton)
    void onAddPlayerClick(View view) {
        // add new player
        if (!ValidationHelper.isEditTextEmpty(mPlayerNameEditText,
                getString(R.string.warning_empty_field),
                mWarningDrawable) && !isNameAddedBefore(mPlayerNameEditText.getText()
                .toString())) {
            mPlayerNameList.getPlayerList().add(mPlayerNameEditText.getText()
                    .toString());
            mPlayerNameEditText.setText("");
            mListAdapter.notifyDataSetChanged();

        } else if (isNameAddedBefore(mPlayerNameEditText.getText()
                .toString())) {
            mPlayerNameEditText.setError(getResources().getString(R.string.warning_player_name_added), mWarningDrawable);
        }
    }

    @OnClick(R.id.init_roundsButton)
    void onStartGameClick(View view) {
        if (!ValidationHelper.isEditTextEmpty(mRoundsEditText,
                getString(R.string.warning_empty_field),
                mWarningDrawable)) {
            if (mPlayerNameList.getPlayerList().isEmpty() || mPlayerNameList.getPlayerList().size() < 2) {
                Toast.makeText(mActivity,
                        getString(R.string.warning_need_players),
                        Toast.LENGTH_LONG).show();
                // if number of player where bigger than
                // round ask user to change it
            } else if (Integer.parseInt(mRoundsEditText.getText()
                    .toString()) >= mPlayerNameList.getPlayerList().size()) {
                if (mWarningDialogFragment == null) {
                    mWarningDialogFragment = CustomDialogFragment
                            .newInstance(
                                    getString(R.string.dialog_warning_title),
                                    getString(R.string.dialog_warning_message),
                                    getString(R.string.dialog_start_button));
                }
                mWarningDialogFragment.show(getFragmentManager(),
                        TAG_DIALOG_WARNING,
                        mWarningDialogOnClickListener);
            } else {
                mStartGameDialogFragment = CustomDialogFragment.newInstance(
                        getString(R.string.dialog_start_title),
                        getString(R.string.dialog_start_message, mPlayerNameList.getPlayerList().size(), Integer.parseInt(mRoundsEditText.getText().toString())),
                        getString(R.string.start_game));

                mStartGameDialogFragment.show(getFragmentManager(),
                        TAG_DIALOG_START_GAME,
                        mStartGameDialogOnClickListener);

            }
        }
    }

    private Drawable mWarningDrawable;

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mPlayerNameList = (IPlayerList) mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_menu, container, false);
        ButterKnife.bind(this, view);
        mWarningDrawable = getResources().getDrawable(R.drawable.ic_warning);
        mWarningDrawable.setBounds(new Rect(0, 0, mWarningDrawable
                .getIntrinsicWidth(), mWarningDrawable.getIntrinsicHeight()));


        mListAdapter = new ArrayAdapter<String>(mActivity, R.layout.row_player_name,
                mPlayerNameList.getPlayerList());


        View header = inflater.inflate(R.layout.header_names, null);

        mPlayerList.addHeaderView(header);
        mPlayerList.setAdapter(mListAdapter);

        return view;
    }


    private View.OnClickListener mStartGameDialogOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mStartGameDialogFragment != null) {
                mStartGameDialogFragment.dismiss();
                Intent intent = null;
                IAlgorithmConfigure algorithmConfigure = (IAlgorithmConfigure) mActivity.getApplication();
                if (SettingsPreferencesFactory.getInstance().areManualParings()) {
                    algorithmConfigure.setAlgorithm(new ManualParingAlgorithm(mPlayerNameList.getPlayerList(), Integer.parseInt(mRoundsEditText.getText().toString())));
                    intent = new Intent(mActivity,
                            ManualPlayerPairingActivity.class);
                } else {
                    algorithmConfigure.setAlgorithm(new ParingAlgorithm(mPlayerNameList.getPlayerList(), Integer.parseInt(mRoundsEditText.getText().toString())));
                    intent = new Intent(mActivity,
                            ParingDashboardActivity.class);

                }
                startActivity(intent);
                mActivity.finish();
            }

        }
    };

    private View.OnClickListener mWarningDialogOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mWarningDialogFragment != null) {
                mWarningDialogFragment.dismiss();
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
