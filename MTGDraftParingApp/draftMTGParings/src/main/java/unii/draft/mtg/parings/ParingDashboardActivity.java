package unii.draft.mtg.parings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unii.draft.mtg.parings.algorithm.AlgorithmFactory;
import unii.draft.mtg.parings.algorithm.IParingAlgorithm;
import unii.draft.mtg.parings.algorithm.IStatisticCalculation;
import unii.draft.mtg.parings.algorithm.StatisticCalculation;
import unii.draft.mtg.parings.config.BaseConfig;
import unii.draft.mtg.parings.pojo.Game;
import unii.draft.mtg.parings.pojo.Player;
import unii.draft.mtg.parings.sharedprefrences.SettingsPreferencesFactory;
import unii.draft.mtg.parings.view.CounterClass;
import unii.draft.mtg.parings.view.fragments.CustomDialogFragment;
import unii.draft.mtg.parings.view.adapters.ParingAdapter;

public class ParingDashboardActivity extends BaseActivity {

    @Bind(R.id.paring_counterTextView)
    TextView mCounterTextView;
    @Bind(R.id.paring_roundTextView)
    TextView mRoundTextView;
    @Bind(R.id.paring_paringListView)
    ListView mParingListView;

    @Bind(R.id.paring_startCounterButton)
    Button mStartButton;

    @OnClick(R.id.paring_startCounterButton)
    void onStartButtonClicked(View view) {
        if (!isCountStarted) {
            isCountStarted = true;
            mCounterClass.start();
        }
    }

    @Bind(R.id.paring_endRoundButon)
    Button mEndRoundButton;

    @OnClick(R.id.paring_endRoundButon)
    void onEndRoundClicked(View view) {
        updateGameResults();
        updatePlayerPoints();
        mStatisticCalculation = new StatisticCalculation(mParingAlgorithm);
        mStatisticCalculation.calculateAll();
        Intent intent = new Intent(ParingDashboardActivity.this,
                ScoreBoardActivity.class);
        startActivity(intent);
        finish();
    }

    @Bind(R.id.toolbar)
    Toolbar mToolBar;

    private ParingAdapter mParingAdapter;

    private List<Game> mGameList;
    // setting time and count down
    private CounterClass mCounterClass;

    private static boolean isCountStarted;
    private IParingAlgorithm mParingAlgorithm;

    private long mTimePerRound;
    private long mFirstVibration;
    private long mSecondVibration;
    private long mVibrationDuration;

    private CustomDialogFragment mCustomDialogFragment;
    private static final String TAG_DIALOG = MainActivity.class.getName()
            + "TAG_DIALOG";
    private IStatisticCalculation mStatisticCalculation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paring_dashboard);
        ButterKnife.bind(this);


        setSupportActionBar(mToolBar);
        mToolBar.setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_name);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setTitle(R.string.app_name);
        init();

    }

    private void init() {
        if (SettingsPreferencesFactory.getInstance().useVibration()) {
            mFirstVibration = SettingsPreferencesFactory.getInstance()
                    .getFirstVibration();
            mSecondVibration = SettingsPreferencesFactory.getInstance()
                    .getSecondVibration();
            mVibrationDuration = SettingsPreferencesFactory.getInstance()
                    .getVibrationDuration();
        } else {
            mFirstVibration = 0;
            mSecondVibration = 0;
            mVibrationDuration = 0;
        }
        if (SettingsPreferencesFactory.getInstance().displayCounterRound()) {
            mCounterTextView.setVisibility(View.VISIBLE);

            mTimePerRound = SettingsPreferencesFactory.getInstance()
                    .getTimePerRound();
            mStartButton.setVisibility(View.VISIBLE);
            mCounterClass = new CounterClass(this, mTimePerRound,
                    BaseConfig.DEFAULT_COUNTER_INTERVAL, mCounterTextView,
                    mFirstVibration, mSecondVibration, mVibrationDuration);
        } else {
            mTimePerRound = 0;
            mStartButton.setVisibility(View.GONE);
            mCounterTextView.setVisibility(View.INVISIBLE);
        }

        isCountStarted = false;

        mParingAlgorithm = AlgorithmFactory.getInstance();
        mGameList = mParingAlgorithm.getParings();
        if (mGameList == null || mGameList.isEmpty()) {
            mCustomDialogFragment = CustomDialogFragment.newInstance(
                    getString(R.string.dialog_error_algorithm_title),
                    getString(R.string.dialog_error_algorithm__message),
                    getString(R.string.dialog_start_button),
                    mDialogButtonClickListener);
            mCustomDialogFragment.show(getSupportFragmentManager(), TAG_DIALOG);
        }
        mParingAdapter = new ParingAdapter(this, mGameList);
        mRoundTextView.setText(getString(R.string.text_round) + " "
                + mParingAlgorithm.getCurrentRound());
        mParingListView.setAdapter(mParingAdapter);


    }


    private void updateGameResults() {
        for (Game g : mGameList) {
            if (g.getPlayerAPoints() > g.getPlayerBPoints()) {
                g.setWinner(g.getPlayerNameA());
            } else if (g.getPlayerAPoints() < g.getPlayerBPoints()) {
                g.setWinner(g.getPlayerNameB());
            } else {
                // it was a draw
                g.setWinner(BaseConfig.DRAW);

            }
        }
    }

    private void updatePlayerPoints() {
        List<Player> playerList = mParingAlgorithm.getSortedPlayerList();
        for (Player p : playerList) {

            // player has bye
            if (mParingAlgorithm.getPlayerWithBye() != null
                    && p.equals(mParingAlgorithm.getPlayerWithBye())) {
                // set maximum
                // points
                // for
                // player
                // with bye

                p.setMatchPoints(p.getMatchPoints() + BaseConfig.MATCH_WIN);

            }

            for (Game game : mGameList) {
                if (p.getPlayerName().equals(game.getPlayerNameA())
                        || p.getPlayerName().equals(game.getPlayerNameB())) {
                    p.getPlayedGame().add(game);
                    // draw
                    if (game.getWinner().equals(BaseConfig.DRAW)) {
                        p.setMatchPoints(p.getMatchPoints()
                                + BaseConfig.MATCH_DRAW);

                        // win match
                    } else if (game.getWinner().equals(p.getPlayerName())) {
                        p.setMatchPoints(p.getMatchPoints()
                                + BaseConfig.MATCH_WIN);

                    }
                    // add "small" points for a player
                    if (p.getPlayerName().equals(game.getPlayerNameA())) {
                        p.setGamePoints(p.getGamePoints()
                                + game.getPlayerAPoints() * 3);
                    } else {
                        p.setGamePoints(p.getGamePoints()
                                + game.getPlayerBPoints() * 3);
                    }
                    // There was a draw so each player gains 1 point
                    if (game.getDraw()) {
                        p.setGamePoints(p.getGamePoints() + 1);
                    }
                    break;

                }

            }

        }
        mParingAlgorithm.getSortedPlayerList();

    }

    private OnClickListener mDialogButtonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mCustomDialogFragment != null) {
                mCustomDialogFragment.dismiss();
                mCounterClass.cancel();
                finish();
            }

        }
    };

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
