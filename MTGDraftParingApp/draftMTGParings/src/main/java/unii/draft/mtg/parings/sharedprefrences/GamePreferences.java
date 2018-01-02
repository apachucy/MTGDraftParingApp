package unii.draft.mtg.parings.sharedprefrences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import unii.draft.mtg.parings.logic.pojo.DraftDataProvider;
import unii.draft.mtg.parings.util.config.GamePreferencesConst;


public class GamePreferences implements IGamePreferences {
    private SharedPreferences mSharedPreferences;
    @NonNull
    private final Context mContext;
    private static final String SHARED_PREFERENCES_NAME = GamePreferencesConst.GAME_PREFERENCES_NAME;

    public GamePreferences(@NonNull Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }


    @Override
    public boolean saveDraftDataProvider(DraftDataProvider draftDataProvider) {
        Gson gson = new Gson();
        String json = gson.toJson(draftDataProvider);
        if (json == null) {
            return false;
        }

        mSharedPreferences.edit().putString(GamePreferencesConst.CURRENT_DRAFT_STATUS, json).commit();
        return true;
    }

    @Nullable
    @Override
    public DraftDataProvider getDraftDataProvider() {
        Gson gson = new Gson();
        String json = mSharedPreferences.getString(GamePreferencesConst.CURRENT_DRAFT_STATUS, null);
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, DraftDataProvider.class);
    }

    @Override
    public boolean isEmpty() {
        String draftStatus = mSharedPreferences.getString(GamePreferencesConst.CURRENT_DRAFT_STATUS, null);
        return draftStatus == null || draftStatus.isEmpty();
    }

    @Override
    public void clean() {
         mSharedPreferences.edit().clear().commit();
    }
}
