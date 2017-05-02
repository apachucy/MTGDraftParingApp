package unii.draft.mtg.parings.buisness.share.draft.detail.player;


import java.util.List;

import unii.draft.mtg.parings.logic.pojo.PlayerDraft;

public interface ISharePlayerAchievements {
    String playerAchievementsToString(String playerName, List<String> playerOverallPosition, List<PlayerDraft> draftList);
}
