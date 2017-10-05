package unii.draft.mtg.parings.view.logic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.base.IParingAlgorithm;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.util.config.BaseConfig;


public class ParingDashboardLogic {
    private Context mContext;

    public ParingDashboardLogic(Context context) {
        mContext = context;
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
                player.setMatchPoints(player.getMatchPoints() - BaseConfig.MATCH_WIN);
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
                        - BaseConfig.MATCH_DRAW);
                // win match
            } else if (lastGame.getWinner().equals(player.getPlayerName())) {
                player.setMatchPoints(player.getMatchPoints()
                        - BaseConfig.MATCH_WIN);

            }

            // There was a draw so each player gains 1 point for each draw
            if (lastGame.getDraws() > 0) {
                player.setGamePoints(player.getGamePoints() - lastGame.getDraws());
            }
            // add "small" points for a player
            if (player.getPlayerName().equals(lastGame.getPlayerNameA())) {
                player.setGamePoints(player.getGamePoints()
                        - lastGame.getPlayerAPoints() * 3);
            } else {
                player.setGamePoints(player.getGamePoints()
                        - lastGame.getPlayerBPoints() * 3);
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


    private void updatePlayerPoints(@NonNull IParingAlgorithm paringAlgorithm, @NonNull List<Game> gameList) {
        List<Player> playerList = paringAlgorithm.getSortedFilteredPlayerList(false);


        for (Player player : playerList) {
            // player has bye
            if (paringAlgorithm.getPlayerWithBye() != null
                    && player.equals(paringAlgorithm.getPlayerWithBye())) {
                // set maximum
                // points
                // for
                // player
                // with bye
                player.setMatchPoints(player.getMatchPoints() + BaseConfig.MATCH_WIN);
            }
            for (Game game : gameList) {
                if (player.getPlayerName().equals(game.getPlayerNameA())
                        || player.getPlayerName().equals(game.getPlayerNameB())) {
                    player.getPlayedGame().add(game);
                    // draw
                    if (game.getWinner().equals(BaseConfig.DRAW)) {
                        player.setMatchPoints(player.getMatchPoints()
                                + BaseConfig.MATCH_DRAW);
                        // win match
                    } else if (game.getWinner().equals(player.getPlayerName())) {
                        player.setMatchPoints(player.getMatchPoints()
                                + BaseConfig.MATCH_WIN);

                    }
                    // add "small" points for a player
                    if (player.getPlayerName().equals(game.getPlayerNameA())) {
                        player.setGamePoints(player.getGamePoints()
                                + game.getPlayerAPoints() * 3);
                    } else {
                        player.setGamePoints(player.getGamePoints()
                                + game.getPlayerBPoints() * 3);
                    }
                    // There was a draw so each player gains 1 point for each draw
                    if (game.getDraws() > 0) {
                        player.setGamePoints(player.getGamePoints() + game.getDraws());
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
}
