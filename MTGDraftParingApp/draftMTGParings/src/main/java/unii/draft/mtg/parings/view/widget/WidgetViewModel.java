package unii.draft.mtg.parings.view.widget;


import java.io.Serializable;

public class WidgetViewModel implements Serializable {
    private int mCurrentRound;
    private String mWinningPlayer;

    public int getCurrentRound() {
        return mCurrentRound;
    }

    public void setCurrentRound(int mCurrentRound) {
        this.mCurrentRound = mCurrentRound;
    }

    public String getWinningPlayer() {
        return mWinningPlayer;
    }

    public void setWinningPlayer(String winningPlayer) {
        this.mWinningPlayer = winningPlayer;
    }
}
