package unii.draft.mtg.parings.view.fragments.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.Lazy;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.util.config.BaseConfig;
import unii.draft.mtg.parings.util.helper.Database;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.view.activities.settings.HistoryPlayerAchievementsActivity;
import unii.draft.mtg.parings.view.activities.settings.HistoryScoreBoardActivity;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

public class HistorySettingsFragment extends BaseFragment {

    private Unbinder mUnbinder;
    private Gson gson;
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
        gson = new Gson();
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

    @OnClick(R.id.settings_exportScoreBoardsButton)
    void onExportLocalDatabaseClicked() {
        if (!isExternalStorageWritable()) {
            //TODO: inform user that phone is mounted
        }
        File file = getPrivateStorageDir(getContext(), BaseConfig.EXPORTED_DATABASE_NAME);
        String localDatabase = gson.toJson(mDatabaseHelper.get().exportDatabase());

        boolean isSaved = writeToFile(file, localDatabase);
        if (isSaved) {
            //TODO: told the user where it is saved
        } else {
            //TODO: inform that there was an issue while saving a file
        }
    }

    @OnClick(R.id.settings_importScoreBoardsButton)
    void onImportLocalDatabaseClicked() {
        //TODO: display Dialog with editTExt for pathName
        String pathFile;
        if (!isExternalStorageWritable()) {
            //TODO: inform user that phone is mounted
        }

        // String localDatabase = gson.toJson(mDatabaseHelper.get().exportDatabase());

        boolean isSaved = writeToFile(file, localDatabase);
        if (isSaved) {
            //TODO: told the user where it is saved
        } else {
            //TODO: inform that there was an issue while saving a file
        }
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

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File getPrivateStorageDir(Context context, String databaseName) {
        // Get the directory for the app's documents directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS), databaseName);
        if (!file.mkdirs()) {
            //TODO: make a toast to inform user
        }
        return file;
    }

    private void loadDatabase(@NonNull String fileName) {
        String jsonFile = openFile(fileName);
        Database database = convertTODatabase(jsonFile);
        mDatabaseHelper.get().importDatabase(database);
    }

    private Database convertTODatabase(@NonNull String jsonFile) {
        Gson gson = new Gson();
        return gson.fromJson(jsonFile, Database.class);
    }

    private String openFile(@NonNull String filePath) {
        String line = "";
        StringBuilder gsonFile = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            while ((line = bufferedReader.readLine()) != null) {
                gsonFile.append(line);
            }
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException e) {
            return "";

        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
            }
        }
        return gsonFile.toString();
    }

    public boolean writeToFile(@NonNull File file, @NonNull String content) {
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                } catch (IOException e) {
                }
            }
        }
    }
}
