package unii.draft.mtg.parings.buisness.algorithm.elimination;

import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;

public class EliminationPredicate implements Predicate<Player> {
    @Override
    public boolean filter(Player player) {
        for (Game game : player.getPlayedGame()) {
            if (!game.getWinner().equals(player.getPlayerName())) {
                return true;
            }
        }
        return false;
    }
}
