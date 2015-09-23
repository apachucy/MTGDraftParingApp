package unii.draft.mtg.parings;

import java.util.ArrayList;

import unii.draft.mtg.parings.algorithm.IAlgorithmConfigure;
import unii.draft.mtg.parings.algorithm.ManualParingAlgorithm;
import unii.draft.mtg.parings.algorithm.ParingAlgorithm;
import unii.draft.mtg.parings.config.BaseConfig;
import unii.draft.mtg.parings.config.BundleConst;
import unii.draft.mtg.parings.sharedprefrences.SettingsPreferencesFactory;
import unii.draft.mtg.parings.validation.ValidationHelper;
import unii.draft.mtg.parings.view.CustomDialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class GameMenuActivity extends Activity {

    private EditText mPlayerNameEditText;
    private EditText mRoundsEditText;
    private ListView mPlayerList;
    private Button mAddPlayer;
    private Button mStartGame;
    private ArrayList<String> mPlayerNameList;
    private ArrayAdapter<String> mListAdapter;
    private Drawable mWarningDrawable;
    private CustomDialogFragment mInfoDialogFragment;

    private static final String TAG_DIALOG_START_GAME = GameMenuActivity.class
            .getName() + "TAG_DIALOG_START_GAME";
    private static final String TAG_DIALOG_WARNING = GameMenuActivity.class
            .getName() + "TAG_DIALOG_WARNING";
    private static final String TAG_DIALOG_INFO = GameMenuActivity.class
            .getName() + "TAG_DIALOG_INFO";
    private CustomDialogFragment mStartGameDialogFragment;
    private CustomDialogFragment mWarningDialogFragment;

    // help library
    private ShowcaseView mShowcaseView;
    private int mShowCaseID = 144;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamers_initial);

        mAddPlayer = (Button) findViewById(R.id.init_addPlayerButton);
        mStartGame = (Button) findViewById(R.id.init_roundsButton);

        mPlayerNameEditText = (EditText) findViewById(R.id.init_playerNameEditText);
        mRoundsEditText = (EditText) findViewById(R.id.init_roundsEditText);


        mWarningDrawable = getResources().getDrawable(R.drawable.ic_warning);
        mWarningDrawable.setBounds(new Rect(0, 0, mWarningDrawable
                .getIntrinsicWidth(), mWarningDrawable.getIntrinsicHeight()));

        mAddPlayer.setOnClickListener(mButtonsOnClickListener);
        mStartGame.setOnClickListener(mButtonsOnClickListener);

        mPlayerNameList = new ArrayList<String>();
        mListAdapter = new ArrayAdapter<String>(this, R.layout.row_player_name,
                mPlayerNameList);
        View header = getLayoutInflater().inflate(R.layout.header_names, null);
        mPlayerList = (ListView) findViewById(R.id.init_playerList);
        mPlayerList.addHeaderView(header);
        mPlayerList.setAdapter(mListAdapter);
        mStartGameDialogFragment = CustomDialogFragment.newInstance(
                getString(R.string.dialog_start_title),
                getString(R.string.dialog_start_message),
                getString(R.string.start_game));


        // hints for users
        ViewTarget target = new ViewTarget(R.id.init_roundsButton, this);
        mShowcaseView = new ShowcaseView.Builder(this, true).setTarget(target)
                .setContentTitle(getString(R.string.help_title))
                .setStyle(R.style.showcaseThem)
                .setContentText(getString(R.string.help_message))
                .singleShot(mShowCaseID).build();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RelativeLayout.LayoutParams buttonBottomLeft = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            buttonBottomLeft.setMargins(BaseConfig.MARGIN_NOT_SET, BaseConfig.MARGIN_NOT_SET, BaseConfig.MARGIN_NOT_SET, BaseConfig.MARGIN_BOTTOM);
            buttonBottomLeft.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            buttonBottomLeft.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mShowcaseView.setButtonPosition(buttonBottomLeft);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(GameMenuActivity.this,
                        SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_info:
                if (mInfoDialogFragment == null) {
                    mInfoDialogFragment = CustomDialogFragment.newInstance(
                            getString(R.string.dialog_info_title),
                            getString(R.string.dialog_info_message),
                            getString(R.string.dialog_start_button));
                }
                mInfoDialogFragment.show(getFragmentManager(), TAG_DIALOG_INFO);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private android.view.View.OnClickListener mButtonsOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.init_addPlayerButton:

                    // add new player
                    if (!ValidationHelper.isEditTextEmpty(mPlayerNameEditText,
                            getString(R.string.warning_empty_field),
                            mWarningDrawable) && !isNameAddedBefore(mPlayerNameEditText.getText()
                            .toString())) {
                        mPlayerNameList.add(mPlayerNameEditText.getText()
                                .toString());
                        mPlayerNameEditText.setText("");
                        mListAdapter.notifyDataSetChanged();

                    } else if (isNameAddedBefore(mPlayerNameEditText.getText()
                            .toString())) {
                        mPlayerNameEditText.setError(getResources().getString(R.string.warning_player_name_added), mWarningDrawable);
                    }
                    break;
                case R.id.init_roundsButton:
                    if (!ValidationHelper.isEditTextEmpty(mRoundsEditText,
                            getString(R.string.warning_empty_field),
                            mWarningDrawable)) {
                        if (mPlayerNameList.isEmpty() || mPlayerNameList.size() < 2) {
                            Toast.makeText(GameMenuActivity.this,
                                    getString(R.string.warning_need_players),
                                    Toast.LENGTH_LONG).show();
                            // if number of player where bigger than
                            // round ask user to change it
                        } else if (Integer.parseInt(mRoundsEditText.getText()
                                .toString()) >= mPlayerNameList.size()) {
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
                            mStartGameDialogFragment.show(getFragmentManager(),
                                    TAG_DIALOG_START_GAME,
                                    mStartGameDialogOnClickListener);

                        }
                    }
                    break;
                default:
                    break;
            }

        }
    };

    private OnClickListener mStartGameDialogOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mStartGameDialogFragment != null) {
                mStartGameDialogFragment.dismiss();
                Intent intent = null;
                IAlgorithmConfigure algorithmConfigure = (IAlgorithmConfigure) getApplication();
                if (SettingsPreferencesFactory.getInstance().areManualParings()) {
                    algorithmConfigure.setAlgorithm(new ManualParingAlgorithm(mPlayerNameList, Integer.parseInt(mRoundsEditText.getText().toString())));
                    intent = new Intent(GameMenuActivity.this,
                            MatchPlayerActivity.class);
                } else {
                    algorithmConfigure.setAlgorithm(new ParingAlgorithm(mPlayerNameList, Integer.parseInt(mRoundsEditText.getText().toString())));
                    intent = new Intent(GameMenuActivity.this,
                            ParingsActivity.class);

                }
                startActivity(intent);
                finish();
            }

        }
    };

    private OnClickListener mWarningDialogOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mWarningDialogFragment != null) {
                mWarningDialogFragment.dismiss();
            }
        }
    };

    private boolean isNameAddedBefore(String playerName) {
        boolean isAddedBefore = false;

        for (String name : mPlayerNameList) {
            if (playerName.equals(name)) {
                isAddedBefore = true;
                break;
            }

        }
        return isAddedBefore;
    }
}
