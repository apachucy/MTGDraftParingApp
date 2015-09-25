package unii.draft.mtg.parings.view.adapters;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.pojo.Player;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayerAdapter extends BaseAdapter {

    private List<Player> mPlayerList;
    private Context mContext;

    public PlayerAdapter(Context context, List<Player> playerList) {
        mPlayerList = playerList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mPlayerList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mPlayerList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater
                    .inflate(R.layout.row_player_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // setting texts
        viewHolder.playerTextView.setText(mPlayerList.get(position)
                .getPlayerName());
        viewHolder.pointTextView.setText(""
                + mPlayerList.get(position).getMatchPoints());
        viewHolder.pgwTextView.setText(String.format("%.2f",
                mPlayerList.get(position).getPlayerGamesOverallWin()));
        viewHolder.ogwTextView.setText(String.format("%.2f",
                mPlayerList.get(position).getOponentsGamesOverallWin()));
        viewHolder.pmwTextView.setText(String.format("%.2f",
                mPlayerList.get(position).getPlayerMatchOverallWin()));
        viewHolder.omwTextView.setText(String.format("%.2f",
                mPlayerList.get(position).getOponentsMatchOveralWins()));

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.row_playerNameTextView)
        TextView playerTextView;
        @Bind(R.id.row_playerPointTextView)
        TextView pointTextView;
        @Bind(R.id.row_playerOMWTextView)
        TextView omwTextView;
        @Bind(R.id.row_playerPGWTextView)
        TextView pmwTextView;
        @Bind(R.id.row_playerOGWTextView)
        TextView ogwTextView;
        @Bind(R.id.row_playerPMWTextView)
        TextView pgwTextView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
