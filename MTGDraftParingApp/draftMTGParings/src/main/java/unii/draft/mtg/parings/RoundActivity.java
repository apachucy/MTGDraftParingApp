package unii.draft.mtg.parings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.TourGuide;
import unii.draft.mtg.parings.buisness.algorithm.base.BaseAlgorithm;
import unii.draft.mtg.parings.buisness.algorithm.base.IStatisticCalculation;
import unii.draft.mtg.parings.buisness.algorithm.base.StatisticCalculation;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.util.config.BaseConfig;
import unii.draft.mtg.parings.util.config.BundleConst;
import unii.draft.mtg.parings.util.helper.TourGuideMenuHelper;
import unii.draft.mtg.parings.view.CounterStates;
import unii.draft.mtg.parings.view.adapters.PlayerMatchParingAdapter;
import unii.draft.mtg.parings.view.custom.CounterClass;
import unii.draft.mtg.parings.view.logic.ParingDashboardLogic;

import static unii.draft.mtg.parings.util.config.BundleConst.BUNDLE_KEY_LOAD_PREVIOUS_DRAFT;
import static unii.draft.mtg.parings.util.config.BundleConst.BUNDLE_KEY_PAIRINGS_GENERATED;
import static unii.draft.mtg.parings.util.config.BundleConst.BUNDLE_KEY_PAIRINGS_TIMER_ON;
import static unii.draft.mtg.parings.util.config.BundleConst.BUNDLE_KEY_PAIRINGS_TIMER_TIME;


public class RoundActivity extends BaseActivity {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Game> mGameList;
    private List<String> mHourGlassActions;
    // setting time and count down
    @Nullable
    private CounterClass mCounterClass;
    private IStatisticCalculation mStatisticCalculation;
    private ParingDashboardLogic mParingDashboardLogic;
    @Nullable
    private TourGuide mTutorialHandler = null;

    private CounterStates counterState;
    private boolean isPairingsGenerated = false;
    private boolean isLoaded = false;
    private long mTimerTimeTillEnd = 0;
    @Nullable
    @BindView(R.id.paring_counterTextView)
    TextView mCounterTextView;
    @Nullable
    @BindView(R.id.paring_roundTextView)
    TextView mRoundTextView;
    @Nullable
    @BindView(R.id.paring_paringListView)
    RecyclerView mRecyclerView;
    @Nullable
    @BindView(R.id.toolbar)
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
        if (!initData(savedInstanceState)) {
            displayErrorDialog();
            return;
        }
        initView();

    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
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
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        isPairingsGenerated = savedInstanceState.getBoolean(BUNDLE_KEY_PAIRINGS_GENERATED);
        counterState = (CounterStates) savedInstanceState.getSerializable(BUNDLE_KEY_PAIRINGS_TIMER_ON);
        mTimerTimeTillEnd = savedInstanceState.getLong(BUNDLE_KEY_PAIRINGS_TIMER_TIME);
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(BUNDLE_KEY_PAIRINGS_GENERATED, isPairingsGenerated);
        outState.putSerializable(BUNDLE_KEY_PAIRINGS_TIMER_ON, counterState);
        outState.putLong(BUNDLE_KEY_PAIRINGS_TIMER_TIME, mTimerTimeTillEnd);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void injectDependencies(@NonNull ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    private void setMenuActions(@NonNull ImageView hourGlassButton, @NonNull ImageView openLifeAppButton) {
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
                    FancyToast.makeText(RoundActivity.this, getString(R.string.settings_counter_off), FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    return;
                }
                if (counterState == CounterStates.STOPPED) {
                    counterState = CounterStates.STARTED;
                    FancyToast.makeText(RoundActivity.this, getString(R.string.message_action_countdown_started), FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                    mCounterClass = newCounterClassInstance(mSharedPreferenceManager.getTimePerRound());
                    mCounterClass.start();
                    //TODO: fix issue reproduction steps: reset and continue state
                } else if (counterState == CounterStates.PAUSED) {
                    mCounterClass = newCounterClassInstance(mTimerTimeTillEnd);
                    mCounterClass.start();
                    counterState = CounterStates.STARTED;

                } else {
                    showSingleChoiceList(RoundActivity.this, getString(R.string.dialog_timer_title), mHourGlassActions, getString(R.string.positive), mHourGlassesListener);
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


    private boolean initData(@Nullable Bundle savedInstanceState) {
        long timePerRound;
        counterState = CounterStates.STOPPED;

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            isPairingsGenerated = savedInstanceState.getBoolean(BUNDLE_KEY_PAIRINGS_GENERATED);
            counterState = (CounterStates) savedInstanceState.getSerializable(BUNDLE_KEY_PAIRINGS_TIMER_ON);
        }
        if (counterState != CounterStates.STOPPED) {
            timePerRound = mTimerTimeTillEnd;
        } else {
            timePerRound = mSharedPreferenceManager.getTimePerRound();
        }
        if (mSharedPreferenceManager.displayCounterRound()) {
            mCounterClass = newCounterClassInstance(timePerRound);
            if (counterState == CounterStates.STARTED) {
                mCounterClass.start();
            }
        }
        mParingDashboardLogic = new ParingDashboardLogic(this, mSharedPreferenceManager.getPointsForGameWinning(), mSharedPreferenceManager.getPointsForGameDraws(),
                mSharedPreferenceManager.getPointsForMatchWinning(), mSharedPreferenceManager.getPointsForMatchDraws());
        mHourGlassActions = Arrays.asList(getString(R.string.dialog_timer_cancel), getString(R.string.dialog_timer_pause));
        return true;
    }

    @Override
    protected void initToolBar() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_header_path_game);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle(R.string.app_header_path_game);
    }


    @Override
    protected void initView() {
        if (mSharedPreferenceManager.displayCounterRound()) {
            mCounterTextView.setVisibility(View.VISIBLE);
        } else {
            mCounterTextView.setVisibility(View.INVISIBLE);
        }
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @NonNull
    private MaterialDialog.SingleButtonCallback mDialogButtonClickListener = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(MaterialDialog dialog, DialogAction which) {
            dialog.dismiss();
            mCounterClass.onCancel();
            counterState = CounterStates.STOPPED;
            finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        try {

            if (mAlgorithmChooser.getCurrentAlgorithm() instanceof BaseAlgorithm) {
                BaseAlgorithm baseAlgorithm = (BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm();
                if (isLoaded = getIntent().getBooleanExtra(BUNDLE_KEY_LOAD_PREVIOUS_DRAFT, false)) {
                    mGameList = baseAlgorithm.getLastPlayedGameList();

                } else if (baseAlgorithm.isLoadCachedDraftWasNeeded() || isPairingsGenerated) {
                    mGameList = ((BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm()).getGameRoundList();
                } else {
                    mGameList = mAlgorithmChooser.getCurrentAlgorithm().getParings(mSharedPreferenceManager.getGeneratedSittingMode());
                    isPairingsGenerated = true;
                    //save round in sharedPreferences
                    baseAlgorithm.cacheDraft();
                }
            }
            if (mGameList == null || mGameList.isEmpty()) {
                throw new NullPointerException("Game List should be populated");
            }

            mAdapter = new PlayerMatchParingAdapter(this, mGameList);
            mRoundTextView.setText(getString(R.string.text_round) + " "
                    + mAlgorithmChooser.getCurrentAlgorithm().getCurrentRound());
            mRecyclerView.setAdapter(mAdapter);
        } catch (NullPointerException exception) {
            displayErrorDialog();
        } finally {
            //nothing here
        }
    }

    @Override
    protected void onPause() {
        if (mCounterClass != null) {
            mTimerTimeTillEnd = mCounterClass.onPause();
        }
        BaseAlgorithm baseAlgorithm = (BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm();
        baseAlgorithm.cacheDraft();
        super.onPause();
    }

    @NonNull
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
        //TODO: This is not optimal - think about update!!
        if (isLoaded) {
            mParingDashboardLogic.removeLastGameResult(mAlgorithmChooser.getCurrentAlgorithm());
        }
        mParingDashboardLogic.addGameResult(mAlgorithmChooser.getCurrentAlgorithm(), mGameList);
        mStatisticCalculation = new StatisticCalculation(mAlgorithmChooser.getCurrentAlgorithm());
        mStatisticCalculation.calculateAll();
        mAlgorithmChooser.getCurrentAlgorithm().setPlayedRound(mAlgorithmChooser.getCurrentAlgorithm().getCurrentRound());
        isPairingsGenerated = false;
        Intent intent = new Intent(RoundActivity.this,
                ScoreBoardActivity.class);
        startActivity(intent);
        counterState = CounterStates.STOPPED;
        if (mCounterClass != null) {
            mCounterClass.onCancel();
        }
        finish();
    }

    @NonNull
    private MaterialDialog.ListCallbackSingleChoice mHourGlassesListener = new MaterialDialog.ListCallbackSingleChoice() {
        @Override
        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
            switch (which) {
                case 0://reset action
                    mCounterClass.onCancel();
                    counterState = CounterStates.STOPPED;
                    FancyToast.makeText(RoundActivity.this, getString(R.string.message_action_countdown_cancel), FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                    mTimerTimeTillEnd = mSharedPreferenceManager.getTimePerRound();
                    break;

                case 1://unpause action
                    counterState = CounterStates.PAUSED;
                    mTimerTimeTillEnd = mCounterClass.onPause();
                    mCounterClass.onCancel();
                    //TODO: add toast
                    break;
            }
            return false;
        }
    };

    private CounterClass newCounterClassInstance(long startTime) {
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

        CounterClass counterClass = new CounterClass(RoundActivity.this, startTime,
                BaseConfig.DEFAULT_COUNTER_INTERVAL, mCounterTextView,
                firstVibration, secondVibration, vibrationDuration);
        return counterClass;
    }
}
