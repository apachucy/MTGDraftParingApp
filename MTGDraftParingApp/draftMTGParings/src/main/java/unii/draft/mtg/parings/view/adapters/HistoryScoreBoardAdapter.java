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

import butterknife.BindView;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.view.fragments.history.IDisplayDetailFragment;


public class HistoryScoreBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;
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
//TODO: Make it work

    @Override
    public int getItemViewType(int position) {
        if (mDraftList.size() == 0) {
            return VIEW_TYPE_EMPTY_LIST_PLACEHOLDER;
        }
        return VIEW_TYPE_OBJECT_VIEW;
    }

    @NonNull
    @Override
    public HistoryScoreBoardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (mDraftList.isEmpty() || VIEW_TYPE_EMPTY_LIST_PLACEHOLDER == viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_score_board, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof EmptyViewHolder || mDraftList.isEmpty()) {
            return;
        }
        HistoryScoreBoardAdapter.ViewHolder historyHolder = (ViewHolder) holder;
        Draft selectedDraft = mDraftList.get(position);
        List<Player> playerList = mDatabaseHelper.getAllPlayersInDraft(selectedDraft.getId());
        historyHolder.draftTitleTextView.setText(mContext.getString(R.string.history_score_board_row_draft_name, mDraftList.get(position).getDraftName()));
        historyHolder.draftDateTextView.setText(mContext.getString(R.string.history_score_board_row_draft_date, mDraftList.get(position).getDraftDate()));
        historyHolder.playerWonTextView.setText(mContext.getString(R.string.history_score_board_row_draft_best_player, playerList.get(0).getPlayerName()));
        historyHolder.draftRoundsTextView.setText(mContext.getString(R.string.history_score_board_row_draft_rounds, selectedDraft.getDraftRounds()));
        historyHolder.draftPlayerCountTextView.setText(mContext.getString(R.string.history_score_board_row_draft_count_players, selectedDraft.getNumberOfPlayers()));
    }


    @Override
    public int getItemCount() {
        return mDraftList.size() > 0 ? mDraftList.size() : 1;
    }


    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.history_score_board_titleTextView)
        TextView draftTitleTextView;
        @Nullable
        @BindView(R.id.history_score_board_playerWonTextView)
        TextView playerWonTextView;
        @Nullable
        @BindView(R.id.history_score_board_dateTextView)
        TextView draftDateTextView;
        @Nullable
        @BindView(R.id.history_score_board_roundsTextView)
        TextView draftRoundsTextView;
        @Nullable
        @BindView(R.id.history_score_board_countPlayerTextView)
        TextView draftPlayerCountTextView;
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mDraftList.size() == 0) {
                        return;
                    }
                    mDisplayHistoryScoreBoardDetail.displayDetailView(mDraftList.get(getAdapterPosition()).getId());
                }
            });
        }
    }

    public void removeItem(int positionId) {
        if (mDraftList.size() == 0) {
            return;
        }
        Draft draft = mDraftList.get(positionId);
        mDatabaseHelper.removeDraft(draft);
        mDraftList.remove(draft);
        notifyItemRemoved(positionId);
    }
}
