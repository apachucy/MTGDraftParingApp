package unii.draft.mtg.parings.util.helper;


import java.util.List;

import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.database.model.PlayerDraftJoinTable;
import unii.draft.mtg.parings.logic.pojo.Player;

public interface IDatabaseHelper {

    Player getPlayer(long draftId, long playerId);

    unii.draft.mtg.parings.database.model.Player getPlayer(long playerId);

    List<unii.draft.mtg.parings.database.model.Player> getAllPlayerList();

    List<String> getAllPlayersNames();

    List<Player> getAllPlayersInDraft(long draftId);

    List<Draft> getAllDraftsForPlayer(long playerId);

    List<Draft> getAllDraftList();

    void saveDraft(List<Player> playerDraftList, String draftName, String draftDate, int numberOfRounds);

    void changePlayerName(unii.draft.mtg.parings.database.model.Player player);

    void cleanDatabase();

    long getPlayerPlaceInDraft(long draftId, long playerId);

}
