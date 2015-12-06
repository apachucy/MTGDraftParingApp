package unii.draft.mtg.parings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.config.BundleConst;
import unii.draft.mtg.parings.helper.MenuHelper;
import unii.draft.mtg.parings.view.fragments.HistoryScoreBoardDetailFragment;
import unii.draft.mtg.parings.view.fragments.HistoryScoreBoardListFragment;
import unii.draft.mtg.parings.view.fragments.IDisplayHistoryScoreBoardDetail;

public class HistoryScoreBoardActivity extends BaseActivity implements IDisplayHistoryScoreBoardDetail {
    private static final String TAG_FRAGMENT_LIST = HistoryScoreBoardActivity.class
            .getName() + "TAG_FRAGMENT_LIST";
    private static final String TAG_FRAGMENT_DETAIL = HistoryScoreBoardActivity.class.getName() + "TAG_FRAGMENT_DETAIL";
    private static String sCurrentFragmentTag;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_score_board);
        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        mToolBar.setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_name);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setTitle(R.string.app_name);
        sCurrentFragmentTag = TAG_FRAGMENT_LIST;
        replaceFragments(new HistoryScoreBoardListFragment(), TAG_FRAGMENT_LIST, R.id.content_frame);

    }

    @Override
    public void displayHistoryScoreBoardDetail(Long detailKey) {
        Fragment historyScoreBoardDetailFragment = new HistoryScoreBoardDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BundleConst.BUNDLE_KEY_HISTORY_DRAFT_DETAIL, detailKey);
        historyScoreBoardDetailFragment.setArguments(bundle);
        sCurrentFragmentTag = TAG_FRAGMENT_DETAIL;
        replaceFragments(historyScoreBoardDetailFragment, TAG_FRAGMENT_DETAIL, R.id.content_frame);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        int padding = MenuHelper.getHelperMenuPadding(getResources().getDisplayMetrics().density);
        ImageView imageButton = (ImageView) menu.getItem(0).getActionView();
        imageButton.setPadding(padding, padding, padding, padding);

        // set an image
        imageButton.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_view_list));
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sCurrentFragmentTag.equals(TAG_FRAGMENT_DETAIL)) {
                    replaceFragments(new HistoryScoreBoardListFragment(), TAG_FRAGMENT_LIST, R.id.content_frame);
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
