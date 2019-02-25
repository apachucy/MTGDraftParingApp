package unii.draft.mtg.parings.view.fragments.settings;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.Lazy;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.database.file.FileOperator;
import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.database.populate.DraftExporter;
import unii.draft.mtg.parings.database.populate.Information;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.view.activities.settings.HistoryPlayerAchievementsActivity;
import unii.draft.mtg.parings.view.activities.settings.HistoryScoreBoardActivity;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

import static unii.draft.mtg.parings.database.file.FileOperator.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

public class HistorySettingsFragment extends BaseFragment {

    private Unbinder mUnbinder;
    private FileOperator mFileOperator;
    @Inject
    Lazy<IDatabaseHelper> mDatabaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_history, container, false);
        mUnbinder = ButterKnife.bind(this, view);
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
        mFileOperator = new FileOperator(getString(R.string.app_name));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
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
            FancyToast.makeText(getActivity(), getString(R.string.message_score_board_not_exists), FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
        } else {
            Intent intent = new Intent(getActivity(), HistoryScoreBoardActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.settings_displayPlayerHistoryButton)
    void onDisplayPlayerHistoryClicked() {
        List<unii.draft.mtg.parings.database.model.Player> playerList = mDatabaseHelper.get().getAllPlayerList();
        if (playerList == null || playerList.size() == 0) {
            FancyToast.makeText(getActivity(), getString(R.string.message_player_list_not_exists), FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
            return;
        }
        Intent intent = new Intent(getActivity(), HistoryPlayerAchievementsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.settings_saveScoreBoardsButton)
    void onSaveHistoryClicked() {
        mFileOperator.checkPermission(getActivity());
        saveFileAndShowDialog();
    }

    void saveFileAndShowDialog() {
        Information information = mFileOperator.saveToFile(mDatabaseHelper.get().exportDraftDatabase());
        String textBody = null;
        switch (information) {
            case SUCCESS:
                textBody = getString(R.string.dialog_save_database_content_success);
                break;
            default:
                textBody = information.toString();
                break;
        }
        showDialog(getContext(), getString(R.string.dialog_save_database), textBody, getString(R.string.dialog_positive));
    }

    @OnClick(R.id.settings_loadScoreBoardsButton)
    void onLoadHistoryClicked() {
        List<DraftExporter> list = new ArrayList<>();
        Information information = mFileOperator.loadToObject(list);
        if (information != Information.SUCCESS) {
            displayInformation(information);
            return;
        }
        information = mDatabaseHelper.get().importDraftDatabase(list);

        displayInformation(information);
    }

    private void displayInformation(Information information) {
        String textBody = null;

        switch (information) {
            case SUCCESS:
                textBody = getString(R.string.dialog_import_database_content_success);
                break;
            default:
                textBody = getString(R.string.dialog_import_database_content_error) + information.toString();
                break;
        }
        showDialog(getContext(), getString(R.string.dialog_import_database), textBody, getString(R.string.dialog_positive));

    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

    @NonNull
    private MaterialDialog.SingleButtonCallback mPositiveAction = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            mDatabaseHelper.get().cleanDatabase();
            FancyToast.makeText(getActivity(), getString(R.string.message_score_boards_removed), FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    saveFileAndShowDialog();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showDialog(getContext(), getString(R.string.dialog_import_database), Information.ERROR_NO_PERMISSION.toString(), getString(R.string.dialog_positive));
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
