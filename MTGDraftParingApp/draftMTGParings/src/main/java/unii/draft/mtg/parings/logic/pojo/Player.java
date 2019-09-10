package unii.draft.mtg.parings.logic.pojo;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import unii.draft.mtg.parings.database.model.PlayerDraftJoinTable;
import unii.draft.mtg.parings.util.config.BaseConfig;
import unii.draft.mtg.parings.view.adapters.IAdapterItem;

public class Player implements IAdapterItem {

    private List<Game> mPlayedGame;

    /**
     * Player name
     */
    private String mPlayerName;
    /**
     * How many match points player has - main points
     */
    private float mMatchPoints;
    /**
     * How many game point player has
     */
    private float mGamePoints;
    /**
     * Did player has win the game with "bye"
     */
    private boolean hasBye;

    /**
     * Counting how many bye player had
     */
    private int mGameWithBye;
    /**
     * Match Overall win in draft - should not be less than 0.33
     */
    private float mPlayerMatchOverallWin;

    /**
     * Match overall wins by oponents in draft - should not be less than 0.33
     */
    private float mOponentsMatchOveralWins;

    /**
     * Overall games win % Match contain games
     */
    private float mPlayerGamesOverallWin;
    /**
     * Overall game win by oponents %
     */
    private float mOponentsGamesOverallWin;

    /**
     * Set to true if player mDropped a draft
     */
    private boolean mDropped;

    public Player(Player player) {
        this.mPlayedGame = new ArrayList<>();

        this.mPlayerName = player.getPlayerName();
        this.mGamePoints = player.getGamePoints();
        this.mMatchPoints = player.getMatchPoints();
        this.hasBye = player.hasBye();
        this.mPlayerMatchOverallWin = player.getPlayerMatchOverallWin();
        this.mOponentsMatchOveralWins = player.getOponentsMatchOveralWins();
        this.mPlayerGamesOverallWin = player.getPlayerGamesOverallWin();
        this.mOponentsGamesOverallWin = player.getOponentsGamesOverallWin();
        this.mDropped = player.isDropped();
        this.mGameWithBye = player.getNumberOfGameWithBye();

        if (player.getPlayedGame() == null) {
            return;
        }
        for (Game game : player.getPlayedGame()) {
            this.mPlayedGame.add(new Game(game));
        }
    }

    public Player(@NonNull unii.draft.mtg.parings.database.model.Player player, @NonNull PlayerDraftJoinTable playerDraftJoinTable) {
        mPlayerName = player.getPlayerName();

        mPlayerMatchOverallWin = playerDraftJoinTable.getPlayerMatchOverallWin();
        mPlayerGamesOverallWin = playerDraftJoinTable.getPlayerGamesOverallWin();
        mOponentsGamesOverallWin = playerDraftJoinTable.getOponentsGamesOverallWin();
        mOponentsMatchOveralWins = playerDraftJoinTable.getOponentsMatchOveralWins();
        mMatchPoints = playerDraftJoinTable.getPlayerMatchPoints();
        mDropped = playerDraftJoinTable.getDropped();
        mGameWithBye = 0;
    }


    public Player(String playerName) {
        mPlayerName = playerName;
        mPlayedGame = new ArrayList<Game>();
        mMatchPoints = 0;
        hasBye = false;
        mDropped = false;
        mPlayerMatchOverallWin = BaseConfig.MIN_OVERALL_VALUE;
        mOponentsMatchOveralWins = BaseConfig.MIN_OVERALL_VALUE;
        mPlayerGamesOverallWin = BaseConfig.MIN_OVERALL_VALUE;
        mOponentsGamesOverallWin = BaseConfig.MIN_OVERALL_VALUE;
        mGameWithBye = 0;
    }

    public float getMatchPoints() {
        return mMatchPoints;
    }

    public void setMatchPoints(float mPoints) {
        this.mMatchPoints = mPoints;
    }

    public boolean hasBye() {
        return hasBye;
    }

    public void setHasBye(boolean hasBye) {
        this.hasBye = hasBye;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public void setPlayerName(String mPlayerName) {
        this.mPlayerName = mPlayerName;
    }

    public List<Game> getPlayedGame() {
        return mPlayedGame;
    }

    public void setPlayedGame(List<Game> mPlayedGame) {
        this.mPlayedGame = mPlayedGame;
    }

    public void addPlayedGame(Game game) {
        mPlayedGame.add(game);
    }

    @NonNull
    public List<String> playedGameWith() {
        List<String> playedGameWith = new ArrayList<String>();

        for (Game g : mPlayedGame) {
            if (!g.getPlayerNameA().equals(mPlayerName)) {
                // if names are different add this player to
                // played list
                playedGameWith.add(g.getPlayerNameA());
            } else {
                playedGameWith.add(g.getPlayerNameB());
            }
        }
        return playedGameWith;

    }

    public float getGamePoints() {
        return mGamePoints;
    }

    public void setGamePoints(float mGamePoints) {
        this.mGamePoints = mGamePoints;
    }

    public float getPlayerMatchOverallWin() {
        return mPlayerMatchOverallWin;
    }

    public void setPlayerMatchOverallWin(float mPlayerMatchOverallWin) {
        this.mPlayerMatchOverallWin = mPlayerMatchOverallWin;
    }

    public float getOponentsMatchOveralWins() {
        return mOponentsMatchOveralWins;
    }

    public void setOponentsMatchOveralWins(float mOponentsMatchOveralWins) {
        this.mOponentsMatchOveralWins = mOponentsMatchOveralWins;
    }

    public float getPlayerGamesOverallWin() {
        return mPlayerGamesOverallWin;
    }

    public void setPlayerGamesOverallWin(float mPlayerGamesOverallWin) {
        this.mPlayerGamesOverallWin = mPlayerGamesOverallWin;
    }

    public float getOponentsGamesOverallWin() {
        return mOponentsGamesOverallWin;
    }

    public void setOponentsGamesOverallWin(float mOponentsGamesOverallWin) {
        this.mOponentsGamesOverallWin = mOponentsGamesOverallWin;
    }


    @Override
    public int getItemType() {
        return ItemType.ITEM;
    }


    public boolean isDropped() {
        return mDropped;
    }

    public void setDropped(boolean dropped) {
        this.mDropped = dropped;
    }

    public int getNumberOfGameWithBye() {
        return mGameWithBye;
    }

    public void setNumberOfGameWithBye(int gamesWithBye) {
        mGameWithBye = gamesWithBye;
    }

    public void increaseBye() {
        mGameWithBye++;
    }

}
