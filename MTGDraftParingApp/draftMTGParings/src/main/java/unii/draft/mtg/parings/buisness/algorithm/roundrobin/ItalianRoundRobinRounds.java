package unii.draft.mtg.parings.buisness.algorithm.roundrobin;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.base.SemiAlgorithm;
import unii.draft.mtg.parings.logic.dagger.ApplicationComponent;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.config.BaseConfig;

//TODO: test this algorithm
// ;) !!
public class ItalianRoundRobinRounds extends SemiAlgorithm {
    private List<Player> leftList;
    private List<Player> rightList;
    private HashMap<String, Player> playerList;
    private Context context;

    @Inject
    ISharedPreferences sharedPreferences;

    public ItalianRoundRobinRounds(Context context, @NonNull ApplicationComponent component) {
        super(context);
        component.inject(this);
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
            Player playerA = getPlayerByName(game.getPlayerNameA());
            playerListLeft.add(playerA);
            Player playerB = getPlayerByName(game.getPlayerNameB());
            playerListRight.add(playerB);
        }
    }


    private Player getPlayerByName(@NonNull String playerName) {

        Player player = playerList.get(playerName);
        if (player == null) {
            if (playerName.startsWith(BaseConfig.PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_AFTER_HALF_ROUNDS)) {
                player = getPlayerByNameAndPrefix(playerName, BaseConfig.PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_AFTER_HALF_ROUNDS);
            } else if (playerName.startsWith(BaseConfig.PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_BEFORE_HALF_ROUNDS)) {
                player = getPlayerByNameAndPrefix(playerName, BaseConfig.PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER_BEFORE_HALF_ROUNDS);
            }
        }

        return player;

    }

    private Player getPlayerByNameAndPrefix(@NonNull String playerName, @NonNull String prefix) {
        Player player = null;
        if (playerName.startsWith(prefix)) {
            String oldName = playerName.replace(prefix, "");
            player = playerList.get(oldName);
            playerList.remove(oldName);
            playerList.put(playerName, player);
        }
        return player;
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

    @Override
    protected void sortPlayers(@NonNull List<Player> playerList) {
        Collections.sort(playerList, new PlayerItalianRoundRobinComparator(playerList, sharedPreferences.getPointsForMatchDraws()));
    }
}
