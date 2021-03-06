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
import unii.draft.mtg.parings.logic.pojo.SettingsMenu;
import unii.draft.mtg.parings.util.config.BundleConst;
import unii.draft.mtg.parings.util.helper.TourGuideMenuHelper;
import unii.draft.mtg.parings.view.fragments.settings.DraftSettingsFragment;
import unii.draft.mtg.parings.view.fragments.settings.HistorySettingsFragment;
import unii.draft.mtg.parings.view.fragments.settings.OtherSettingsFragment;
import unii.draft.mtg.parings.view.fragments.settings.TimeSettingsFragment;

public class SettingsMenuActivity extends BaseActivity {

    private static final String TAG_FRAGMENT_DRAFT = SettingsMenuActivity.class
            .getName() + "TAG_FRAGMENT_DRAFT";
    private static final String TAG_FRAGMENT_OTHER = SettingsMenuActivity.class
            .getName() + "TAG_FRAGMENT_OTHER";
    private static final String TAG_FRAGMENT_TIME = SettingsMenuActivity.class
            .getName() + "TAG_FRAGMENT_TIME";
    private static final String TAG_FRAGMENT_HISTORY = SettingsMenuActivity.class
            .getName() + "TAG_FRAGMENT_HISTORY";
    private String mCurrentFragment;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        setMenuActions((ImageView) menu.getItem(0).getActionView(), (ImageView) menu.getItem(1).getActionView());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
        Bundle bundle = getIntent().getExtras();
        if (!bundle.containsKey(BundleConst.BUNDLE_KEY_SETTINGS_FRAGMENT)) {
            finish();
        }
        int selectedSettings = bundle.getInt(BundleConst.BUNDLE_KEY_SETTINGS_FRAGMENT);
        Fragment fragment;
        switch (selectedSettings) {
            case SettingsMenu.MENU_DRAFT:
                mCurrentFragment = TAG_FRAGMENT_DRAFT;
                fragment = new DraftSettingsFragment();
                break;
            case SettingsMenu.MENU_HISTORY:
                mCurrentFragment = TAG_FRAGMENT_HISTORY;
                fragment = new HistorySettingsFragment();
                break;
            case SettingsMenu.MENU_TIME:
                mCurrentFragment = TAG_FRAGMENT_TIME;
                fragment = new TimeSettingsFragment();
                break;
            case SettingsMenu.MENU_OTHER:
                mCurrentFragment = TAG_FRAGMENT_OTHER;
                fragment = new OtherSettingsFragment();
                break;
            default:
                mCurrentFragment = TAG_FRAGMENT_DRAFT;
                fragment = new DraftSettingsFragment();
                break;
        }
        replaceFragments(fragment, mCurrentFragment, R.id.content_frame);

    }

    private void setMenuActions(ImageView aboutButton, ImageView settingsButton) {
        // just adding some padding to look better
        int padding = TourGuideMenuHelper.getHelperMenuPadding(getResources().getDisplayMetrics().density);

        aboutButton.setPadding(padding, padding, padding, padding);
        settingsButton.setPadding(padding, padding, padding, padding);

        // set an image
        aboutButton.setImageDrawable(getSingleDrawable(R.drawable.ic_info));
        settingsButton.setImageDrawable(getSingleDrawable(R.drawable.ic_settings_applications));

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoDialog(getString(R.string.dialog_info_title),
                        getString(R.string.dialog_info_message),
                        getString(R.string.dialog_start_button));
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
}
