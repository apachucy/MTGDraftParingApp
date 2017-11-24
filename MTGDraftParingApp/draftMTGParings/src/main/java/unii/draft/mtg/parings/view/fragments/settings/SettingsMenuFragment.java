package unii.draft.mtg.parings.view.fragments.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.SettingsMenu;
import unii.draft.mtg.parings.util.config.BundleConst;
import unii.draft.mtg.parings.util.config.SettingsMenuItems;
import unii.draft.mtg.parings.util.helper.SupportAnimation;
import unii.draft.mtg.parings.view.activities.settings.SettingsMenuActivity;
import unii.draft.mtg.parings.view.adapters.SettingsGridViewAdapter;
import unii.draft.mtg.parings.view.fragments.BaseFragment;


public class SettingsMenuFragment extends BaseFragment {
    private final static int MAX_ITEMS_IN_LINE = 2;


    private Unbinder mUnbinder;
    private SettingsMenuItems mSettingsMenuItems;

    @Nullable
    @BindView(R.id.settings_menuRecyclerView)
    RecyclerView mRecyclerView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_view, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        injectDependencies();
        initFragmentData();
        initFragmentView();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    protected void initFragmentView() {
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getContext(), MAX_ITEMS_IN_LINE);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        RecyclerView.Adapter settingsGridViewAdapter = new SettingsGridViewAdapter(mSettingsMenuItems.getSettingsMenuList(), mOnGridItemSelectedListener);
        mRecyclerView.setAdapter(settingsGridViewAdapter);

    }

    @Override
    protected void initFragmentData() {
        mSettingsMenuItems = new SettingsMenuItems(getActivity());
    }


    private void injectDependencies() {
        getActivityComponent().inject(this);
    }


    @NonNull
    private OnGridItemSelected mOnGridItemSelectedListener = new OnGridItemSelected() {
        @Override
        public void onCategorySelected(@NonNull View itemView, @NonNull SettingsMenu settingsMenu) {
            Intent intent = new Intent(getActivity(), SettingsMenuActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(BundleConst.BUNDLE_KEY_SETTINGS_FRAGMENT, settingsMenu.getId());


            if (SupportAnimation.checkIfAnimationAreSupported()) {
                ImageView imageView = (ImageView) itemView.findViewById(R.id.row_settings_chooser_image);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), imageView, "settingsImage");
                bundle.putAll(options.toBundle());
            }
            intent.putExtras(bundle);
            startActivity(intent, bundle);
        }
    };

    public interface OnGridItemSelected {
        void onCategorySelected(View itemView, SettingsMenu settingsMenu);
    }


}
