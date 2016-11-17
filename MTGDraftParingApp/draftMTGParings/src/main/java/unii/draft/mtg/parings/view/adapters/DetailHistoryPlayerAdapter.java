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
import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;

public class DetailHistoryPlayerAdapter extends RecyclerView.Adapter<DetailHistoryPlayerAdapter.ViewHolder> {

    private Context mContext;
    private IDatabaseHelper mDatabaseHelper;
    private List<Draft> mDraftList;
    private long mPlayerId;

    public DetailHistoryPlayerAdapter(Context context, IDatabaseHelper databaseHelper, long playerId) {
        mContext = context;
        mDatabaseHelper = databaseHelper;
        mPlayerId = playerId;
        mDraftList = mDatabaseHelper.getAllDraftsForPlayer(mPlayerId);
    }

    @Override
    public DetailHistoryPlayerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detail_history_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailHistoryPlayerAdapter.ViewHolder holder, int position) {
        Draft selectedDraft = mDraftList.get(position);

        holder.mDraftNameTextView.setText(selectedDraft.getDraftName());
        holder.mPlayerDraftPositionTextView.setText(mContext.getString(R.string.history_player_detail_place,
                mDatabaseHelper.getPlayerPlaceInDraft(selectedDraft.getId(), mPlayerId), selectedDraft.getNumberOfPlayers()));
    }

    @Override
    public int getItemCount() {
        return mDraftList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.history_detail_player_draft_name)
        TextView mDraftNameTextView;
        @Bind(R.id.history_detail_player_draft_position)
        TextView mPlayerDraftPositionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
