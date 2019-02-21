package unii.draft.mtg.parings.database.populate;

import android.support.annotation.NonNull;

import java.util.List;

import unii.draft.mtg.parings.logic.pojo.Player;

public class DraftExporter {
    private List<Player> playerList;
    private String draftName;
    private String draftDate;
    private Integer draftRounds;

    public DraftExporter(@NonNull List<Player> playerList, @NonNull String draftName, @NonNull String draftDate, Integer draftRounds) {
        this.playerList = playerList;
        this.draftName = draftName;
        this.draftDate = draftDate;
        this.draftRounds = draftRounds;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public String getDraftName() {
        return draftName;
    }

    public String getDraftDate() {
        return draftDate;
    }

    public Integer getDraftRounds() {
        return draftRounds;
    }


}
