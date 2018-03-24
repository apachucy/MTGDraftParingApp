package unii.draft.mtg.parings.view.fragments.history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.Lazy;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.util.config.BundleConst;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.view.adapters.DividerItemDecorator;
import unii.draft.mtg.parings.view.adapters.PlayerRivalisationAdapter;
import unii.draft.mtg.parings.view.fragments.BaseFragment;


public class HistoryPlayerRivalisationFragment extends BaseFragment {
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Unbinder mUnbinder;
    private Long mPlayerId;
    private List<Game> mAdapterData;
    @Nullable
    @BindView(R.id.settings_menuRecyclerView)
    RecyclerView mRecyclerView;

    @Inject
    Lazy<IDatabaseHelper> mDatabaseHelper;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_rivalisation, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        injectDependencies();
        initFragmentData();
        initFragmentView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    protected void initFragmentView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecorator(getContext(), DividerItemDecorator.VERTICAL_LIST));
        mAdapter = new PlayerRivalisationAdapter(mAdapterData, getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initFragmentData() {
        Bundle bundle = getArguments();
        if (!bundle.containsKey(BundleConst.BUNDLE_KEY_PLAYER_DRAFT_DETAIL)) {
            getActivity().finish();
        }
        mPlayerId = bundle.getLong(BundleConst.BUNDLE_KEY_PLAYER_DRAFT_DETAIL);
        String playerName = mDatabaseHelper.get().getPlayer(mPlayerId).getPlayerName();
        List<Game> playerGamesList = mDatabaseHelper.get().getAllGamesForPlayer(mPlayerId);
        mAdapterData = adapterData(convertListToMap(playerGamesList, playerName), playerName);

    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

    private List<Game> adapterData(Map<String, List<Game>> playedGames, String playerName) {
        List<Game> displayedData = new ArrayList<>();
        for (String key : playedGames.keySet()) {
            List<Game> playedWithRival = playedGames.get(key);

            Game gameSummary = new Game(playerName, key, 0);
            for (Game game : playedWithRival) {
                if (game.getWinner().equals(playerName)) {
                    gameSummary.setPlayerAPoints(gameSummary.getPlayerAPoints() + 1);
                } else if (game.getWinner().equals(key)) {
                    gameSummary.setPlayerBPoints(gameSummary.getPlayerBPoints() + 1);

                } else {
                    gameSummary.setDraws(gameSummary.getDraws() + 1);
                }
            }
            displayedData.add(gameSummary);
        }
        return displayedData;
    }

    private Map<String, List<Game>> convertListToMap(List<Game> playerGamesList, String playerName) {
        Map<String, List<Game>> games = new HashMap<>();

        for (Game game : playerGamesList) {
            String opponent = playerName.equals(game.getPlayerNameA()) ? game.getPlayerNameB() : game.getPlayerNameA();

            if (games.get(opponent) == null) {
                List<Game> playersGames = new ArrayList<>();
                playersGames.add(game);
                games.put(opponent, playersGames);
            } else {
                games.get(opponent).add(game);
            }
        }
        return games;
    }
}
