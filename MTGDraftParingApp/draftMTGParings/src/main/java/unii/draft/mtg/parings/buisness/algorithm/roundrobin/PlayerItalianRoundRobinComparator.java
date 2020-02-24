package unii.draft.mtg.parings.buisness.algorithm.roundrobin;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Comparator;
import java.util.List;

import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.util.config.BaseConfig;

import static unii.draft.mtg.parings.util.config.BaseConfig.DRAW;

public class PlayerItalianRoundRobinComparator implements Comparator<Player> {
    /**
     * Left object has more points
     */
    public final static int LEFT = -1;
    /**
     * Right object has more points
     */
    public final static int RIGHT = 1;
    /**
     * Object has the same points
     */
    public final static int EQUAL = 0;

    private List<Player> playerList;

    private final static int MULTIPLIER_OPPONENTS_POINTS = 2;

    private float pointsForDraw;

    public PlayerItalianRoundRobinComparator(@NonNull List<Player> playerList, float pointsForDraw) {
        this.playerList = playerList;
        this.pointsForDraw = pointsForDraw;
    }

    @Override
    public int compare(@NonNull Player lhs, @NonNull Player rhs) {
        float lhlOpponentsPoints = calculatePlayerOpponentPoints(lhs) * MULTIPLIER_OPPONENTS_POINTS;
        float rhsOpponentsPoints = calculatePlayerOpponentPoints(rhs) * MULTIPLIER_OPPONENTS_POINTS;
        if (lhlOpponentsPoints > rhsOpponentsPoints) {
            return LEFT;
        } else if (lhlOpponentsPoints < rhsOpponentsPoints) {
            return RIGHT;
        } else {
            return compareOpponentsDrawPoints(lhs, rhs);
        }
    }

    private int compareOpponentsDrawPoints(@NonNull Player lhs, @NonNull Player rhs) {
        float lhlOpponentsPoints = calculatePlayerOpponentDrawPointsInComparisionToAllPoints(lhs);
        float rhsOpponentsPoints = calculatePlayerOpponentDrawPointsInComparisionToAllPoints(rhs);
        if (lhlOpponentsPoints > rhsOpponentsPoints) {
            return LEFT;
        } else if (lhlOpponentsPoints < rhsOpponentsPoints) {
            return RIGHT;
        } else {
            return whoWinGameBetweenThemself(lhs, rhs);
        }
    }

    private int whoWinGameBetweenThemself(@NonNull Player lhs, @NonNull Player rhs) {
        List<Game> playedGames = lhs.getPlayedGame();
        for (Game game : playedGames) {
            if (!(isPlayer(rhs.getPlayerName(), game.getPlayerNameA())
                    || isPlayer(rhs.getPlayerName(), game.getPlayerNameB()))) {
                continue;
            }

            if (isPlayer(lhs.getPlayerName(), game.getWinner())) {
                return LEFT;
            } else if (isPlayer(rhs.getPlayerName(), game.getWinner())) {
                return RIGHT;
            } else {
                return EQUAL;
            }
        }
        return EQUAL;
    }

    private float calculatePlayerOpponentDrawPointsInComparisionToAllPoints(@NonNull Player player) {
        float opponentsDrawPoints = calculateDrawPoints(player);
        float opponentsPoints = calculatePlayerOpponentPoints(player);
        if (opponentsPoints == 0) {
            opponentsPoints = 1;
        }
        return opponentsDrawPoints / opponentsPoints;

    }

    private float calculatePlayerOpponentPoints(@NonNull Player player) {
        float opponentsPoints = 0;
        List<Game> gameList = player.getPlayedGame();
        for (Game game : gameList) {
            Player opponent = null;
            if (isPlayer(player.getPlayerName(), game.getPlayerNameA())) {
                opponent = getPlayer(game.getPlayerNameB());
            } else if (isPlayer(player.getPlayerName(),
                    game.getPlayerNameB())) {
                opponent = getPlayer(game.getPlayerNameA());
            }
            if (opponent == null) {
                continue;
            }
            opponentsPoints += player.getMatchPoints();

        }
        return opponentsPoints;

    }

    private float calculateDrawPoints(@NonNull Player player) {
        float drawPoints = 0;
        List<Game> gameList = player.getPlayedGame();
        for (Game game : gameList) {
            Player opponent = null;
            if (isPlayer(player.getPlayerName(), game.getPlayerNameA())) {
                opponent = getPlayer(game.getPlayerNameB());
            } else if (isPlayer(player.getPlayerName(),
                    game.getPlayerNameB())) {
                opponent = getPlayer(game.getPlayerNameA());

            }
            if (opponent == null) {
                continue;
            }
            drawPoints += calculateOpponentsDrawPoints(opponent);

        }
        return drawPoints;

    }

    private float calculateOpponentsDrawPoints(@NonNull Player player) {
        float drawPoints = 0;
        List<Game> gameList = player.getPlayedGame();
        for (Game game : gameList) {
            if (game.getWinner().equals(DRAW)) {
                drawPoints += pointsForDraw;
            }
        }
        return drawPoints;
    }

    @Nullable
    private Player getPlayer(@NonNull String playerName) {
        for (Player player : playerList) {
            if (isPlayer(playerName, player.getPlayerName())) {
                return player;
            }
        }
        return null;
    }

    private boolean isPlayer(@NonNull String playerName, @NonNull String playerOnList) {
        return playerOnList.equals(playerName) ||
                playerOnList.equals(BaseConfig.PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_BEFORE_HALF_ROUNDS + playerName)
                || playerOnList.equals(BaseConfig.PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_AFTER_HALF_ROUNDS + playerName);
    }

}

