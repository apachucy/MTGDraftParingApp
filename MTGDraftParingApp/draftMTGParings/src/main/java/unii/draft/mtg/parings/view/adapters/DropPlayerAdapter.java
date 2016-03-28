package unii.draft.mtg.parings.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.pojo.Player;

/**
 * Created by Unii on 2016-03-26.
 */
public class DropPlayerAdapter extends RecyclerView.Adapter<DropPlayerAdapter.ViewHolder> {
    private Context mContext;
    private List<Player> mNotDroppedPlayerList;

    public DropPlayerAdapter(Context context, List<Player> playerList) {
        mContext = context;
        mNotDroppedPlayerList = playerList;
    }

    @Override
    public DropPlayerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_drop_player, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DropPlayerAdapter.ViewHolder holder, final int position) {
        holder.playerNameCheckBox.setOnCheckedChangeListener(null);
        int textColor;
        holder.playerNameCheckBox.setSelected(mNotDroppedPlayerList.get(position).isDropped());
        if (holder.playerNameCheckBox.isSelected()) {
            textColor = mContext.getResources().getColor(R.color.grey_light);
        } else {
            textColor = mContext.getResources().getColor(R.color.black);
        }
        holder.playerNameCheckBox.setTextColor(textColor);

        holder.playerNameCheckBox.setText(mContext.getString(R.string.drop_players_item, mNotDroppedPlayerList.get(position).getPlayerName(), mNotDroppedPlayerList.get(position).getMatchPoints()));
        holder.playerNameCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNotDroppedPlayerList.get(position).setDropped(isChecked);
                int textColor;
                if (isChecked) {
                    textColor = mContext.getResources().getColor(R.color.grey_light);
                } else {
                    textColor = mContext.getResources().getColor(R.color.black);
                }
                holder.playerNameCheckBox.setTextColor(textColor);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotDroppedPlayerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.row_drop_playerCheckBox)
        CheckBox playerNameCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            playerNameCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                }
            });
        }
    }
}
