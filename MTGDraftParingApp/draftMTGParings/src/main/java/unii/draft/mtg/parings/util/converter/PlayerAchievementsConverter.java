package unii.draft.mtg.parings.util.converter;


import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unii.draft.mtg.parings.database.model.Player;
import unii.draft.mtg.parings.database.model.PlayerDraftJoinTable;
import unii.draft.mtg.parings.logic.pojo.PlayerAchievements;

public class PlayerAchievementsConverter implements Converter<PlayerAchievements, Player> {
    @Override
    public PlayerAchievements convert(Player player) {
        int draftPlayed = player.getPlayers().size();
        @SuppressLint("UseSparseArrays") Map<Integer, Integer> playerPositionInDraft = new HashMap<>();
        List<PlayerDraftJoinTable> temp = new ArrayList<>();
        temp.addAll(player.getPlayers());

        for (PlayerDraftJoinTable playerDraftJoinTable : player.getPlayers()) {
            int place = playerDraftJoinTable.getPlayerPlace();
            int count = 0;
            if (playerPositionInDraft.containsKey(place)) {
                continue;
            }
            List<PlayerDraftJoinTable> toRemoveItems = new ArrayList<>();
            if (temp.isEmpty()) {
                break;
            }
            for (PlayerDraftJoinTable checkPlayerPlace : temp) {
                if (checkPlayerPlace.getPlayerPlace() == place) {
                    count++;
                    toRemoveItems.add(checkPlayerPlace);
                }
            }
            playerPositionInDraft.put(place, count);
            temp.removeAll(toRemoveItems);

        }

        return new PlayerAchievements(player, playerPositionInDraft, draftPlayed);
    }

    @Override
    public PlayerAchievements convert(Player player, String data) {
        return null;
    }
}
