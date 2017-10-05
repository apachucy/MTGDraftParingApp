package unii.draft.mtg.parings.view.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
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
        holder.mPlayerInfoTextView.setText(mContext.getString(R.string.history_player_played_drafts, player.getDraftPlayed()));
        int playerWinsDraft = 0;
        if (player.getPlaceInDrafts().containsKey(PLAYER_PLACE_1)) {
            playerWinsDraft = player.getPlaceInDrafts().get(PLAYER_PLACE_1);
        }
        holder.mPlayerDraftWinTextView.setText(mContext.getString(R.string.history_player_wins_drafts, playerWinsDraft));
    }

    @Override
    public int getItemCount() {
        return mPlayerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @Bind(R.id.history_score_board_playerTextView)
        TextView mPlayerTextView;
        @Nullable
        @Bind(R.id.history_score_board_playerInfoTextView)
        TextView mPlayerInfoTextView;
        @Nullable
        @Bind(R.id.history_score_board_playerDraftWinTextView)
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
