package unii.draft.mtg.parings.buisness.share.draft.detail.player;


import android.support.annotation.Nullable;

import java.util.List;

import unii.draft.mtg.parings.logic.pojo.PlayerDraft;

public interface ISharePlayerAchievements {
    @Nullable
    String playerAchievementsToString(String playerName, List<String> playerOverallPosition, List<PlayerDraft> draftList);
}
