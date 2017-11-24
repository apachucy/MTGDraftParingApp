package unii.draft.mtg.parings.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.Player;


public class DropPlayerAdapter extends RecyclerView.Adapter<DropPlayerAdapter.ViewHolder> {
    private Context mContext;
    private List<Player> mNotDroppedPlayerList;

    public DropPlayerAdapter(Context context, List<Player> playerList) {
        mContext = context;
        mNotDroppedPlayerList = playerList;
    }

    @NonNull
    @Override
    public DropPlayerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_drop_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DropPlayerAdapter.ViewHolder holder, final int position) {
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
        @Nullable
        @BindView(R.id.row_drop_playerCheckBox)
        CheckBox playerNameCheckBox;

        public ViewHolder(@NonNull View itemView) {
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
