package unii.draft.mtg.parings.view.adapters;

import android.content.Context;
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
import unii.draft.mtg.parings.logic.pojo.Game;

/**
 * Created by Unii on 2015-12-12.
 */
public class MatchPlayerCustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Game> mPlayerGame;
    private Context mContext;

    public MatchPlayerCustomAdapter(Context context, List<Game> playerGame) {
        mContext = context;
        mPlayerGame = playerGame;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_match_player_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof ViewHolder)) {
            return;
        }

        ViewHolder viewHolder = (ViewHolder) holder;
        Game game = mPlayerGame.get(position);
        viewHolder.player1TextView.setText(game.getPlayerNameA());
        viewHolder.player2TextView.setText(game.getPlayerNameB());
    }

    @Override
    public int getItemCount() {
        return mPlayerGame.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.row_match_player_player1TextView)
        TextView player1TextView;
        @Nullable
        @BindView(R.id.row_match_player_player2TextView)
        TextView player2TextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
