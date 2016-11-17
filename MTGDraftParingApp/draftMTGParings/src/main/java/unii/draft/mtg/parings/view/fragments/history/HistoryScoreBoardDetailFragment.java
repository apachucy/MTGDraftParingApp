package unii.draft.mtg.parings.view.fragments.history;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.Lazy;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.ItemHeader;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.util.config.BundleConst;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.view.adapters.IAdapterItem;
import unii.draft.mtg.parings.view.adapters.PlayerScoreboardAdapter;
import unii.draft.mtg.parings.view.fragments.BaseFragment;


public class HistoryScoreBoardDetailFragment extends BaseFragment {

    private Activity mContext;
    private List<IAdapterItem> mPlayerScoreBoardList;


    @Inject
    Lazy<IDatabaseHelper> mDatabaseHelper;

    @Bind(R.id.settings_menuRecyclerView)
    RecyclerView mRecyclerView;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
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
        RecyclerView.Adapter adapter = new PlayerScoreboardAdapter(mContext, mPlayerScoreBoardList);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initFragmentData() {
        Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey(BundleConst.BUNDLE_KEY_HISTORY_DRAFT_DETAIL)) {
            mContext.finish();
        }
        long draftKey = bundle.getLong(BundleConst.BUNDLE_KEY_HISTORY_DRAFT_DETAIL);
        List<Player> playerList = new ArrayList<>();

        playerList.addAll(mDatabaseHelper.get().getAllPlayersInDraft(draftKey));
        mPlayerScoreBoardList = new ArrayList<>();
        mPlayerScoreBoardList.add(new ItemHeader());
        mPlayerScoreBoardList.addAll(playerList);
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

}
