package unii.draft.mtg.parings.view;

import java.util.List;

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

			viewHolder = new ViewHolder();
			viewHolder.playerTextView = (TextView) convertView
					.findViewById(R.id.row_playerNameTextView);
			viewHolder.pointTextView = (TextView) convertView
					.findViewById(R.id.row_playerPointTextView);
			viewHolder.omwTextView = (TextView) convertView
					.findViewById(R.id.row_playerOMWTextView);
			viewHolder.ogwTextView = (TextView) convertView
					.findViewById(R.id.row_playerOGWTextView);
			viewHolder.pgwTextView = (TextView) convertView
					.findViewById(R.id.row_playerPGWTextView);
			viewHolder.pmwTextView = (TextView) convertView
					.findViewById(R.id.row_playerPMWTextView);
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
		TextView playerTextView;
		TextView pointTextView;
		TextView omwTextView;
		TextView pmwTextView;
		TextView ogwTextView;
		TextView pgwTextView;

	}
}
