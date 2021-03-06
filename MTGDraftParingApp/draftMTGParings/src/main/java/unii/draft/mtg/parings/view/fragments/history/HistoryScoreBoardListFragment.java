package unii.draft.mtg.parings.view.fragments.history;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.Lazy;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.view.adapters.DividerItemDecorator;
import unii.draft.mtg.parings.view.adapters.HistoryScoreBoardAdapter;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

public class HistoryScoreBoardListFragment extends BaseFragment {

    private Activity mContext;
    private IDisplayDetailFragment mDisplayHistoryScoreBoardDetail;

    @Bind(R.id.settings_menuRecyclerView)
    RecyclerView mRecyclerView;

    @Inject
    Lazy<IDatabaseHelper> mDatabaseHelper;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        if (activity instanceof IDisplayDetailFragment) {
            mDisplayHistoryScoreBoardDetail = (IDisplayDetailFragment) activity;
        } else {
            //noinspection ThrowableInstanceNeverThrown
            new Throwable("Activity should implement IDisplayDetailFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_view, container, false);
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
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecorator(mContext, DividerItemDecorator.VERTICAL_LIST));
        RecyclerView.Adapter adapter = new HistoryScoreBoardAdapter(mContext, mDatabaseHelper.get(), mDisplayHistoryScoreBoardDetail);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initFragmentData() {

    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

}
