package unii.draft.mtg.parings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unii.draft.mtg.parings.algorithm.AlgorithmFactory;
import unii.draft.mtg.parings.algorithm.IParingAlgorithm;
import unii.draft.mtg.parings.algorithm.IStatisticCalculation;
import unii.draft.mtg.parings.algorithm.StatisticCalculation;
import unii.draft.mtg.parings.config.BaseConfig;
import unii.draft.mtg.parings.config.BundleConst;
import unii.draft.mtg.parings.helper.MenuHelper;
import unii.draft.mtg.parings.pojo.Game;
import unii.draft.mtg.parings.pojo.Player;
import unii.draft.mtg.parings.sharedprefrences.SettingsPreferencesFactory;
import unii.draft.mtg.parings.view.CounterClass;
import unii.draft.mtg.parings.view.adapters.PlayerMatchParingAdapter;
import unii.draft.mtg.parings.view.fragments.CustomDialogFragment;

public class ParingDashboardActivity extends BaseActivity {

    @Bind(R.id.paring_counterTextView)
    TextView mCounterTextView;
    @Bind(R.id.paring_roundTextView)
    TextView mRoundTextView;
    @Bind(R.id.paring_paringListView)
    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private CustomDialogFragment mDownloadLifeCounterApp;
    private final static String TAG_DIALOG_DOWNLOAD_LIFE_COUNTER_APP = "TAG_DIALOG_DOWNLOAD_LIFE_COUNTER_APP";
    @Bind(R.id.paring_nextRound)
    FloatingActionButton mFloatingActionButton;

    @OnClick(R.id.paring_openCounterApplication)
    void onOpenCounterClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(BundleConst.BUNDLE_KEY_PLAYERS_NAMES, getPlayerNameList());
        bundle.putInt(BundleConst.BUNDLE_KEY_ROUND_TIME, (int) (SettingsPreferencesFactory.getInstance()
                .getTimePerRound() / BaseConfig.DEFAULT_TIME_MINUT));
        Intent sendIntent = new Intent(BaseConfig.INTENT_PACKAGE_LIFE_COUNTER_APP);
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.setType("text/plain");
        sendIntent.putExtras(bundle);

        Intent chooser = Intent.createChooser(sendIntent, getString(R.string.chooser_intent_title));

        // Verify that the intent will resolve to an activity
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            //open app
            startActivity(chooser);
        } else {
            //display information about possibility of downloading new app
            mDownloadLifeCounterApp = CustomDialogFragment.newInstance(getString(R.string.chooser_dialog_title), getString(R.string.chooser_dialog_body), getString(R.string.chooser_dialog_button_name));
            mDownloadLifeCounterApp.show(getSupportFragmentManager(), TAG_DIALOG_DOWNLOAD_LIFE_COUNTER_APP, mDownloadMTGCounterAppListener);
        }
    }

    @OnClick(R.id.paring_nextRound)
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
            mCounterClass = new CounterClass(this, mTimePerRound,
                    BaseConfig.DEFAULT_COUNTER_INTERVAL, mCounterTextView,
                    mFirstVibration, mSecondVibration, mVibrationDuration);
        } else {
            mTimePerRound = 0;
            mCounterTextView.setVisibility(View.INVISIBLE);
        }

        isCountStarted = false;

        mParingAlgorithm = AlgorithmFactory.getInstance();
        if (mParingAlgorithm == null) {
            displayErrorDialog();
        }
        mGameList = mParingAlgorithm.getParings();
        if (mGameList == null || mGameList.isEmpty()) {
            displayErrorDialog();
        }
        mAdapter = new PlayerMatchParingAdapter(this, mGameList);
        mRoundTextView.setText(getString(R.string.text_round) + " "
                + mParingAlgorithm.getCurrentRound());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.paring_dashboard, menu);
        setMenuActions((ImageView) menu.getItem(0).getActionView());
        return true;
    }

    private void displayErrorDialog() {
        mCustomDialogFragment = CustomDialogFragment.newInstance(
                getString(R.string.dialog_error_algorithm_title),
                getString(R.string.dialog_error_algorithm__message),
                getString(R.string.dialog_start_button),
                mDialogButtonClickListener);
        mCustomDialogFragment.show(getSupportFragmentManager(), TAG_DIALOG);
    }

    private void setMenuActions(ImageView hourGlassButton) {
        // just adding some padding to look better
        int padding = MenuHelper.getHelperMenuPadding(getResources().getDisplayMetrics().density);

        hourGlassButton.setPadding(padding, padding, padding, padding);

        // set an image
        hourGlassButton.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_hourglass));


        hourGlassButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCountStarted) {
                    isCountStarted = true;
                    mCounterClass.start();
                    Toast.makeText(ParingDashboardActivity.this, getString(R.string.message_action_countdown_started), Toast.LENGTH_SHORT).show();
                } else {
                    isCountStarted = false;
                    mCounterClass.cancel();
                    Toast.makeText(ParingDashboardActivity.this, getString(R.string.message_action_countdown_cancel), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
        //Remove dummy decorator, unused element
        if (mGameList.get(mGameList.size() - 1).getPlayerNameA().equals(getString(R.string.dummy_player)) && mGameList.get(mGameList.size() - 1).getPlayerNameB().equals(getString(R.string.dummy_player))) {
            mGameList.remove(mGameList.size() - 1);
        }
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


    private String[] getPlayerNameList() {
        List<String> playerNameList = new ArrayList<>();
        for (Game game : mGameList) {
            //do not add dummy players!
            if (!game.getPlayerNameA().equals(getString(R.string.dummy_player)) && !game.getPlayerNameB().equals(getString(R.string.dummy_player))) {
                playerNameList.add(game.getPlayerNameA());
                playerNameList.add(game.getPlayerNameB());
            }
        }
        String[] playerArray = new String[playerNameList.size()];
        playerArray = playerNameList.toArray(playerArray);
        return playerArray;
    }

    private OnClickListener mDownloadMTGCounterAppListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            openGooglePlayMTGCounterApp();
        }
    };

    private void openGooglePlayMTGCounterApp() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BaseConfig.INTENT_OPEN_GOOGLE_PLAY + BaseConfig.INTENT_PACKAGE_LIFE_COUNTER_APP_UNII)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BaseConfig.INTENT_OPEN_GOOGLE_PLAY_WWW + BaseConfig.INTENT_PACKAGE_LIFE_COUNTER_APP_UNII)));
        }
    }
}
