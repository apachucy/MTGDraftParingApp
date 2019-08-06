package unii.draft.mtg.parings.buisness.algorithm.automatic;

import android.content.Context;
import androidx.annotation.Nullable;

import org.paukov.combinatorics.Generator;

import java.util.ArrayList;
import java.util.List;

import unii.draft.mtg.parings.buisness.algorithm.base.SemiAlgorithm;
import unii.draft.mtg.parings.buisness.sittings.SittingsMode;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;

public class AutomaticParingAlgorithm extends SemiAlgorithm {


    public AutomaticParingAlgorithm(Context context) {
        super(context);
    }

    @Nullable
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
            if (sittingsMode == SittingsMode.SITTINGS_TOURNAMENT) {
                sittingsPairingAtStart(filteredPlayerList);
            } else {
                calculatePairAtStart(filteredPlayerList);
            }
        } else if (getCurrentRound() < getMaxRound()) {
            sortPlayers(filteredPlayerList);

        } else {
            // game should end so return null
            return gameList;
        }
        List<Player> addedPlayers = new ArrayList<Player>();
        addedPlayers.addAll(filteredPlayerList);
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


}