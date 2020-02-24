package unii.draft.mtg.parings.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;

public class DragAndDropAdapter extends GestureAdapter<String, DragAndDropAdapter.ViewHolder> {

    @NonNull
    private List<String> mPlayerNames;

    public DragAndDropAdapter(@NonNull List<String> playerNames) {
        mPlayerNames = playerNames;
        setData(mPlayerNames);
    }

    @NonNull
    @Override
    public DragAndDropAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_drag_drop_player_name, parent, false);
        return new DragAndDropAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DragAndDropAdapter.ViewHolder holder, int position) {
        String playerName = mPlayerNames.get(position);
        holder.mPlayerName.setText(playerName);
    }


    public class ViewHolder extends GestureViewHolder<String> {
        @Nullable
        @BindView(R.id.row_player)
        TextView mPlayerName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public boolean canDrag() {
            return true;
        }

        @Override
        public boolean canSwipe() {
            return false;
        }
    }
}
