package unii.draft.mtg.parings.logic.pojo;


import androidx.annotation.NonNull;

import java.util.Map;

public class PlayerAchievements {
    private Long mPlayerId;
    private String mPlayerName;
    private Map<Integer, Integer> mPlaceInDrafts;
    private Integer mDraftPlayed;

    public PlayerAchievements(@NonNull unii.draft.mtg.parings.database.model.Player player, Map<Integer, Integer> placeInDrafts, Integer draftPlayed) {
        this.mPlayerId = player.getId();
        this.mPlayerName = player.getPlayerName();
        this.mPlaceInDrafts = placeInDrafts;
        this.mDraftPlayed = draftPlayed;
    }

    public Long getPlayerId() {
        return mPlayerId;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public Map<Integer, Integer> getPlaceInDrafts() {
        return mPlaceInDrafts;
    }

    public Integer getDraftPlayed() {
        return mDraftPlayed;
    }
}
