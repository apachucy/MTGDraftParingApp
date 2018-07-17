package unii.draft.mtg.parings.buisness.algorithm.manual;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import unii.draft.mtg.parings.buisness.algorithm.base.BaseAlgorithm;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;


public class ManualParingAlgorithm extends BaseAlgorithm {


    public ManualParingAlgorithm(Context context) {
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
        setCurrentRound(getCurrentRound() + 1);
        return getGameRoundList();
    }


    @Override
    public List<Player> getSortedPlayerList() {
        List<Player> playerList = getDraftStartedPlayerList();
        sortPlayers(playerList);
        return playerList;
    }

    @NonNull
    @Override
    public List<Player> getSortedFilteredPlayerList(boolean dropped) {
        List<Player> playerList = getFilteredPlayerList(dropped);
        sortPlayers(playerList);
        return playerList;
    }


    @Override
    public void setPlayerGameList(List<Game> playerList) {
        setRoundList(playerList);
    }

    @Override
    public void setPlayerWithBye(Player playerWithBye) {
        super.setPlayerWithBye(playerWithBye);
        getDraftStartedPlayerList().get(getDraftStartedPlayerList().indexOf(super.getPlayerWithBye())).setHasBye(true);
    }
}
