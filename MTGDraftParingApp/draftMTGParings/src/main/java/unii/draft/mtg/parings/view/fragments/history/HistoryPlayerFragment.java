package unii.draft.mtg.parings.view.fragments.history;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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
        private int PLAYER_FRAGMENTS_SIZE = 4;
        private final int PLAYER_GENERAL_INFORMATION = 0;
        private final int PLAYER_RIVALISATION = 1;
        private final int PLAYER_CHARTS_GAMES = 2;
        private final int PLAYER_CHART_MATCHES = 3;

        public PlayerHistoryPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            Bundle bundle = new Bundle();
            bundle.putLong(BundleConst.BUNDLE_KEY_PLAYER_DRAFT_DETAIL, mPlayerId);

            switch (position) {
                case PLAYER_GENERAL_INFORMATION:
                    fragment = new HistoryPlayerAchievementsFragment();
                    break;
                case PLAYER_RIVALISATION:
                    fragment = new HistoryPlayerRivalisationFragment();
                    break;
                case PLAYER_CHARTS_GAMES:
                    fragment = new HistoryPlayerChartsFragment();
                    bundle.putBoolean(BundleConst.BUNDLE_KEY_GAME_CHART, true);
                    bundle.putBoolean(BundleConst.BUNDLE_KEY_MATCH_CHART, false);

                    break;
                case PLAYER_CHART_MATCHES:
                    fragment = new HistoryPlayerChartsFragment();
                    bundle.putBoolean(BundleConst.BUNDLE_KEY_MATCH_CHART, true);
                    bundle.putBoolean(BundleConst.BUNDLE_KEY_GAME_CHART, false);
                    break;
                default:
                    fragment = new HistoryPlayerAchievementsFragment();
                    break;
            }
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return PLAYER_FRAGMENTS_SIZE;
        }
    }
}


