package unii.draft.mtg.parings.logic.pojo;


import java.util.List;

public class DraftDataProvider {
    private List<Player> mPlayerList;
    private int mDefaultRandomSeed;
    private int mMaxRounds;
    /**
     * Finished rounds
     */
    private int mPlayedRound;
    /**
     * Current played round
     */
    private int mCurrentRound;
    private Player mPlayerWithBye;
    private List<Game> mParingGameList;
    private boolean isInitialized;

    public DraftDataProvider() {
        isInitialized = false;
        mPlayedRound = 0;
        mCurrentRound = 0;
    }

    public boolean isObjectInitialized() {
        return isInitialized;
    }

    public void objectInitialized() {
        isInitialized = true;
    }

    public void setPlayerList(List<Player> mPlayerList) {
        this.mPlayerList = mPlayerList;
    }

    public List<Player> getPlayerList() {
        return mPlayerList;
    }

    public int getDefaultRandomSeed() {
        return mDefaultRandomSeed;
    }

    public void setDefaultRandomSeed(int mDefaultRandomSeed) {
        this.mDefaultRandomSeed = mDefaultRandomSeed;
    }

    public int getMaxRound() {
        return mMaxRounds;
    }

    public void setMaxRound(int mMaxRounds) {
        this.mMaxRounds = mMaxRounds;
    }

    public int getCurrentRound() {
        return mCurrentRound;
    }

    public void setCurrentRound(int mCurrentRound) {
        this.mCurrentRound = mCurrentRound;
    }

    public Player getPlayerWithBye() {
        return mPlayerWithBye;
    }

    public void setPlayerWithBye(Player mPlayerWithBye) {
        this.mPlayerWithBye = mPlayerWithBye;
    }

    public List<Game> getParingGameList() {
        return mParingGameList;
    }

    public void setParingGameList(List<Game> mParingGameList) {
        this.mParingGameList = mParingGameList;
    }

    public int getPlayedRound() {
        return mPlayedRound;
    }

    public void setPlayedRound(int mPlayedRound) {
        this.mPlayedRound = mPlayedRound;
    }
}
