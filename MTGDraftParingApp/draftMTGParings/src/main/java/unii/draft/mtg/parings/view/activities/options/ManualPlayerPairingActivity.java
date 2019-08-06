package unii.draft.mtg.parings.view.activities.options;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unii.draft.mtg.parings.BaseActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.RoundActivity;
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
    @Nullable
    private SingleChoiceListCallback mSelectPlayer1Action;
    @Nullable
    private SingleChoiceListCallback mSelectPlayer2Action;

    @Nullable
    @BindView(R.id.matchPlayer_firstPlayerButton)
    Button mPlayer1TextView;
    @Nullable
    @BindView(R.id.matchPlayer_secondPlayerButton)
    Button mPlayer2TextView;
    @Nullable
    @BindView(R.id.matchPlayer_PlayersList)
    RecyclerView mRecyclerMatchPlayerView;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @Nullable
    @BindView(R.id.floating_action_button_next)
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
    protected void injectDependencies(@NonNull ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void onPause() {
        saveRound();
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
            mPlayerNameList.clear();
            List<Player> playerList = mAlgorithmChooser.getCurrentAlgorithm().getSortedPlayerList();
            mPlayerNameList.addAll(getPlayerNameList(playerList));
            mRecyclerMatchPlayerView.setAdapter(mRecyclerMatchPlayerAdapter);
        } catch (NullPointerException exception) {
            displayErrorDialog();
        } finally {
            //Nothing here
        }
    }

    @Override
    protected void initToolBar() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_header_path_game);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle(R.string.app_header_path_game);
    }

    @Override
    protected void initView() {
        mSelectPlayer1Action = new SingleChoiceListCallback(this, mPlayer1TextView);
        mSelectPlayer2Action = new SingleChoiceListCallback(this, mPlayer2TextView);

        mRecyclerMatchPlayerAdapter = new MatchPlayerCustomAdapter(this, mGameList);
        mRecyclerMatchPlayerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerMatchPlayerView.setLayoutManager(mLayoutManager);
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

    @OnClick(R.id.matchPlayer_cleanParingButton)
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


    @OnClick(R.id.matchPlayer_addParingButton)
    void onParingButtonClicked() {
        if (canAddPlayers()) {
            mGameList.add(new Game(mSelectPlayer1Action.getCurrentName(),
                    mSelectPlayer2Action.getCurrentName(), mAlgorithmChooser.getCurrentAlgorithm().getCurrentRound() + 1));
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
            FancyToast.makeText(ManualPlayerPairingActivity.this, getString(R.string.activity_paring_warning_empty_list), FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
        } else {
            FancyToast.makeText(ManualPlayerPairingActivity.this, getString(R.string.activity_paring_warning_manual_pairing_error), FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();

        }
    }

    private void initData() {
        mPlayerNameList = new ArrayList<>();
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
            FancyToast.makeText(ManualPlayerPairingActivity.this, getString(R.string.activity_paring_warning_paring_not_finished), FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
        } else if (mPlayerNameList.size() > 1 && mAlgorithmChooser.getCurrentAlgorithm().getPlayer(mPlayerNameList.get(0)).hasBye()) {
            FancyToast.makeText(ManualPlayerPairingActivity.this, getString(R.string.activity_paring_warning_bye), FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
        } else {
            mAlgorithmChooser.getCurrentAlgorithm().setPlayerGameList(mGameList);
            //someone has a bye
            if (mPlayerNameList.size() == 1 && !mPlayerNameList.get(0).equals(getString(R.string.spinner_empty_player_list))) {
                mAlgorithmChooser.getCurrentAlgorithm().setPlayerWithBye(mAlgorithmChooser.getCurrentAlgorithm().getPlayer(mPlayerNameList.get(0)));
            }

            Intent intent = new Intent(ManualPlayerPairingActivity.this, RoundActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @NonNull
    private List<String> getPlayerNameList(@NonNull List<Player> playerList) {
        List<String> playerNameList = new ArrayList<>();
        for (Player player : playerList) {
            playerNameList.add(player.getPlayerName());
        }
        return playerNameList;
    }


    private void saveRound() { //save round in sharedPreferences
        if (mAlgorithmChooser.getCurrentAlgorithm() instanceof BaseAlgorithm) {
            BaseAlgorithm baseAlgorithm = (BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm();
            baseAlgorithm.cacheDraft();
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
        public void onClick(MaterialDialog dialog, DialogAction which) {
            dialog.dismiss();
            finish();
        }
    };
}
