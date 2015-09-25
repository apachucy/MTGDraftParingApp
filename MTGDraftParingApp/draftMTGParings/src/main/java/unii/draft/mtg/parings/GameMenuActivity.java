package unii.draft.mtg.parings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.TourGuide;
import unii.draft.mtg.parings.helper.MenuHelper;
import unii.draft.mtg.parings.sharedprefrences.SettingsPreferencesFactory;
import unii.draft.mtg.parings.view.CustomDialogFragment;
import unii.draft.mtg.parings.view.fragments.GameMenuFragment;
import unii.draft.mtg.parings.view.fragments.SettingsFragment;


public class GameMenuActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    private static final String TAG_FRAGMENT_GAME = GameMenuActivity.class
            .getName() + "TAG_FRAGMENT_GAME";
    private static final String TAG_FRAGMENT_SETTINGS = GameMenuActivity.class.getName() + "TAG_FRAGMENT_SETTINGS";
    private CustomDialogFragment mInfoDialogFragment;
    private static final String TAG_DIALOG_INFO = GameMenuActivity.class
            .getName() + "TAG_DIALOG_INFO";
    // help library
    private TourGuide mTutorialHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamers_initial);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        mToolBar.setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_name);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setTitle(R.string.app_name);
        replaceFragments(new GameMenuFragment(), TAG_FRAGMENT_GAME, R.id.content_frame);
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


    private void setMenuActions(ImageView aboutButton, ImageView settingsButton) {
        // just adding some padding to look better
        int padding = MenuHelper.getHelperMenuPadding(getResources().getDisplayMetrics().density);

        aboutButton.setPadding(padding, padding, padding, padding);
        settingsButton.setPadding(padding, padding, padding, padding);

        // set an image
        aboutButton.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_info));
        settingsButton.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_settings_applications));

        if (SettingsPreferencesFactory.getInstance().isFirstRun()) {
            Sequence sequence = null;

            sequence = new Sequence.SequenceBuilder().add(bindTourGuideButton(getString(R.string.tutorial_info), aboutButton), bindTourGuideButton(getString(R.string.tutorial_settings), settingsButton))
                    .setDefaultOverlay(new Overlay().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mTutorialHandler.next();
                        }
                    })).setContinueMethod(Sequence.ContinueMethod.OverlayListener).setDefaultPointer(new Pointer()).build();


            mTutorialHandler = TourGuide.init(this).playInSequence(sequence);
        }

        aboutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInfoDialogFragment == null) {
                    mInfoDialogFragment = CustomDialogFragment.newInstance(
                            getString(R.string.dialog_info_title),
                            getString(R.string.dialog_info_message),
                            getString(R.string.dialog_start_button));
                }
                mInfoDialogFragment.show(getSupportFragmentManager(), TAG_DIALOG_INFO);
            }
        });

        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragmentFound = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_GAME);
                if (fragmentFound != null) {
                    replaceFragments(new SettingsFragment(), TAG_FRAGMENT_SETTINGS, R.id.content_frame);
                } else {
                    replaceFragments(new GameMenuFragment(), TAG_FRAGMENT_GAME, R.id.content_frame);
                }

            }
        });
    }

}
