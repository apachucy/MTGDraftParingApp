package unii.draft.mtg.parings.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.util.config.BaseConfig;
import unii.draft.mtg.parings.view.custom.CustomOnCheckedChangeListener;

import static unii.draft.mtg.parings.util.config.BaseConfig.PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER;


public class PlayerMatchParingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Game> mGameList;
    private Context mContext;
    private boolean lockView;

    public PlayerMatchParingAdapter(Context context, List<Game> games, boolean lockViews) {
        mGameList = games;
        mContext = context;
        lockView = lockViews;
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
        Game game = mGameList.get(holder.getPosition());
        if (holder instanceof EmptyViewHolder) {
            return;
        }
        //lock View with "R " player as this point won't be appointed
        if (lockView && (game.getPlayerNameA().startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER) ||
                game.getPlayerNameB().startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER))) {

            setViewAndChildrenEnabled(((ViewHolder) holder).itemView, false);
            if (game.getPlayerNameB().startsWith(PREFIX_ITALIAN_ROUND_ROBIN_DROPPED_PLAYER)) {
                game.setPlayerAPoints(BaseConfig.GAME_WIN_DEFAULT);
            }else{
                game.setPlayerBPoints(BaseConfig.GAME_WIN_DEFAULT);
            }
        } else {
            setViewAndChildrenEnabled(((ViewHolder) holder).itemView, true);
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

    private static void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
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
        @NonNull
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }
}
