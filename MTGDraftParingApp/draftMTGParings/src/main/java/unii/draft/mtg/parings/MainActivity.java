package unii.draft.mtg.parings;

import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Lazy;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.TourGuide;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.config.SetPreferencesOnFirstRun;
import unii.draft.mtg.parings.util.helper.TourGuideMenuHelper;
import unii.draft.mtg.parings.view.custom.IPlayerList;
import unii.draft.mtg.parings.view.fragments.GameMenuFragment;
import unii.draft.mtg.parings.view.fragments.settings.SettingsMenuFragment;


public class MainActivity extends BaseActivity implements IPlayerList {

    private static final String TAG_FRAGMENT_GAME = MainActivity.class
            .getName() + "TAG_FRAGMENT_GAME";
    private static final String TAG_FRAGMENT_SETTINGS = MainActivity.class.getName() + "TAG_FRAGMENT_SETTINGS";

    // help library
    @Nullable
    private TourGuide mTutorialHandler = null;
    private ArrayList<String> mPlayerNameList;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @Inject
    Lazy<ISharedPreferences> mSharedPreferenceManager;


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
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
    public ArrayList<String> getPlayerList() {
        return mPlayerNameList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cointainer);
        ButterKnife.bind(this);
        initPreferenceManager();
        mPlayerNameList = new ArrayList<>();
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

        mToolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        mToolBar.setTitle(R.string.app_header_path_configuration);
    }

    @Override
    protected void initView() {
        replaceFragments(new GameMenuFragment(), TAG_FRAGMENT_GAME, R.id.content_frame);

    }

    private void initPreferenceManager() {
        if (mSharedPreferenceManager.get().isFirstRun()) {
            SetPreferencesOnFirstRun.defaultSharedPreferencesConfig(mSharedPreferenceManager.get());
        }
    }

    private void setMenuActions(@NonNull ImageView aboutButton, @NonNull ImageView settingsButton) {
        // just adding some padding to look better
        int padding = TourGuideMenuHelper.getHelperMenuPadding(getResources().getDisplayMetrics().density);

        aboutButton.setPadding(padding, padding, padding, padding);
        settingsButton.setPadding(padding, padding, padding, padding);

        // set an image
        aboutButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_info));
        settingsButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_settings_applications));

        if (mSharedPreferenceManager.get().showGuideTourOnMainScreen()) {
            Sequence sequence = new Sequence.SequenceBuilder().add(bindTourGuideButton(getString(R.string.tutorial_info), aboutButton),
                    bindTourGuideButton(getString(R.string.tutorial_settings), settingsButton))
                    .setDefaultOverlay(new Overlay().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mTutorialHandler.next();
                        }
                    })).setContinueMethod(Sequence.ContinueMethod.OverlayListener).
                            setDefaultPointer(new Pointer()).build();

            mSharedPreferenceManager.get().setGuideTourOnMainScreen(false);
            mTutorialHandler = TourGuide.init(this).playInSequence(sequence);
        }

        aboutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoDialog(getString(R.string.dialog_info_title),
                        getString(R.string.dialog_info_message),
                        getString(R.string.dialog_start_button));
            }
        });

        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragmentFound = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_GAME);
                if (fragmentFound != null) {
                    replaceFragments(new SettingsMenuFragment(), TAG_FRAGMENT_SETTINGS, R.id.content_frame);
                } else {
                    replaceFragments(new GameMenuFragment(), TAG_FRAGMENT_GAME, R.id.content_frame);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_SETTINGS);
        if (fragment != null && fragment.isVisible()) {
            replaceFragments(new GameMenuFragment(), TAG_FRAGMENT_GAME, R.id.content_frame);
            return;
        }
        super.onBackPressed();

    }


}
