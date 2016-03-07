package unii.draft.mtg.parings.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.pojo.Game;
import unii.draft.mtg.parings.view.custom.CustomOnCheckedChangeListener;

/**
 * Created by Unii on 2015-12-09.
 */
public class PlayerMatchParingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Game> mGameList;
    private Context mContext;

    public PlayerMatchParingAdapter(Context context, List<Game> games) {
        mGameList = games;
           mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_paring_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof EmptyViewHolder) {
            return;
        }
        // remove listeners
        ((ViewHolder) holder).playerLeftRadioGroup.setOnCheckedChangeListener(null);
        ((ViewHolder) holder).playerRightRadioGroup.setOnCheckedChangeListener(null);
        ((ViewHolder) holder).drawRadioGroup.setOnClickListener(null);
        // set points in RadioGroup
        setCheckBoxes(position, ((ViewHolder) holder));
        // setting player names

        ((ViewHolder) holder).playerLeftTextView.setText(mGameList.get(position)
                .getPlayerNameA());
        ((ViewHolder) holder).playerRightTextView.setText(mGameList.get(position)
                .getPlayerNameB());
        // add listeners
        ((ViewHolder) holder).playerLeftRadioGroup
                .setOnCheckedChangeListener(new CustomOnCheckedChangeListener(
                        mGameList.get(position)));

        ((ViewHolder) holder).playerRightRadioGroup
                .setOnCheckedChangeListener(new CustomOnCheckedChangeListener(
                        mGameList.get(position)));
        ((ViewHolder) holder).drawRadioGroup.setOnCheckedChangeListener(new CustomOnCheckedChangeListener(
                mGameList.get(position)));
    }

    @Override
    public int getItemCount() {
        return mGameList.size();
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

        switch (mGameList.get(position).getDraws()) {
            case 0:
                viewHolder.drawRadioGroup.check(R.id.row_drawRadio0);
                break;
            case 1:
                viewHolder.drawRadioGroup.check(R.id.row_drawRadio1);
                break;
            case 2:
                viewHolder.drawRadioGroup.check(R.id.row_drawRadio2);
                break;

        }

    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.row_leftPlayerTextView)
        TextView playerLeftTextView;
        @Bind(R.id.row_rightPlayerTextView)
        TextView playerRightTextView;
        @Bind(R.id.row_leftPlayerRadioGroup)
        RadioGroup playerLeftRadioGroup;
        @Bind(R.id.row_rightPlayerRadioGroup)
        RadioGroup playerRightRadioGroup;
        @Bind(R.id.row_drawRadioGroup)
        RadioGroup drawRadioGroup;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
