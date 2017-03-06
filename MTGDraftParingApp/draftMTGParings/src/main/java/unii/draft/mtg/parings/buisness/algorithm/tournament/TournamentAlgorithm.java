package unii.draft.mtg.parings.buisness.algorithm.tournament;


import android.content.Context;

import java.util.List;

import unii.draft.mtg.parings.buisness.algorithm.base.BaseAlgorithm;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;

public class TournamentAlgorithm extends BaseAlgorithm {
//TODO: remove me
    public TournamentAlgorithm(Context context) {
        super(context);
    }

    @Override
    public List<Game> getParings() {
        return null;
    }

    @Override
    public List<Player> getSortedPlayerList() {
        return null;
    }

    @Override
    public List<Player> getSortedFilteredPlayerList(boolean dropped) {
        return null;
    }

    @Override
    public void setPlayerGameList(List<Game> playerList) {

    }
}
