package unii.draft.mtg.parings.buisness.model;


import java.util.ArrayList;
import java.util.List;

import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.util.AlgorithmChooser;

public class AddPlayerModel {

    private static final int LAGGARD = 1;

    private AlgorithmChooser mAlgorithmChooser;
    private List<String> mAddedPlayers;
    private List<Player> mCurrentPlayerList;
    private int pointsForBye;

    public AddPlayerModel(AlgorithmChooser algorithmChooser, int pointForBye) {
        this.mAlgorithmChooser = algorithmChooser;
        this.mAddedPlayers = new ArrayList<>();
        this.mCurrentPlayerList = mAlgorithmChooser.getCurrentAlgorithm().getSortedPlayerList();
        this.pointsForBye = pointForBye;
    }

    public List<String> getAddedPlayers() {
        return mAddedPlayers;
    }

    public List<Player> getCurrentPlayerList() {
        return mCurrentPlayerList;
    }

    public boolean isPlayerNameEmpty(String userName) {
        return userName == null || userName.trim().equals("");
    }

    public boolean isNameUnique(String userName) {
        boolean alreadyAdded = playerAlreadyAdded(userName);
        if (alreadyAdded) {
            return false;
        }

        alreadyAdded = playerPlayDraft(userName, mCurrentPlayerList);
        return !alreadyAdded;
    }

    private boolean playerAlreadyAdded(String userName) {
        for (String addedPlayer : mAddedPlayers) {
            if (addedPlayer.equals(userName)) {
                return true;
            }
        }
        return false;
    }

    private boolean playerPlayDraft(String userName, List<Player> playerList) {
        for (Player currentPlayer : playerList) {
            if (currentPlayer.getPlayerName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public void addPlayerToList(String player) {
        mAddedPlayers.add(player);
    }

    public void addPlayersToDraft() {
        if (mAddedPlayers.isEmpty()) {
            return;
        }
        if (isLaggard()) {
            Player player = addLaggard(mAddedPlayers.get(0));
            mCurrentPlayerList.add(player);
            return;
        } else {
            for (String playerName : mAddedPlayers) {
                Player newPlayer = new Player(playerName);
                mCurrentPlayerList.add(newPlayer);
            }
        }
    }

    /**
     * Who is laggard?
     * Only one person that was late in first round and previous players doesn't have anyone that take a bye
     *
     * @return
     */
    private boolean isLaggard() {
        return mAddedPlayers.size() == LAGGARD && mAlgorithmChooser.getCurrentAlgorithm().getCurrentRound() == LAGGARD && mCurrentPlayerList.size() % 2 == 0;
    }

    private Player addLaggard(String userName) {
        Player laggardPlayer = new Player(userName);
        laggardPlayer.setHasBye(true);
        laggardPlayer.setMatchPoints(pointsForBye);
        return laggardPlayer;
    }
}
