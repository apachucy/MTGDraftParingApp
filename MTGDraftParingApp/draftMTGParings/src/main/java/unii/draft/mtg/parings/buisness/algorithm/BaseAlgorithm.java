package unii.draft.mtg.parings.buisness.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import unii.draft.mtg.parings.logic.pojo.Player;

/**
 * Root algorithm for manual and generated parings
 */
public abstract class BaseAlgorithm implements IParingAlgorithm {
    private List<Player> mDraftStartedPlayers;

    @Override
    public void startAlgorithm(List<String> playerNames, int rounds) {
        populatePlayersList(playerNames);
    }


    public void setDraftStartedPlayersList(List<Player> playerList) {
        this.mDraftStartedPlayers = playerList;
    }

    public List<Player> getDraftStartedPlayerList() {
        return this.mDraftStartedPlayers;
    }

    public List<Player> getFilteredPlayerList(boolean dropped) {
        List<Player> filteredPlayerList = new ArrayList<>();
        for (Player player : this.mDraftStartedPlayers) {
            if (player.isDropped() == dropped) {
                filteredPlayerList.add(player);
            }
        }
        return filteredPlayerList;
    }

    /**
     * Sort players using comparator {@link PlayersComparator}
     */
    protected void sortPlayers(List<Player> playerList) {
        Collections.sort(playerList, new PlayersComparator());
    }

    private void populatePlayersList(List<String> players) {
        mDraftStartedPlayers = new ArrayList<Player>();
        for (String playerName : players) {
            Player player = new Player(playerName);
            mDraftStartedPlayers.add(player);
        }
    }

    @Override
    public Player getPlayer(String playerName) {
        for (Player player : mDraftStartedPlayers) {
            if (player.getPlayerName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }

}
