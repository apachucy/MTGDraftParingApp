package unii.draft.mtg.parings.view.adapters;


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

public class AddPlayerAdapter extends RecyclerView.Adapter<AddPlayerAdapter.ViewHolder> {

    private List<String> mPlayerNames;

    public AddPlayerAdapter(List<String> playerNames) {
        mPlayerNames = playerNames;
    }

    @NonNull
    @Override
    public AddPlayerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player_name, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddPlayerAdapter.ViewHolder holder, int position) {
        String playerName = mPlayerNames.get(position);
        holder.mPlayerName.setText(playerName);
    }

    @Override
    public int getItemCount() {
        return mPlayerNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.row_player)
        TextView mPlayerName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
