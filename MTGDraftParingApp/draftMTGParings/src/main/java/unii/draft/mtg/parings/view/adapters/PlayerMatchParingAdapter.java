package unii.draft.mtg.parings.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.view.custom.CustomOnCheckedChangeListener;


public class PlayerMatchParingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Game> mGameList;
    private Context mContext;

    public PlayerMatchParingAdapter(Context context, List<Game> games) {
        mGameList = games;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        setCheckBoxes(holder.getPosition(), ((ViewHolder) holder));
        // setting player names

        ((ViewHolder) holder).playerLeftTextView.setText(mGameList.get(holder.getPosition())
                .getPlayerNameA());
        ((ViewHolder) holder).playerRightTextView.setText(mGameList.get(holder.getPosition())
                .getPlayerNameB());
        // add listeners
        ((ViewHolder) holder).playerLeftRadioGroup
                .setOnCheckedChangeListener(new CustomOnCheckedChangeListener(
                        mGameList.get(holder.getPosition())));

        ((ViewHolder) holder).playerRightRadioGroup
                .setOnCheckedChangeListener(new CustomOnCheckedChangeListener(
                        mGameList.get(holder.getPosition())));
        ((ViewHolder) holder).drawRadioGroup
                .setOnCheckedChangeListener(new CustomOnCheckedChangeListener(
                        mGameList.get(holder.getPosition())));
    }

    @Override
    public int getItemCount() {
        return mGameList.size();
    }

    private void setCheckBoxes(int position, @NonNull ViewHolder viewHolder) {

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

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.row_leftPlayerTextView)
        TextView playerLeftTextView;
        @Nullable
        @BindView(R.id.row_rightPlayerTextView)
        TextView playerRightTextView;
        @Nullable
        @BindView(R.id.row_leftPlayerRadioGroup)
        RadioGroup playerLeftRadioGroup;
        @Nullable
        @BindView(R.id.row_rightPlayerRadioGroup)
        RadioGroup playerRightRadioGroup;
        @Nullable
        @BindView(R.id.row_drawRadioGroup)
        RadioGroup drawRadioGroup;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
