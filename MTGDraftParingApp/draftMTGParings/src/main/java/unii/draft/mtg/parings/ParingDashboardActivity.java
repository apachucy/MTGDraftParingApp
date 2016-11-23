package unii.draft.mtg.parings;

import android.content.Intent;
import android.os.Bundle;
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

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.TourGuide;
import unii.draft.mtg.parings.buisness.algorithm.BaseAlgorithm;
import unii.draft.mtg.parings.buisness.algorithm.IStatisticCalculation;
import unii.draft.mtg.parings.buisness.algorithm.StatisticCalculation;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.util.config.BaseConfig;
import unii.draft.mtg.parings.util.config.BundleConst;
import unii.draft.mtg.parings.util.helper.TourGuideMenuHelper;
import unii.draft.mtg.parings.view.adapters.PlayerMatchParingAdapter;
import unii.draft.mtg.parings.view.custom.CounterClass;
import unii.draft.mtg.parings.view.logic.ParingDashboardLogic;


public class ParingDashboardActivity extends BaseActivity {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Game> mGameList;
    // setting time and count down
    private CounterClass mCounterClass;
    private IStatisticCalculation mStatisticCalculation;
    private ParingDashboardLogic mParingDashboardLogic;
    private TourGuide mTutorialHandler = null;

    private static boolean isCountStarted;

    @Bind(R.id.paring_counterTextView)
    TextView mCounterTextView;
    @Bind(R.id.paring_roundTextView)
    TextView mRoundTextView;
    @Bind(R.id.paring_paringListView)
    RecyclerView mRecyclerView;


    @Bind(R.id.toolbar)
    Toolbar mToolBar;

    @Inject
    ISharedPreferences mSharedPreferenceManager;
    @Inject
    AlgorithmChooser mAlgorithmChooser;


    @OnClick(R.id.floating_action_button_next)
    void onEndRoundClicked() {
        //Validate Points
        if (!mParingDashboardLogic.validateDataSet(mGameList)) {
            showInfoDialog(getString(R.string.validation_dialog_title), getString(R.string.validation_dialog_body), getString(R.string.validation_dialog_button_name));
        } else {
            moveToScoreBoard();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paring_dashboard);
        ButterKnife.bind(this);
        initToolBar();
        if (!initData()) {
            displayErrorDialog();
            return;
        }
        initView();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.paring_dashboard, menu);
        setMenuActions((ImageView) menu.getItem(0).getActionView(), (ImageView) menu.getItem(1).getActionView());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    protected void injectDependencies(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    private void setMenuActions(ImageView hourGlassButton, ImageView openLifeAppButton) {
        // just adding some padding to look better
        int padding = TourGuideMenuHelper.getHelperMenuPadding(getResources().getDisplayMetrics().density);

        hourGlassButton.setPadding(padding, padding, padding, padding);
        openLifeAppButton.setPadding(padding, padding, padding, padding);
        // set an image
        hourGlassButton.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_hourglass));
        openLifeAppButton.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_life_small));


        if (mSharedPreferenceManager.showGuideTourOnParingScreen()) {
            Sequence sequence = new Sequence.SequenceBuilder().add(bindTourGuideButton(getString(R.string.tutorial_hour_glass), hourGlassButton),
                    bindTourGuideButton(getString(R.string.tutorial_life_app), openLifeAppButton))
                    .setDefaultOverlay(new Overlay().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mTutorialHandler.next();
                        }
                    })).setContinueMethod(Sequence.ContinueMethod.OverlayListener).
                            setDefaultPointer(new Pointer()).build();
            mSharedPreferenceManager.setGuideTourOnParingScreen(false);
            mTutorialHandler = TourGuide.init(this).playInSequence(sequence);
        }
        hourGlassButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mSharedPreferenceManager.displayCounterRound()) {
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

        openLifeAppButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putStringArray(BundleConst.BUNDLE_KEY_PLAYERS_NAMES, mParingDashboardLogic.getPlayerNameList(mGameList));
                bundle.putInt(BundleConst.BUNDLE_KEY_ROUND_TIME, (int) (mSharedPreferenceManager
                        .getTimePerRound() / BaseConfig.DEFAULT_TIME_MINUT));
                Intent sendIntent = new Intent(BaseConfig.INTENT_PACKAGE_LIFE_COUNTER_APP);
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.setType(BaseConfig.INTENT_SHARE_DATA_TYPE);
                sendIntent.putExtras(bundle);

                Intent chooser = Intent.createChooser(sendIntent, getString(R.string.chooser_intent_title_life_counter));

                // Verify that the intent will resolve to an activity
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    //open app

                    startActivity(chooser);
                } else {
                    //display information about possibility of downloading new app
                    showInfoDialog(getString(R.string.chooser_dialog_title_life_counter), getString(R.string.chooser_dialog_body_life_counter), getString(R.string.chooser_dialog_button_name), mDownloadMTGCounterAppListener);
                }
            }
        });
    }


    private boolean initData() {

        long timePerRound;
        long firstVibration;
        long secondVibration;
        long vibrationDuration;
        if (mSharedPreferenceManager.useVibration()) {
            firstVibration = mSharedPreferenceManager.getFirstVibration();
            secondVibration = mSharedPreferenceManager.getSecondVibration();
            vibrationDuration = mSharedPreferenceManager.getVibrationDuration();
        } else {
            firstVibration = 0;
            secondVibration = 0;
            vibrationDuration = 0;
        }
        if (mSharedPreferenceManager.displayCounterRound()) {
            timePerRound = mSharedPreferenceManager.getTimePerRound();
            mCounterClass = new CounterClass(this, timePerRound,
                    BaseConfig.DEFAULT_COUNTER_INTERVAL, mCounterTextView,
                    firstVibration, secondVibration, vibrationDuration);
        }
        isCountStarted = false;

        if (mAlgorithmChooser.getCurrentAlgorithm() instanceof BaseAlgorithm) {
            BaseAlgorithm baseAlgorithm = (BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm();
            if (baseAlgorithm.isLoadCachedDraftWasNeeded()) {
                mGameList = ((BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm()).getGameRoundList();
            } else {
                mGameList = mAlgorithmChooser.getCurrentAlgorithm().getParings();
                //save round in sharedPreferences
                baseAlgorithm.cacheDraft();


            }
        }
        if (mGameList == null || mGameList.isEmpty()) {
            return false;
        }
        mParingDashboardLogic = new ParingDashboardLogic(this);
        return true;
    }

    @Override
    protected void initToolBar() {
        setSupportActionBar(mToolBar);
        mToolBar.setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_name);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setTitle(R.string.app_name);
    }


    @Override
    protected void initView() {
        if (mSharedPreferenceManager.displayCounterRound()) {
            mCounterTextView.setVisibility(View.VISIBLE);
        } else {
            mCounterTextView.setVisibility(View.INVISIBLE);
        }

        mAdapter = new PlayerMatchParingAdapter(this, mGameList);
        mRoundTextView.setText(getString(R.string.text_round) + " "
                + mAlgorithmChooser.getCurrentAlgorithm().getCurrentRound());
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private MaterialDialog.SingleButtonCallback mDialogButtonClickListener = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(MaterialDialog dialog, DialogAction which) {
            dialog.dismiss();
            mCounterClass.cancel();
            finish();
        }
    };

    @Override
    protected void onPause() {

        super.onPause();

    }

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
        mParingDashboardLogic.addGameResult(mAlgorithmChooser.getCurrentAlgorithm(), mGameList);
        mStatisticCalculation = new StatisticCalculation(mAlgorithmChooser.getCurrentAlgorithm());
        mStatisticCalculation.calculateAll();
        Intent intent = new Intent(ParingDashboardActivity.this,
                ScoreBoardActivity.class);
        startActivity(intent);
        finish();
    }


}
