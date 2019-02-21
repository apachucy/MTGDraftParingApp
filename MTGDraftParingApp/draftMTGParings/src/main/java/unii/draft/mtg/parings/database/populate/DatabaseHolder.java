package unii.draft.mtg.parings.database.populate;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.database.model.Player;

public class DatabaseHolder {
    private List<Draft> draftList;
    private HashMap<Long, Player> playerList;

    public DatabaseHolder(@NonNull List<Draft> draftList, @NonNull List<Player> playerList) {

        this.draftList = draftList;
        for (Player player : playerList) {
            this.playerList.put(player.getId(), player);
        }
    }

    public List<Draft> getDraftList() {
        return draftList;
    }

    public HashMap<Long, Player> getPlayerList() {
        return playerList;
    }
}
