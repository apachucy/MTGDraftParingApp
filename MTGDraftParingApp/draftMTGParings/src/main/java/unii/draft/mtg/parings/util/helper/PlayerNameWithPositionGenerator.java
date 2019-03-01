package unii.draft.mtg.parings.util.helper;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import unii.draft.mtg.parings.buisness.algorithm.base.PlayersComparator;
import unii.draft.mtg.parings.logic.pojo.Player;

public class PlayerNameWithPositionGenerator {
    private static Comparator<Player> sPlayerComparator = new PlayersComparator();

    private static List<Player> createCopyList(List<Player> playerList) {
        List<Player> copyPlayerList = new ArrayList<>();

        for (Player player : playerList) {
            copyPlayerList.add(new Player(player));
        }
        return copyPlayerList;
    }

    public static List<Player> getListWithNames(List<Player> playerList) {
        List<Player> playerLocalList = createCopyList(playerList);
        int positionOnList = 1;

        for (int i = 0; i < playerLocalList.size(); i++) {
            int nextElement = i + 1;
            if (nextElement < playerLocalList.size()) {
                int better = sPlayerComparator.compare(playerLocalList.get(i), playerLocalList.get(nextElement));
                playerLocalList.get(i).setPlayerName(Integer.toString(positionOnList) + ". " + playerLocalList.get(i).getPlayerName());
                if (better == PlayersComparator.LEFT) {
                    positionOnList = nextElement + 1;
                }
            }
        }
        //set value for last element
        int lastElement = playerLocalList.size() - 1;
        playerLocalList.get(lastElement).setPlayerName(Integer.toString(positionOnList) + ". " + playerLocalList.get(lastElement).getPlayerName());
        return playerLocalList;
    }

}
