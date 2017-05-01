package unii.draft.mtg.parings.view.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.SettingsMenu;
import unii.draft.mtg.parings.view.fragments.settings.SettingsMenuFragment;

public class SettingsGridViewAdapter extends RecyclerView.Adapter<SettingsGridViewAdapter.ViewHolder> {

    private List<SettingsMenu> mSettingsMenuList;
    private SettingsMenuFragment.OnGridItemSelected mOnGridItemSelected;

    public SettingsGridViewAdapter(List<SettingsMenu> settingsMenuList, SettingsMenuFragment.OnGridItemSelected onGridItemSelected) {
        mSettingsMenuList = settingsMenuList;
        mOnGridItemSelected = onGridItemSelected;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_settings_chooser, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextMenuTextView.setText(mSettingsMenuList.get(position).getTextMenu());
        holder.mImageMenuImageView.setImageResource(mSettingsMenuList.get(position).getImageResourceId());
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mSettingsMenuList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.row_settings_chooser_text)
        TextView mTextMenuTextView;
        @Bind(R.id.row_settings_chooser_image)
        ImageView mImageMenuImageView;

        public ViewHolder(final View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnGridItemSelected.onCategorySelected(view, mSettingsMenuList.get(getPosition()));
                }
            });
            ButterKnife.bind(this, view);
        }
    }
}
