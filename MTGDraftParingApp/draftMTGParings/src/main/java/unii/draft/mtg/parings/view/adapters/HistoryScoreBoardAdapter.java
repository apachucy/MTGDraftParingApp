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
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.view.fragments.history.IDisplayDetailFragment;


public class HistoryScoreBoardAdapter extends RecyclerView.Adapter<HistoryScoreBoardAdapter.ViewHolder> {
    private List<Draft> mDraftList;
    private Context mContext;
    private IDisplayDetailFragment mDisplayHistoryScoreBoardDetail;
    private IDatabaseHelper mDatabaseHelper;

    public HistoryScoreBoardAdapter(Context context, IDatabaseHelper databaseHelper, IDisplayDetailFragment displayHistoryScoreBoardDetail) {
        mContext = context;
        mDisplayHistoryScoreBoardDetail = displayHistoryScoreBoardDetail;
        mDatabaseHelper = databaseHelper;
        mDraftList = mDatabaseHelper.getAllDraftList();

    }

    @Override
    public HistoryScoreBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_score_board, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryScoreBoardAdapter.ViewHolder holder, int position) {
        Draft selectedDraft = mDraftList.get(position);
        List<Player> playerList = mDatabaseHelper.getAllPlayersInDraft(selectedDraft.getId());
        holder.draftTitleTextView.setText(mContext.getString(R.string.history_score_board_row_draft_name, mDraftList.get(position).getDraftName()));
        holder.draftDateTextView.setText(mContext.getString(R.string.history_score_board_row_draft_date, mDraftList.get(position).getDraftDate()));
        holder.playerWonTextView.setText(mContext.getString(R.string.history_score_board_row_draft_best_player, playerList.get(0).getPlayerName()));
    }

    @Override
    public int getItemCount() {
        return mDraftList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.history_score_board_titleTextView)
        TextView draftTitleTextView;
        @Bind(R.id.history_score_board_playerWonTextView)
        TextView playerWonTextView;
        @Bind(R.id.history_score_board_dateTextView)
        TextView draftDateTextView;

        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDisplayHistoryScoreBoardDetail.displayDetailView(mDraftList.get(getAdapterPosition()).getId());
                }
            });
        }
    }
}
