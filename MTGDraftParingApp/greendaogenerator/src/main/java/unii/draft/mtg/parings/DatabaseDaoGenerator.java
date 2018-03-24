package unii.draft.mtg.parings;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class DatabaseDaoGenerator {
    public static void main(String args[]) throws Exception {
        //Create schema add version name and set package
        Schema schema = new Schema(4, "unii.draft.mtg.parings.database.model");

        //add entity - player
        Entity player = schema.addEntity("Player");
        player.addIdProperty();
        player.addStringProperty("PlayerName");


        Entity draft = schema.addEntity("Draft");
        draft.addIdProperty();
        draft.addStringProperty("DraftName");
        draft.addStringProperty("DraftDate");
        draft.addIntProperty("DraftRounds");
        draft.addIntProperty("NumberOfPlayers");

        Entity playerDraftJoinTable = schema.addEntity("PlayerDraftJoinTable");
        playerDraftJoinTable.addIdProperty();
        playerDraftJoinTable.addIntProperty("PlayerMatchPoints");
        playerDraftJoinTable.addFloatProperty("PlayerMatchOverallWin");
        playerDraftJoinTable.addFloatProperty("OponentsMatchOveralWins");
        playerDraftJoinTable.addFloatProperty("PlayerGamesOverallWin");
        playerDraftJoinTable.addFloatProperty("OponentsGamesOverallWin");
        playerDraftJoinTable.addBooleanProperty("Dropped");
        playerDraftJoinTable.addIntProperty("playerPlace");
        //add simple relation - players:jointTable (n:1)

        Property playerDraftJoinTableId = playerDraftJoinTable.addLongProperty("playerDraftJoinTableId").getProperty();
        //playerDraftJoinTable.addToOne(player, playerDraftJoinTableId);
        ToMany playerToPlayerDraftJoinTable = player.addToMany(playerDraftJoinTable, playerDraftJoinTableId);
        playerToPlayerDraftJoinTable.setName("Players");

        //add simple relation - draft:jointTable (n:1)
        Property draftPlayerJoinTableId = playerDraftJoinTable.addLongProperty("draftPlayerJoinTableId").getProperty();
        // playerDraftJoinTable.addToOne(draft, draftPlayerJoinTableId);
        ToMany draftToPlayerDraftJoinTable = draft.addToMany(playerDraftJoinTable, draftPlayerJoinTableId);
        draftToPlayerDraftJoinTable.setName("Drafts");


        Entity game = schema.addEntity("Game");
        game.addIdProperty();
        game.addStringProperty("Winner");
        game.addIntProperty("Games");
        game.addIntProperty("Round");
        game.addIntProperty("Draws");
        game.addIntProperty("PlayerAPoints");
        game.addIntProperty("PlayerBPoints");

        //  Entity playerDraftGameJoinTable = schema.addEntity("playerDraftGameJoinTable");
        //  playerDraftGameJoinTable.addIdProperty();

        Property playerAGameJoinTableId = game.addLongProperty("PlayerAGameJoinTableId").getProperty();
        ToMany playerAToGameJoinTable = player.addToMany(game, playerAGameJoinTableId);
        playerAToGameJoinTable.setName("PlayerAInGame");


        Property playerBGameJoinTableId = game.addLongProperty("PlayerBGameJoinTableId").getProperty();
        ToMany playerBToGameJoinTable = player.addToMany(game, playerBGameJoinTableId);
        playerBToGameJoinTable.setName("PlayerBInGame");

        Property draftGameJoinTableId = game.addLongProperty("DraftGameJoinTableId").getProperty();
        ToMany draftToGameJoinTable = draft.addToMany(game, draftGameJoinTableId);
        draftToGameJoinTable.setName("DraftsInGame");


        //  Property gamesJoinTableId = playerDraftGameJoinTable.addLongProperty("GamesJoinTableId").getProperty();
        //   ToMany gamesToGameJoinTable = player.addToMany(playerDraftGameJoinTable, gamesJoinTableId);
        //  gamesToGameJoinTable.setName("Games");

        //generate
        new DaoGenerator().generateAll(schema, "../draftMTGParings/src/main/java");
    }
}
