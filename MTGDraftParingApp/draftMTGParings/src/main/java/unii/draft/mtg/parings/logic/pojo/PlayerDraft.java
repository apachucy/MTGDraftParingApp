package unii.draft.mtg.parings.logic.pojo;


public class PlayerDraft {
    //TODO: Add banding - witch player
    //id? or something else
    private int mPlace;
    private int mTotalPlayers;
    private String mDraftName;

    public PlayerDraft(String draftName, int place, int totalPlayers) {
        this.mPlace = place;
        this.mTotalPlayers = totalPlayers;
        this.mDraftName = draftName;
    }

    public int getPlace() {
        return mPlace;
    }

    public int getTotalPlayers() {
        return mTotalPlayers;
    }

    public String getDraftName() {
        return mDraftName;
    }
}
