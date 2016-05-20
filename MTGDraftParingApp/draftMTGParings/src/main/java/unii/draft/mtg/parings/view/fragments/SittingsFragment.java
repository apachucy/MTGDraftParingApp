package unii.draft.mtg.parings.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unii.draft.mtg.parings.ManualPlayerPairingActivity;
import unii.draft.mtg.parings.ParingDashboardActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.algorithm.IAlgorithmConfigure;
import unii.draft.mtg.parings.algorithm.ManualParingAlgorithm;
import unii.draft.mtg.parings.algorithm.ParingAlgorithm;
import unii.draft.mtg.parings.pojo.Player;
import unii.draft.mtg.parings.sharedprefrences.SettingsPreferencesFactory;
import unii.draft.mtg.parings.sittings.ISittingGenerator;
import unii.draft.mtg.parings.sittings.RandomSittingGenerator;
import unii.draft.mtg.parings.view.adapters.DropPlayerAdapter;
import unii.draft.mtg.parings.view.adapters.SittingsPlayerAdapter;

/**
 * Created by Unii on 2016-05-08.
 */
public class SittingsFragment extends BaseFragment {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ISittingGenerator mSittingsGenerator;
    @Bind(R.id.table_sittingsRecyclerView)
    RecyclerView mRecyclerView;

    @OnClick(R.id.sittings_next)
    public void onActionButtonNextClicked(View view) {
        Intent intent = null;
        IAlgorithmConfigure algorithmConfigure = (IAlgorithmConfigure) getActivity().getApplication();
        if (SettingsPreferencesFactory.getInstance().areManualParings()) {
            intent = new Intent(getActivity(),
                    ManualPlayerPairingActivity.class);
        } else {
            intent = new Intent(getActivity(),
                    ParingDashboardActivity.class);

        }
        startActivity(intent);
        getActivity().finish();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sittings, container, false);
        ButterKnife.bind(this, view);
        IAlgorithmConfigure algorithmConfigure = (IAlgorithmConfigure) getActivity().getApplication();

        mSittingsGenerator = new RandomSittingGenerator();

        List<String> playerNameList = mSittingsGenerator.generateSittings(getPlayerNameList(algorithmConfigure.getInstance().getSortedPlayerList()));
        mAdapter = new SittingsPlayerAdapter(getActivity(), playerNameList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return view;

    }

    private List<String> getPlayerNameList(List<Player> playerList) {
        List<String> playerNameList = new ArrayList<>();
        for (Player player : playerList) {
            playerNameList.add(player.getPlayerName());
        }
        return playerNameList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
