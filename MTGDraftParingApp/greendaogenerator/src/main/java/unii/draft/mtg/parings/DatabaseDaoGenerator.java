package unii.draft.mtg.parings;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class DatabaseDaoGenerator {
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "unii.draft.mtg.parings.database.model");

        Entity player = schema.addEntity("Player");
        player.addIdProperty();
        player.addStringProperty("PlayerName");
        player.addIntProperty("PlayerMatchPoints");

        player.addFloatProperty("PlayerMatchOverallWin");
        player.addFloatProperty("OponentsMatchOveralWins");
        player.addFloatProperty("PlayerGamesOverallWin");
        player.addFloatProperty("OponentsGamesOverallWin");


        Entity draft = schema.addEntity("Draft");
        draft.addIdProperty();
        draft.addStringProperty("DraftName");
        draft.addStringProperty("DraftDate");

        Property draftId = player.addLongProperty("DraftId").getProperty();
        player.addToOne(draft,draftId);
        //Property playerId = draft.addLongProperty("PlayerId").getProperty();
        //ToMany playerToDraft = player.addToMany(player,playerId);
        ToMany playerToDraft = draft.addToMany(player,draftId);
        playerToDraft.setName("Players");

        new DaoGenerator().generateAll(schema,"../draftMTGParings/src/main/java");
    }
}
