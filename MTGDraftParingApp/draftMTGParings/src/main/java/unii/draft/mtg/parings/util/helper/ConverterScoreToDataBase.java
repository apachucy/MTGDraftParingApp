package unii.draft.mtg.parings.util.helper;

import java.util.ArrayList;
import java.util.List;

import unii.draft.mtg.parings.database.model.DaoSession;
import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.database.model.DraftDao;
import unii.draft.mtg.parings.database.model.Player;
import unii.draft.mtg.parings.database.model.PlayerDao;


public class ConverterScoreToDataBase {
    private ConverterScoreToDataBase() {
    }

    public static void saveToDd(DaoSession daoSession, List<unii.draft.mtg.parings.logic.pojo.Player> playersToBeSaved, String draftName, String draftDate) {
        Long draftId = draftDaoSaver(daoSession, draftName, draftDate);
        playerDaoSaver(daoSession, playersToBeSaved, draftId);


    }

    private static List<Player> playerListConverter(List<unii.draft.mtg.parings.logic.pojo.Player> playersToBeSaved, Long draftId) {
        ArrayList<Player> playerList = new ArrayList<>();
        for (unii.draft.mtg.parings.logic.pojo.Player player : playersToBeSaved) {
            playerList.add(playerConverter(player, draftId));
        }
        return playerList;

    }

    private static void playerDaoSaver(DaoSession daoSession, List<unii.draft.mtg.parings.logic.pojo.Player> playersToBeSaved, Long draftId) {
        List<Player> playerList = playerListConverter(playersToBeSaved, draftId);
        PlayerDao playerDao = daoSession.getPlayerDao();
        for (Player player : playerList) {
            playerDao.insert(player);
        }
    }

    private static Long draftDaoSaver(DaoSession daoSession, String draftName, String draftDate) {
        DraftDao draftDao = daoSession.getDraftDao();
        Draft draft = new Draft();
        draft.setDraftDate(draftDate);
        draft.setDraftName(draftName);
        draftDao.insert(draft);

        return draft.getId();

    }

    private static Player playerConverter(unii.draft.mtg.parings.logic.pojo.Player player, Long draftId) {
        Player dbPlayer = new Player();

        dbPlayer.setOponentsGamesOverallWin(player.getOponentsGamesOverallWin());
        dbPlayer.setOponentsMatchOveralWins(player.getOponentsMatchOveralWins());
        dbPlayer.setPlayerGamesOverallWin(player.getPlayerGamesOverallWin());
        dbPlayer.setPlayerMatchOverallWin(player.getPlayerMatchOverallWin());
        dbPlayer.setPlayerMatchPoints(player.getMatchPoints());
        dbPlayer.setPlayerName(player.getPlayerName());
        dbPlayer.setDropped(player.isDropped());
        dbPlayer.setDraftId(draftId);
        return dbPlayer;
    }


}
