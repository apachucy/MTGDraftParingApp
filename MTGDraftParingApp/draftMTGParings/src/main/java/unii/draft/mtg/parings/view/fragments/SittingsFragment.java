package unii.draft.mtg.parings.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.base.BaseAlgorithm;
import unii.draft.mtg.parings.buisness.sittings.ISittingGenerator;
import unii.draft.mtg.parings.buisness.sittings.SittingsMode;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.view.adapters.SingleTextViewAdapter;

public class SittingsFragment extends BaseFragment {

    private List<String> mPlayerNameList;
    private Unbinder mUnbinder;
    @Inject
    AlgorithmChooser mAlgorithmChooser;
    @Inject
    ISittingGenerator mSittingsGenerator;
    @Inject
    ISharedPreferences mSharedPreferenceManager;
    @Nullable
    @BindView(R.id.table_sittingsRecyclerView)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sittings, container, false);
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

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

    @NonNull
    private List<String> getPlayerNameList(@NonNull List<Player> playerList) {
        List<String> playerNameList = new ArrayList<>();
        for (Player player : playerList) {
            playerNameList.add(player.getPlayerName());
        }
        return playerNameList;
    }

    @Override
    protected void initFragmentView() {
        RecyclerView.Adapter adapter = new SingleTextViewAdapter(getActivity(), mPlayerNameList);
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
            mPlayerNameList = mSittingsGenerator.generateSittings(getPlayerNameList(mAlgorithmChooser.getCurrentAlgorithm().getSortedPlayerList()));

            if (mSharedPreferenceManager.getGeneratedSittingMode() == SittingsMode.SITTINGS_TOURNAMENT) {
                mAlgorithmChooser.getCurrentAlgorithm().reoderPlayerList(mPlayerNameList);
            }
        } catch (NullPointerException exception) {
            getActivity().finish();
        } finally {
            //Nothing here
        }
    }
}