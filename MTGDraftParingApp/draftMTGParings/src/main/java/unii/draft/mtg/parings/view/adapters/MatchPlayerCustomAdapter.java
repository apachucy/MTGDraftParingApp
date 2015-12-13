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
import unii.draft.mtg.parings.pojo.Game;
import unii.draft.mtg.parings.pojo.ItemType;

/**
 * Created by Unii on 2015-12-12.
 */
public class MatchPlayerCustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<IAdapterItem> mPlayerGame;
    private Context mContext;

    public MatchPlayerCustomAdapter(Context context, List<IAdapterItem> playerGame) {
        mContext = context;
        mPlayerGame = playerGame;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (getItemViewType(viewType) == ItemType.FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_empty_view_holder, parent, false);
            EmptyViewHolder emptyViewHolder = new EmptyViewHolder(view);
            return emptyViewHolder;

        } //HEADER not exist else if (getItemViewType(viewType) == ItemType.HEADER) {}
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_match_player_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof ViewHolder)) {
            return;
        }

        ViewHolder viewHolder = (ViewHolder) holder;
        Game game = (Game) mPlayerGame.get(position);
        viewHolder.player1TextView.setText(game.getPlayerNameA());
        viewHolder.player2TextView.setText(game.getPlayerNameB());
    }

    @Override
    public int getItemCount() {
        return mPlayerGame.size();
    }


    public class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.row_match_player_player1TextView)
        TextView player1TextView;
        @Bind(R.id.row_match_player_player2TextView)
        TextView player2TextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mPlayerGame.size() - 1 && mPlayerGame.get(position).getItemType() == ItemType.FOOTER) {
            return ItemType.FOOTER;
        } else if (position == 0 && mPlayerGame.get(position).getItemType() == ItemType.HEADER) {
            return ItemType.HEADER;
        } else {
            return position;
        }

    }
}
