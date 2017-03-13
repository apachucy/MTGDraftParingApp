package unii.draft.mtg.parings.view.activities.options;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unii.draft.mtg.parings.BaseActivity;
import unii.draft.mtg.parings.ParingDashboardActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.base.BaseAlgorithm;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.view.adapters.MatchPlayerCustomAdapter;
import unii.draft.mtg.parings.view.custom.SingleChoiceListCallback;


public class ManualPlayerPairingActivity extends BaseActivity {

    private List<String> mPlayerNameList;
    private List<Game> mGameList;
    private RecyclerView.Adapter mRecyclerMatchPlayerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SingleChoiceListCallback mSelectPlayer1Action;
    private SingleChoiceListCallback mSelectPlayer2Action;

    @Bind(R.id.matchPlayer_firstPlayerButton)
    Button mPlayer1TextView;
    @Bind(R.id.matchPlayer_secondPlayerButton)
    Button mPlayer2TextView;
    @Bind(R.id.matchPlayer_PlayersList)
    RecyclerView mRecyclerMatchPlayerView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;

    @Bind(R.id.floating_action_button_next)
    FloatingActionButton mFloatingActionButton;

    @Inject
    AlgorithmChooser mAlgorithmChooser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_player_pairing);
        ButterKnife.bind(this);

        initToolBar();
        initData();
        initView();
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
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setTitle(R.string.app_name);
    }

    @Override
    protected void initView() {
        mSelectPlayer1Action = new SingleChoiceListCallback(this, mPlayer1TextView);
        mSelectPlayer2Action = new SingleChoiceListCallback(this, mPlayer2TextView);

        mRecyclerMatchPlayerAdapter = new MatchPlayerCustomAdapter(this, mGameList);
        mRecyclerMatchPlayerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerMatchPlayerView.setLayoutManager(mLayoutManager);
        mRecyclerMatchPlayerView.setAdapter(mRecyclerMatchPlayerAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @OnClick(R.id.matchPlayer_firstPlayerButton)
    void onFirstPlayerClicked() {
        showSingleChoiceList(this, getString(R.string.select_player_manual_title), mPlayerNameList, getString(R.string.select_player_manual_add), mSelectPlayer1Action);
    }

    @OnClick(R.id.matchPlayer_secondPlayerButton)
    void onSecondPlayerClicked() {
        showSingleChoiceList(this, getString(R.string.select_player_manual_title), mPlayerNameList, getString(R.string.select_player_manual_add), mSelectPlayer2Action);
    }

    @OnClick(R.id.matchPlayer_cleanButton)
    void onCleanButtonClicked() {
        mGameList.clear();
        mRecyclerMatchPlayerAdapter.notifyDataSetChanged();
        mPlayerNameList.clear();
        List<Player> playerList = mAlgorithmChooser.getCurrentAlgorithm().getSortedPlayerList();
        mPlayerNameList.addAll(getPlayerNameList(playerList));

        mRecyclerMatchPlayerAdapter.notifyDataSetChanged();
        mSelectPlayer1Action.cleanName();
        mSelectPlayer2Action.cleanName();
    }


    @OnClick(R.id.matchPlayer_ParingButton)
    void onParingButtonClicked() {
        if (canAddPlayers()) {
            mGameList.add(new Game(mSelectPlayer1Action.getCurrentName(),
                    mSelectPlayer2Action.getCurrentName()));
            mPlayerNameList.remove(mSelectPlayer1Action.getCurrentName());
            mPlayerNameList.remove(mSelectPlayer2Action.getCurrentName());
            if (mPlayerNameList.isEmpty()) {
                mPlayerNameList.add(getString(R.string.spinner_empty_player_list));
            }
            mRecyclerMatchPlayerAdapter.notifyDataSetChanged();
            mSelectPlayer1Action.cleanName();
            mSelectPlayer2Action.cleanName();

        } else if (mPlayerNameList == null || mPlayerNameList.isEmpty() ||
                mPlayerNameList.get(0).equals(getString(R.string.spinner_empty_player_list))) {
            Toast.makeText(ManualPlayerPairingActivity.this, getString(R.string.activity_paring_warning_empty_list), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ManualPlayerPairingActivity.this, getString(R.string.activity_paring_warning_equal_names), Toast.LENGTH_LONG).show();

        }
    }

    private void initData() {
        if (mAlgorithmChooser.getCurrentAlgorithm() instanceof BaseAlgorithm) {
            BaseAlgorithm baseAlgorithm = (BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm();
            baseAlgorithm.isLoadCachedDraftWasNeeded();
        }
        List<Player> playerList = mAlgorithmChooser.getCurrentAlgorithm().getSortedPlayerList();
        mPlayerNameList = new ArrayList<>();
        mPlayerNameList.addAll(getPlayerNameList(playerList));
        mGameList = new ArrayList<>();
    }


    private boolean canAddPlayers() {
        boolean isPlayer1NameSelected = !(mSelectPlayer1Action.getCurrentName() == null || mSelectPlayer1Action.getCurrentName().isEmpty() || mSelectPlayer1Action.getCurrentName().equals(getString(R.string.spinner_empty_player_list)));
        boolean isPlayer2NameSelected = !(mSelectPlayer2Action.getCurrentName() == null || mSelectPlayer2Action.getCurrentName().isEmpty() || mSelectPlayer2Action.getCurrentName().equals(getString(R.string.spinner_empty_player_list)));

        return isPlayer1NameSelected && isPlayer2NameSelected && !mSelectPlayer1Action.getCurrentName().equals(mSelectPlayer2Action.getCurrentName())
                && mPlayerNameList != null && !mPlayerNameList.isEmpty();
    }

    @OnClick(R.id.floating_action_button_next)
    void onStartGameButtonClicked() {

        if (!mPlayerNameList.isEmpty() && mPlayerNameList.size() > 1) {
            Toast.makeText(ManualPlayerPairingActivity.this, getString(R.string.activity_paring_warning_paring_not_finished), Toast.LENGTH_LONG).show();
        } else if (mPlayerNameList.size() > 1 && mAlgorithmChooser.getCurrentAlgorithm().getPlayer(mPlayerNameList.get(0)).hasBye()) {
            Toast.makeText(ManualPlayerPairingActivity.this, getString(R.string.activity_paring_warning_bye), Toast.LENGTH_LONG).show();
        } else {
            mAlgorithmChooser.getCurrentAlgorithm().setPlayerGameList(mGameList);
            //someone has a bye
            if (mPlayerNameList.size() == 1 && !mPlayerNameList.get(0).equals(getString(R.string.spinner_empty_player_list))) {
                mAlgorithmChooser.getCurrentAlgorithm().setPlayerWithBye(mAlgorithmChooser.getCurrentAlgorithm().getPlayer(mPlayerNameList.get(0)));
            }

            saveRound();
            Intent intent = new Intent(ManualPlayerPairingActivity.this, ParingDashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private List<String> getPlayerNameList(List<Player> playerList) {
        List<String> playerNameList = new ArrayList<>();
        for (Player player : playerList) {
            playerNameList.add(player.getPlayerName());
        }
        return playerNameList;
    }

    private void showSingleChoiceList(Context context, String title, List<String> list, String positiveText, MaterialDialog.ListCallbackSingleChoice singleListCallback) {
        new MaterialDialog.Builder(context)
                .title(title).items(list)
                .itemsCallbackSingleChoice(-1, singleListCallback).backgroundColorRes(R.color.windowBackground)
                .positiveText(positiveText)
                .show();
    }

    private void saveRound() { //save round in sharedPreferences
        if (mAlgorithmChooser.getCurrentAlgorithm() instanceof BaseAlgorithm) {
            BaseAlgorithm baseAlgorithm = (BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm();
            baseAlgorithm.cacheDraft();
        }
    }
}
