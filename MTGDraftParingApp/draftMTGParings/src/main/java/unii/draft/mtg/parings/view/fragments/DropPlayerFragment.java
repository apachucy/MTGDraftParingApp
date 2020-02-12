package unii.draft.mtg.parings.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.base.BaseAlgorithm;
import unii.draft.mtg.parings.buisness.algorithm.roundrobin.ItalianRoundRobinRounds;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.view.adapters.DropPlayerAdapter;
import unii.draft.mtg.parings.view.logic.ParingDashboardLogic;

import static android.app.Activity.RESULT_OK;
import static unii.draft.mtg.parings.util.config.BaseConfig.PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER;


public class DropPlayerFragment extends BaseFragment {

    private List<Player> mNotDroppedPlayerList;
    private Unbinder mUnbinder;
    @Nullable
    @BindView(R.id.table_dropPlayerRecyclerView)
    RecyclerView mRecyclerView;

    @Inject
    AlgorithmChooser mAlgorithmChooser;

    @Inject
    ISharedPreferences mSharedPreferenceManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drop_player, container, false);
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

    @OnClick(R.id.save_dropPlayerButton)
    public void onSaveButtonClicked() {
        if (mAlgorithmChooser.getCurrentAlgorithm() instanceof ItalianRoundRobinRounds) {
            operateItalianRoundRobinDropPlayer();
        }

        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    private void operateItalianRoundRobinDropPlayer() {
        for (Player player : mNotDroppedPlayerList) {
            if (!player.isDropped()) {
                continue;
            }
            //Add R as retire player
            if (!player.getPlayerName().startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER)) {
                player.setPlayerName(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER + player.getPlayerName());
            }
        }
        if (isRoundBeforeHalf(mAlgorithmChooser)) {
            ParingDashboardLogic paringDashboardLogic = new ParingDashboardLogic(getContext(), mSharedPreferenceManager.getPointsForGameWinning(),
                    mSharedPreferenceManager.getPointsForGameDraws(),
                    mSharedPreferenceManager.getPointsForMatchWinning(), mSharedPreferenceManager.getPointsForMatchDraws(),
                    mAlgorithmChooser.getCurrentAlgorithm() instanceof ItalianRoundRobinRounds);
            paringDashboardLogic.updateAllPlayerList(mNotDroppedPlayerList);
        }
    }


    private boolean isRoundBeforeHalf(@NonNull AlgorithmChooser algorithmChooser) {
        return algorithmChooser.getCurrentAlgorithm().getCurrentRound() < algorithmChooser.getCurrentAlgorithm().getMaxRound() / 2.0f;
    }

    public void clearSelections() {
        for (Player player : mNotDroppedPlayerList) {
            if (player.isDropped()) {
                player.setDropped(false);
            }
        }
    }

    @Override
    protected void initFragmentView() {
        RecyclerView.Adapter adapter = new DropPlayerAdapter(getActivity(), mNotDroppedPlayerList);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initFragmentData() {
        try {
            if (mAlgorithmChooser.getCurrentAlgorithm() instanceof BaseAlgorithm) {
                BaseAlgorithm base = (BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm();
                base.isLoadCachedDraftWasNeeded();
            }
            mNotDroppedPlayerList = mAlgorithmChooser.getCurrentAlgorithm().getSortedFilteredPlayerList(false);
        } catch (NullPointerException exception) {
            getActivity().finish();
        } finally {
            //Nothing here
        }

    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

}
