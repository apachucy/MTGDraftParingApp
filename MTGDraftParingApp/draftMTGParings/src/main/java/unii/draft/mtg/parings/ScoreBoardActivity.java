package unii.draft.mtg.parings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
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
import unii.draft.mtg.parings.logic.pojo.ItemHeader;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.util.config.BaseConfig;
import unii.draft.mtg.parings.util.config.BundleConst;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.util.helper.TourGuideMenuHelper;
import unii.draft.mtg.parings.view.activities.options.DropPlayerActivity;
import unii.draft.mtg.parings.view.activities.options.ManualPlayerPairingActivity;
import unii.draft.mtg.parings.view.activities.options.SaveScoreBoardActivity;
import unii.draft.mtg.parings.view.adapters.IAdapterItem;
import unii.draft.mtg.parings.view.adapters.PlayerScoreboardAdapter;

import static unii.draft.mtg.parings.util.config.BundleConst.BUNDLE_KEY_DRAFT_SAVED_NAME;


public class ScoreBoardActivity extends BaseActivity {


    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // help library
    private TourGuide mTutorialHandler = null;

    private List<Player> mPlayerList;
    private List<IAdapterItem> mPlayerScoreBoardList;
    private String mDraftName;

    @Bind(R.id.player_position_roundTextView)
    TextView mRoundTextView;
    @Bind(R.id.player_position_winnerTextView)
    TextView mWinnerTextView;

    @Bind(R.id.player_position_playerListView)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
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
                        ParingDashboardActivity.class);
            }
            startActivity(intent);
            finish();
        }
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
            mPlayerScoreBoardList.addAll(mPlayerList);

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
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mDraftName = savedInstanceState.getString(BUNDLE_KEY_DRAFT_SAVED_NAME);
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(BUNDLE_KEY_DRAFT_SAVED_NAME, mDraftName);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
          /*TODO: this is only temporary fix ?
        *Activity don't have a possibility to get points for header to display toll tips
        *  Add about and display all information about games
        *
        *
        */
        getMenuInflater().inflate(R.menu.dashboard, menu);
        setListGuideActions((TextView) findViewById(R.id.header_playerPMWTextView), (TextView) findViewById(R.id.header_playerOMWTextView),
                (TextView) findViewById(R.id.header_playerPGWTextView), (TextView) findViewById(R.id.header_playerOGWTextView),
                (ImageView) menu.getItem(0).getActionView(), (ImageView) menu.getItem(1).getActionView(),
                (ImageView) menu.getItem(2).getActionView());
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == BaseConfig.DRAFT_NAME_SET) {
                if (data.getExtras().containsKey(BundleConst.BUNDLE_KEY_SAVED_GAME_NAME)) {
                    mDraftName = data.getExtras().getString(BundleConst.BUNDLE_KEY_SAVED_GAME_NAME);
                }
            } else if (requestCode == BaseConfig.DRAFT_PLAYERS_DROPPED) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void injectDependencies(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initToolBar() {
        setSupportActionBar(mToolBar);
        mToolBar.setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_name);
        mToolBar.setTitleTextColor(getSingleColor(R.color.white));
        mToolBar.setTitle(R.string.app_name);
    }

    @Override
    protected void initView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    private MaterialDialog.SingleButtonCallback mOnDialogButtonClick = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
            if ((mDraftName != null && mSharedPreferenceManager.getSaveDraftResults() == 0) || mSharedPreferenceManager.getSaveDraftResults() == 1) {
                saveDraft(mDraftName);
            }
            BaseAlgorithm baseAlgorithm = (BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm();
            baseAlgorithm.clearCache();
            finish();
        }
    };

    private void saveDraft(String draftName) {
        String tempDraftName;
        SimpleDateFormat sdf = new SimpleDateFormat(BaseConfig.DATE_PATTERN);
        String currentDateAndTime = sdf.format(new Date());
        if (draftName == null || draftName.isEmpty()) {
            tempDraftName = currentDateAndTime;
        } else {
            tempDraftName = draftName;
        }
        mDatabaseHelper.get().saveDraft(mPlayerList, tempDraftName, currentDateAndTime, mAlgorithmChooser.getCurrentAlgorithm().getCurrentRound());
        Toast.makeText(ScoreBoardActivity.this, getString(R.string.message_score_board_saved), Toast.LENGTH_LONG).show();

    }

    private void setListGuideActions(TextView pmwTextView, TextView omwTextView, TextView pgwTextView, TextView ogwTextView,
                                     ImageView dropPlayerButton, ImageView saveDraft, ImageView shareContent) {
        // just adding some padding to look better
        int padding = TourGuideMenuHelper.getHelperMenuPadding(getResources().getDisplayMetrics().density);

        dropPlayerButton.setPadding(padding, padding, padding, padding);
        saveDraft.setPadding(padding, padding, padding, padding);
        shareContent.setPadding(padding, padding, padding, padding);
        // set an image
        dropPlayerButton.setImageDrawable(getSingleDrawable(R.drawable.ic_person_minus));
        saveDraft.setImageDrawable(getSingleDrawable(R.drawable.ic_save));
        shareContent.setImageDrawable(getSingleDrawable(R.drawable.ic_share));
        if (mSharedPreferenceManager.showGuideTourOnScoreBoardScreen()) {
            Sequence sequence = new Sequence.SequenceBuilder().add(bindTourGuideButton(getString(R.string.help_pmw), pmwTextView, Gravity.START | Gravity.BOTTOM),
                    bindTourGuideButton(getString(R.string.help_omw), omwTextView, Gravity.START | Gravity.BOTTOM),
                    bindTourGuideButton(getString(R.string.help_pgw), pgwTextView, Gravity.START | Gravity.BOTTOM),
                    bindTourGuideButton(getString(R.string.help_ogw), ogwTextView, Gravity.START | Gravity.BOTTOM),

                    bindTourGuideButton(getString(R.string.help_drop_player), dropPlayerButton),
                    bindTourGuideButton(getString(R.string.help_save_dashboard), saveDraft),
                    bindTourGuideButton(getString(R.string.help_share_content), shareContent)
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
                    Toast.makeText(ScoreBoardActivity.this, getString(R.string.warning_drop_player), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(ScoreBoardActivity.this, DropPlayerActivity.class);
                    startActivityForResult(intent, BaseConfig.DRAFT_PLAYERS_DROPPED);
                }
            }
        });
        saveDraft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlgorithmChooser.getCurrentAlgorithm().getCurrentRound() >= mAlgorithmChooser.getCurrentAlgorithm().getMaxRound()) {
                    Intent intent = new Intent(ScoreBoardActivity.this, SaveScoreBoardActivity.class);
                    startActivityForResult(intent, BaseConfig.DRAFT_NAME_SET);
                } else {
                    Toast.makeText(ScoreBoardActivity.this, getString(R.string.warning_save_dashboard), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(ScoreBoardActivity.this, getString(R.string.warning_save_dashboard), Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            mDraftName = savedInstanceState.getString(BUNDLE_KEY_DRAFT_SAVED_NAME);
        }

        mPlayerScoreBoardList = new ArrayList<>();
    }

    private void displayErrorDialog() {
        showInfoDialog(getString(R.string.dialog_error_algorithm_title),
                getString(R.string.dialog_error_algorithm__message),
                getString(R.string.dialog_start_button), mDialogButtonClickListener);
    }

    private MaterialDialog.SingleButtonCallback mDialogButtonClickListener = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(MaterialDialog dialog, DialogAction which) {
            dialog.dismiss();
            finish();
        }
    };
}
