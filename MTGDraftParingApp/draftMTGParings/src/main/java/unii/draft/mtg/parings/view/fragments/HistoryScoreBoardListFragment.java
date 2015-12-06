package unii.draft.mtg.parings.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.database.model.DraftDao;
import unii.draft.mtg.parings.database.model.IDatabaseHelper;
import unii.draft.mtg.parings.view.adapters.DividerItemDecorator;
import unii.draft.mtg.parings.view.adapters.HistoryScoreBoardAdapter;

/**
 * Created by Unii on 2015-12-05.
 */
public class HistoryScoreBoardListFragment extends BaseFragment {
    private Activity mContext;


    @Bind(R.id.table_historyScoreBoardRecyclerView)
    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private IDisplayHistoryScoreBoardDetail mDisplayHistoryScoreBoardDetail;

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
        DraftDao draftDao = ((IDatabaseHelper) mContext.getApplication()).getDaoSession().getDraftDao();

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecorator(mContext, DividerItemDecorator.VERTICAL_LIST));
        mAdapter = new HistoryScoreBoardAdapter(mContext, new ArrayList<Draft>(draftDao.loadAll()), mDisplayHistoryScoreBoardDetail);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
