package unii.draft.mtg.parings.buisness.share.draft.list;


import java.util.List;

import unii.draft.mtg.parings.logic.pojo.PlayerAchievements;

public interface ISharePlayerHistoryList {
    String getPlayerListToString(List<PlayerAchievements> list);
}
