package unii.draft.mtg.parings.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.view.fragments.IDisplayHistoryScoreBoardDetail;

/**
 * Created by Unii on 2015-12-05.
 */
public class HistoryScoreBoardAdapter extends RecyclerView.Adapter<HistoryScoreBoardAdapter.ViewHolder> {
    private ArrayList<Draft> mDraftList;
    private Context mContext;
    private IDisplayHistoryScoreBoardDetail mDisplayHistoryScoreBoardDetail;

    public HistoryScoreBoardAdapter(Context context, ArrayList<Draft> draftList, IDisplayHistoryScoreBoardDetail displayHistoryScoreBoardDetail) {
        mContext = context;
        mDraftList = draftList;
        mDisplayHistoryScoreBoardDetail = displayHistoryScoreBoardDetail;
    }

    @Override
    public HistoryScoreBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_score_board, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryScoreBoardAdapter.ViewHolder holder, int position) {
        holder.draftTitleTextView.setText(mDraftList.get(position).getDraftName());
        holder.draftDateTextView.setText(mContext.getString(R.string.history_score_board_row_draft_date, mDraftList.get(position).getDraftDate()));
        holder.playerWonTextView.setText(mContext.getString(R.string.history_score_board_row_draft_best_player, mDraftList.get(position).getPlayers().get(0).getPlayerName()));
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
                    mDisplayHistoryScoreBoardDetail.displayHistoryScoreBoardDetail(mDraftList.get(getPosition()).getId());
                }
            });
        }
    }
}
