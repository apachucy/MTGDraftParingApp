package unii.draft.mtg.parings.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.config.BundleConst;
import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.database.model.DraftDao;
import unii.draft.mtg.parings.database.model.IDatabaseHelper;
import unii.draft.mtg.parings.pojo.Player;
import unii.draft.mtg.parings.view.adapters.PlayerAdapter;

/**
 * Created by Unii on 2015-12-06.
 */
public class HistoryScoreBoardDetailFragment extends BaseFragment {

    private Activity mContext;
    private PlayerAdapter mAdapter;
    List<Player> mPlayerList;

    @Bind(R.id.player_position_playerListView)
    ListView mPlayerListView;

    private long mDraftKey;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_score_board_detail, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (!bundle.containsKey(BundleConst.BUNDLE_KEY_HISTORY_DRAFT_DETAIL)) {
            mContext.finish();
        }
        mDraftKey = bundle.getLong(BundleConst.BUNDLE_KEY_HISTORY_DRAFT_DETAIL);
        DraftDao draftDao = ((IDatabaseHelper) mContext.getApplication()).getDaoSession().getDraftDao();

        Draft draft = draftDao.load(mDraftKey);
        mPlayerList = new ArrayList<>();
        for (unii.draft.mtg.parings.database.model.Player player : draft.getPlayers()) {
            mPlayerList.add(new Player(player));
        }

        mAdapter = new PlayerAdapter(mContext, mPlayerList);
        View header = mContext.getLayoutInflater().inflate(R.layout.header_player_list,
                null);

        mPlayerListView.addHeaderView(header);
        mPlayerListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
