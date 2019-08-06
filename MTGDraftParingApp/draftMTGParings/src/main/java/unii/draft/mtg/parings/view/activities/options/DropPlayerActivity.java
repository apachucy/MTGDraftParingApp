package unii.draft.mtg.parings.view.activities.options;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.BaseActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.view.fragments.DropPlayerFragment;


public class DropPlayerActivity extends BaseActivity {

    private static final String TAG_FRAGMENT_DROP_PLAYER = DropPlayerActivity.class
            .getName() + "TAG_FRAGMENT_DROP_PLAYER";

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_DROP_PLAYER);
        if (fragment instanceof DropPlayerFragment) {
            ((DropPlayerFragment) fragment).clearSelections();
        }
        super.onBackPressed();

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
        mToolBar.setLogoDescription(R.string.app_header_path_configuration);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(R.string.app_header_path_configuration);
    }

    @Override
    protected void initView() {
        replaceFragments(new DropPlayerFragment(), TAG_FRAGMENT_DROP_PLAYER, R.id.content_frame);
    }


}
