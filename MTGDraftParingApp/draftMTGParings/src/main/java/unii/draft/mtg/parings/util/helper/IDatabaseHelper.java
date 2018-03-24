package unii.draft.mtg.parings.util.helper;


import android.support.annotation.NonNull;

import java.util.List;

import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.database.model.PlayerDraftJoinTable;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;

public interface IDatabaseHelper {

    @NonNull
    Player getPlayer(long draftId, long playerId);

    unii.draft.mtg.parings.database.model.Player getPlayer(long playerId);

    @NonNull
    List<unii.draft.mtg.parings.database.model.Player> getAllPlayerList();

    @NonNull
    List<String> getAllPlayersNames();

    @NonNull
    List<Player> getAllPlayersInDraft(long draftId);

    @NonNull
    List<Draft> getAllDraftsForPlayer(long playerId);

    @NonNull
    List<Draft> getAllDraftList();

    void saveDraft(List<Player> playerDraftList, String draftName, String draftDate, int numberOfRounds);

    void changePlayerName(unii.draft.mtg.parings.database.model.Player player);

    void cleanDatabase();

    long getPlayerPlaceInDraft(long draftId, long playerId);


    List<Game> getAllGamesForDraft(long draftId);

    List<Game> getAllGamesForPlayer(long playerId);

    Game getGameForPlayersAndDraft(long draftId, long playerAId, long playerBId, int round);

}
