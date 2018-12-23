package unii.draft.mtg.parings.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.BaseActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.base.IApplicationDraftMemoryState;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.logic.pojo.Round;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.util.converter.SparseArrayToArrayListConverter;
import unii.draft.mtg.parings.view.adapters.ExpandableListAdapter;

public class DraftRoundHistoryActivity extends BaseActivity {
    private ExpandableListAdapter mExpandableRoundAdapter;

    @Nullable
    @BindView(R.id.settings_roundExpandableListView)
    ExpandableListView mExpandableListView;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @Inject
    AlgorithmChooser algorithmChooser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_history);
        ButterKnife.bind(this);

        initData(savedInstanceState);
        initToolBar();
        initView();
    }

    private void initData(Bundle savedInstanceState) {
        IApplicationDraftMemoryState memoryState = (IApplicationDraftMemoryState) algorithmChooser.getCurrentAlgorithm();
        List<Player> playerList = memoryState.getInstance().getDraftDataProvider().getPlayerList();
        List<Game> gameList = new ArrayList<>();
        for (Player player : playerList) {
            gameList.addAll(player.getPlayedGame());
        }

        List<Round> rounds = convertToRounds(gameList);
        mExpandableRoundAdapter = new ExpandableListAdapter(rounds, this);
    }

    @Override
    protected void injectDependencies(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initToolBar() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_header_path_game);
        mToolBar.setTitleTextColor(getSingleColor(R.color.white));
        getSupportActionBar().setTitle(R.string.app_header_path_game);
    }

    @Override
    protected void initView() {
        mExpandableListView.setAdapter(mExpandableRoundAdapter);
    }

    private List<Round> convertToRounds(List<Game> playedGames) {
        SparseArray<Round> roundList = new SparseArray<>();
        for (Game game : playedGames) {
            int currentRound = game.getRound();
            if (roundList.get(currentRound) == null) {
                List<Game> gameList = new ArrayList<>();
                gameList.add(game);
                Round round = new Round(currentRound, gameList);
                roundList.put(currentRound, round);
            } else {
                Round savedRound = roundList.get(currentRound);
                List<Game> savedGames = savedRound.getGameList();

                if (!savedGames.contains(game)) {
                    roundList.get(currentRound).getGameList().add(game);
                }
            }
        }
        return SparseArrayToArrayListConverter.asList(roundList);
    }

}
