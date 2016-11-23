package unii.draft.mtg.parings.buisness.algorithm;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;

public class AutomaticParingAlgorithm extends BaseAlgorithm {


    public AutomaticParingAlgorithm(Context context) {
        super(context);
    }


    @Override
    public List<Game> getParings() {
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

        for (int i = 0; i < filteredPlayerList.size(); i++) {
            // get only two players name
            // not added last player
            if (addedPlayers.contains(filteredPlayerList.get(i))) {
                Integer paringPartner = null;
                for (int j = 0; j < addedPlayers.size(); j++) {
                    //this player did not play a game yet!
                    if (!addedPlayers.get(j).playedGameWith()
                            .contains(filteredPlayerList.get(i).getPlayerName())
                            && filteredPlayerList.get(i) != addedPlayers.get(j)) {
                        paringPartner = j;
                        break;
                    }
                }
                if (paringPartner != null) {
                    Player player1 = filteredPlayerList.get(i);
                    Player player2 = addedPlayers.get(paringPartner);
                    Game game = new Game(player1.getPlayerName(),
                            player2.getPlayerName());
                    gameList.add(game);
                    addedPlayers.remove(player1);
                    addedPlayers.remove(player2);
                    paringPartner = null;
                } else {
                    //someone played a game before
                    if (addedPlayers.size() % 2 == 0) {
                        Player player1 = filteredPlayerList.get(i);
                        addedPlayers.remove(player1);
                        Player player2 = addedPlayers.get(0);//get first element
                        addedPlayers.remove(player2);
                        Game game = new Game(player1.getPlayerName(), player2.getPlayerName());
                        gameList.add(game);

                    }
                }
            }

        }
        setCurrentRound(getCurrentRound() + 1);
        setRoundList(gameList);
        return gameList;
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
        // swaping with other players
        for (int i = playerPosition; i + 1 < playerList.size(); i++) {
            Collections.swap(playerList, i, i + 1);
        }

    }

}
