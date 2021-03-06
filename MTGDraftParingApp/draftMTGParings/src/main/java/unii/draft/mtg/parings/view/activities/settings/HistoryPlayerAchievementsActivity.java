package unii.draft.mtg.parings.view.activities.settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.BaseActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.util.config.BundleConst;
import unii.draft.mtg.parings.util.helper.TourGuideMenuHelper;
import unii.draft.mtg.parings.view.fragments.history.HistoryPlayerAchievementsFragment;
import unii.draft.mtg.parings.view.fragments.history.HistoryPlayerListFragment;
import unii.draft.mtg.parings.view.fragments.history.IDisplayDetailFragment;

public class HistoryPlayerAchievementsActivity extends BaseActivity implements IDisplayDetailFragment {
    private static final String TAG_FRAGMENT_LIST = HistoryPlayerAchievementsActivity.class
            .getName() + "TAG_FRAGMENT_LIST";
    private static final String TAG_FRAGMENT_DETAIL = HistoryPlayerAchievementsActivity.class.getName() + "TAG_FRAGMENT_DETAIL";
    private static String sCurrentFragmentTag;

    @Bind(R.id.toolbar)
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
    protected void injectDependencies(ActivityComponent activityComponent) {
        activityComponent.inject(this);

    }

    @Override
    protected void initToolBar() {
        setSupportActionBar(mToolBar);
        mToolBar.setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_name);
        mToolBar.setTitleTextColor(getSingleColor(R.color.white));
        mToolBar.setTitle(R.string.app_name);
    }

    @Override
    protected void initView() {
        sCurrentFragmentTag = TAG_FRAGMENT_LIST;
        replaceFragments(new HistoryPlayerListFragment(), TAG_FRAGMENT_LIST, R.id.content_frame);
    }

    @Override
    public void displayDetailView(Long detailKey) {
        Fragment historyPlayerAchievementsFragment = new HistoryPlayerAchievementsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BundleConst.BUNDLE_KEY_PLAYER_DRAFT_DETAIL, detailKey);
        historyPlayerAchievementsFragment.setArguments(bundle);
        sCurrentFragmentTag = TAG_FRAGMENT_DETAIL;
        replaceFragments(historyPlayerAchievementsFragment, TAG_FRAGMENT_DETAIL, R.id.content_frame);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        int padding = TourGuideMenuHelper.getHelperMenuPadding(getResources().getDisplayMetrics().density);
        ImageView imageButton = (ImageView) menu.getItem(0).getActionView();
        imageButton.setPadding(padding, padding, padding, padding);

        // set an image
        imageButton.setImageDrawable(getSingleDrawable(R.drawable.ic_view_list));
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sCurrentFragmentTag.equals(TAG_FRAGMENT_DETAIL)) {
                    replaceFragments(new HistoryPlayerListFragment(), TAG_FRAGMENT_LIST, R.id.content_frame);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
