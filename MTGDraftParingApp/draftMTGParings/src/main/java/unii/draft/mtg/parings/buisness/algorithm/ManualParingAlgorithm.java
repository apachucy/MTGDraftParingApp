package unii.draft.mtg.parings.buisness.algorithm;

import java.util.List;

import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;

/**
 * Created by Arkadiusz Pachucy on 2015-07-14.
 */
public class ManualParingAlgorithm extends BaseAlgorithm {
    public static int DEFAULT_RANDOM_SEED;

    private int mMaxRounds;
    private int mCurrentRound;
    private Player mPlayerWithBye;
    private List<Game> mParingGameList;

    /**
     * @param playerNames list of players
     * @param rounds  max number of played rounds
     */
    @Override
    public void startAlgorithm(List<String> playerNames, int rounds) {
        super.startAlgorithm(playerNames, rounds);
        mMaxRounds = rounds;
        mCurrentRound = 0;
        DEFAULT_RANDOM_SEED = playerNames.size();
        mPlayerWithBye = null;


    }

    @Override
    public List<Game> getParings() {
        mCurrentRound++;
        return mParingGameList;
    }

    @Override
    public int getCurrentRound() {
        return mCurrentRound;
    }

    @Override
    public int getMaxRound() {
        return mMaxRounds;
    }

    @Override
    public List<Player> getSortedPlayerList() {
        List<Player> playerList = getDraftStartedPlayerList();
        sortPlayers(playerList);
        return playerList;
    }

    @Override
    public List<Player> getSortedFilteredPlayerList(boolean dropped) {
        List<Player> playerList = getFilteredPlayerList(dropped);
        sortPlayers(playerList);
        return playerList;
    }

    @Override
    public Player getPlayerWithBye() {
        return mPlayerWithBye;
    }

    @Override
    public void setPlayerGameList(List<Game> playerList) {
        mParingGameList = playerList;
    }

    @Override
    public void setPlayerWithBye(Player playerWithBye) {
        mPlayerWithBye = playerWithBye;
        getDraftStartedPlayerList().get(getDraftStartedPlayerList().indexOf(mPlayerWithBye)).setHasBye(true);
    }
}
