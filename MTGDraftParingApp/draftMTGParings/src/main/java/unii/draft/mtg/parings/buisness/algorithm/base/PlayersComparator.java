package unii.draft.mtg.parings.buisness.algorithm.base;

import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.List;

import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;

public class PlayersComparator implements Comparator<Player> {
	/**
	 * Left object has more points
	 */
	private final static int LEFT = -1;
	/**
	 * Right object has more points
	 */
	private final static int RIGHT = 1;
	/**
	 * Object has the same points
	 */
	private final static int EQUAL = 0;

	/**
	 * In game max matches
	 */
	private final static int MAX_MATCHES = 3;

	/**
	 * Compare player by :<br>
	 * -score <br>
	 * -obtained bye<br>
	 * -by comparing score in match between them<br>
	 */
	@Override
	public int compare(@NonNull Player lhs, @NonNull Player rhs) {

		if (lhs.getMatchPoints() > rhs.getMatchPoints()) {
			return LEFT;
		} else if (lhs.getMatchPoints() < rhs.getMatchPoints()) {
			return RIGHT;
		} else {
			int byeCompare = compareBye(lhs.hasBye(), rhs.hasBye());
			if (byeCompare == EQUAL) {
				return compareOMW(lhs, rhs);
			} else {
				return byeCompare;
			}
		}
	}

	/**
	 * Compare stats in game between two players by oponents match win 
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	private int compareOMW(@NonNull Player lhs, @NonNull Player rhs) {
		float lhsOMW = lhs.getOponentsMatchOveralWins();
		float rhsOMW = rhs.getOponentsMatchOveralWins();
		if (lhsOMW > rhsOMW) {
			return LEFT;
		}
		else if(rhsOMW > lhsOMW){
			return RIGHT;
		}
		else{
			return compareGamePoints(lhs, rhs);
		}
	}

	private int compareGamePoints(@NonNull Player lhs, @NonNull Player rhs) {
		// small points divided by max game played per matcher and rounds played
		float lhsSmallPoints = lhs.getPlayerGamesOverallWin();// ((float)
																// lhs.getGamePoints())
																// /
																// (MAX_MATCHES*
																// lhs.getPlayedGame().size());
		float rhsSmallPoints = rhs.getPlayerGamesOverallWin();// ((float)
																// rhs.getGamePoints())
																// /
																// (MAX_MATCHES*
																// rhs.getPlayedGame().size());

		if (lhsSmallPoints > rhsSmallPoints) {
			return LEFT;
		} else if (lhsSmallPoints < rhsSmallPoints) {
			return RIGHT;
		} else {
			return playedBefore(lhs, rhs);
		}
	}

	private int compareBye(boolean leftBye, boolean rightBye) {
		if (leftBye && rightBye) {
			return EQUAL;
		} else if (leftBye) {
			// left has a bye
			// so right player is better
			return RIGHT;
		} else if (rightBye) {
			return LEFT;
		}
		// sanity check
		return EQUAL;
	}

	private int playedBefore(@NonNull Player playerA, @NonNull Player playerB) {
		List<Game> games = playerA.getPlayedGame();
		Game game = null;
		for (Game g : games) {
			if (playerB.getPlayerName().equals(g.getPlayerNameA())
					|| playerB.getPlayerName().equals(g.getPlayerNameB()))
				game = g;

		}
		if (game == null) {
			return EQUAL;
		} else {
			return compareGamePointsInMatch(playerA, game);
		}
	}

	private int compareGamePointsInMatch(@NonNull Player playerA, @NonNull Game g) {
		int compareWinGame = comparePlayerAWinGame(g);
		if (compareWinGame == EQUAL) {
			return EQUAL;
		} else if (compareWinGame == LEFT) {
			if (playerA.getPlayerName().equals(g.getPlayerNameA())) {
				return LEFT;
			} else {
				return RIGHT;
			}
		}

		else {
			if (playerA.getPlayerName().equals(g.getPlayerNameB())) {
				return RIGHT;
			} else {
				return LEFT;
			}
		}

	}

	/**
	 * Compare which one player win
	 * 
	 * @param g
	 *            played game
	 * @return
	 */
	private int comparePlayerAWinGame(@NonNull Game g) {
		if (g.getWinner().equals(g.getPlayerNameA())) {
			return LEFT;
		} else if (g.getWinner().equals(g.getPlayerNameB())) {
			return RIGHT;
		} else {
			return EQUAL;
		}

	}
}
