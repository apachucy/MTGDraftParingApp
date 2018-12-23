package unii.draft.mtg.parings.view.activities.options;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.BaseActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.view.fragments.settings.AddPlayerFragment;


public class AddPlayerActivity extends BaseActivity {

    private static final String TAG_FRAGMENT_ADD_PLAYER = AddPlayerActivity.class
            .getName() + "TAG_FRAGMENT_ADD_PLAYER";

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.disclaimer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_about:
                showInfoDialog(getString(R.string.add_player_dialog_title), getString(R.string.add_player_dialog_body), getString(R.string.dialog_positive));
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        mToolBar.setLogoDescription(R.string.app_header_path_configuration);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(R.string.app_header_path_configuration);
    }

    @Override
    protected void initView() {
        replaceFragments(new AddPlayerFragment(), TAG_FRAGMENT_ADD_PLAYER, R.id.content_frame);
    }

}
