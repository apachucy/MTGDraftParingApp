package unii.draft.mtg.parings.algorithm;

import java.util.List;

import unii.draft.mtg.parings.pojo.Game;
import unii.draft.mtg.parings.pojo.Player;

public interface IParingAlgorithm {
	/**
	 * get actual paring in form of <br>
	 * list of{@link Game}
	 * 
	 * @return game list or in case of error null
	 */
	public List<Game> getParings();

	/**
	 * getting current rounds
	 * 
	 * @return current round
	 */
	public int getCurrentRound();

	/**
	 * 
	 * @return MaxRound that will be played
	 */
	public int getMaxRound();

	/**
	 * getting sorted player list<br>
	 * sort of player using {@link PlayersComparator}
	 * 
	 * @return
	 */
	public List<Player> getSortedPlayerList();

	/**
	 * 
	 * @return player <br>
	 *         with bye for last round or <br>
	 *         null if no player has bye
	 */
	public Player getPlayerWithBye();

	/**
	 * In case when there is only manual control with parings
	 * @param playerList
	 */
	public void setPlayerGameList(List<Game> playerList);
	public void setPlayerWithBye(Player playerWithBye);
}
