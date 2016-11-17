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

/**
 * DisplaySittings in Random order
 */
public class SittingsPlayerAdapter extends RecyclerView.Adapter<SittingsPlayerAdapter.ViewHolder> {
    private List<String> mPlayerNameList;
    private Context mContext;

    public SittingsPlayerAdapter(Context context, List<String> playerNameList) {
        mContext = context;
        mPlayerNameList = playerNameList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player_name, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int displayNumber = position + 1;
        holder.playerTextView.setText(mContext.getString(R.string.player_sittings_position, displayNumber, mPlayerNameList.get(position)));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.row_player)
        TextView playerTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    @Override
    public int getItemCount() {
        return mPlayerNameList.size();
    }
}
