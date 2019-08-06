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
import unii.draft.mtg.parings.logic.pojo.PlayerAchievements;
import unii.draft.mtg.parings.view.fragments.history.IDisplayDetailFragment;

public class HistoryPlayerAdapter extends RecyclerView.Adapter<HistoryPlayerAdapter.ViewHolder> {

    public static final int PLAYER_PLACE_1 = 1;
    private List<PlayerAchievements> mPlayerList;
    private Context mContext;
    private IDisplayDetailFragment mDisplayHistoryScoreBoardDetail;

    public HistoryPlayerAdapter(Context context, List<PlayerAchievements> playerAchievements, IDisplayDetailFragment displayHistoryScoreBoardDetail) {
        mContext = context;
        mDisplayHistoryScoreBoardDetail = displayHistoryScoreBoardDetail;
        mPlayerList = playerAchievements;
    }

    @NonNull
    @Override
    public HistoryPlayerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_item_player, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryPlayerAdapter.ViewHolder holder, int position) {
        PlayerAchievements player = mPlayerList.get(position);

        holder.mPlayerTextView.setText(player.getPlayerName());
        holder.mPlayerInfoTextView.setText(Integer.toString(player.getDraftPlayed()));
        int playerWinsDraft = 0;
        if (player.getPlaceInDrafts().containsKey(PLAYER_PLACE_1)) {
            playerWinsDraft = player.getPlaceInDrafts().get(PLAYER_PLACE_1);
        }
        holder.mPlayerDraftWinTextView.setText(Integer.toString(playerWinsDraft));
    }

    @Override
    public int getItemCount() {
        return mPlayerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.history_score_board_playerTextView)
        TextView mPlayerTextView;
        @Nullable
        @BindView(R.id.history_score_board_playerInfoTextView)
        TextView mPlayerInfoTextView;
        @Nullable
        @BindView(R.id.history_score_board_playerDraftWinTextView)
        TextView mPlayerDraftWinTextView;
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.itemView = itemView;
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDisplayHistoryScoreBoardDetail.displayDetailView(mPlayerList.get(getPosition()).getPlayerId());
                }
            });
        }
    }
}
