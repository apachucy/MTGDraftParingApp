package unii.draft.mtg.parings;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Lazy;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.TourGuide;
import unii.draft.mtg.parings.buisness.algorithm.base.BaseAlgorithm;
import unii.draft.mtg.parings.buisness.algorithm.base.PairingMode;
import unii.draft.mtg.parings.buisness.share.scoreboard.IShareData;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.logic.pojo.DraftDataProvider;
import unii.draft.mtg.parings.logic.pojo.ItemHeader;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.util.config.BaseConfig;
import unii.draft.mtg.parings.util.config.BundleConst;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.util.helper.PlayerNameWithPositionGenerator;
import unii.draft.mtg.parings.util.helper.TourGuideMenuHelper;
import unii.draft.mtg.parings.view.activities.DraftRoundHistoryActivity;
import unii.draft.mtg.parings.view.activities.options.AddPlayerActivity;
import unii.draft.mtg.parings.view.activities.options.DropPlayerActivity;
import unii.draft.mtg.parings.view.activities.options.ManualPlayerPairingActivity;
import unii.draft.mtg.parings.view.activities.options.SaveScoreBoardActivity;
import unii.draft.mtg.parings.view.adapters.IAdapterItem;
import unii.draft.mtg.parings.view.adapters.PlayerScoreboardAdapter;
import unii.draft.mtg.parings.view.widget.DraftWidgetProvider;
import unii.draft.mtg.parings.view.widget.WidgetViewModel;

import static unii.draft.mtg.parings.util.config.BundleConst.BUNDLE_KEY_DRAFT_SAVED_NAME;
import static unii.draft.mtg.parings.util.config.BundleConst.BUNDLE_KEY_LOAD_PREVIOUS_DRAFT;
import static unii.draft.mtg.parings.view.widget.DraftWidgetProvider.BUNDLE_EXTRA;


public class ScoreBoardActivity extends BaseActivity {


    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // help library
    @Nullable
    private TourGuide mTutorialHandler = null;

    private List<Player> mPlayerList;
    private List<IAdapterItem> mPlayerScoreBoardList;
    @Nullable
    private String mDraftName;

    @Nullable
    @BindView(R.id.player_position_roundTextView)
    TextView mRoundTextView;
    @Nullable
    @BindView(R.id.player_position_winnerTextView)
    TextView mWinnerTextView;

    @Nullable
    @BindView(R.id.player_position_playerListView)
    RecyclerView mRecyclerView;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolBar;


    @Inject
    AlgorithmChooser mAlgorithmChooser;
    @Inject
    ISharedPreferences mSharedPreferenceManager;
    @Inject
    Lazy<IDatabaseHelper> mDatabaseHelper;
    @Inject
    IShareData mShareData;


    @OnClick(R.id.paring_nextRound)
    void onNextGameButtonClicked() {
        if (mAlgorithmChooser.getCurrentAlgorithm().getCurrentRound() >= mAlgorithmChooser.getCurrentAlgorithm().getMaxRound()) {
            showInfoDialog(getString(R.string.dialog_end_title),
                    getString(R.string.dialog_end_message),
                    getString(R.string.dialog_start_button), mOnDialogButtonClick);
        } else {
            Intent intent;
            if (mSharedPreferenceManager.getPairingType() == PairingMode.PAIRING_MANUAL) {
                intent = new Intent(ScoreBoardActivity.this,
                        ManualPlayerPairingActivity.class);
            } else {
                intent = new Intent(ScoreBoardActivity.this,
                        RoundActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R.id.paring_previousRound)
    void onPreviousGameButtonClicked() {

        showInfoDialog(getString(R.string.dialog_warning_title),
                getString(R.string.dialog_warning_go_back_body),
                getString(R.string.positive),
                mOnEditRoundButtonClick);
    }

    @OnClick(R.id.paring_endGame)
    void onGameEndedButtonClicked() {
        showInfoDialog(getString(R.string.dialog_cancel_game_title), getString(R.string.dialog_cancel_game_body), getString(R.string.dialog_positive), getString(R.string.dialog_negative), mOnCancelTournamentClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scoreboard);
        ButterKnife.bind(this);

        initData(savedInstanceState);
        initToolBar();
        initView();
    }

    @Override
    protected void onPause() {
        if (mAlgorithmChooser.getCurrentAlgorithm() instanceof BaseAlgorithm) {
            BaseAlgorithm baseAlgorithm = (BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm();
            baseAlgorithm.cacheDraft();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (mAlgorithmChooser.getCurrentAlgorithm() instanceof BaseAlgorithm) {
                BaseAlgorithm baseAlgorithm = (BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm();
                baseAlgorithm.isLoadCachedDraftWasNeeded();
            }
            mPlayerList = mAlgorithmChooser.getCurrentAlgorithm().getSortedPlayerList();
            mPlayerScoreBoardList.clear();
            mPlayerScoreBoardList.add(new ItemHeader());


            mPlayerScoreBoardList.addAll(PlayerNameWithPositionGenerator.getListWithNames(mPlayerList));

            mAdapter = new PlayerScoreboardAdapter(this, mPlayerScoreBoardList);

            mRecyclerView.setAdapter(mAdapter);
            mRoundTextView.setText(getString(R.string.text_after_game) + " "
                    + mAlgorithmChooser.getCurrentAlgorithm().getCurrentRound() + " "
                    + getString(R.string.text_from) + " "
                    + +mAlgorithmChooser.getCurrentAlgorithm().getMaxRound());
            // when there was last game change button name
            // show winner
            if (mAlgorithmChooser.getCurrentAlgorithm().getCurrentRound() == mAlgorithmChooser.getCurrentAlgorithm().getMaxRound()) {
                mWinnerTextView.setVisibility(View.VISIBLE);
                mWinnerTextView.setText(getString(R.string.text_winner) + " "
                        + mPlayerList.get(0).getPlayerName());
            } else {
                mWinnerTextView.setVisibility(View.INVISIBLE);
            }
        } catch (NullPointerException exception) {
            displayErrorDialog();
        } finally {
            //nothing
        }
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        mDraftName = savedInstanceState.getString(BUNDLE_KEY_DRAFT_SAVED_NAME);
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(BUNDLE_KEY_DRAFT_SAVED_NAME, mDraftName);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.dashboard, menu);
        controlVisibility(menu);
        setListGuideActions(
                (ImageView) menu.getItem(0).getActionView(), (ImageView) menu.getItem(1).getActionView(),
                (ImageView) menu.getItem(2).getActionView(), (ImageView) menu.getItem(6).getActionView());
        return true;
    }

    private void controlVisibility(Menu menu) {
        ImageView dropPlayer = (ImageView) menu.getItem(0).getActionView();
        MenuItem menuAddPlayer = menu.getItem(4);
        MenuItem menuSave = menu.getItem(3);
        MenuItem menuChangeAlgorithm = menu.getItem(5);
        dropPlayer.setVisibility(!isLastRound() ? View.VISIBLE : View.INVISIBLE);
        menuAddPlayer.setVisible(!isLastRound() && !isGameInTournamentMode());
        menuChangeAlgorithm.setVisible(mSharedPreferenceManager.getPairingType() == PairingMode.PAIRING_MANUAL);
        menuSave.setVisible(isLastRound());
    }

    private boolean isLastRound() {
        return mAlgorithmChooser.getCurrentAlgorithm().getCurrentRound() >= mAlgorithmChooser.getCurrentAlgorithm().getMaxRound();
    }

    @Override
    protected void onDestroy() {
        updateWidget();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_save:
                if (isLastRound()) {
                    Intent intent = new Intent(ScoreBoardActivity.this, SaveScoreBoardActivity.class);
                    startActivityForResult(intent, BaseConfig.DRAFT_NAME_SET);
                } else {
                    FancyToast.makeText(ScoreBoardActivity.this, getString(R.string.warning_save_dashboard), FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
                }
                return true;
            case R.id.action_add_player:
                if (isLastRound()) {
                    FancyToast.makeText(ScoreBoardActivity.this, getString(R.string.warning_drop_player), FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
                } else if (isGameInTournamentMode()) {
                    FancyToast.makeText(ScoreBoardActivity.this, getString(R.string.warning_add_player), FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
                } else {
                    Intent intent = new Intent(ScoreBoardActivity.this, AddPlayerActivity.class);
                    startActivityForResult(intent, BaseConfig.DRAFT_PLAYERS_MODIFIED);
                }
                return true;
            case R.id.action_switch_algorithm:
                showInfoDialog(getString(R.string.dialog_title_change_algorithm), getString(R.string.dialog_body_change_algorithm), getString(R.string.positive), getString(R.string.negative), changeAlgorithmType);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    //onActivityResult is triggered faster than onResume
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == BaseConfig.DRAFT_NAME_SET) {
                if (data.getExtras().containsKey(BundleConst.BUNDLE_KEY_SAVED_GAME_NAME)) {
                    mDraftName = data.getExtras().getString(BundleConst.BUNDLE_KEY_SAVED_GAME_NAME);
                }
            } else if (requestCode == BaseConfig.DRAFT_PLAYERS_MODIFIED) {
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }

            }
        }
    }


    @Override
    protected void injectDependencies(@NonNull ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initToolBar() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_header_path_game);
        mToolBar.setTitleTextColor(getSingleColor(R.color.white));
        getSupportActionBar().setTitle(R.string.app_header_path_game);
    }

    @Override
    protected void initView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    @NonNull
    private MaterialDialog.SingleButtonCallback mOnDialogButtonClick = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            closeApplication(dialog);
        }
    };

    private void closeApplication(@NonNull MaterialDialog dialog) {
        dialog.dismiss();
        if (mSharedPreferenceManager.getSaveDraftResults() == 1) {
            saveDraft(mDraftName);
        }
        BaseAlgorithm baseAlgorithm = (BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm();
        baseAlgorithm.clearCache();
        finish();
    }

    @NonNull
    private MaterialDialog.SingleButtonCallback mOnCancelTournamentClick = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
            closeApplication(materialDialog);
        }
    };

    @NonNull
    private MaterialDialog.SingleButtonCallback mOnEditRoundButtonClick = new MaterialDialog.SingleButtonCallback() {

        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            Intent intent = new Intent(ScoreBoardActivity.this, RoundActivity.class);
            intent.putExtra(BUNDLE_KEY_LOAD_PREVIOUS_DRAFT, true);
            startActivity(intent);
            finish();
        }
    };


    private boolean isGameInTournamentMode() {
        return mSharedPreferenceManager.getPairingType() == PairingMode.PAIRING_TOURNAMENT || mSharedPreferenceManager.getPairingType() == PairingMode.PAIRING_ROUND_ROBIN;
    }

    private void saveDraft(@Nullable String draftName) {
        String tempDraftName;
        SimpleDateFormat sdf = new SimpleDateFormat(BaseConfig.DATE_PATTERN, Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        if (draftName == null || draftName.isEmpty()) {
            tempDraftName = currentDateAndTime;
        } else {
            tempDraftName = draftName;
        }
        mDatabaseHelper.get().saveDraft(mPlayerList, tempDraftName, currentDateAndTime, mAlgorithmChooser.getCurrentAlgorithm().getCurrentRound() + 1);
        FancyToast.makeText(ScoreBoardActivity.this, getString(R.string.message_score_board_saved), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

    }


    private void setListGuideActions(
            @NonNull ImageView dropPlayerButton, @NonNull ImageView information, @NonNull ImageView shareContent, @NonNull ImageView showRoundHistory) {
        // just adding some padding to look better
        int padding = TourGuideMenuHelper.getHelperMenuPadding(getResources().getDisplayMetrics().density);

        dropPlayerButton.setPadding(padding, padding, padding, padding);
        shareContent.setPadding(padding, padding, padding, padding);
        information.setPadding(padding, padding, padding, padding);
        showRoundHistory.setPadding(padding, padding, padding, padding);
        // set an image
        dropPlayerButton.setImageDrawable(getSingleDrawable(R.drawable.ic_person_minus));
        shareContent.setImageDrawable(getSingleDrawable(R.drawable.ic_share));
        information.setImageDrawable(getSingleDrawable(R.drawable.ic_info));
        showRoundHistory.setImageDrawable(getSingleDrawable(R.drawable.ic_access_time_white_24dp));
        if (mSharedPreferenceManager.showGuideTourOnScoreBoardScreen()) {
            Sequence sequence = new Sequence.SequenceBuilder().
                    add(
                            bindTourGuideButton(getString(R.string.help_drop_player), dropPlayerButton),
                            bindTourGuideButton(getString(R.string.help_share_content), shareContent),
                            bindTourGuideButton(getString(R.string.help_information_scoreboard), information),
                            bindTourGuideButton(getString(R.string.round_history), showRoundHistory)
                    ).setDefaultOverlay(new Overlay().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTutorialHandler.next();
                }
            })).setContinueMethod(Sequence.ContinueMethod.OverlayListener).setDefaultPointer(new Pointer()).build();
            mTutorialHandler = TourGuide.init(this).playInSequence(sequence);
            mSharedPreferenceManager.setGuideTourOnScoreBoardScreen(false);

        }

        dropPlayerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlgorithmChooser.getCurrentAlgorithm().getCurrentRound() >= mAlgorithmChooser.getCurrentAlgorithm().getMaxRound()) {
                    FancyToast.makeText(ScoreBoardActivity.this, getString(R.string.warning_drop_player), FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
                } else {
                    Intent intent = new Intent(ScoreBoardActivity.this, DropPlayerActivity.class);
                    startActivityForResult(intent, BaseConfig.DRAFT_PLAYERS_MODIFIED);
                }
            }
        });
        shareContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlgorithmChooser.getCurrentAlgorithm().getCurrentRound() >= mAlgorithmChooser.getCurrentAlgorithm().getMaxRound()) {

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, mShareData.getPlayerWithPoints(mAlgorithmChooser.getCurrentAlgorithm().getSortedPlayerList()));
                    sendIntent.setType(BaseConfig.INTENT_SHARE_DATA_TYPE);
                    startActivity(sendIntent);
                } else {
                    FancyToast.makeText(ScoreBoardActivity.this, getString(R.string.help_share_content), FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();

                }
            }
        });
        information.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog(getString(R.string.dialog_scoreboard_info_title), getString(R.string.dialog_scoreboard_info_body), getString(R.string.dialog_positive));
            }
        });

        showRoundHistory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreBoardActivity.this, DraftRoundHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            mDraftName = savedInstanceState.getString(BUNDLE_KEY_DRAFT_SAVED_NAME);
        }

        mPlayerScoreBoardList = new ArrayList<>();

        //TODO: Refactor  All component - onCreate/onResume for all App states
        if (mAlgorithmChooser.getCurrentAlgorithm() instanceof BaseAlgorithm) {
            BaseAlgorithm baseAlgorithm = (BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm();
            baseAlgorithm.isLoadCachedDraftWasNeeded();
        }
    }

    private void displayErrorDialog() {
        showInfoDialog(getString(R.string.dialog_error_algorithm_title),
                getString(R.string.dialog_error_algorithm__message),
                getString(R.string.dialog_start_button), mDialogButtonClickListener);
    }

    @NonNull
    private MaterialDialog.SingleButtonCallback mDialogButtonClickListener = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
            finish();
        }
    };

    private void updateWidget() {
        WidgetViewModel widgetViewModel = new WidgetViewModel();
        widgetViewModel.setCurrentRound(mAlgorithmChooser.getCurrentAlgorithm().getCurrentRound());
        widgetViewModel.setWinningPlayer(mPlayerList.get(0).getPlayerName());
        Intent intent = new Intent(this, DraftWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds((new ComponentName(getApplication(), DraftWidgetProvider.class)));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_EXTRA, widgetViewModel);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    @NonNull
    private MaterialDialog.SingleButtonCallback changeAlgorithmType = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            DraftDataProvider draftDataProvider = ((BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm()).getDraftDataProvider();
            mSharedPreferenceManager.setPairingType(PairingMode.PAIRING_AUTOMATIC_CAN_REPEAT_PAIRINGS);

            ((BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm()).setDraftDataProvider(draftDataProvider);

        }
    };
}
