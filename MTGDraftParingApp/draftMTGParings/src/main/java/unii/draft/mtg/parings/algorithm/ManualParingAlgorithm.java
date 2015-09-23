package unii.draft.mtg.parings.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import unii.draft.mtg.parings.pojo.Game;
import unii.draft.mtg.parings.pojo.Player;

/**
 * Created by Arkadiusz Pachucy on 2015-07-14.
 */
public class ManualParingAlgorithm implements IParingAlgorithm {
    public static int DEFAULT_RANDOM_SEED;
    private List<Player> mPlayers;
    private int mMaxRounds;
    private int mCurrentRound;
    private Player mPlayerWithBye;

    private List<Game> mParingGameList;

    /**
     *
     * @param players
     *            list of players
     * @param rounds
     *            max number of played rounds
     */
    public ManualParingAlgorithm(List<String> players, int rounds){
        populatePlayersList(players);
        mMaxRounds = rounds;
        mCurrentRound = 0;
        DEFAULT_RANDOM_SEED = players.size();
        mPlayerWithBye = null;

    }

    private void populatePlayersList(List<String> players) {
        mPlayers = new ArrayList<Player>();
        for (String playerName : players) {
            Player player = new Player(playerName);
            mPlayers.add(player);
        }
    }

    /**
     * Sort players using comparator {@link PlayersComparator}
     */
    private void sortPlayers() {
        Collections.sort(mPlayers, new PlayersComparator());
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
        sortPlayers();
        return mPlayers;
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
        mPlayerWithBye= playerWithBye;
        mPlayers.get(mPlayers.indexOf(mPlayerWithBye)).setHasBye(true);
    }
}
