package unii.draft.mtg.parings.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.pojo.Game;
import unii.draft.mtg.parings.pojo.Player;

/**
 * Created by Arkadiusz Pachucy on 2015-07-18.
 */
public class MatchPlayerCustomListAdapter extends BaseAdapter {

    private List<Game> mPlayerGame;
    private Context mContext;
    public MatchPlayerCustomListAdapter(Context context, List<Game> playerGame
    ){
        mContext = context;
        mPlayerGame = playerGame;
    }
    @Override
    public int getCount() {
        return mPlayerGame.size();
    }

    @Override
    public Object getItem(int position) {
        return mPlayerGame.get(position);
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
                .inflate(R.layout.row_match_player_list, null);

        TextView player1TextView = (TextView)convertView.findViewById(R.id.row_match_player_player1TextView);
        TextView player2TextView = (TextView)convertView.findViewById(R.id.row_match_player_player2TextView);

        player1TextView.setText(mPlayerGame.get(position).getPlayerNameA());
        player2TextView.setText(mPlayerGame.get(position).getPlayerNameB());
        return convertView;

    }
}
