package unii.draft.mtg.parings.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.base.BaseAlgorithm;
import unii.draft.mtg.parings.buisness.sittings.ISittingGenerator;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.view.adapters.SittingsPlayerAdapter;

public class SittingsFragment extends BaseFragment {

    private List<String> mPlayerNameList;

    @Inject
    AlgorithmChooser mAlgorithmChooser;
    @Inject
    ISittingGenerator mSittingsGenerator;

    @Bind(R.id.table_sittingsRecyclerView)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sittings, container, false);
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

    private List<String> getPlayerNameList(List<Player> playerList) {
        List<String> playerNameList = new ArrayList<>();
        for (Player player : playerList) {
            playerNameList.add(player.getPlayerName());
        }
        return playerNameList;
    }

    @Override
    protected void initFragmentView() {
        RecyclerView.Adapter adapter = new SittingsPlayerAdapter(getActivity(), mPlayerNameList);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void initFragmentData() {
        if (mAlgorithmChooser.getCurrentAlgorithm() instanceof BaseAlgorithm) {
            BaseAlgorithm base = (BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm();
            base.isLoadCachedDraftWasNeeded();
        }
        mPlayerNameList = mSittingsGenerator.generateSittings(getPlayerNameList(mAlgorithmChooser.getCurrentAlgorithm().getSortedPlayerList()));

    }
}
