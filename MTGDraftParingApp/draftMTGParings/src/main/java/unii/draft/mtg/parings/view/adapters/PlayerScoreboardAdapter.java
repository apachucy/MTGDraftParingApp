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
import unii.draft.mtg.parings.pojo.ItemType;
import unii.draft.mtg.parings.pojo.Player;

/**
 * Created by Unii on 2015-12-11.
 */
public class PlayerScoreboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<IAdapterItem> mPlayerList;
    private Context mContext;

    public PlayerScoreboardAdapter(Context context, List<IAdapterItem> playerList) {
        mPlayerList = playerList;
        mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (getItemViewType(viewType) == ItemType.HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_player_list, parent, false);
            EmptyViewHolder emptyViewHolder = new EmptyViewHolder(view);
            return emptyViewHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == mPlayerList.size() - 1 && mPlayerList.get(position).getItemType() == ItemType.FOOTER) {
            return ItemType.FOOTER;
        } else if (position == 0 && mPlayerList.get(position).getItemType() == ItemType.HEADER) {
            return ItemType.HEADER;
        } else {
            return position;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EmptyViewHolder) {
            return;
        }
        Player player = (Player) mPlayerList.get(position);
        ViewHolder viewHolder = ((ViewHolder) holder);
        //set text fields color if player dropped
        int textColor;
        if (player.isDropped()) {
            textColor = mContext.getResources().getColor(R.color.grey_light);
        } else {
            textColor = mContext.getResources().getColor(R.color.black);
        }
        viewHolder.playerTextView.setTextColor(textColor);
        viewHolder.pointTextView.setTextColor(textColor);
        viewHolder.pgwTextView.setTextColor(textColor);
        viewHolder.ogwTextView.setTextColor(textColor);
        viewHolder.pmwTextView.setTextColor(textColor);
        viewHolder.omwTextView.setTextColor(textColor);
        // setting texts
        viewHolder.playerTextView.setText(player
                .getPlayerName());
        viewHolder.pointTextView.setText(""
                + player.getMatchPoints());
        viewHolder.pgwTextView.setText(String.format("%.2f",
                player.getPlayerGamesOverallWin()));
        viewHolder.ogwTextView.setText(String.format("%.2f",
                player.getOponentsGamesOverallWin()));
        viewHolder.pmwTextView.setText(String.format("%.2f",
                player.getPlayerMatchOverallWin()));
        viewHolder.omwTextView.setText(String.format("%.2f",
                player.getOponentsMatchOveralWins()));

    }

    @Override
    public int getItemCount() {
        return mPlayerList.size();
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.row_playerNameTextView)
        TextView playerTextView;
        @Bind(R.id.row_playerPointTextView)
        TextView pointTextView;
        @Bind(R.id.row_playerOMWTextView)
        TextView omwTextView;
        @Bind(R.id.row_playerPGWTextView)
        TextView pgwTextView;
        @Bind(R.id.row_playerOGWTextView)
        TextView ogwTextView;
        @Bind(R.id.row_playerPMWTextView)
        TextView pmwTextView;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
