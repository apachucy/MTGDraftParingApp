package unii.draft.mtg.parings.view.fragments.history;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.Lazy;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.share.draft.list.IShareDraftHistory;
import unii.draft.mtg.parings.buisness.share.draft.list.ShareDraftDataHistory;
import unii.draft.mtg.parings.logic.pojo.Draft;
import unii.draft.mtg.parings.util.converter.Converter;
import unii.draft.mtg.parings.util.converter.DraftConverter;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.view.adapters.DividerItemDecorator;
import unii.draft.mtg.parings.view.adapters.HistoryScoreBoardAdapter;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

public class HistoryScoreBoardListFragment extends BaseFragment {

    private Activity mContext;
    private IDisplayDetailFragment mDisplayHistoryScoreBoardDetail;
    private IShareDraftHistory mShareDraftHistory;
    private List<Draft> mDraftListForShare;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.settings_share, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                if (mDraftListForShare == null) {
                    mDraftListForShare = createDraftListForSharing();
                }
                shareAction(mShareDraftHistory.getDraftListToString(mDraftListForShare));
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        mShareDraftHistory = new ShareDraftDataHistory(getContext());


    }

    private List<Draft> createDraftListForSharing() {
        List<Draft> draftList = new ArrayList<>();
        List<unii.draft.mtg.parings.database.model.Draft> databaseDraftList = mDatabaseHelper.get().getAllDraftList();
        Converter<Draft, unii.draft.mtg.parings.database.model.Draft> draftConverter = new DraftConverter();
        for (unii.draft.mtg.parings.database.model.Draft draft : databaseDraftList) {
            draftList.add(draftConverter.convert(draft, mDatabaseHelper.get().getAllPlayersInDraft(draft.getId()).get(0).getPlayerName()));
        }
        return draftList;
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

}
