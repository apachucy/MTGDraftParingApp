package unii.draft.mtg.parings.sharedprefrences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import unii.draft.mtg.parings.logic.pojo.DraftDataProvider;
import unii.draft.mtg.parings.util.config.GamePreferencesConst;


public class GamePreferences implements IGamePreferences {
    private SharedPreferences mSharedPreferences;
    private final Context mContext;
    private static final String SHARED_PREFERENCES_NAME = GamePreferencesConst.GAME_PREFERENCES_NAME;

    public GamePreferences(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }


    @Override
    public void saveDraftDataProvider(DraftDataProvider draftDataProvider) {
        Gson gson = new Gson();

        String json = gson.toJson(draftDataProvider);
        mSharedPreferences.edit().putString(GamePreferencesConst.CURRENT_DRAFT_STATUS, json).apply();
    }

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
    public void clean() {
        mSharedPreferences.edit().clear().apply();
    }
}
