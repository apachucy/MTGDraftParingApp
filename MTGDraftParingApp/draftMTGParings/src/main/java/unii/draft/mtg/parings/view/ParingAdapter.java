package unii.draft.mtg.parings.view;

import java.util.List;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.pojo.Game;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ParingAdapter extends BaseAdapter {

	private List<Game> mGameList;
	private Context mContext;

	public ParingAdapter(Context context, List<Game> games) {
		mGameList = games;
		mContext = context;
	}

	@Override
	public int getCount() {
		return mGameList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mGameList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater
					.inflate(R.layout.row_paring_list, null);

			viewHolder = new ViewHolder();
			viewHolder.playerLeftTextView = (TextView) convertView
					.findViewById(R.id.row_leftPlayerTextView);
			viewHolder.playerRightTextView = (TextView) convertView
					.findViewById(R.id.row_rightPlayerTextView);

			viewHolder.playerLeftRadioGroup = (RadioGroup) convertView
					.findViewById(R.id.row_leftPlayerRadioGroup);
			viewHolder.playerRightRadioGroup = (RadioGroup) convertView
					.findViewById(R.id.row_rightPlayerRadioGroup);
			viewHolder.playerDraw = (CheckBox) convertView
					.findViewById(R.id.row_draw);
			// setting points

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// remove listeners
		viewHolder.playerLeftRadioGroup.setOnCheckedChangeListener(null);
		viewHolder.playerRightRadioGroup.setOnCheckedChangeListener(null);
		viewHolder.playerDraw.setOnClickListener(null);
		// set points in RadioGroup
		setCheckBoxes(position, viewHolder);
		// setting player names
		viewHolder.playerLeftTextView.setText(mGameList.get(position)
				.getPlayerNameA());
		viewHolder.playerRightTextView.setText(mGameList.get(position)
				.getPlayerNameB());

		// add listeners
		viewHolder.playerLeftRadioGroup
				.setOnCheckedChangeListener(new CustomOnCheckedChangeListener(
						mGameList.get(position)));

		viewHolder.playerRightRadioGroup
				.setOnCheckedChangeListener(new CustomOnCheckedChangeListener(
						mGameList.get(position)));
		viewHolder.playerDraw.setOnClickListener(new CustomRadioButtonOnClick(
				mGameList.get(position)));
		return convertView;
	}

	private void setCheckBoxes(int position, ViewHolder viewHolder) {

		switch (mGameList.get(position).getPlayerAPoints()) {
		case 0:
			viewHolder.playerLeftRadioGroup.check(R.id.row_leftPlayerRadio0);
			break;
		case 1:
			viewHolder.playerLeftRadioGroup.check(R.id.row_leftPlayerRadio1);
			break;
		case 2:
			viewHolder.playerLeftRadioGroup.check(R.id.row_leftPlayerRadio2);
			break;
		}
		switch (mGameList.get(position).getPlayerBPoints()) {
		case 0:
			viewHolder.playerRightRadioGroup.check(R.id.row_rightPlayerRadio0);
			break;
		case 1:
			viewHolder.playerRightRadioGroup.check(R.id.row_rightPlayerRadio1);
			break;
		case 2:
			viewHolder.playerRightRadioGroup.check(R.id.row_rightPlayerRadio2);
			break;
		}

		if (mGameList.get(position).getDraw()) {
			viewHolder.playerDraw.setChecked(true);
		} else {
			viewHolder.playerDraw.setChecked(false);

		}

	}

	static class ViewHolder {
		TextView playerLeftTextView;
		TextView playerRightTextView;
		RadioGroup playerLeftRadioGroup;
		RadioGroup playerRightRadioGroup;
		CheckBox playerDraw;
	}

}
