package unii.draft.mtg.parings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.TourGuide;
import unii.draft.mtg.parings.algorithm.AlgorithmFactory;
import unii.draft.mtg.parings.algorithm.IParingAlgorithm;
import unii.draft.mtg.parings.pojo.Player;
import unii.draft.mtg.parings.sharedprefrences.SettingsPreferencesFactory;
import unii.draft.mtg.parings.view.CustomDialogFragment;
import unii.draft.mtg.parings.view.PlayerAdapter;

public class PlayerPositionActivity extends BaseActivity {

    @Bind(R.id.player_position_roundTextView)
    TextView mRoundTextView;
    @Bind(R.id.player_position_winnerTextView)
    TextView mWinnerTextView;
    @Bind(R.id.player_position_nextGameButton)
    Button mNextGameButton;
    @Bind(R.id.player_position_playerListView)
    ListView mPlayerListView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;

    private IParingAlgorithm mAlgorithm;
    private PlayerAdapter mAdapter;
    private CustomDialogFragment mCustomDialogFragment;
    private static final String TAG_DIALOG = PlayerPositionActivity.class
            .getName() + "TAG_DIALOG";

    // help library
    private TourGuide mTutorialHandler = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_position);
        ButterKnife.bind(this);

        mAlgorithm = AlgorithmFactory.getInstance();
        List<Player> playerList = mAlgorithm.getSortedPlayerList();
        mAdapter = new PlayerAdapter(this, playerList);
        View header = getLayoutInflater().inflate(R.layout.header_player_list,
                null);

        mPlayerListView.addHeaderView(header);
        mPlayerListView.setAdapter(mAdapter);

        mRoundTextView.setText(getString(R.string.text_after_game) + " "
                + mAlgorithm.getCurrentRound() + " "
                + getString(R.string.text_from) + " "
                + +mAlgorithm.getMaxRound());
        mNextGameButton.setOnClickListener(mOnButtonClick);


        setSupportActionBar(mToolBar);
        mToolBar.setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_name);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setTitle(R.string.app_name);
        // when there was last game change button name
        // show winner
        if (mAlgorithm.getCurrentRound() == mAlgorithm.getMaxRound()) {
            mNextGameButton.setText(getString(R.string.text_end_round));
            mWinnerTextView.setVisibility(View.VISIBLE);
            mWinnerTextView.setText(getString(R.string.text_winner) + " "
                    + playerList.get(0).getPlayerName());
        } else {
            mWinnerTextView.setVisibility(View.INVISIBLE);

        }
        mCustomDialogFragment = CustomDialogFragment.newInstance(
                getString(R.string.dialog_end_title),
                getString(R.string.dialog_end_message),
                getString(R.string.dialog_start_button), mOnDialogButtonClick);


        setListGuideActions((TextView) findViewById(R.id.header_playerPMWTextView), (TextView) findViewById(R.id.header_playerOMWTextView), (TextView) findViewById(R.id.header_playerPGWTextView), (TextView) findViewById(R.id.header_playerOGWTextView));

    }


    private OnClickListener mOnButtonClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.player_position_nextGameButton:
                    if (mAlgorithm.getCurrentRound() >= mAlgorithm.getMaxRound()) {
                        mCustomDialogFragment
                                .show(getSupportFragmentManager(), TAG_DIALOG);
                    } else {
                        Intent intent = null;
                        if (SettingsPreferencesFactory.getInstance().areManualParings()) {
                            intent = new Intent(PlayerPositionActivity.this,
                                    MatchPlayerActivity.class);
                        } else {
                            intent = new Intent(PlayerPositionActivity.this,
                                    ParingsActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private OnClickListener mOnDialogButtonClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mCustomDialogFragment != null) {
                mCustomDialogFragment.dismiss();
            }
            finish();
        }
    };


    private void setListGuideActions(TextView pmwTextView, TextView omwTextView, TextView pgwTextView, TextView ogwTextView) {


        // just adding some padding to look better
        /*int padding = MenuHelper.getHelperMenuPadding(getResources().getDisplayMetrics().density);
        pmwTextView.setPadding(padding,padding,padding, padding);
        omwTextView.setPadding(padding,padding,padding, padding);
        pgwTextView.setPadding(padding,padding,padding, padding);
        ogwTextView.setPadding(padding,padding,padding, padding);*/

        if (SettingsPreferencesFactory.getInstance().isFirstRun()) {
            SettingsPreferencesFactory.getInstance().setFirstRun(false);
            Sequence sequence = null;
            sequence = new Sequence.SequenceBuilder().add(bindTourGuideButton(getString(R.string.help_pmw), pmwTextView, Gravity.LEFT | Gravity.BOTTOM),
                    bindTourGuideButton(getString(R.string.help_omw), omwTextView, Gravity.LEFT | Gravity.BOTTOM),
                    bindTourGuideButton(getString(R.string.help_pgw), pgwTextView, Gravity.LEFT | Gravity.BOTTOM),
                    bindTourGuideButton(getString(R.string.help_ogw), ogwTextView, Gravity.LEFT | Gravity.BOTTOM)
            ).setDefaultOverlay(new Overlay().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTutorialHandler.next();
                }
            })).setContinueMethod(Sequence.ContinueMethod.OverlayListener).setDefaultPointer(new Pointer()).build();
            mTutorialHandler = TourGuide.init(this).playInSequence(sequence);
        }


    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
