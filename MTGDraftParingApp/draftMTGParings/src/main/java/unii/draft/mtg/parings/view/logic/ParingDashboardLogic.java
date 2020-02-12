package unii.draft.mtg.parings.view.logic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.base.IParingAlgorithm;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.util.config.BaseConfig;

import static unii.draft.mtg.parings.util.config.BaseConfig.GAME_DROPPED_NAME;
import static unii.draft.mtg.parings.util.config.BaseConfig.GAME_DROPPED_POINTS;
import static unii.draft.mtg.parings.util.config.BaseConfig.MATCH_DROPPED;
import static unii.draft.mtg.parings.util.config.BaseConfig.PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER;

/**
 * TODO: how many points should I add for winning a game with R_ player
 */

public class ParingDashboardLogic {
    private Context mContext;
    private int pointsForGameWinning;
    private float pointsForGameDraw;
    private int pointsForMatchWinning;
    private float pointsForMatchDraw;
    private boolean droppedPlayerLoseGame;

    public ParingDashboardLogic(Context context, int pointsForGameWinning, float pointsForGameDraw,
                                int pointsForMatchWinning, float pointsForMatchDraw, boolean droppedPlayerLoseGame) {
        mContext = context;
        this.pointsForGameDraw = pointsForGameDraw;
        this.pointsForGameWinning = pointsForGameWinning;
        this.pointsForMatchDraw = pointsForMatchDraw;
        this.pointsForMatchWinning = pointsForMatchWinning;
        this.droppedPlayerLoseGame = droppedPlayerLoseGame;
    }


    public void addGameResult(@NonNull IParingAlgorithm paringAlgorithm, @NonNull List<Game> gameList) {
        updateGameResults(gameList);
        updatePlayerPoints(paringAlgorithm, gameList);
    }


    public void removeLastGameResult(@NonNull IParingAlgorithm paringAlgorithm) {
        List<Player> playerList = paringAlgorithm.getSortedFilteredPlayerList(false);
        for (Player player : playerList) {
            if (paringAlgorithm.getPlayerWithBye() != null
                    && player.equals(paringAlgorithm.getPlayerWithBye())) {
                // remove maximum
                // points
                // for
                // player
                // with bye
                player.setMatchPoints(player.getMatchPoints() - pointsForMatchWinning);
                continue;
            }

            List<Game> playedGames = player.getPlayedGame();
            if (playedGames == null || playedGames.isEmpty() || playedGames.size() == 0) {
                continue;
            }

            Game lastGame = playedGames.get(playedGames.size() - 1);

            playedGames.remove(lastGame);


            // draw
            if (lastGame.getWinner().equals(BaseConfig.DRAW)) {
                player.setMatchPoints(player.getMatchPoints()
                        - pointsForMatchDraw);
                // win match
            } else if (lastGame.getWinner().equals(player.getPlayerName())) {
                player.setMatchPoints(player.getMatchPoints()
                        - pointsForMatchWinning);

            }

            // There was a draw so each player gains 1 point for each draw
            if (lastGame.getDraws() > 0) {
                player.setGamePoints(player.getGamePoints() - lastGame.getDraws() * pointsForGameDraw);
            }
            // add "small" points for a player
            if (player.getPlayerName().equals(lastGame.getPlayerNameA())) {
                player.setGamePoints(player.getGamePoints()
                        - lastGame.getPlayerAPoints() * pointsForMatchWinning);
            } else {
                player.setGamePoints(player.getGamePoints()
                        - lastGame.getPlayerBPoints() * pointsForMatchWinning);
            }
        }
    }

    public boolean validateDataSet(@NonNull List<Game> gameList) {
        boolean isAllGamePointsSet = true;
        for (Game game : gameList) {
            //IF PLAYER HAS name check points value
            if (!game.getPlayerNameA().equals(mContext.getString(R.string.dummy_player)) && !game.getPlayerNameB().equals(mContext.getString(R.string.dummy_player)) && game.getPlayerAPoints() == 0 && game.getPlayerBPoints() == 0 && game.getDraws() == 0) {
                isAllGamePointsSet = false;
            }
        }
        return isAllGamePointsSet;
    }

    private void updateGameResults(@NonNull List<Game> gameList) {
        for (Game g : gameList) {
            if (g.getPlayerAPoints() > g.getPlayerBPoints()) {
                g.setWinner(g.getPlayerNameA());
            } else if (g.getPlayerAPoints() < g.getPlayerBPoints()) {
                g.setWinner(g.getPlayerNameB());
            } else {
                // it was a draw
                g.setWinner(BaseConfig.DRAW);
            }
            g.setGamesPlayed(g.getPlayerAPoints() + g.getPlayerBPoints() + g.getDraws());
        }
    }

    private boolean playerLeftWinAsDefaultWithRightPlayer(@NonNull String playerLeft, @NonNull String playerRight) {
        return droppedPlayerLoseGame &&
                playerRight.startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER) &&
                !playerLeft.startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER);
    }

    /**
     * TODO: what should be done when one of player is dropped,
     * Two players are dropped,
     * Not me but opponent dropped;
     *
     * @param paringAlgorithm
     * @param gameList
     */
    private void updatePlayerPoints(@NonNull IParingAlgorithm paringAlgorithm, @NonNull List<Game> gameList) {
        List<Player> playerList = paringAlgorithm.getSortedFilteredPlayerList(false);


        for (Player player : playerList) {
            // player has bye
            if (paringAlgorithm.getPlayerWithBye() != null
                    && player.getPlayerName().equals(paringAlgorithm.getPlayerWithBye().getPlayerName())) {
                // set maximum
                // points
                // for
                // player
                // with bye
                player.setMatchPoints(player.getMatchPoints() + pointsForMatchWinning);
            }
            for (Game game : gameList) {
                if (player.getPlayerName().equals(game.getPlayerNameA())
                        || player.getPlayerName().equals(game.getPlayerNameB())) {
                    player.getPlayedGame().add(game);
                    // draw
                    if (game.getWinner().equals(BaseConfig.DRAW)) {
                        if (!player.getPlayerName().startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER)
                        ) {
                            player.setMatchPoints(player.getMatchPoints()
                                    + pointsForMatchDraw);
                        }
                        // win match
                    } else if (game.getWinner().equals(player.getPlayerName())) {
                        player.setMatchPoints(player.getMatchPoints()
                                + pointsForMatchWinning);

                    }
                    if (player.getPlayerName().startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER)) {
                        continue;
                    }
                    // add "small" points for a player
                    if (player.getPlayerName().equals(game.getPlayerNameA())) {
                        player.setGamePoints(player.getGamePoints()
                                + game.getPlayerAPoints() * pointsForGameWinning);
                    } else {
                        player.setGamePoints(player.getGamePoints()
                                + game.getPlayerBPoints() * pointsForGameWinning);
                    }
                    // There was a draw so each player gains 1 point for each draw
                    if (game.getDraws() > 0) {
                        player.setGamePoints(player.getGamePoints() + game.getDraws() * pointsForGameDraw);
                    }
                    break;

                }

            }
        }
        paringAlgorithm.getSortedPlayerList();


    }

    @NonNull
    public String[] getPlayerNameList(@NonNull List<Game> gameList) {
        List<String> playerNameList = new ArrayList<>();
        for (Game game : gameList) {
            //do not add dummy players!
            if (!game.getPlayerNameA().equals(mContext.getString(R.string.dummy_player)) && !game.getPlayerNameB().equals(mContext.getString(R.string.dummy_player))) {
                playerNameList.add(game.getPlayerNameA());
                playerNameList.add(game.getPlayerNameB());
            }
        }
        String[] playerArray = new String[playerNameList.size()];
        playerArray = playerNameList.toArray(playerArray);
        return playerArray;
    }

    public void openGooglePlayMTGCounterApp() {
        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BaseConfig.INTENT_OPEN_GOOGLE_PLAY + BaseConfig.INTENT_PACKAGE_LIFE_COUNTER_APP_UNII)));
        } catch (android.content.ActivityNotFoundException anfe) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BaseConfig.INTENT_OPEN_GOOGLE_PLAY_WWW + BaseConfig.INTENT_PACKAGE_LIFE_COUNTER_APP_UNII)));
        }
    }


    private void resetPlayer(@NonNull Game game, @NonNull Player resetPlayer) {
        //THIS MONOLIT OF CODE IS FOR TESTING PURPOSE
        //TODO: clean this code after

        if (resetPlayer.getPlayerName().equals(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER + game.getPlayerNameA())
                || resetPlayer.getPlayerName().equals(game.getPlayerNameA())) {
            resetPlayer.setGamePoints(resetPlayer.getGamePoints() - game.getPlayerAPoints());
            if (game.getWinner().equals(BaseConfig.DRAW)) {
                resetPlayer.setMatchPoints(resetPlayer.getMatchPoints() - pointsForMatchDraw);
            } else if (resetPlayer.getPlayerName().equals(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER + game.getWinner())
                    || resetPlayer.getPlayerName().equals(game.getWinner())) {
                resetPlayer.setMatchPoints(resetPlayer.getMatchPoints() - pointsForMatchWinning);
            }
        } else if (resetPlayer.getPlayerName().equals(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER + game.getPlayerNameB())
                || resetPlayer.getPlayerName().equals(game.getPlayerNameB())) {
            resetPlayer.setGamePoints(resetPlayer.getGamePoints() - game.getPlayerBPoints());
            if (game.getWinner().equals(BaseConfig.DRAW)) {
                resetPlayer.setMatchPoints(resetPlayer.getMatchPoints() - pointsForMatchDraw);
            } else if (resetPlayer.getPlayerName().equals(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER + game.getWinner())
                    || resetPlayer.getPlayerName().equals(game.getWinner())) {
                resetPlayer.setMatchPoints(resetPlayer.getMatchPoints() - pointsForMatchWinning);
            }
        }
    }

    private void resetGame(@NonNull Game game) {
        game.setPlayerAPoints(GAME_DROPPED_POINTS);
        game.setGamesPlayed(MATCH_DROPPED);
        game.setPlayerBPoints(GAME_DROPPED_POINTS);
        game.setWinner(GAME_DROPPED_NAME);
        game.setPlayerAPoints(MATCH_DROPPED);
        game.setPlayerBPoints(MATCH_DROPPED);
        game.setDraws(MATCH_DROPPED);
    }

    public void updateAllPlayerList(@NonNull List<Player> playerList) {
        for (Player player : playerList) {
            for (Game game : player.getPlayedGame()) {
                if (player.getPlayerName().startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER)) {
                    resetPlayer(game, player);
                }
            }
        }

        for (Player player : playerList) {
            for (Game game : player.getPlayedGame()) {
                if (player.getPlayerName().startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER)) {
                    resetGame(game);
                }
            }
        }

    }
}
