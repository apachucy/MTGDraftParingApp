package unii.draft.mtg.parings.view.fragments.history;

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
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.Lazy;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.share.draft.detail.player.ISharePlayerAchievements;
import unii.draft.mtg.parings.buisness.share.draft.detail.player.SharePlayerAchievements;
import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.database.model.Player;
import unii.draft.mtg.parings.logic.pojo.PlayerAchievements;
import unii.draft.mtg.parings.logic.pojo.PlayerDraft;
import unii.draft.mtg.parings.util.config.BundleConst;
import unii.draft.mtg.parings.util.converter.Converter;
import unii.draft.mtg.parings.util.converter.PlayerAchievementsConverter;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.view.adapters.DetailHistoryPlayerAdapter;
import unii.draft.mtg.parings.view.adapters.DividerItemDecorator;
import unii.draft.mtg.parings.view.adapters.SingleTextViewAdapter;
import unii.draft.mtg.parings.view.fragments.BaseFragment;
import unii.draft.mtg.parings.view.fragments.settings.TimeSettingsFragment;

/**
 * TODO: FIX adapter for OverallView or make a generic
 */
public class HistoryPlayerAchievementsFragment extends BaseFragment {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Unbinder mUnbinder;

    private List<String> mPlayerOverallList;
    private List<PlayerDraft> mPlayerDraftPlayedList;
    private Long mPlayerId;
    private unii.draft.mtg.parings.database.model.Player mPlayer;
    private ISharePlayerAchievements mSharePlayerAchievements;

    @Nullable
    @BindView(R.id.history_player_detail_playerAchievementsList)
    RecyclerView mPlayerAchievementRecyclerView;
    @Nullable
    @BindView(R.id.history_player_detail_playerNameTextView)
    TextView mHistoryPlayerNameTextView;
    @Nullable
    @BindView(R.id.history_player_detail_playerOverallPositionsList)
    RecyclerView mPlayerPositionOverallRecyclerView;

    @Inject
    Lazy<IDatabaseHelper> mDatabaseHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_player_detail, container, false);
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


    @OnClick(R.id.icon_editImageView)
    void onEditNamePlayerClicked() {
        showEditTextDialogWithAnyValue(getActivity(), getString(R.string.rename_player_dialog_title), getString(R.string.rename_player_dialog_body), "", mPlayer.getPlayerName(), updateName);
    }


    @OnClick(R.id.icon_deleteImageView)
    void onRemovePlayerClicked() {
        showDialogWithTwoOptions(getActivity(), getString(R.string.remove_player_dialog_tile), getString(R.string.remove_player_dialog_body), getString(R.string.positive), getString(R.string.negative), singleButtonCallback);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareAction(mSharePlayerAchievements.playerAchievementsToString(mPlayer.getPlayerName(), mPlayerOverallList, mPlayerDraftPlayedList));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initFragmentView() {
        mHistoryPlayerNameTextView.setText(mPlayer.getPlayerName());

        mPlayerAchievementRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mPlayerAchievementRecyclerView.setLayoutManager(mLayoutManager);
        mPlayerAchievementRecyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), DividerItemDecorator.VERTICAL_LIST));
        mPlayerAchievementRecyclerView.setAdapter(mAdapter);

        RecyclerView.Adapter adapter = new SingleTextViewAdapter(getActivity(), mPlayerOverallList);
        mPlayerPositionOverallRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mPlayerPositionOverallRecyclerView.setLayoutManager(layoutManager);
        mPlayerPositionOverallRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initFragmentData() {

        Bundle bundle = getArguments();
        if (!bundle.containsKey(BundleConst.BUNDLE_KEY_PLAYER_DRAFT_DETAIL)) {
            getActivity().finish();
        }
        mPlayerId = bundle.getLong(BundleConst.BUNDLE_KEY_PLAYER_DRAFT_DETAIL);
        mPlayer = mDatabaseHelper.get().getPlayer(mPlayerId);


        Converter<PlayerAchievements, Player> converter = new PlayerAchievementsConverter();
        PlayerAchievements playerAchievements = converter.convert(mPlayer);
        mPlayerDraftPlayedList = createPlayerDraftPlayedList();
        mPlayerOverallList = createPlayerOverallList(playerAchievements);
        mAdapter = new DetailHistoryPlayerAdapter(getActivity(), mPlayerDraftPlayedList);
        mSharePlayerAchievements = new SharePlayerAchievements(getActivity());

    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

    private String playerAchievementToString(Integer key, Integer value) {
        if (value > 1) {
            return getString(R.string.history_detail_player_overall_count_positions, key, value);
        }
        return getString(R.string.history_detail_player_overall_count_position, key, value);
    }

    @NonNull
    private List<String> createPlayerOverallList(@NonNull PlayerAchievements playerAchievements) {
        List<String> list = new ArrayList<>();
        for (Integer keys : playerAchievements.getPlaceInDrafts().keySet()) {
            list.add(playerAchievementToString(keys, playerAchievements.getPlaceInDrafts().get(keys)));
        }
        return list;
    }

    @NonNull
    private List<PlayerDraft> createPlayerDraftPlayedList() {
        List<PlayerDraft> playerDraftPlayedList = new ArrayList<>();
        List<Draft> dbDraftList = mDatabaseHelper.get().getAllDraftsForPlayer(mPlayerId);
        for (Draft item : dbDraftList) {
            long playerPosition = mDatabaseHelper.get().getPlayerPlaceInDraft(item.getId(), mPlayerId);
            PlayerDraft playerDraft = new PlayerDraft(item.getDraftName(), (int) playerPosition, item.getNumberOfPlayers());
            playerDraftPlayedList.add(playerDraft);
        }
        return playerDraftPlayedList;
    }

    @NonNull
    private TimeSettingsFragment.UpdateData updateName = new UpdateData() {
        @Override
        public void updateView() {
            mHistoryPlayerNameTextView.setText(mPlayer.getPlayerName());
        }

        @Override
        public void updateSharedPreferences(String newData) {
            mPlayer.setPlayerName(newData);
            mDatabaseHelper.get().changePlayerName(mPlayer);
        }
    };

    @NonNull
    private MaterialDialog.SingleButtonCallback singleButtonCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            mDatabaseHelper.get().removePlayer(mPlayer);
            getActivity().onBackPressed();
        }
    };
}
