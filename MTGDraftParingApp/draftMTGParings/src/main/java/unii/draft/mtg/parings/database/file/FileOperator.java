package unii.draft.mtg.parings.database.file;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import unii.draft.mtg.parings.database.populate.DraftExporter;
import unii.draft.mtg.parings.database.populate.Information;



public class FileOperator {
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private String fileName;


    public FileOperator(@NonNull String fileName) {
        this.fileName = fileName;
    }

    public Information saveToFile(@NonNull List<DraftExporter> draftExporterList) {

        if (!isExternalStorageWritable()) {
            return Information.ERROR_NOT_WRITABLE;
        }
        File file = getFile(fileName);
        FileWriter outputStream;

        try {
            Gson gson = new Gson();
            String json = gson.toJson(draftExporterList);
            outputStream = new FileWriter(file);
            outputStream.write(json);
            outputStream.close();
            return Information.SUCCESS;
        } catch (Exception e) {
            return Information.ERROR_SAVE;
        }
    }


    public Information checkPermission(@NonNull Activity context, @NonNull Fragment fragment) {


        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Request for permission
           fragment.requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            return Information.ERROR_NO_PERMISSION;

        }
        return Information.SUCCESS;
    }


    public Information loadToObject(@NonNull List<DraftExporter> draftExporterList) {
        String dataFromFile = null;
        try {
            dataFromFile = loadDataFromFile();
        } catch (FileNotFoundException exception) {
            return Information.ERROR_FILE_NOT_FOUND;
        } catch (IOException e) {
            return Information.ERROR_DATA_CORRUPTED;
        }
        try {
            draftExporterList.addAll(parseDataFromFile(dataFromFile));
            return Information.SUCCESS;
        } catch (JsonSyntaxException error) {
            return Information.ERROR_DATA_CORRUPTED;
        }
    }

    private File getFile(String filename) {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private String loadDataFromFile() throws IOException, FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;
        in = new BufferedReader(new FileReader(getFile(fileName)));
        while ((line = in.readLine()) != null) stringBuilder.append(line);


        return stringBuilder.toString();
    }

    private List<DraftExporter> parseDataFromFile(@NonNull String text) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<DraftExporter>>() {
        }.getType();
        return gson.fromJson(text, listType);
    }
}
