package unii.draft.mtg.parings.view.logic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.SparseArray;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.base.IParingAlgorithm;
import unii.draft.mtg.parings.buisness.algorithm.roundrobin.ItalianRoundRobinRounds;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.logic.pojo.Round;
import unii.draft.mtg.parings.util.config.BaseConfig;
import unii.draft.mtg.parings.util.converter.SparseArrayToArrayListConverter;

import static unii.draft.mtg.parings.util.config.BaseConfig.GAME_DROPPED_NAME;
import static unii.draft.mtg.parings.util.config.BaseConfig.MATCH_DROPPED;
import static unii.draft.mtg.parings.util.config.BaseConfig.PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_AFTER_HALF_ROUNDS;
import static unii.draft.mtg.parings.util.config.BaseConfig.PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_BEFORE_HALF_ROUNDS;

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


            if (lastGame.getWinner().equals(GAME_DROPPED_NAME)) {
                continue;
            }
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

    public boolean validateDataSet(@NonNull List<Game> gameList, @NonNull IParingAlgorithm paringAlgorithm) {
        boolean isAllGamePointsSet = true;
        for (Game game : gameList) {

            if (isItalianAlgorithmWithDroppedPlayers(game, paringAlgorithm)) {
                continue;
            }
            //IF PLAYER HAS name check points value
            if (!game.getPlayerNameA().equals(mContext.getString(R.string.dummy_player)) && !game.getPlayerNameB().equals(mContext.getString(R.string.dummy_player))
                    && game.getPlayerAPoints() == 0 && game.getPlayerBPoints() == 0 && game.getDraws() == 0
            ) {
                isAllGamePointsSet = false;
            }
        }
        return isAllGamePointsSet;
    }

    private boolean isItalianAlgorithmWithDroppedPlayers(@NonNull Game game, @NonNull IParingAlgorithm paringAlgorithm) {
        return paringAlgorithm instanceof ItalianRoundRobinRounds && ((game.getPlayerNameA().startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_BEFORE_HALF_ROUNDS)
                || game.getPlayerNameA().startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_AFTER_HALF_ROUNDS)) ||
                (game.getPlayerNameB().startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_BEFORE_HALF_ROUNDS)
                        || game.getPlayerNameB().startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_AFTER_HALF_ROUNDS)));
    }

    private void updateGameResults(@NonNull List<Game> gameList) {
        for (Game g : gameList) {
            if (g.getPlayerAPoints() > g.getPlayerBPoints()) {
                g.setWinner(g.getPlayerNameA());
            } else if (g.getPlayerAPoints() < g.getPlayerBPoints()) {
                g.setWinner(g.getPlayerNameB());
            }
            /* else if (isDroppedPlayer(g.getPlayerNameA()) || isDroppedPlayer(g.getPlayerNameB())) {
                g.setWinner(GAME_DROPPED_NAME);
            }*/
            else if (g.getWinner() != null && g.getWinner().equals(GAME_DROPPED_NAME)) {
                //DO nothing
            } else {
                // it was a draw
                g.setWinner(BaseConfig.DRAW);
            }
            g.setGamesPlayed(g.getPlayerAPoints() + g.getPlayerBPoints() + g.getDraws());
        }
    }

  /*  private boolean playerLeftWinAsDefaultWithRightPlayer(@NonNull String playerLeft, @NonNull String playerRight) {
        return droppedPlayerLoseGame &&
                playerRight.startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_BEFORE_HALF_ROUNDS) &&
                !playerLeft.startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_BEFORE_HALF_ROUNDS);
    }*/


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
                    if (game.getWinner().equals(GAME_DROPPED_NAME)) {
                        continue;
                    }
                    if (game.getWinner().equals(BaseConfig.DRAW)) {
                        if (!isDroppedPlayer(player.getPlayerName())) {
                            player.setMatchPoints(player.getMatchPoints()
                                    + pointsForMatchDraw);
                        }
                        // win match
                    } else if (game.getWinner().equals(player.getPlayerName())) {
                        player.setMatchPoints(player.getMatchPoints()
                                + pointsForMatchWinning);

                    }
                    if (isDroppedPlayer(player.getPlayerName())) {
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

    //@NonNull Game game,
    private void resetPlayer(@NonNull Player resetPlayer) {
        resetPlayer.setMatchPoints(0);
        resetPlayer.setGamePoints(0);
        resetPlayer.setPlayerGamesOverallWin(0);
        resetPlayer.setPlayerMatchOverallWin(0);
        resetPlayer.setOponentsGamesOverallWin(0);
        resetPlayer.setOponentsMatchOveralWins(0);

    }

    private void resetGame(@NonNull Game game) {
        game.setPlayerAPoints(MATCH_DROPPED);
        game.setGamesPlayed(MATCH_DROPPED);
        game.setPlayerBPoints(MATCH_DROPPED);
        game.setWinner(GAME_DROPPED_NAME);
        game.setDraws(MATCH_DROPPED);
    }

    //TODO: BUG - jeżeli zamkniesz turnije w trakcie (zapiszesz go) jego wyniki beda sie pozniej dublowały z nastepnym
    //should not occure
    public void updateAllPlayerList(@NonNull IParingAlgorithm paringAlgorithm, @NonNull List<Player> playerList) {
        for (Player player : playerList) {
            resetPlayer(player);
        }

        for (Player player : playerList) {
            for (Game game : player.getPlayedGame()) {
                if (game.getPlayerNameA().startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_BEFORE_HALF_ROUNDS) ||
                        game.getPlayerNameB().startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_BEFORE_HALF_ROUNDS)) {
                    resetGame(game);
                }
            }
        }
        List<Game> gameList = new ArrayList<>();

        for (Player player : playerList) {
            gameList.addAll(player.getPlayedGame());
            player.setPlayedGame(new ArrayList<>());
        }

        List<Round> rounds = convertToRounds(gameList);
        for (Round round : rounds) {
            sortGameListAlphabeticallyByPlayerAName(round.getGameList());
        }
        for (Round round : rounds) {
            updatePlayerPoints(paringAlgorithm, round.getGameList());
        }
    }

    public boolean isDroppedPlayer(@NonNull String playerName) {
        return playerName.startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_AFTER_HALF_ROUNDS) ||
                playerName.startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_BEFORE_HALF_ROUNDS);
    }


    private List<Round> convertToRounds(List<Game> playedGames) {
        SparseArray<Round> roundList = new SparseArray<>();
        for (Game game : playedGames) {
            int currentRound = game.getRound();
            if (roundList.get(currentRound) == null) {
                List<Game> gameList = new ArrayList<>();
                gameList.add(game);
                Round round = new Round(currentRound, gameList);
                roundList.put(currentRound, round);
            } else {
                Round savedRound = roundList.get(currentRound);
                List<Game> savedGames = savedRound.getGameList();

                if (!savedGames.contains(game)) {
                    roundList.get(currentRound).getGameList().add(game);
                }
            }
        }
        return SparseArrayToArrayListConverter.asList(roundList);
    }

    private void sortGameListAlphabeticallyByPlayerAName(@NonNull List<Game> gameList) {

        Collections.sort(gameList, (o1, o2) -> (o1.getPlayerNameA().compareTo(o2.getPlayerNameA())));
    }
}
