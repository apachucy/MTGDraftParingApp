package unii.draft.mtg.parings.view.fragments.history;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.util.config.BundleConst;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

public class HistoryPlayerFragment extends BaseFragment {
    private Unbinder mUnbinder;
    private Long mPlayerId;
    private PlayerHistoryPagerAdapter mAdapter;


    @Nullable
    @BindView(R.id.player_viewpager)
    ViewPager mViewPager;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        injectDependencies();
        initFragmentData();
        initFragmentView();

        return view;
    }

    @Override
    protected void initFragmentView() {
        mAdapter = new PlayerHistoryPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    protected void initFragmentData() {
        Bundle bundle = getArguments();
        if (!bundle.containsKey(BundleConst.BUNDLE_KEY_PLAYER_DRAFT_DETAIL)) {
            getActivity().finish();
        }
        mPlayerId = bundle.getLong(BundleConst.BUNDLE_KEY_PLAYER_DRAFT_DETAIL);
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private class PlayerHistoryPagerAdapter extends FragmentStatePagerAdapter {
        private int PLAYER_FRAGMENTS_SIZE = 3;
        private final int PLAYER_GENERAL_INFORMATION = 0;
        private final int PLAYER_RIVALISATION = 1;
        private final int PLAYER_CHARTS = 2;

        public PlayerHistoryPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case PLAYER_GENERAL_INFORMATION:
                    fragment = new HistoryPlayerAchievementsFragment();
                    break;
                case PLAYER_RIVALISATION:
                    fragment = new HistoryPlayerRivalisationFragment();
                    break;
                case PLAYER_CHARTS:
                    fragment = new HistoryPlayerChartsFragment();
                    break;
                default:
                    fragment = new HistoryPlayerAchievementsFragment();
                    break;
            }
            Bundle bundle = new Bundle();
            bundle.putLong(BundleConst.BUNDLE_KEY_PLAYER_DRAFT_DETAIL, mPlayerId);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return PLAYER_FRAGMENTS_SIZE;
        }
    }
}


