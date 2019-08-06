package unii.draft.mtg.parings.buisness.algorithm.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;

public interface IParingAlgorithm {


    void startAlgorithm(List<String> playerNames, int rounds);

    /**
     * get actual paring in form of <br>
     * list of{@link Game}
     *
     * @param sittingsType check: {@linkplain unii.draft.mtg.parings.buisness.sittings.SittingsMode}
     * @return game list or in case of error null
     */
    List<Game> getParings(int sittingsType);

    /**
     * getting current rounds
     *
     * @return current round
     */
    int getCurrentRound();

    /**
     * @return MaxRound that will be played
     */
    int getMaxRound();

    /**
     * getting sorted player list<br>
     * sort of player using {@link PlayersComparator}
     *
     * @return
     */
    List<Player> getSortedPlayerList();


    List<Player> getSortedFilteredPlayerList(boolean dropped);

    /**
     * @return player <br>
     * with bye for last round or <br>
     * null if no player has bye
     */
    Player getPlayerWithBye();

    /**
     * In case when there is only manual control with parings
     *
     * @param playerList
     */
    void setPlayerGameList(List<Game> playerList);

    void setPlayerWithBye(Player playerWithBye);

    @Nullable
    Player getPlayer(String playerName);

    int playedRound();

    void setPlayedRound(int roundsPlayed);

    List<Game> getLastPlayedGameList();

    void reoderPlayerList(@NonNull List<String> playerNames);
}