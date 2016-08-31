package unii.draft.mtg.parings.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.view.adapters.DropPlayerAdapter;


public class DropPlayerFragment extends BaseFragment {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Player> mNotDroppedPlayerList;

    @Bind(R.id.table_dropPlayerRecyclerView)
    RecyclerView mRecyclerView;

    @Inject
    AlgorithmChooser mAlgorithmChooser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drop_player, container, false);
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

    @OnClick(R.id.save_dropPlayerButton)
    public void onSaveButtonClicked() {
        getActivity().setResult(getActivity().RESULT_OK);
        getActivity().finish();
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
        mAdapter = new DropPlayerAdapter(getActivity(), mNotDroppedPlayerList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initFragmentData() {
        mNotDroppedPlayerList = mAlgorithmChooser.getCurrentAlgorithm().getSortedFilteredPlayerList(false);
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

}
