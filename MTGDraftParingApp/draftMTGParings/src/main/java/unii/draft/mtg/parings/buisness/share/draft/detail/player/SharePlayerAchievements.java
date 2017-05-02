package unii.draft.mtg.parings.buisness.share.draft.detail.player;

import android.content.Context;

import java.util.List;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.PlayerDraft;


public class SharePlayerAchievements implements ISharePlayerAchievements {
    private Context mContext;

    public SharePlayerAchievements(Context context) {
        mContext = context;
    }

    @Override
    public String playerAchievementsToString(String playerName, List<String> playerOverallPosition, List<PlayerDraft> draftList) {
        if (playerName == null || playerName.isEmpty() ||
                playerOverallPosition == null || playerOverallPosition.isEmpty() ||
                draftList == null || draftList.isEmpty()) {
            return null;
        }
        return playerName +"\n"+ playerOverallPositionToString(playerOverallPosition)+ " "+ draftListToString(draftList);

    }

    private String playerOverallPositionToString(List<String> playerOverallPosition) {
        String position = "";
        for (String item : playerOverallPosition) {
            position += item + "\n";
        }
        return position;
    }

    private String draftListToString(List<PlayerDraft> draftList) {
        String draft = "";

        for (PlayerDraft item : draftList) {
            draft += item.getDraftName() + " " + mContext.getString(R.string.history_player_detail_place,
                    item.getPlace(), item.getTotalPlayers()) + "\n";
        }
        return draft;
    }
}
