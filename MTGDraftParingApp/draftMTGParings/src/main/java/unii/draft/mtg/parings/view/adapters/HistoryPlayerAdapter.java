package unii.draft.mtg.parings.view.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.database.model.Player;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.view.fragments.history.IDisplayDetailFragment;

public class HistoryPlayerAdapter extends RecyclerView.Adapter<HistoryPlayerAdapter.ViewHolder> {

    private List<Player> mPlayerList;
    private Context mContext;
    private IDisplayDetailFragment mDisplayHistoryScoreBoardDetail;

    public HistoryPlayerAdapter(Context context, IDatabaseHelper databaseHelper, IDisplayDetailFragment displayHistoryScoreBoardDetail) {
        mContext = context;
        mDisplayHistoryScoreBoardDetail = displayHistoryScoreBoardDetail;
        mPlayerList = databaseHelper.getAllPlayerList();

    }

    @Override
    public HistoryPlayerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_item_player, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryPlayerAdapter.ViewHolder holder, int position) {
        Player player = mPlayerList.get(position);

        holder.mPlayerTextView.setText(player.getPlayerName());
        holder.mPlayerInfoTextView.setText(mContext.getString(R.string.history_player_played_drafts, player.getPlayers().size()));

    }

    @Override
    public int getItemCount() {
        return mPlayerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.history_score_board_playerTextView)
        TextView mPlayerTextView;
        @Bind(R.id.history_score_board_playerInfoTextView)
        TextView mPlayerInfoTextView;

        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.itemView = itemView;
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDisplayHistoryScoreBoardDetail.displayDetailView(mPlayerList.get(getPosition()).getId());
                }
            });
        }
    }
}
