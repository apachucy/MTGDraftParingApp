package unii.draft.mtg.parings.logic.pojo;


/***
 * This class represent a simple game between two players
 *
 * @author Arkadiusz Pachucy
 */
public class Game {

    /**
     * Game status between two players
     */
    private String mWinner;

    /**
     * PlayerA
     */
    private String mPlayerAName;
    /**
     * PlayerB
     */
    private String mPlayerBName;
    /**
     * PlayerA game points
     */
    private int mPlayerAPoints;
    /**
     * PlayerB game points
     */
    private int mPlayerBPoints;
    private int mGamesPlayed;
    /**
     * In case of a draw 0/1/2
     */
    private int mMatchDraws;

    private int mRound;


    public Game(unii.draft.mtg.parings.database.model.Game game, String playerAName, String playerBName) {
        this.mWinner = game.getWinner();
        this.mGamesPlayed = game.getGames();
        this.mPlayerAName = playerAName;
        this.mPlayerBName = playerBName;
        this.mPlayerAPoints = game.getPlayerAPoints();
        this.mPlayerBPoints = game.getPlayerBPoints();
        this.mMatchDraws = game.getDraws();
        this.mRound = game.getRound();
    }

    public Game(Game game) {
        this.mWinner = game.getWinner();
        this.mGamesPlayed = game.getGamesPlayed();
        this.mPlayerAName = game.getPlayerNameA();
        this.mPlayerBName = game.getPlayerNameB();
        this.mPlayerAPoints = game.getPlayerAPoints();
        this.mPlayerBPoints = game.getPlayerBPoints();
        this.mMatchDraws = game.getDraws();
        this.mRound = game.getRound();
    }

    public Game(String playerA, String playerB, int round) {
        mPlayerAName = playerA;
        mPlayerBName = playerB;
        mRound = round;
    }

    public String getWinner() {
        return mWinner;
    }

    public void setWinner(String winner) {
        this.mWinner = winner;
    }

    public String getPlayerNameA() {
        return mPlayerAName;
    }

    public void setPlayerNameA(String playerA) {
        mPlayerAName = playerA;
    }

    public String getPlayerNameB() {
        return mPlayerBName;
    }

    public void setPlayerNameB(String playerB) {
        mPlayerBName = playerB;
    }

    public int getPlayerAPoints() {
        return mPlayerAPoints;
    }

    public void setPlayerAPoints(int mPlayerAPoints) {
        this.mPlayerAPoints = mPlayerAPoints;
    }

    public int getPlayerBPoints() {
        return mPlayerBPoints;
    }

    public void setPlayerBPoints(int mPlayerBPoints) {
        this.mPlayerBPoints = mPlayerBPoints;
    }


    public int getDraws() {
        return mMatchDraws;
    }

    public void setDraws(int mDraws) {
        this.mMatchDraws = mDraws;
    }

    public boolean isGameADraw() {
        return (mMatchDraws >= maxPoints(mPlayerAPoints, mPlayerBPoints));
    }

    private int maxPoints(int pointsA, int pointsB) {
        if (pointsA >= pointsB) {
            return pointsA;
        } else {
            return pointsB;
        }
    }

    public int getGamesPlayed() {
        return mGamesPlayed;
    }

    public void setGamesPlayed(int mGamesPlayed) {
        this.mGamesPlayed = mGamesPlayed;
    }

    public int getRound() {
        return mRound;
    }
}
