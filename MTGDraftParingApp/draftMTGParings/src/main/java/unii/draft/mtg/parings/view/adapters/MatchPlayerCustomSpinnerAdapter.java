package unii.draft.mtg.parings.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.Player;

/**
 * Created by Arkadiusz Pachucy on 2015-07-15.
 */
public class MatchPlayerCustomSpinnerAdapter extends BaseAdapter {
    private List<Player> mPlayerList;
    private Context mContext;

    public MatchPlayerCustomSpinnerAdapter(Context context, List<Player> playerList
    ) {
        mContext = context;
        mPlayerList = playerList;
    }

    @Override
    public int getCount() {
        return mPlayerList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPlayerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater
                .inflate(R.layout.row_player_name, null);

        TextView textView = (TextView) convertView.findViewById(R.id.row_player);
        textView.setText(mPlayerList.get(position).getPlayerName());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater
                .inflate(R.layout.row_player_name_spinner, null);

        TextView textView = (TextView) convertView.findViewById(R.id.row_player);
        textView.setText(mPlayerList.get(position).getPlayerName());

        return convertView;
    }
}
