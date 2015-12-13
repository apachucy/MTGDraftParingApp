package unii.draft.mtg.parings.pojo;

import unii.draft.mtg.parings.view.adapters.IAdapterItem;

/***
 * This class represent a simple game between two players
 *
 * @author Arkadiusz Pachucy
 */
public class Game implements IAdapterItem {

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

    /**
     * In case of a draw
     */
    private boolean isDraw;

    public Game(String playerA, String playerB) {
        mPlayerAName = playerA;
        mPlayerBName = playerB;
        isDraw = false;
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

    public boolean getDraw() {
        return isDraw;
    }

    public void setDraw(boolean isDraw) {
        this.isDraw = isDraw;
    }

    @Override
    public int getItemType() {
        return ItemType.ITEM;
    }
}
