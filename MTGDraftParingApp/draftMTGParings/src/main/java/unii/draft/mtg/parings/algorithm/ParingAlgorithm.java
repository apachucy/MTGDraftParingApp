package unii.draft.mtg.parings.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import unii.draft.mtg.parings.config.BaseConfig;
import unii.draft.mtg.parings.pojo.Game;
import unii.draft.mtg.parings.pojo.Player;

public class ParingAlgorithm implements IParingAlgorithm {
    public static int DEFAULT_RANDOM_SEED;
    private List<Player> mPlayers;
    private int mMaxRounds;
    private int mCurrentRound;
    private Player mPlayerWithBye;

    /**
     * @param players list of players
     * @param rounds  max number of played rounds
     */
    public ParingAlgorithm(List<String> players, int rounds) {
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
     * Each player has equal point <br>
     * so make parings at random
     *
     * @return pairTable
     */
    private void calculatePairAtStart() {
        Random random = new Random();
        // swap element at random
        for (int i = 0; i < DEFAULT_RANDOM_SEED; i++) {
            int swapA = random.nextInt(mPlayers.size());
            int swapB = random.nextInt(mPlayers.size());
            Collections.swap(mPlayers, swapA, swapB);
        }
    }

    /**
     * Sort players using comparator {@link PlayersComparator}
     */
    private void sortPlayers() {
        Collections.sort(mPlayers, new PlayersComparator());
    }

    /**
     * sort list of players <br>
     * and move player<br>
     * with bye to last position
     */
    private void movePlayerWithByeOnLastPosition() {

        int playerPosition = 0;
        boolean firstPlayerWithoutBye = false;
        for (int i = mPlayers.size() - 1; i >= 0; i--) {
            if (!mPlayers.get(i).hasBye() && !firstPlayerWithoutBye) {
                mPlayers.get(i).setHasBye(true);
                firstPlayerWithoutBye = true;
                mPlayerWithBye = mPlayers.get(i);
                playerPosition = i;
                break;
            }
        }

        // move player with bye on last position
        // swaping with other players
        for (int i = playerPosition; i + 1 < mPlayers.size(); i++) {
            Collections.swap(mPlayers, i, i + 1);
        }

    }

    @Override
    public List<Game> getParings() {
        List<Game> gameList = null;
        if (mCurrentRound == 0) {
            calculatePairAtStart();

        } else if (mCurrentRound < mMaxRounds) {
            sortPlayers();

        } else {
            // game should end so return null
            return gameList;
        }
        List<Player> addedPlayers = new ArrayList<Player>();
        addedPlayers.addAll(mPlayers);
        // someone needs bye
        if (mPlayers.size() % 2 == 1) {

            movePlayerWithByeOnLastPosition();
            mPlayerWithBye = mPlayers.get(mPlayers.size() - 1);
            addedPlayers.remove(mPlayerWithBye);

        }
        // Game list should have size players/2
        gameList = new ArrayList<Game>(mPlayers.size() / 2);

        for (int i = 0; i < mPlayers.size(); i++) {
            // get only two players name
            // not added last player
            if (addedPlayers.contains(mPlayers.get(i))) {
                Integer paringPartner = null;
                for (int j = 0; j < addedPlayers.size(); j++) {
                    if (!addedPlayers.get(j).playedGameWith()
                            .contains(mPlayers.get(i).getPlayerName())
                            && mPlayers.get(i) != addedPlayers.get(j)) {
                        paringPartner = j;
                        break;
                    }
                }
                if (paringPartner != null) {
                    Player player1 = mPlayers.get(i);
                    Player player2 = addedPlayers.get(paringPartner);
                    Game game = new Game(player1.getPlayerName(),
                            player2.getPlayerName());
                    gameList.add(game);
                    addedPlayers.remove(player1);
                    addedPlayers.remove(player2);
                    paringPartner = null;
                } else {
                    if (addedPlayers.size() % 2 == 0) {
                        Player player1 = mPlayers.get(i);
                        addedPlayers.remove(player1);
                        Player player2 = addedPlayers.get(0);//get first element
                        addedPlayers.remove(player2);
                        Game game = new Game(player1.getPlayerName(), player2.getPlayerName());
                        gameList.add(game);
                    }
                }
            }

        }

        mCurrentRound++;

        return gameList;
    }

    @Override
    public int getCurrentRound() {
        return mCurrentRound;
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
        //not implemented

    }

    @Override
    public void setPlayerWithBye(Player playerWithBye) {
        //not implemented

    }


    @Override
    public int getMaxRound() {
        return mMaxRounds;
    }


}
