package unii.draft.mtg.parings.buisness.share.draft.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.PlayerAchievements;

public class SharePlayerAchievementList implements ISharePlayerHistoryList {
    private static final int DRAFT_PLACE_1 = 1;
    private Context mContext;

    public SharePlayerAchievementList(Context context) {
        mContext = context;
    }


    @Nullable
    @Override
    public String getPlayerListToString(@Nullable List<PlayerAchievements> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        String playerAchievementsListToString = "";
        for (PlayerAchievements playerAchievements : list) {
            playerAchievementsListToString += playerAchievementToString(playerAchievements);
        }
        return playerAchievementsListToString;
    }

    private String playerAchievementToString(@NonNull PlayerAchievements player) {
        int wins = 0;
        if (player.getPlaceInDrafts().containsKey(DRAFT_PLACE_1)) {
            wins = player.getPlaceInDrafts().get(DRAFT_PLACE_1);
        }
        return mContext.getString(R.string.shared_data_player_list, player.getPlayerName(), player.getDraftPlayed(), wins);
    }
}
