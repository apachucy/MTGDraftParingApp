package unii.draft.mtg.parings.view.adapters;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.Game;

public class PlayerRivalisationAdapter extends RecyclerView.Adapter<PlayerRivalisationAdapter.ViewHolder> {
    private final List<Game> playedGames;
    private final Context context;

    public PlayerRivalisationAdapter(List<Game> playedGames, Context context) {
        this.playedGames = playedGames;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group_sub_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String playerAName = playedGames.get(position).getPlayerNameA();
        String playerBName = playedGames.get(position).getPlayerNameB();
        holder.mPlayersName.setText(context.getString(R.string.history_group_players_name, playerAName, playerBName));
        int playerAWinningPoints = playedGames.get(position).getPlayerAPoints();
        int playerBWinningPoints = playedGames.get(position).getPlayerBPoints();
        int draws = playedGames.get(position).getDraws();
        holder.mPoints.setText(context.getString(R.string.history_group_player_results, playerAWinningPoints, draws, playerBWinningPoints));

    }

    @Override
    public int getItemCount() {
        return playedGames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.group_players_names)
        TextView mPlayersName;
        @Nullable
        @BindView(R.id.group_players_results)
        TextView mPoints;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
