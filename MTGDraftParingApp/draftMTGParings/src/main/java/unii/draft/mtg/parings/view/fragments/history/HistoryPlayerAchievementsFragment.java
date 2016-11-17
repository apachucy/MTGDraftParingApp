package unii.draft.mtg.parings.view.fragments.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.Lazy;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.util.config.BundleConst;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.view.adapters.DetailHistoryPlayerAdapter;
import unii.draft.mtg.parings.view.adapters.DividerItemDecorator;
import unii.draft.mtg.parings.view.fragments.BaseFragment;


public class HistoryPlayerAchievementsFragment extends BaseFragment {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Long mPlayerId;
    private unii.draft.mtg.parings.database.model.Player mPlayer;

    @Bind(R.id.history_player_detail_playerAchievementsTextView)
    RecyclerView mRecyclerView;
    @Bind(R.id.history_player_detail_playerNameTextView)
    TextView mHistoryPlayerNameTextView;

    @Inject
    Lazy<IDatabaseHelper> mDatabaseHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_history_player_detail, container, false);
        ButterKnife.bind(this, view);
        injectDependencies();
        initFragmentData();
        initFragmentView();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void initFragmentView() {
        mHistoryPlayerNameTextView.setText(mPlayer.getPlayerName());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), DividerItemDecorator.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initFragmentData() {

        Bundle bundle = getArguments();
        if (!bundle.containsKey(BundleConst.BUNDLE_KEY_PLAYER_DRAFT_DETAIL)) {
            getActivity().finish();
        }
        mPlayerId = bundle.getLong(BundleConst.BUNDLE_KEY_PLAYER_DRAFT_DETAIL);
        mPlayer = mDatabaseHelper.get().getPlayer(mPlayerId);
        mAdapter = new DetailHistoryPlayerAdapter(getActivity(), mDatabaseHelper.get(), mPlayerId
        );

    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

}
