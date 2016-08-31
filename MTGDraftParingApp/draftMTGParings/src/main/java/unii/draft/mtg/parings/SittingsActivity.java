package unii.draft.mtg.parings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.view.fragments.SittingsFragment;

/**
 * Display Sittings
 */
public class SittingsActivity extends BaseActivity {
    private static final String TAG_FRAGMENT_SITTINGS = SittingsActivity.class
            .getName() + "TAG_FRAGMENT_SITTINGS";


    @Bind(R.id.toolbar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sittings);
        ButterKnife.bind(this);

        initToolBar();
        initView();
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
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
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setTitle(R.string.app_name);
    }

    @Override
    protected void initView() {
        replaceFragments(new SittingsFragment(), TAG_FRAGMENT_SITTINGS, R.id.content_frame);

    }
}
