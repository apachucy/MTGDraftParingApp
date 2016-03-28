package unii.draft.mtg.parings.pojo;


/***
 * This class represent a simple game between two players
 *
 * @author Arkadiusz Pachucy
 */
public class Game  {

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

    public Game(String playerA, String playerB) {
        mPlayerAName = playerA;
        mPlayerBName = playerB;
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
}
