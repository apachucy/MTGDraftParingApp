package unii.draft.mtg.parings.buisness.algorithm.base;

import android.content.Context;
import android.support.annotation.NonNull;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;

public class SemiAlgorithm extends BaseAlgorithm {
    protected final static int BYE_COUNTER_MIN_VALUE = 999;
    protected final static int NOT_FOUND = -1;


    public SemiAlgorithm(Context context) {
        super(context);
    }


    @Override
    public List<Player> getSortedPlayerList() {
        List<Player> playerList = getDraftStartedPlayerList();
        sortPlayers(playerList);
        return playerList;
    }

    @NonNull
    @Override
    public List<Player> getSortedFilteredPlayerList(boolean dropped) {
        List<Player> playerList = getFilteredPlayerList(dropped);
        sortPlayers(playerList);
        return playerList;
    }

    @Override
    public void setPlayerGameList(List<Game> playerList) {

    }


    protected List<Game> generateParingsWithPlayersOtherThanLastPlayed(@NonNull Generator<Player> allPermutation, @NonNull List<Game> gameList, @NonNull List<Player> filteredPlayerList) {
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
                                player2.getPlayerName(), getCurrentRound() + 1);
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

    protected List<Game> generateParings
            (@NonNull Generator<Player> allPermutation, @NonNull List<Game> gameList, @NonNull List<Player> filteredPlayerList) {
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
                                player2.getPlayerName(), getCurrentRound() + 1);
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

    @NonNull
    protected Generator<Player> generatePermutations(@NonNull List<Player> playerList) {
        ICombinatoricsVector<Player> originalVector = Factory.createVector(playerList);
        Generator<Player> generatedPermutation = Factory.createPermutationGenerator(originalVector);
        return generatedPermutation;
    }


    @Override
    public void setPlayerWithBye(Player playerWithBye) {
        super.setPlayerWithBye(playerWithBye);
    }

    /**
     * Each player has equal point <br>
     * so make parings at random
     */

    protected void calculatePairAtStart(@NonNull List<Player> playerList) {
        Random random = new Random();
        // swap element at random
        for (int i = 0; i < getDefaultRandomSeed(); i++) {
            int swapA = random.nextInt(playerList.size());
            int swapB = random.nextInt(playerList.size());
            Collections.swap(playerList, swapA, swapB);
        }
    }

    protected void sittingsPairingAtStart(@NonNull List<Player> playerList) {
        //If there are no more than 3 players this sittings are useless
        if (playerList.size() < 3) {
            return;
        }
        int pivot = playerList.size() / 2;
        List<Player> firstSubList = new ArrayList<>(playerList.subList(0, pivot));
        List<Player> secondSubList = new ArrayList<>(playerList.subList(pivot + 1, playerList.size() - 1));
        playerList.clear();

        for (int i = 0; i < secondSubList.size(); i++) {
            if (i < firstSubList.size()) {
                playerList.add(firstSubList.get(i));
            }
            if (i < secondSubList.size()) {
                playerList.add(secondSubList.get(i));
            }
        }
    }


    /**
     * sort list of players <br>
     * and move player<br>
     * with bye to last position
     */
    protected void movePlayerWithByeOnLastPosition(@NonNull List<Player> playerList) {

        int playerPosition = positionOfPlayerWithoutBye(playerList);

        if (playerPosition == NOT_FOUND) {
            int smallestBye = smallestByeNumber(playerList);
            playerPosition = positionOfPlayerWithoutBye(playerList, smallestBye);
        }

        // move player with bye on last position
        // swapping with other players
        for (int i = playerPosition; i + 1 < playerList.size(); i++) {
            Collections.swap(playerList, i, i + 1);
        }

    }


    private int positionOfPlayerWithoutBye(@NonNull List<Player> playerList, int smallestByeNumber) {
        int playerPosition = NOT_FOUND;
        for (int i = playerList.size() - 1; i >= 0; i--) {
            if (playerList.get(i).getNumberOfGameWithBye() == smallestByeNumber) {
                setByeForPlayer(playerList.get(i));
                playerPosition = i;
                return playerPosition;
            }
        }
        return playerPosition;
    }

    private int smallestByeNumber(@NonNull List<Player> playerList) {

        int byeNumber = BYE_COUNTER_MIN_VALUE;
        for (int i = playerList.size() - 1; i >= 0; i--) {
            int playersBye = playerList.get(i).getNumberOfGameWithBye();
            if (playerList.get(i).getNumberOfGameWithBye() < byeNumber) {
                byeNumber = playersBye;
            }
        }
        return byeNumber;
    }

    private int positionOfPlayerWithoutBye(@NonNull List<Player> playerList) {
        int playerPosition = NOT_FOUND;
        for (int i = playerList.size() - 1; i >= 0; i--) {
            if (!playerList.get(i).hasBye()) {
                setByeForPlayer(playerList.get(i));
                playerPosition = i;
                return playerPosition;
            }
        }
        return playerPosition;
    }

    private void setByeForPlayer(Player player) {
        player.setHasBye(true);
        player.increaseBye();
        setPlayerWithBye(player);
    }
}
