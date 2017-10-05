package unii.draft.mtg.parings.view.fragments.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Lazy;
import unii.draft.mtg.parings.view.activities.settings.HistoryPlayerAchievementsActivity;
import unii.draft.mtg.parings.view.activities.settings.HistoryScoreBoardActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

public class HistorySettingsFragment extends BaseFragment {


    @Inject
    Lazy<IDatabaseHelper> mDatabaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
    void onRemoveScoreBoardClicked() {
        showDialogWithTwoOptions(getActivity(), getString(R.string.settings_history_remove_clean_history_dialog_title),
                getString(R.string.settings_history_remove_clean_history_dialog_body), getString(R.string.dialog_positive),
                getString(R.string.dialog_negative), mPositiveAction);

    }

    @OnClick(R.id.settings_displayScoreBoardsButton)
    void onDisplayScoreBoardsClicked() {
        List<Draft> draftList = mDatabaseHelper.get().getAllDraftList();
        if (draftList == null || draftList.size() == 0) {
            Toast.makeText(getActivity(), getString(R.string.message_score_board_not_exists), Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(getActivity(), HistoryScoreBoardActivity.class);
            startActivity(intent);
        }
    }


    @OnClick(R.id.settings_displayPlayerHistoryButton)
    void onDisplayPlayerHistoryClicked() {
        List<unii.draft.mtg.parings.database.model.Player> playerList = mDatabaseHelper.get().getAllPlayerList();
        if (playerList == null || playerList.size() == 0) {
            Toast.makeText(getActivity(), getString(R.string.message_player_list_not_exists), Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(getActivity(), HistoryPlayerAchievementsActivity.class);
        startActivity(intent);
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

    @NonNull
    private MaterialDialog.SingleButtonCallback mPositiveAction = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            mDatabaseHelper.get().cleanDatabase();
            Toast.makeText(getActivity(), getString(R.string.message_score_boards_removed), Toast.LENGTH_LONG).show();
        }
    };
}
