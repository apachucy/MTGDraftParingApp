package unii.draft.mtg.parings.buisness.algorithm.roundrobin;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.base.SemiAlgorithm;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;

//TODO: test this algorithm
// ;) !!
public class ItalianRoundRobinRounds extends SemiAlgorithm {
    private List<Player> leftList;
    private List<Player> rightList;
    private HashMap<String, Player> playerList;
    private Context context;

    public ItalianRoundRobinRounds(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getMaxRound() {
        return super.getMaxRound();
    }

    public void startAlgorithm(@NonNull List<String> playerNames, int rounds) {
        if (playerNames.size() % 2 == 1) {
            //add player to be even
            playerNames.add(context.getString(R.string.bye_player_when_odd_players));
        }
        super.startAlgorithm(playerNames, rounds);
        playerList = new HashMap<>();

        for (Player player : getDraftStartedPlayerList()) {
            playerList.put(player.getPlayerName(), player);
        }

    }

    @NonNull
    @Override
    public List<Player> getSortedFilteredPlayerList(boolean dropped) {
        return getDraftStartedPlayerList();
    }

    @Override
    public List<Game> getParings(int sittingsMode) {
        /**
         * If current played round is bigger than finished round
         * load last generated game
         */
        if (getCurrentRound() > playedRound()) {
            return super.getParings(0);
        }
        if (leftList == null && rightList == null) {
            leftList = new ArrayList<>();
            rightList = new ArrayList<>();
        }
        List<Game> gameList;
        if (getCurrentRound() == 0) {
            divideList(getDraftStartedPlayerList(), leftList, rightList);
            gameList = generateParingList(leftList, rightList);
            setCurrentRound(getCurrentRound() + 1);
            setRoundList(gameList);
            return gameList;
        }
        createListFromGames(getGameRoundList(), leftList, rightList);
        gameList = generateParingList(leftList, rightList);

        setCurrentRound(getCurrentRound() + 1);

        setRoundList(gameList);
        return gameList;
    }

    private void createListFromGames(List<Game> gameList, List<Player> playerListLeft, List<Player> playerListRight) {
        playerListLeft.clear();
        playerListRight.clear();
        for (Game game : gameList) {
            Player playerA = playerList.get(game.getPlayerNameA());
            playerListLeft.add(playerA);
            Player playerB = playerList.get(game.getPlayerNameB());
            playerListRight.add(playerB);
        }
    }

    private void divideList(List<Player> starterList, List<Player> leftList, List<Player> rightList) {
        leftList.addAll(starterList.subList(0, starterList.size() / 2));
        rightList.addAll(starterList.subList(starterList.size() / 2, starterList.size()));
        Collections.reverse(rightList);
    }

    private List<Game> generateParingList(List<Player> leftList, List<Player> rightList) {
        if (getCurrentRound() == 0) {
            return generateGameRoundFirst(leftList, rightList);
        }
        if ((getCurrentRound() + 1) % 2 == 0) {
            return generateGameRoundEven(leftList, rightList);
        }
        return generateGamerRoundNotEven(leftList, rightList);
    }

    private List<Game> generateGameRoundFirst(List<Player> leftList, List<Player> rightList) {
        List<Game> gameList = new ArrayList<>();
        int round = getCurrentRound() + 1;
        for (int i = 0; i < rightList.size(); i++) {
            gameList.add(new Game(leftList.get(i).getPlayerName(),
                    rightList.get(i).getPlayerName(),
                    round));
        }
        return gameList;
    }

    private List<Game> generateGameRoundEven(List<Player> leftList, List<Player> rightList) {
        List<Game> gameList = new ArrayList<>();
        int round = getCurrentRound() + 1;
        gameList.add(new Game(rightList.get(0).getPlayerName(),
                rightList.get(rightList.size() - 1).getPlayerName(),
                round));

        for (int i = rightList.size() - 2; i > 0; i--) {
            gameList.add(new Game(rightList.get(i).getPlayerName(),
                    leftList.get(i + 1).getPlayerName(),
                    round));
        }
        gameList.add(new Game(leftList.get(0).getPlayerName(),
                leftList.get(1).getPlayerName(),
                round));

        return gameList;
    }

    private List<Game> generateGamerRoundNotEven(List<Player> leftList, List<Player> rightList) {
        List<Game> gameList = new ArrayList<>();
        int round = getCurrentRound() + 1;

        gameList.add(new Game(rightList.get(rightList.size() - 1).getPlayerName(),
                leftList.get(0).getPlayerName(),
                round));

        for (int i = rightList.size() - 2; i >= 0; i--) {
            gameList.add(new Game(rightList.get(i).getPlayerName(),
                    leftList.get(i + 1).getPlayerName(),
                    round));
        }
        return gameList;

    }

}
