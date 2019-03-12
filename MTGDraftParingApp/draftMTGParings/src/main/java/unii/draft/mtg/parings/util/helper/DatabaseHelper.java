package unii.draft.mtg.parings.util.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import unii.draft.mtg.parings.buisness.algorithm.base.PlayersComparator;
import unii.draft.mtg.parings.database.model.DaoMaster;
import unii.draft.mtg.parings.database.model.DaoSession;
import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.database.model.DraftDao;
import unii.draft.mtg.parings.database.model.GameDao;
import unii.draft.mtg.parings.database.model.PlayerDao;
import unii.draft.mtg.parings.database.model.PlayerDraftJoinTable;
import unii.draft.mtg.parings.database.model.PlayerDraftJoinTableDao;
import unii.draft.mtg.parings.database.populate.DraftExporter;
import unii.draft.mtg.parings.database.populate.Information;
import unii.draft.mtg.parings.logic.dagger.ApplicationComponent;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.util.config.BaseConfig;

public class DatabaseHelper implements IDatabaseHelper {
    private static final int NO_MATCH = -1;
    private Context mContext;
    @Nullable
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public DatabaseHelper(Context context, @NonNull ApplicationComponent component) {
        component.inject(this);
        mContext = context;
        if (mHelper == null) {
            mHelper = new DaoMaster.DevOpenHelper(mContext, BaseConfig.DATABASE_NAME, null);
            mDb = mHelper.getWritableDatabase();
            mDaoMaster = new DaoMaster(mDb);
            mDaoSession = mDaoMaster.newSession();
        }
    }

    @NonNull
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

    @NonNull
    @Override
    public List<unii.draft.mtg.parings.database.model.Player> getAllPlayerList() {
        List<unii.draft.mtg.parings.database.model.Player> playerList = new ArrayList<>();
        playerList.addAll(mDaoSession.getPlayerDao().loadAll());
        return playerList;
    }

    @NonNull
    @Override
    public List<String> getAllPlayersNames() {
        List<String> playerNameList = new ArrayList<>();
        List<unii.draft.mtg.parings.database.model.Player> playerList = getAllPlayerList();
        for (unii.draft.mtg.parings.database.model.Player player : playerList) {
            playerNameList.add(player.getPlayerName());
        }
        return playerNameList;
    }

    @NonNull
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

    @NonNull
    @Override
    public List<Draft> getAllDraftsForPlayer(long playerId) {
        List<PlayerDraftJoinTable> playerDraftJoinTableList = mDaoSession.getPlayerDraftJoinTableDao().queryBuilder()
                .where(PlayerDraftJoinTableDao.Properties.PlayerDraftJoinTableId.eq(playerId)).build().list();
        List<Draft> draftList = new ArrayList<>();

        for (PlayerDraftJoinTable playerDraftJoinTable : playerDraftJoinTableList) {
            Draft draft = mDaoSession.getDraftDao().load(playerDraftJoinTable.getDraftPlayerJoinTableId());
            if (draft != null) {
                draftList.add(draft);
            }
        }

        return draftList;
    }

    @NonNull
    @Override
    public List<Draft> getAllDraftList() {
        List<Draft> draftList = new ArrayList<>();
        draftList.addAll(mDaoSession.getDraftDao().loadAll());
        return draftList;
    }

    @Override
    public void saveDraft(@NonNull List<Player> playerDraftList, String draftName, String draftDate, int numberOfRounds) {
        long draftId = saveDraftDao(draftName, draftDate, playerDraftList.size(), numberOfRounds);
        int position = 0;
        int EQUAL = 0;
        PlayersComparator comparator = new PlayersComparator();
        for (int playerIndex = 0; playerIndex < playerDraftList.size(); playerIndex++) {
            Player player = playerDraftList.get(playerIndex);
            long playerId = getPlayerId(player.getPlayerName());
            if (playerId == NO_MATCH) {
                playerId = savePlayerDao(player.getPlayerName());
            }
            savePlayerToDraftDao(playerId, draftId, player, position);
            for (Game game : player.getPlayedGame()) {
                long playerIdA = getPlayerId(game.getPlayerNameA());
                long playerIdB = getPlayerId(game.getPlayerNameB());
                if (playerIdB != NO_MATCH && playerIdA != NO_MATCH
                        && NO_MATCH == getIdForGame(draftId, playerIdA, playerIdB, game.getRound())
                        && NO_MATCH == getIdForGame(draftId, playerIdB, playerIdA, game.getRound())) {
                    saveGameToDraft(game, draftId, playerIdA, playerIdB);
                }
            }
            //check
            int nextIndex = playerIndex + 1;
            Player nextPlayer = nextIndex < playerDraftList.size() ? playerDraftList.get(nextIndex) : null;
            if (nextPlayer == null || !(comparator.compare(player, nextPlayer) == EQUAL)) {
                position = nextIndex;
            }
        }


    }

    @Override
    public void changePlayerName(unii.draft.mtg.parings.database.model.Player player) {
        mDaoSession.getPlayerDao().update(player);
    }

    @Override
    public void cleanDatabase() {
        mDaoSession.clear();

        for (AbstractDao abstractDao : mDaoSession.getAllDaos()) {
            abstractDao.deleteAll();
        }
    }

    @Override
    public long getPlayerPlaceInDraft(long draftId, long playerId) {
        PlayerDraftJoinTable playerDraftJoinTable = mDaoSession.getPlayerDraftJoinTableDao().queryBuilder().where(PlayerDraftJoinTableDao.Properties.PlayerDraftJoinTableId.eq(playerId), PlayerDraftJoinTableDao.Properties.DraftPlayerJoinTableId.eq(draftId)).build().unique();
        return playerDraftJoinTable.getPlayerPlace();
    }

    @Override
    public List<Game> getAllGamesForDraft(long draftId) {
        List<unii.draft.mtg.parings.database.model.Game> loadedGames = mDaoSession.getGameDao().queryBuilder().where(GameDao.Properties.DraftGameJoinTableId.eq(draftId)).build().list();
        List<Game> convertedGames = new ArrayList<>();

        for (unii.draft.mtg.parings.database.model.Game game : loadedGames) {
            unii.draft.mtg.parings.database.model.Player playerA = getPlayer(game.getPlayerAGameJoinTableId());
            unii.draft.mtg.parings.database.model.Player playerB = getPlayer(game.getPlayerBGameJoinTableId());

            if (playerA == null || playerB == null) {
                continue;
            }

            String playerAName = playerA.getPlayerName();
            String playerBName = playerB.getPlayerName();
            convertedGames.add(new Game(game, playerAName, playerBName));
        }

        return convertedGames;
    }

    @Override
    public List<Game> getAllGamesForPlayer(long playerId) {
        List<unii.draft.mtg.parings.database.model.Game> loadedGames = mDaoSession.getGameDao().queryBuilder().whereOr(GameDao.Properties.PlayerAGameJoinTableId.eq(playerId), GameDao.Properties.PlayerBGameJoinTableId.eq(playerId)).build().list();
        List<Game> convertedGames = new ArrayList<>();

        for (unii.draft.mtg.parings.database.model.Game game : loadedGames) {
            unii.draft.mtg.parings.database.model.Player playerA = getPlayer(game.getPlayerAGameJoinTableId());
            unii.draft.mtg.parings.database.model.Player playerB = getPlayer(game.getPlayerBGameJoinTableId());

            if (playerA == null || playerB == null) {
                continue;
            }

            String playerAName = playerA.getPlayerName();
            String playerBName = playerB.getPlayerName();
            convertedGames.add(new Game(game, playerAName, playerBName));
        }

        return convertedGames;
    }

    @Override
    public List<Game> getAllGamesForPlayerInDraft(long playerId, long draftId) {
        List<unii.draft.mtg.parings.database.model.Game> loadedGames = mDaoSession.getGameDao().queryBuilder()
                .where(GameDao.Properties.DraftGameJoinTableId.eq(draftId))
                .whereOr(GameDao.Properties.PlayerAGameJoinTableId.eq(playerId), GameDao.Properties.PlayerBGameJoinTableId.eq(playerId))
                .build().list();

        List<Game> convertedGames = new ArrayList<>();

        for (unii.draft.mtg.parings.database.model.Game game : loadedGames) {
            unii.draft.mtg.parings.database.model.Player playerA = getPlayer(game.getPlayerAGameJoinTableId());
            unii.draft.mtg.parings.database.model.Player playerB = getPlayer(game.getPlayerBGameJoinTableId());

            if (playerA == null || playerB == null) {
                continue;
            }

            String playerAName = playerA.getPlayerName();
            String playerBName = playerB.getPlayerName();
            convertedGames.add(new Game(game, playerAName, playerBName));
        }

        return convertedGames;
    }

    private long getIdForGame(long draftId, long playerAId, long playerBId, int round) {
        unii.draft.mtg.parings.database.model.Game loadedGame = mDaoSession.getGameDao().queryBuilder().where(GameDao.Properties.PlayerAGameJoinTableId.eq(playerAId),
                GameDao.Properties.PlayerBGameJoinTableId.eq(playerBId),
                GameDao.Properties.DraftGameJoinTableId.eq(draftId),
                GameDao.Properties.Round.eq(round)).build().unique();

        if (loadedGame == null) {
            return NO_MATCH;
        }
        return loadedGame.getId();
    }

    @Override
    public Game getGameForPlayersAndDraft(long draftId, long playerAId, long playerBId, int round) {
        unii.draft.mtg.parings.database.model.Game loadedGame = mDaoSession.getGameDao().queryBuilder().where(GameDao.Properties.PlayerAGameJoinTableId.eq(playerAId),
                GameDao.Properties.PlayerBGameJoinTableId.eq(playerBId),
                GameDao.Properties.DraftGameJoinTableId.eq(draftId),
                GameDao.Properties.Round.eq(round)).build().unique();

        String playerAName = getPlayer(playerAId).getPlayerName();
        String playerBName = getPlayer(playerBId).getPlayerName();

        if (loadedGame == null) {
            return new Game(playerAName, playerBName, round);
        }

        return new Game(loadedGame, playerAName, playerBName);
    }

    @Override
    public void removeDraft(Draft draft) {
        removeDraft(draft.getId());
    }

    @Override
    public void removeDraft(long draftId) {
        List<unii.draft.mtg.parings.database.model.Game> loadedGames = mDaoSession.getGameDao().queryBuilder().where(GameDao.Properties.DraftGameJoinTableId.eq(draftId)).build().list();
        for (unii.draft.mtg.parings.database.model.Game game : loadedGames) {
            mDaoSession.getGameDao().delete(game);
        }

        Draft draft = mDaoSession.getDraftDao().queryBuilder().where(DraftDao.Properties.Id.eq(draftId)).build().unique();
        mDaoSession.getDraftDao().delete(draft);

        List<PlayerDraftJoinTable> playerDraftJoinTableList = draft.getDrafts();

        for (PlayerDraftJoinTable playerDraftJoinTable : playerDraftJoinTableList) {
            mDaoSession.getPlayerDraftJoinTableDao().delete(playerDraftJoinTable);
        }
    }

    @Override
    public void removePlayer(unii.draft.mtg.parings.database.model.Player player) {
        for (PlayerDraftJoinTable table : player.getPlayers()) {
            mDaoSession.getPlayerDraftJoinTableDao().delete(table);
        }
        mDaoSession.getPlayerDao().delete(player);
    }

    @Override
    public List<DraftExporter> exportDraftDatabase() {
        List<DraftExporter> draftExporterList = new ArrayList<>();
        List<Draft> draftList = getAllDraftList();
        for (Draft draft : draftList) {
            draftExporterList.add(exportDraft(draft));
        }
        return draftExporterList;
    }


    private DraftExporter exportDraft(Draft draft) {
        //TODO: export bye ?
        List<Player> playerList = new ArrayList<>();

        for (PlayerDraftJoinTable playerDraftJoinTable : draft.getDrafts()) {
            playerList.add(exportPlayer(draft.getId(), getPlayer(playerDraftJoinTable.getPlayerDraftJoinTableId()), playerDraftJoinTable));
        }
        DraftExporter draftExporter = new DraftExporter(playerList, draft.getDraftName(), draft.getDraftDate(), draft.getDraftRounds());
        return draftExporter;
    }

    private Player exportPlayer(long draftId, unii.draft.mtg.parings.database.model.Player player, PlayerDraftJoinTable playerDraftJoinTable) {
        Player oldPlayer = new Player(player, playerDraftJoinTable);
        oldPlayer.setPlayedGame(getAllGamesForPlayerInDraft(player.getId(), draftId));
        return oldPlayer;
    }

    private Game exportGame(unii.draft.mtg.parings.database.model.Game game, String playerAName, String playerBName) {
        Game playedGame = new Game(game, playerAName, playerBName);
        return playedGame;
    }

    @Override
    public Information importDraftDatabase(@NonNull List<DraftExporter> database) {
        try {
            for (DraftExporter draftExporter : database) {
                importDraft(draftExporter);
            }
            return Information.SUCCESS;
        } catch (Error e) {
            return Information.ERROR_DATA_CORRUPTED;
        }
    }

    private Information importDraft(DraftExporter database) {
        saveDraft(database.getPlayerList(), database.getDraftName(), database.getDraftDate(), database.getDraftRounds());
        return Information.SUCCESS;
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

    private long saveGameToDraft(Game game, long draftId, long playerAId, long playerBId) {
        unii.draft.mtg.parings.database.model.Game saveGame = new unii.draft.mtg.parings.database.model.Game();
        saveGame.setDraftGameJoinTableId(draftId);
        saveGame.setPlayerAGameJoinTableId(playerAId);
        saveGame.setPlayerBGameJoinTableId(playerBId);
        saveGame.setDraws(game.getDraws());
        saveGame.setRound(game.getRound());
        saveGame.setWinner(game.getWinner());

        saveGame.setPlayerAPoints(game.getPlayerAPoints());
        saveGame.setPlayerBPoints(game.getPlayerBPoints());

        saveGame.setGames(game.getGamesPlayed());
        mDaoSession.getGameDao().insert(saveGame);
        return saveGame.getId();

    }

    private long savePlayerToDraftDao(long playerId, long draftId, @NonNull Player player, int position) {
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
