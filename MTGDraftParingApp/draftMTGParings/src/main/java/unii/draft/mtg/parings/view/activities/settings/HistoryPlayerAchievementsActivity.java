package unii.draft.mtg.parings.view.activities.settings;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.BaseActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.util.config.BundleConst;
import unii.draft.mtg.parings.view.fragments.history.HistoryPlayerFragment;
import unii.draft.mtg.parings.view.fragments.history.HistoryPlayerListFragment;
import unii.draft.mtg.parings.view.fragments.history.IDisplayDetailFragment;

public class HistoryPlayerAchievementsActivity extends BaseActivity implements IDisplayDetailFragment {
    private static final String TAG_FRAGMENT_LIST = HistoryPlayerAchievementsActivity.class
            .getName() + "TAG_FRAGMENT_LIST";
    private static final String TAG_FRAGMENT_DETAIL = HistoryPlayerAchievementsActivity.class.getName() + "TAG_FRAGMENT_DETAIL";
    private static String sCurrentFragmentTag;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cointainer);
        ButterKnife.bind(this);
        initToolBar();
        initView();
    }


    @Override
    protected void injectDependencies(@NonNull ActivityComponent activityComponent) {
        activityComponent.inject(this);

    }

    @Override
    protected void initToolBar() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_header_path_history);
        mToolBar.setTitleTextColor(getSingleColor(R.color.white));
        getSupportActionBar().setTitle(R.string.app_header_path_history);
    }

    @Override
    protected void initView() {
        sCurrentFragmentTag = TAG_FRAGMENT_LIST;
        replaceFragments(new HistoryPlayerListFragment(), TAG_FRAGMENT_LIST, R.id.content_frame);
    }

    @Override
    public void displayDetailView(Long detailKey) {
        Fragment historyPlayerFragment = new HistoryPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BundleConst.BUNDLE_KEY_PLAYER_DRAFT_DETAIL, detailKey);
        historyPlayerFragment.setArguments(bundle);
        sCurrentFragmentTag = TAG_FRAGMENT_DETAIL;
        replaceFragments(historyPlayerFragment, TAG_FRAGMENT_DETAIL, R.id.content_frame);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_DETAIL);
        if (fragment != null && fragment.isVisible()) {
            sCurrentFragmentTag = TAG_FRAGMENT_LIST;
            replaceFragments(new HistoryPlayerListFragment(), TAG_FRAGMENT_LIST, R.id.content_frame);
            return;
        }
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_score_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_list:
                if (sCurrentFragmentTag.equals(TAG_FRAGMENT_DETAIL)) {
                    replaceFragments(new HistoryPlayerListFragment(), TAG_FRAGMENT_LIST, R.id.content_frame);
                }
                return true;
            default:
                return false;
        }
    }
}
