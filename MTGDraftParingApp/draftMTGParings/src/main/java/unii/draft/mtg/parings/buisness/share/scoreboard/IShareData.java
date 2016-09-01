package unii.draft.mtg.parings.buisness.share.scoreboard;


import java.util.List;

import unii.draft.mtg.parings.logic.pojo.Player;

public interface IShareData {
    String getPlayerWithPoints(List<Player> players);

}
