package unii.draft.mtg.parings.view.fragments.history;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.Lazy;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.share.draft.list.ISharePlayerHistoryList;
import unii.draft.mtg.parings.buisness.share.draft.list.SharePlayerAchievementList;
import unii.draft.mtg.parings.database.model.Player;
import unii.draft.mtg.parings.logic.pojo.PlayerAchievements;
import unii.draft.mtg.parings.util.converter.Converter;
import unii.draft.mtg.parings.util.converter.PlayerAchievementsConverter;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.view.adapters.DividerItemDecorator;
import unii.draft.mtg.parings.view.adapters.HistoryPlayerAdapter;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

public class HistoryPlayerListFragment extends BaseFragment {
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private IDisplayDetailFragment mDisplayHistoryScoreBoardDetail;
    private Activity mContext;
    private List<PlayerAchievements> mPlayerList;
    private ISharePlayerHistoryList mDraftListForShare;
    private Unbinder mUnbinder;

    @Nullable
    @BindView(R.id.settings_menuRecyclerView)
    RecyclerView mRecyclerView;

    @Inject
    Lazy<IDatabaseHelper> mDatabaseHelper;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        if (activity instanceof IDisplayDetailFragment) {
            mDisplayHistoryScoreBoardDetail = (IDisplayDetailFragment) activity;
        } else {
            new Throwable("Activity should implement IDisplayDetailFragment");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_view, container, false);
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
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareAction(mDraftListForShare.getPlayerListToString(mPlayerList));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initFragmentView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecorator(mContext, DividerItemDecorator.VERTICAL_LIST));
        mAdapter = new HistoryPlayerAdapter(mContext, mPlayerList, mDisplayHistoryScoreBoardDetail);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initFragmentData() {
        mDraftListForShare = new SharePlayerAchievementList(getActivity());
        Converter<PlayerAchievements, Player> converter = new PlayerAchievementsConverter();
        mPlayerList = new ArrayList<>();

        for (Player player : mDatabaseHelper.get().getAllPlayerList()) {
            mPlayerList.add(converter.convert(player));
        }
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

}
