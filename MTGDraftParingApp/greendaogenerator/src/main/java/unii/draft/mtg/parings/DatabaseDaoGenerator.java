package unii.draft.mtg.parings;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class DatabaseDaoGenerator {
    public static void main(String args[]) throws Exception {
        //Create schema add version name and set package
        Schema schema = new Schema(2, "unii.draft.mtg.parings.database.model");
        //add entity - player
        Entity player = schema.addEntity("Player");
        player.addIdProperty();
        player.addStringProperty("PlayerName");
        player.addIntProperty("PlayerMatchPoints");
        player.addFloatProperty("PlayerMatchOverallWin");
        player.addFloatProperty("OponentsMatchOveralWins");
        player.addFloatProperty("PlayerGamesOverallWin");
        player.addFloatProperty("OponentsGamesOverallWin");
        player.addBooleanProperty("Dropped");
        //add entity draft
        Entity draft = schema.addEntity("Draft");
        draft.addIdProperty();
        draft.addStringProperty("DraftName");
        draft.addStringProperty("DraftDate");

        //add simple relation - players:draft (n:1)
        //create new property in player entity
        Property draftId = player.addLongProperty("DraftId").getProperty();
        player.addToOne(draft,draftId);
        ToMany playerToDraft = draft.addToMany(player,draftId);
        playerToDraft.setName("Players");
        //generate
        new DaoGenerator().generateAll(schema,"../draftMTGParings/src/main/java");
    }
}
