package unii.draft.mtg.parings.logic.pojo;


import androidx.annotation.NonNull;

public class Draft {
    private String draftName;
    private String draftDate;
    private Integer draftRounds;
    private Integer numberOfPlayers;
    private String winnerName;

    public Draft(String winnerName, @NonNull unii.draft.mtg.parings.database.model.Draft draft) {
        this.draftDate = draft.getDraftDate();
        this.draftName = draft.getDraftName();
        this.draftRounds = draft.getDraftRounds();
        this.numberOfPlayers = draft.getNumberOfPlayers();
        this.winnerName = winnerName;
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

    public Integer getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public String getWinnerName() {
        return winnerName;
    }
}
