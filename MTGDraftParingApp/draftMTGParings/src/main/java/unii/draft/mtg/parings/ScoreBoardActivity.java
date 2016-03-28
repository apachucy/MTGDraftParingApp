package unii.draft.mtg.parings;

import android.content.Intent;
import android.os.Bundle;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.TourGuide;
import unii.draft.mtg.parings.algorithm.AlgorithmFactory;
import unii.draft.mtg.parings.algorithm.IParingAlgorithm;
import unii.draft.mtg.parings.config.BaseConfig;
import unii.draft.mtg.parings.config.BundleConst;
import unii.draft.mtg.parings.database.model.IDatabaseHelper;
import unii.draft.mtg.parings.helper.ConverterToDataBase;
import unii.draft.mtg.parings.helper.MenuHelper;
import unii.draft.mtg.parings.pojo.ItemHeader;
import unii.draft.mtg.parings.pojo.Player;
import unii.draft.mtg.parings.sharedprefrences.SettingsPreferencesFactory;
import unii.draft.mtg.parings.view.adapters.IAdapterItem;
import unii.draft.mtg.parings.view.adapters.PlayerScoreboardAdapter;

public class ScoreBoardActivity extends BaseActivity {
    //TODO: Change color of dropped players
    @Bind(R.id.player_position_roundTextView)
    TextView mRoundTextView;
    @Bind(R.id.player_position_winnerTextView)
    TextView mWinnerTextView;

    @OnClick(R.id.paring_nextRound)
    void onNextGameButtonClicked(View view) {
        if (mAlgorithm.getCurrentRound() >= mAlgorithm.getMaxRound()) {
            showInfoDialog(getString(R.string.dialog_end_title),
                    getString(R.string.dialog_end_message),
                    getString(R.string.dialog_start_button), mOnDialogButtonClick);
        } else {

            Intent intent = null;
            if (SettingsPreferencesFactory.getInstance().areManualParings()) {
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


    @Bind(R.id.player_position_playerListView)
    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Bind(R.id.toolbar)
    Toolbar mToolBar;

    private IParingAlgorithm mAlgorithm;

    // help library
    private TourGuide mTutorialHandler = null;

    List<Player> mPlayerList;
    List<IAdapterItem> mPlayerScoreBoardList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        ButterKnife.bind(this);

        mAlgorithm = AlgorithmFactory.getInstance();
        mPlayerList = mAlgorithm.getSortedPlayerList();
        mPlayerScoreBoardList = new ArrayList<>();
        mPlayerScoreBoardList.add(new ItemHeader());
        mPlayerScoreBoardList.addAll(mPlayerList);
        mAdapter = new PlayerScoreboardAdapter(this, mPlayerScoreBoardList);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRoundTextView.setText(getString(R.string.text_after_game) + " "
                + mAlgorithm.getCurrentRound() + " "
                + getString(R.string.text_from) + " "
                + +mAlgorithm.getMaxRound());


        setSupportActionBar(mToolBar);
        mToolBar.setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_name);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setTitle(R.string.app_name);
        // when there was last game change button name
        // show winner
        if (mAlgorithm.getCurrentRound() == mAlgorithm.getMaxRound()) {
            mWinnerTextView.setVisibility(View.VISIBLE);
            mWinnerTextView.setText(getString(R.string.text_winner) + " "
                    + mPlayerList.get(0).getPlayerName());
        } else {
            mWinnerTextView.setVisibility(View.INVISIBLE);

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        setListGuideActions((TextView) findViewById(R.id.header_playerPMWTextView), (TextView) findViewById(R.id.header_playerOMWTextView), (TextView) findViewById(R.id.header_playerPGWTextView), (TextView) findViewById(R.id.header_playerOGWTextView), (ImageView) menu.getItem(0).getActionView(), (ImageView) menu.getItem(1).getActionView());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private MaterialDialog.SingleButtonCallback mOnDialogButtonClick = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(MaterialDialog dialog, DialogAction which) {
            dialog.dismiss();
            finish();
        }
    };


    private void setListGuideActions(TextView pmwTextView, TextView omwTextView, TextView pgwTextView, TextView ogwTextView, ImageView saveButton, ImageView dropPlayerButton) {
        // just adding some padding to look better
        int padding = MenuHelper.getHelperMenuPadding(getResources().getDisplayMetrics().density);

        saveButton.setPadding(padding, padding, padding, padding);
        dropPlayerButton.setPadding(padding, padding, padding, padding);
        // set an image
        saveButton.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_save));
        dropPlayerButton.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_person_minus));
        if (SettingsPreferencesFactory.getInstance().isFirstRun()) {
            SettingsPreferencesFactory.getInstance().setFirstRun(false);
            Sequence sequence = new Sequence.SequenceBuilder().add(bindTourGuideButton(getString(R.string.help_pmw), pmwTextView, Gravity.LEFT | Gravity.BOTTOM),
                    bindTourGuideButton(getString(R.string.help_omw), omwTextView, Gravity.LEFT | Gravity.BOTTOM),
                    bindTourGuideButton(getString(R.string.help_pgw), pgwTextView, Gravity.LEFT | Gravity.BOTTOM),
                    bindTourGuideButton(getString(R.string.help_ogw), ogwTextView, Gravity.LEFT | Gravity.BOTTOM),
                    bindTourGuideButton(getString(R.string.help_save_dashboard), saveButton),
                    bindTourGuideButton(getString(R.string.help_drop_player), dropPlayerButton)
            ).setDefaultOverlay(new Overlay().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTutorialHandler.next();
                }
            })).setContinueMethod(Sequence.ContinueMethod.OverlayListener).setDefaultPointer(new Pointer()).build();
            mTutorialHandler = TourGuide.init(this).playInSequence(sequence);
        }
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAlgorithm.getCurrentRound() >= mAlgorithm.getMaxRound()) {
                    Intent intent = null;
                    intent = new Intent(ScoreBoardActivity.this, SaveScoreBoardActivity.class);
                    startActivityForResult(intent, BaseConfig.DRAFT_NAME_SET);
                } else {
                    Toast.makeText(ScoreBoardActivity.this, getString(R.string.warning_save_dashboard), Toast.LENGTH_LONG).show();
                }
            }
        });


        dropPlayerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlgorithm.getCurrentRound() >= mAlgorithm.getMaxRound()) {
                    Toast.makeText(ScoreBoardActivity.this, getString(R.string.warning_drop_player), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = null;
                    intent = new Intent(ScoreBoardActivity.this, DropPlayerActivity.class);
                    startActivityForResult(intent, BaseConfig.DRAFT_PLAYERS_DROPPED);

                }
            }
        });
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
                    String savedGameName = data.getExtras().getString(BundleConst.BUNDLE_KEY_SAVED_GAME_NAME);

                    SimpleDateFormat sdf = new SimpleDateFormat(BaseConfig.DATE_PATTERN);
                    String currentDateandTime = sdf.format(new Date());
                    ConverterToDataBase.saveToDd(((IDatabaseHelper) getApplication()).getDaoSession(), mPlayerList, savedGameName, currentDateandTime);
                    Toast.makeText(ScoreBoardActivity.this, getString(R.string.message_score_board_saved), Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == BaseConfig.DRAFT_PLAYERS_DROPPED) {

                mAdapter.notifyDataSetChanged();
            }
        }

    }
}
