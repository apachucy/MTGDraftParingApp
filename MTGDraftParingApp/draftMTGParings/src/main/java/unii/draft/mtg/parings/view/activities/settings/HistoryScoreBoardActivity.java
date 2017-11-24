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
import unii.draft.mtg.parings.view.fragments.history.HistoryScoreBoardDetailFragment;
import unii.draft.mtg.parings.view.fragments.history.HistoryScoreBoardListFragment;
import unii.draft.mtg.parings.view.fragments.history.IDisplayDetailFragment;

public class HistoryScoreBoardActivity extends BaseActivity implements IDisplayDetailFragment {
    private static final String TAG_FRAGMENT_LIST = HistoryScoreBoardActivity.class
            .getName() + "TAG_FRAGMENT_LIST";
    private static final String TAG_FRAGMENT_DETAIL = HistoryScoreBoardActivity.class.getName() + "TAG_FRAGMENT_DETAIL";
    private static String sCurrentFragmentTag;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @Override
    public void displayDetailView(Long detailKey) {
        Fragment historyScoreBoardDetailFragment = new HistoryScoreBoardDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BundleConst.BUNDLE_KEY_HISTORY_DRAFT_DETAIL, detailKey);
        historyScoreBoardDetailFragment.setArguments(bundle);
        sCurrentFragmentTag = TAG_FRAGMENT_DETAIL;
        replaceFragments(historyScoreBoardDetailFragment, TAG_FRAGMENT_DETAIL, R.id.content_frame);
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
                    replaceFragments(new HistoryScoreBoardListFragment(), TAG_FRAGMENT_LIST, R.id.content_frame);
                }
                return true;
            default:
                return false;
        }
    }

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
        mToolBar.setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_name);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setTitle(R.string.app_header_path_history);
    }

    @Override
    protected void initView() {
        sCurrentFragmentTag = TAG_FRAGMENT_LIST;
        replaceFragments(new HistoryScoreBoardListFragment(), TAG_FRAGMENT_LIST, R.id.content_frame);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_DETAIL);
        if (fragment != null && fragment.isVisible()) {
            sCurrentFragmentTag = TAG_FRAGMENT_LIST;
            replaceFragments(new HistoryScoreBoardListFragment(), TAG_FRAGMENT_LIST, R.id.content_frame);
            return;
        }
        super.onBackPressed();

    }
}
