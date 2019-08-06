package unii.draft.mtg.parings.buisness.share.scoreboard;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.Player;

/**
 * Class will provide formatted data to share via e-mail or other things
 */
public class ShareDataContent implements IShareData {
    private Context mContext;

    public ShareDataContent(@NonNull Context context) {
        mContext = context;
    }

    @Nullable
    @Override
    public String getPlayerWithPoints(@Nullable List<Player> players) {
        if (players == null || players.isEmpty()) {
            return null;
        }

        String formattedOutPut = winnerToString(players.get(0));
        for (Player player : players) {
            formattedOutPut += playerToString(player);
        }
        return formattedOutPut;
    }


    private String winnerToString(@NonNull Player player) {
        return mContext.getString(R.string.shared_data_formatted_winner, player.getPlayerName());
    }

    private String playerToString(@NonNull Player player) {
        return mContext.getString(R.string.shared_data_formatted_player, player.getPlayerName(), player.getMatchPoints());
    }
}
