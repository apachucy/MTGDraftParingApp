package unii.draft.mtg.parings.util.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import unii.draft.mtg.parings.database.model.DaoMaster;
import unii.draft.mtg.parings.database.model.DaoSession;
import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.database.model.DraftDao;
import unii.draft.mtg.parings.database.model.PlayerDao;
import unii.draft.mtg.parings.database.model.PlayerDraftJoinTable;
import unii.draft.mtg.parings.database.model.PlayerDraftJoinTableDao;
import unii.draft.mtg.parings.logic.dagger.ApplicationComponent;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.util.config.BaseConfig;

public class DatabaseHelper implements IDatabaseHelper {
    private static final int NO_MATCH = -1;
    private Context mContext;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public DatabaseHelper(Context context, ApplicationComponent component) {
        component.inject(this);
        mContext = context;
        if (mHelper == null) {
            mHelper = new DaoMaster.DevOpenHelper(mContext, BaseConfig.DATABASE_NAME, null);
            mDb = mHelper.getWritableDatabase();
            mDaoMaster = new DaoMaster(mDb);
            mDaoSession = mDaoMaster.newSession();
        }
    }

    @Override
    public Player getPlayer(long draftId, long playerId) {
        unii.draft.mtg.parings.database.model.Player player = mDaoSession.getPlayerDao().load(playerId);
        PlayerDraftJoinTable playerDraftJoinTable = mDaoSession.getPlayerDraftJoinTableDao().queryBuilder()
                .where(PlayerDraftJoinTableDao.Properties.DraftPlayerJoinTableId.eq(draftId), PlayerDraftJoinTableDao.Properties.PlayerDraftJoinTableId.eq(playerId
                )).build().unique();

        return new Player(player, playerDraftJoinTable);

    }

    @Override
    public unii.draft.mtg.parings.database.model.Player getPlayer(long playerId) {
        unii.draft.mtg.parings.database.model.Player player = mDaoSession.getPlayerDao().load(playerId);
        return player;
    }

    @Override
    public List<unii.draft.mtg.parings.database.model.Player> getAllPlayerList() {
        List<unii.draft.mtg.parings.database.model.Player> playerList = new ArrayList<>();
        playerList.addAll(mDaoSession.getPlayerDao().loadAll());
        return playerList;
    }

    @Override
    public List<String> getAllPlayersNames() {
        List<String> playerNameList = new ArrayList<>();
        List<unii.draft.mtg.parings.database.model.Player> playerList = getAllPlayerList();
        for (unii.draft.mtg.parings.database.model.Player player : playerList) {
            playerNameList.add(player.getPlayerName());
        }
        return playerNameList;
    }

    @Override
    public List<Player> getAllPlayersInDraft(long draftId) {
        List<PlayerDraftJoinTable> playerDraftJoinTableList = mDaoSession.getPlayerDraftJoinTableDao().queryBuilder()
                .where(PlayerDraftJoinTableDao.Properties.DraftPlayerJoinTableId.eq(draftId)).build().list();
        List<Player> playerList = new ArrayList<>();

        for (PlayerDraftJoinTable playerDraftJoinTable : playerDraftJoinTableList) {
            unii.draft.mtg.parings.database.model.Player playerDatabase = mDaoSession.getPlayerDao().load(playerDraftJoinTable.getPlayerDraftJoinTableId());
            playerList.add(new Player(playerDatabase, playerDraftJoinTable));
        }

        return playerList;
    }

    @Override
    public List<Draft> getAllDraftsForPlayer(long playerId) {
        List<PlayerDraftJoinTable> playerDraftJoinTableList = mDaoSession.getPlayerDraftJoinTableDao().queryBuilder()
                .where(PlayerDraftJoinTableDao.Properties.PlayerDraftJoinTableId.eq(playerId)).build().list();
        List<Draft> draftList = new ArrayList<>();

        for (PlayerDraftJoinTable playerDraftJoinTable : playerDraftJoinTableList) {
            Draft draft = mDaoSession.getDraftDao().load(playerDraftJoinTable.getDraftPlayerJoinTableId());
            draftList.add(draft);
        }

        return draftList;
    }

    @Override
    public List<Draft> getAllDraftList() {
        List<Draft> draftList = new ArrayList<>();
        draftList.addAll(mDaoSession.getDraftDao().loadAll());
        return draftList;
    }

    @Override
    public void saveDraft(List<Player> playerDraftList, String draftName, String draftDate, int numberOfRounds) {
        long draftId = saveDraftDao(draftName, draftDate, playerDraftList.size(), numberOfRounds);
        int position = 0;
        for (Player player : playerDraftList) {
            long playerId = getPlayerId(player.getPlayerName());
            if (playerId == NO_MATCH) {
                playerId = savePlayerDao(player.getPlayerName());
            }
            savePlayerToDraftDao(playerId, draftId, player, position);
            position++;
        }
    }

    @Override
    public void changePlayerName(unii.draft.mtg.parings.database.model.Player player) {
        mDaoSession.getPlayerDao().update(player);
    }

    @Override
    public void cleanDatabase() {
        mDaoSession.getPlayerDraftJoinTableDao().deleteAll();
        mDaoSession.getPlayerDao().deleteAll();
        mDaoSession.getDraftDao().deleteAll();
    }

    @Override
    public long getPlayerPlaceInDraft(long draftId, long playerId) {
        PlayerDraftJoinTable playerDraftJoinTable = mDaoSession.getPlayerDraftJoinTableDao().queryBuilder().where(PlayerDraftJoinTableDao.Properties.PlayerDraftJoinTableId.eq(playerId), PlayerDraftJoinTableDao.Properties.DraftPlayerJoinTableId.eq(draftId)).build().unique();
        return playerDraftJoinTable.getPlayerPlace();
    }


    private long saveDraftDao(String draftName, String draftDate, int numberOfPlayers, int numberOfRounds) {
        Draft draftToBeSaved = new Draft();
        draftToBeSaved.setDraftDate(draftDate);
        draftToBeSaved.setDraftName(draftName);
        draftToBeSaved.setDraftRounds(numberOfRounds);
        draftToBeSaved.setNumberOfPlayers(numberOfPlayers);

        mDaoSession.getDraftDao().insert(draftToBeSaved);
        return draftToBeSaved.getId();
    }

    private long savePlayerDao(String playerName) {
        unii.draft.mtg.parings.database.model.Player player = new unii.draft.mtg.parings.database.model.Player();
        player.setPlayerName(playerName);

        mDaoSession.getPlayerDao().insert(player);
        return player.getId();
    }

    private long getPlayerId(String playerName) {
        unii.draft.mtg.parings.database.model.Player player = mDaoSession.getPlayerDao().queryBuilder().where(PlayerDao.Properties.PlayerName.like(playerName)).build().unique();
        if (player != null) {
            return player.getId();
        } else return NO_MATCH;
    }

    private long savePlayerToDraftDao(long playerId, long draftId, Player player, int position) {
        PlayerDraftJoinTable playerDraftJoinTable = new PlayerDraftJoinTable();
        playerDraftJoinTable.setDropped(player.isDropped());
        playerDraftJoinTable.setOponentsGamesOverallWin(player.getOponentsGamesOverallWin());
        playerDraftJoinTable.setOponentsMatchOveralWins(player.getOponentsMatchOveralWins());
        playerDraftJoinTable.setPlayerGamesOverallWin(player.getPlayerGamesOverallWin());
        playerDraftJoinTable.setPlayerMatchOverallWin(player.getPlayerMatchOverallWin());
        playerDraftJoinTable.setPlayerMatchPoints(player.getMatchPoints());
        playerDraftJoinTable.setPlayerPlace(position + 1);
        playerDraftJoinTable.setPlayerDraftJoinTableId(playerId);
        playerDraftJoinTable.setDraftPlayerJoinTableId(draftId);

        mDaoSession.getPlayerDraftJoinTableDao().insert(playerDraftJoinTable);
        return playerDraftJoinTable.getId();
    }
}
