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
import unii.draft.mtg.parings.logic.pojo.PlayerDraft;

public class DetailHistoryPlayerAdapter extends RecyclerView.Adapter<DetailHistoryPlayerAdapter.ViewHolder> {

    private Context mContext;
    private List<PlayerDraft> mDraftList;

    public DetailHistoryPlayerAdapter(Context context, List<PlayerDraft> list) {
        mContext = context;
        mDraftList = list;
    }

    @NonNull
    @Override
    public DetailHistoryPlayerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detail_history_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailHistoryPlayerAdapter.ViewHolder holder, int position) {
        PlayerDraft selectedDraft = mDraftList.get(position);

        holder.mDraftNameTextView.setText(selectedDraft.getDraftName());
        holder.mPlayerDraftPositionTextView.setText(mContext.getString(R.string.history_player_detail_place,
                selectedDraft.getPlace(), selectedDraft.getTotalPlayers()));
    }

    @Override
    public int getItemCount() {
        return mDraftList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.history_detail_player_draft_name)
        TextView mDraftNameTextView;
        @Nullable
        @BindView(R.id.history_detail_player_draft_position)
        TextView mPlayerDraftPositionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
