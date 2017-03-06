package unii.draft.mtg.parings.buisness.algorithm.base;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import unii.draft.mtg.parings.logic.pojo.DraftDataProvider;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.sharedprefrences.GamePreferences;
import unii.draft.mtg.parings.sharedprefrences.IGamePreferences;

/**
 * Root algorithm for manual and generated parings
 */
public abstract class BaseAlgorithm implements IParingAlgorithm, IApplicationDraftMemoryState {
    private DraftDataProvider mDraftDataProvider;

    private IGamePreferences mGamePreferences;


    public BaseAlgorithm(Context context) {
        mGamePreferences = new GamePreferences(context);
        mDraftDataProvider = new DraftDataProvider();
    }

    public int getDefaultRandomSeed() {
        return mDraftDataProvider.getDefaultRandomSeed();
    }

    public void setDefaultRandomSeed(int defaultRandomSeed) {
        mDraftDataProvider.setDefaultRandomSeed(defaultRandomSeed);
    }

    @Override
    public void startAlgorithm(List<String> playerNames, int rounds) {
        populatePlayersList(playerNames);
        mDraftDataProvider.setMaxRound(rounds);
        mDraftDataProvider.setCurrentRound(0);
        mDraftDataProvider.setDefaultRandomSeed(playerNames.size());
        mDraftDataProvider.setPlayerWithBye(null);
        mDraftDataProvider.objectInitialized();
    }


    @Override
    public void cacheDraft() {
        saveDraftInMemory();
    }

    @Override
    public void clearCache() {
        mGamePreferences.clean();
    }

    @Override
    public boolean isLoadCachedDraftWasNeeded() {
        return loadDraftFromMemory();
    }

    public void setDraftStartedPlayersList(List<Player> playerList) {
        mDraftDataProvider.setPlayerList(playerList);
    }


    public List<Player> getDraftStartedPlayerList() {
        return mDraftDataProvider.getPlayerList();
    }

    public List<Player> getFilteredPlayerList(boolean dropped) {
        List<Player> filteredPlayerList = new ArrayList<>();
        for (Player player : getDraftStartedPlayerList()) {
            if (player.isDropped() == dropped) {
                filteredPlayerList.add(player);
            }
        }
        return filteredPlayerList;
    }

    /**
     * Sort players using comparator {@link PlayersComparator}
     */
    protected void sortPlayers(@NonNull List<Player> playerList) {
        Collections.sort(playerList, new PlayersComparator());
    }

    private void populatePlayersList(List<String> players) {
        List<Player> draftStartedPlayers = new ArrayList<>();
        for (String playerName : players) {
            Player player = new Player(playerName);
            draftStartedPlayers.add(player);
        }
        mDraftDataProvider.setPlayerList(draftStartedPlayers);
    }

    @Override
    public Player getPlayer(String playerName) {
        for (Player player : mDraftDataProvider.getPlayerList()) {
            if (player.getPlayerName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public int getMaxRound() {
        return mDraftDataProvider.getMaxRound();
    }

    public void setMaxRound(int mMaxRounds) {
        mDraftDataProvider.setMaxRound(mMaxRounds);
    }

    public int getCurrentRound() {
        return mDraftDataProvider.getCurrentRound();
    }

    public void setCurrentRound(int mCurrentRound) {
        mDraftDataProvider.setCurrentRound(mCurrentRound);
    }

    public Player getPlayerWithBye() {
        return mDraftDataProvider.getPlayerWithBye();
    }


    public void setRoundList(List<Game> roundList) {
        mDraftDataProvider.setParingGameList(roundList);
    }

    public List<Game> getGameRoundList() {
        return mDraftDataProvider.getParingGameList();
    }

    public void setPlayerWithBye(Player mPlayerWithBye) {
        mDraftDataProvider.setPlayerWithBye(mPlayerWithBye);
    }

    private void saveDraftInMemory() {
        mGamePreferences.saveDraftDataProvider(mDraftDataProvider);
    }

    private boolean loadDraftFromMemory() {
        if (mDraftDataProvider == null || !mDraftDataProvider.isObjectInitialized()) {
            mDraftDataProvider = mGamePreferences.getDraftDataProvider();
            return true;
        }
        return false;
    }
}
