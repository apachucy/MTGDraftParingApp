package unii.draft.mtg.parings.view.fragments.history;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.Lazy;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.share.scoreboard.IShareData;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.ItemHeader;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.logic.pojo.Round;
import unii.draft.mtg.parings.util.config.BundleConst;
import unii.draft.mtg.parings.util.converter.SparseArrayToArrayListConverter;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.util.helper.PlayerNameWithPositionGenerator;
import unii.draft.mtg.parings.view.adapters.ExpandableListAdapter;
import unii.draft.mtg.parings.view.adapters.IAdapterItem;
import unii.draft.mtg.parings.view.adapters.PlayerScoreboardAdapter;
import unii.draft.mtg.parings.view.fragments.BaseFragment;


public class HistoryScoreBoardDetailFragment extends BaseFragment {

    private Activity mContext;
    private List<IAdapterItem> mPlayerScoreBoardList;
    private List<Player> mPlayerList;
    private Unbinder mUnbinder;
    private ExpandableListAdapter mExpandableRoundAdapter;
    @Inject
    Lazy<IDatabaseHelper> mDatabaseHelper;

    @Inject
    IShareData mShareData;

    @Nullable
    @BindView(R.id.settings_menuRecyclerView)
    RecyclerView mRecyclerView;

    @Nullable
    @BindView(R.id.settings_roundExpandableListView)
    ExpandableListView mExpandableListView;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_scoreboard, container, false);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.settings_share, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareAction(mShareData.getPlayerWithPoints(mPlayerList));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initFragmentView() {
        RecyclerView.Adapter adapter = new PlayerScoreboardAdapter(mContext, mPlayerScoreBoardList);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);

        mExpandableListView.setAdapter(mExpandableRoundAdapter);
    }

    @Override
    protected void initFragmentData() {
        Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey(BundleConst.BUNDLE_KEY_HISTORY_DRAFT_DETAIL)) {
            mContext.finish();
        }
        long draftKey = bundle.getLong(BundleConst.BUNDLE_KEY_HISTORY_DRAFT_DETAIL);
        mPlayerList = new ArrayList<>();

        mPlayerList.addAll(PlayerNameWithPositionGenerator.getListWithNames(mDatabaseHelper.get().getAllPlayersInDraft(draftKey)));
        mPlayerScoreBoardList = new ArrayList<>();
        mPlayerScoreBoardList.add(new ItemHeader());
        mPlayerScoreBoardList.addAll(mPlayerList);
        List<Game> gameList = mDatabaseHelper.get().getAllGamesForDraft(draftKey);
        SparseArray<Round> rounds = new SparseArray<>();
        for (Game game : gameList) {
            if (rounds.get(game.getRound()) == null) {
                List<Game> roundGames = new ArrayList<>();
                roundGames.add(game);
                Round round = new Round(game.getRound(), roundGames);
                rounds.put(game.getRound(), round);
            } else {
                Round round = rounds.get(game.getRound());
                round.getGameList().add(game);
            }

        }

        mExpandableRoundAdapter = new ExpandableListAdapter(SparseArrayToArrayListConverter.asList(rounds), getContext());
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

}
