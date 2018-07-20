package unii.draft.mtg.parings.buisness.algorithm.elimination;

import android.content.Context;
import android.support.annotation.NonNull;

import org.paukov.combinatorics.Generator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import unii.draft.mtg.parings.buisness.algorithm.base.SemiAlgorithm;
import unii.draft.mtg.parings.buisness.sittings.SittingsMode;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;

public class SuddenDeathAlgorithm extends SemiAlgorithm {

    public SuddenDeathAlgorithm(Context context) {
        super(context);
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
        List<Player> filteredPlayerList = getFilteredPlayerList(false);
        List<Game> gameList = null;
        if (getCurrentRound() == 0) {
            if (sittingsMode == SittingsMode.SITTINGS_RANDOM) {
                sittingsPairingAtStart(filteredPlayerList);
            } else {
                calculatePairAtStart(filteredPlayerList);
            }
        } else if (getCurrentRound() < getMaxRound()) {
            filterDeathPlayer(filteredPlayerList);
        } else {
            return gameList;
        }
        List<Player> addedPlayers = new ArrayList<Player>(filteredPlayerList);
        // someone needs bye
        if (filteredPlayerList.size() % 2 == 1) {

            movePlayerWithByeOnLastPosition(filteredPlayerList);
            setPlayerWithBye(filteredPlayerList.get(filteredPlayerList.size() - 1));
            addedPlayers.remove(getPlayerWithBye());

        } else {
            setPlayerWithBye(null);
        }
        // Game list should have size players/2
        gameList = new ArrayList<>(filteredPlayerList.size() / 2);
        Generator<Player> allPermutation = generatePermutations(addedPlayers);
        gameList = generateParings(allPermutation, gameList, filteredPlayerList);
        if (gameList == null) {
            gameList = new ArrayList<>(filteredPlayerList.size() / 2);
            gameList = generateParingsWithPlayersOtherThanLastPlayed(allPermutation, gameList, filteredPlayerList);
        }
        setCurrentRound(getCurrentRound() + 1);

        setRoundList(gameList);
        return gameList;
    }

    private void filterDeathPlayer(@NonNull List<Player> playerList) {
        Predicate<Player> predicate = new EliminationPredicate();
        Iterator<Player> iterator = playerList.iterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            if (predicate.filter(player)) {
                iterator.remove();
            }
        }
    }
}
