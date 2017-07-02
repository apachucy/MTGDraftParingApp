package unii.draft.mtg.parings.buisness.algorithm.automatic;

import android.content.Context;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import unii.draft.mtg.parings.buisness.algorithm.base.BaseAlgorithm;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;
public class AutomaticParingAlgorithm extends BaseAlgorithm {

    public AutomaticParingAlgorithm(Context context) {
        super(context);
    }

    @Override
    public List<Game> getParings() {
        /**
         * If current played round is bigger than finished round
         * load last generated game
         */
        if (getCurrentRound() > playedRound()) {
            return super.getParings();
        }
        List<Player> filteredPlayerList = getFilteredPlayerList(false);
        List<Game> gameList = null;
        if (getCurrentRound() == 0) {
            calculatePairAtStart(filteredPlayerList);

        } else if (getCurrentRound() < getMaxRound()) {
            sortPlayers(filteredPlayerList);

        } else {
            // game should end so return null
            return gameList;
        }
        List<Player> addedPlayers = new ArrayList<Player>();
        addedPlayers.addAll(filteredPlayerList);
        // someone needs bye
        if (filteredPlayerList.size() % 2 == 1) {

            movePlayerWithByeOnLastPosition(filteredPlayerList);
            setPlayerWithBye(filteredPlayerList.get(filteredPlayerList.size() - 1));
            addedPlayers.remove(getPlayerWithBye());

        }
        // Game list should have size players/2
        gameList = new ArrayList<>(filteredPlayerList.size() / 2);
        Generator<Player> allPermutation = generatePermutations(addedPlayers);
        gameList = generateParings(allPermutation, gameList, filteredPlayerList);
        if (gameList == null) {
            gameList = new ArrayList<>(filteredPlayerList.size() / 2);
            gameList = generateParingsWithPlayersOtherThanLastPlayed(allPermutation, gameList, filteredPlayerList);
        }
        setCurrentRound(getCurrentRound() + 1);

        setRoundList(gameList);
        return gameList;
    }

    private List<Game> generateParingsWithPlayersOtherThanLastPlayed(Generator<Player> allPermutation, List<Game> gameList, List<Player> filteredPlayerList) {
        for (ICombinatoricsVector<Player> playersPermutation : allPermutation) {
            gameList.clear();
            List<Player> playerListPermutation = playersPermutation.getVector();
            for (int i = 0; i < filteredPlayerList.size(); i++) {
                // get only two players name
                // not added last player
                if (playerListPermutation.contains(filteredPlayerList.get(i))) {
                    Integer paringPartner = null;
                    for (int j = 0; j < playerListPermutation.size(); j++) {
                        int rounds = playerListPermutation.get(j).playedGameWith().size();
                        if (!playerListPermutation.get(j).playedGameWith().get(rounds - 1)
                                .equals(filteredPlayerList.get(i).getPlayerName())
                                && filteredPlayerList.get(i) != playerListPermutation.get(j)) {
                            paringPartner = j;
                            break;
                        }
                    }
                    if (paringPartner != null) {
                        Player player1 = filteredPlayerList.get(i);
                        Player player2 = playerListPermutation.get(paringPartner);
                        Game game = new Game(player1.getPlayerName(),
                                player2.getPlayerName());
                        gameList.add(game);
                        playerListPermutation.remove(player1);
                        playerListPermutation.remove(player2);
                        if (playerListPermutation.isEmpty()) {
                            return gameList;
                        }
                    }
                }
            }
        }

        return null;
    }

    private List<Game> generateParings
    (Generator<Player> allPermutation, List<Game> gameList, List<Player> filteredPlayerList) {
        for (ICombinatoricsVector<Player> playersPermutation : allPermutation) {
            gameList.clear();
            List<Player> playerListPermutation = playersPermutation.getVector();
            for (int i = 0; i < filteredPlayerList.size(); i++) {
                // get only two players name
                // not added last player
                if (playerListPermutation.contains(filteredPlayerList.get(i))) {
                    Integer paringPartner = null;
                    for (int j = 0; j < playerListPermutation.size(); j++) {
                        //this player did not play a game yet!
                        if (!playerListPermutation.get(j).playedGameWith()
                                .contains(filteredPlayerList.get(i).getPlayerName())
                                && filteredPlayerList.get(i) != playerListPermutation.get(j)) {
                            paringPartner = j;
                            break;
                        }
                    }
                    if (paringPartner != null) {
                        Player player1 = filteredPlayerList.get(i);
                        Player player2 = playerListPermutation.get(paringPartner);
                        Game game = new Game(player1.getPlayerName(),
                                player2.getPlayerName());
                        gameList.add(game);
                        playerListPermutation.remove(player1);
                        playerListPermutation.remove(player2);
                        if (playerListPermutation.isEmpty()) {
                            return gameList;
                        }
                    }
                }
            }
        }
        return null;
    }

    private Generator<Player> generatePermutations(List<Player> playerList) {
        ICombinatoricsVector<Player> originalVector = Factory.createVector(playerList);
        Generator<Player> generatedPermutation = Factory.createPermutationGenerator(originalVector);
        return generatedPermutation;
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
    public void setPlayerGameList(List<Game> playerList) {
        //not implemented
    }

    @Override
    public void setPlayerWithBye(Player playerWithBye) {
        super.setPlayerWithBye(playerWithBye);
    }

    /**
     * Each player has equal point <br>
     * so make parings at random
     */

    private void calculatePairAtStart(List<Player> playerList) {
        Random random = new Random();
        // swap element at random
        for (int i = 0; i < getDefaultRandomSeed(); i++) {
            int swapA = random.nextInt(playerList.size());
            int swapB = random.nextInt(playerList.size());
            Collections.swap(playerList, swapA, swapB);
        }
    }

    /**
     * sort list of players <br>
     * and move player<br>
     * with bye to last position
     */
    private void movePlayerWithByeOnLastPosition(List<Player> playerList) {

        int playerPosition = 0;
        for (int i = playerList.size() - 1; i >= 0; i--) {
            if (!playerList.get(i).hasBye()) {
                playerList.get(i).setHasBye(true);
                setPlayerWithBye(playerList.get(i));
                playerPosition = i;
                break;
            }
        }
        // move player with bye on last position
        // swapping with other players
        for (int i = playerPosition; i + 1 < playerList.size(); i++) {
            Collections.swap(playerList, i, i + 1);
        }

    }

}
