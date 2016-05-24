package unii.draft.mtg.parings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.view.fragments.DropPlayerFragment;

/**
 * Created by Unii on 2016-03-26.
 */
public class DropPlayerActivity extends BaseActivity {

    private static final String TAG_FRAGMENT_DROP_PLAYER = DropPlayerActivity.class
            .getName() + "TAG_FRAGMENT_DROP_PLAYER";

    @Bind(R.id.toolbar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_player);
        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        mToolBar.setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_name);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setTitle(R.string.app_name);
        replaceFragments(new DropPlayerFragment(), TAG_FRAGMENT_DROP_PLAYER, R.id.content_frame);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_DROP_PLAYER);
        if (fragment instanceof DropPlayerFragment) {
            ((DropPlayerFragment) fragment).clearSelections();
        }
        super.onBackPressed();

    }
}