package unii.draft.mtg.parings.view.logic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.algorithm.IParingAlgorithm;
import unii.draft.mtg.parings.config.BaseConfig;
import unii.draft.mtg.parings.pojo.Game;
import unii.draft.mtg.parings.pojo.Player;

/**
 * Created by apachucy on 2016-03-04.
 */
public class ParingDashboardLogic {
    private Context mContext;

    public ParingDashboardLogic(Context context) {
        mContext = context;
    }

    public void addDummyPlayer(List<Game> gameList) {
        gameList.add(new Game(mContext.getString(R.string.dummy_player), mContext.getString(R.string.dummy_player)));//add empty element
    }

    public void removeDummyPlayer(List<Game> gameList) {
     /*   Game dummyGame = null;
        for (Game game : gameList) {
            if (game.getPlayerNameA().equals(mContext.getString(R.string.dummy_player)) && game.getPlayerNameB().equals(mContext.getString(R.string.dummy_player))) {
                dummyGame = game;
            }*/
         Iterator<Game> gameIterator = gameList.iterator();
        while (gameIterator.hasNext()) {
            Game dummyGame = gameIterator.next();
            if (dummyGame.getPlayerNameA().equals(mContext.getString(R.string.dummy_player)) && dummyGame.getPlayerNameB().equals(mContext.getString(R.string.dummy_player))) {
                gameIterator.remove();
            }
        }

        }
      /*  if (dummyGame != null) {
            gameList.remove(dummyGame);
        }*/



    public void addGameResult(IParingAlgorithm paringAlgorithm, List<Game> gameList) {
        updateGameResults(gameList);
        updatePlayerPoints(paringAlgorithm, gameList);
    }


    public boolean validateDataSet(List<Game> gameList) {
        boolean isAllGamePointsSet = true;
        for (Game game : gameList) {
            //IF PLAYER HAS name check points value
            if (!game.getPlayerNameA().equals(mContext.getString(R.string.dummy_player)) && !game.getPlayerNameB().equals(mContext.getString(R.string.dummy_player)) && game.getPlayerAPoints() == 0 && game.getPlayerBPoints() == 0 && game.getDraws() == 0) {
                isAllGamePointsSet = false;
            }
        }
        return isAllGamePointsSet;
    }

    private void updateGameResults(List<Game> gameList) {
        for (Game g : gameList) {
            if (g.getPlayerAPoints() > g.getPlayerBPoints()) {
                g.setWinner(g.getPlayerNameA());
            } else if (g.getPlayerAPoints() < g.getPlayerBPoints()) {
                g.setWinner(g.getPlayerNameB());
            } else {
                // it was a draw
                g.setWinner(BaseConfig.DRAW);
            }
        }
    }


    private void updatePlayerPoints(IParingAlgorithm paringAlgorithm, List<Game> gameList) {
        List<Player> playerList = paringAlgorithm.getSortedPlayerList();
        //Remove dummy decorator, unused element
        removeDummyPlayer(gameList);

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

    public String[] getPlayerNameList(List<Game> gameList) {
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
