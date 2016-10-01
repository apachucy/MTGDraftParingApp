package unii.draft.mtg.parings.view.fragments.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import unii.draft.mtg.parings.HistoryScoreBoardActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.database.model.DaoSession;
import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.database.model.DraftDao;
import unii.draft.mtg.parings.database.model.PlayerDao;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

public class HistorySettingsFragment extends BaseFragment {


    @Inject
    DaoSession mDaoSession;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_history, container, false);
        ButterKnife.bind(this, view);
        injectDependencies();
        initFragmentData();
        initFragmentView();
        return view;
    }

    @Override
    protected void initFragmentView() {

    }

    @Override
    protected void initFragmentData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.settings_removeScoreBoardsButton)
    void onRemoveScoreBoardClicked(View view) {
        PlayerDao playerDao = mDaoSession.getPlayerDao();
        DraftDao draftDao = mDaoSession.getDraftDao();
        playerDao.deleteAll();
        draftDao.deleteAll();
        Toast.makeText(getActivity(), getString(R.string.message_score_boards_removed), Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.settings_displayScoreBoardsTextView)
    void onDisplayScoreBoardsClicked(View view) {
        DraftDao draftDao = mDaoSession.getDraftDao();
        List<Draft> draftList = draftDao.loadAll();
        if (draftList == null || draftList.size() == 0) {
            Toast.makeText(getActivity(), getString(R.string.message_score_board_not_exists), Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(getActivity(), HistoryScoreBoardActivity.class);
            startActivity(intent);
        }
    }


    @OnClick(R.id.settings_displayPlayerHistoryTextView)
    void onDisplayPlayerHistoryClicked(View view) {
        //TODO: implement me
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

}
