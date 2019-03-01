package unii.draft.mtg.parings.view.fragments.settings;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.base.BaseAlgorithm;
import unii.draft.mtg.parings.buisness.model.AddPlayerModel;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.view.adapters.AddPlayerAdapter;
import unii.draft.mtg.parings.view.adapters.DividerItemDecorator;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

import static android.app.Activity.RESULT_OK;

public class AddPlayerFragment extends BaseFragment {


    private Unbinder mUnbinder;
    private AddPlayerModel mModel;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Inject
    AlgorithmChooser mAlgorithmChooser;
    @Inject
    ISharedPreferences sharedPreferences;

    @Nullable
    @BindView(R.id.table_addPlayerRecyclerView)
    RecyclerView mPlayerList;
    @Nullable
    @BindView(R.id.init_playerNameTextInput)
    TextInputLayout mPlayerNameTextInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_player, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        injectDependencies();
        initFragmentData();
        initFragmentView();

        return view;

    }

    @Override
    protected void initFragmentData() {
        try {
            if (mAlgorithmChooser.getCurrentAlgorithm() instanceof BaseAlgorithm) {
                BaseAlgorithm base = (BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm();
                base.isLoadCachedDraftWasNeeded();
            }
            mModel = new AddPlayerModel(mAlgorithmChooser, sharedPreferences.getPointsForMatchWinning());
            mAdapter = new AddPlayerAdapter(mModel.getAddedPlayers());
        } catch (NullPointerException exception) {
            getActivity().finish();
        } finally {
            //Nothing here
        }
    }

    @Override
    protected void initFragmentView() {

        mPlayerList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mPlayerList.setLayoutManager(mLayoutManager);
        mPlayerList.addItemDecoration(new DividerItemDecorator(getActivity(), DividerItemDecorator.VERTICAL_LIST));

        mPlayerNameTextInput.setHint(getString(R.string.hint_player_name));
        mPlayerNameTextInput.setErrorEnabled(true);
        mPlayerList.setAdapter(mAdapter);

    }

    @OnClick(R.id.init_addPlayerButton)
    void onAddPlayerClick() {
        String playerName = mPlayerNameTextInput.getEditText().getText().toString();

        if (mModel.isPlayerNameEmpty(playerName)) {
            mPlayerNameTextInput.setError(getString(R.string.warning_empty_field));
            return;
        }
        if (!mModel.isNameUnique(playerName)) {
            mPlayerNameTextInput.setError(getString(R.string.warning_player_name_added));
            return;
        }
        mPlayerNameTextInput.setError(null);
        mModel.addPlayerToList(playerName);
        mPlayerNameTextInput.getEditText().setText("");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

    @OnClick(R.id.save_addPlayerButton)
    public void onSaveButtonClicked() {
        mModel.addPlayersToDraft();
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }
}
