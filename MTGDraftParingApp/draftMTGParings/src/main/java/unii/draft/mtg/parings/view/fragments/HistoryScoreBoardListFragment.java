package unii.draft.mtg.parings.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.database.model.DaoSession;
import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.database.model.DraftDao;
import unii.draft.mtg.parings.view.adapters.DividerItemDecorator;
import unii.draft.mtg.parings.view.adapters.HistoryScoreBoardAdapter;

public class HistoryScoreBoardListFragment extends BaseFragment {

    private Activity mContext;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private IDisplayHistoryScoreBoardDetail mDisplayHistoryScoreBoardDetail;
    private DraftDao mDraftDao;

    @Bind(R.id.table_historyScoreBoardRecyclerView)
    RecyclerView mRecyclerView;

    @Inject
    DaoSession mDaoSession;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        if (activity instanceof IDisplayHistoryScoreBoardDetail) {
            mDisplayHistoryScoreBoardDetail = (IDisplayHistoryScoreBoardDetail) activity;
        } else {
            new Throwable("Activity should implement IDisplayHistoryScoreBoardDetail");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_score_board, container, false);
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

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initFragmentView() {
        mDraftDao = mDaoSession.getDraftDao();
    }

    @Override
    protected void initFragmentData() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecorator(mContext, DividerItemDecorator.VERTICAL_LIST));
        mAdapter = new HistoryScoreBoardAdapter(mContext, new ArrayList<Draft>(mDraftDao.loadAll()), mDisplayHistoryScoreBoardDetail);
        mRecyclerView.setAdapter(mAdapter);
    }
}
