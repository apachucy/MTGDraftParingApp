package unii.draft.mtg.parings;

import android.content.Intent;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

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
import unii.draft.mtg.parings.sharedprefrences.SettingsPreferencesFactory;
import unii.draft.mtg.parings.view.adapters.PlayerMatchParingAdapter;
import unii.draft.mtg.parings.view.custom.CounterClass;
import unii.draft.mtg.parings.view.logic.ParingDashboardLogic;

public class ParingDashboardActivity extends BaseActivity {


    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private List<Game> mGameList;
    // setting time and count down
    private CounterClass mCounterClass;

    private static boolean isCountStarted;
    private IParingAlgorithm mParingAlgorithm;


    private IStatisticCalculation mStatisticCalculation;
    private ParingDashboardLogic mParingDashboardLogic;

    @Bind(R.id.paring_counterTextView)
    TextView mCounterTextView;
    @Bind(R.id.paring_roundTextView)
    TextView mRoundTextView;
    @Bind(R.id.paring_paringListView)
    RecyclerView mRecyclerView;

    @Bind(R.id.paring_nextRound)
    FloatingActionButton mFloatingActionButton;

    @OnClick(R.id.paring_openCounterApplication)
    void onOpenCounterClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(BundleConst.BUNDLE_KEY_PLAYERS_NAMES, mParingDashboardLogic.getPlayerNameList(mGameList));
        bundle.putInt(BundleConst.BUNDLE_KEY_ROUND_TIME, (int) (SettingsPreferencesFactory.getInstance()
                .getTimePerRound() / BaseConfig.DEFAULT_TIME_MINUTE));
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
            showInfoDialog(getString(R.string.chooser_dialog_title), getString(R.string.chooser_dialog_body), getString(R.string.chooser_dialog_button_name), mDownloadMTGCounterAppListener);
        }
    }

    @OnClick(R.id.paring_nextRound)
    void onEndRoundClicked(View view) {
        //Validate Points
        if (!mParingDashboardLogic.validateDataSet(mGameList)) {
            showInfoDialog(getString(R.string.validation_dialog_title), getString(R.string.validation_dialog_body), getString(R.string.validation_dialog_button_name));
        } else {
            moveToScoreBoard();
        }

    }

    @Bind(R.id.toolbar)
    Toolbar mToolBar;


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.paring_dashboard, menu);
        setMenuActions((ImageView) menu.getItem(0).getActionView());
        return true;
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
                if (!SettingsPreferencesFactory.getInstance().displayCounterRound()) {
                    Toast.makeText(ParingDashboardActivity.this, getString(R.string.settings_counter_off), Toast.LENGTH_SHORT).show();
                    return;
                }
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


    private void init() {
        long timePerRound;
        long firstVibration;
        long secondVibration;
        long vibrationDuration;
        if (SettingsPreferencesFactory.getInstance().useVibration()) {
            firstVibration = SettingsPreferencesFactory.getInstance()
                    .getFirstVibration();
            secondVibration = SettingsPreferencesFactory.getInstance()
                    .getSecondVibration();
            vibrationDuration = SettingsPreferencesFactory.getInstance()
                    .getVibrationDuration();
        } else {
            firstVibration = 0;
            secondVibration = 0;
            vibrationDuration = 0;
        }
        if (SettingsPreferencesFactory.getInstance().displayCounterRound()) {
            mCounterTextView.setVisibility(View.VISIBLE);

            timePerRound = SettingsPreferencesFactory.getInstance()
                    .getTimePerRound();
            mCounterClass = new CounterClass(this, timePerRound,
                    BaseConfig.DEFAULT_COUNTER_INTERVAL, mCounterTextView,
                    firstVibration, secondVibration, vibrationDuration);
        } else {
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
        mParingDashboardLogic = new ParingDashboardLogic(this);
        // mParingDashboardLogic.addDummyPlayer(mGameList);
        mAdapter = new PlayerMatchParingAdapter(this, mGameList);
        mRoundTextView.setText(getString(R.string.text_round) + " "
                + mParingAlgorithm.getCurrentRound());
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    private MaterialDialog.SingleButtonCallback mDialogButtonClickListener = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(MaterialDialog dialog, DialogAction which) {
            dialog.dismiss();
            mCounterClass.cancel();
            finish();
        }
    };


    private MaterialDialog.SingleButtonCallback mDownloadMTGCounterAppListener = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(MaterialDialog dialog, DialogAction which) {
            mParingDashboardLogic.openGooglePlayMTGCounterApp();
            dialog.dismiss();
        }
    };

    private void displayErrorDialog() {
        showInfoDialog(getString(R.string.dialog_error_algorithm_title),
                getString(R.string.dialog_error_algorithm__message),
                getString(R.string.dialog_start_button), mDialogButtonClickListener);

    }

    private void moveToScoreBoard() {
        mParingDashboardLogic.addGameResult(mParingAlgorithm, mGameList);
        mStatisticCalculation = new StatisticCalculation(mParingAlgorithm);
        mStatisticCalculation.calculateAll();
        Intent intent = new Intent(ParingDashboardActivity.this,
                ScoreBoardActivity.class);
        startActivity(intent);
        finish();
    }

}
