package unii.draft.mtg.parings.view.activities.options;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;
import unii.draft.mtg.parings.BaseActivity;
import unii.draft.mtg.parings.RoundActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.algorithm.base.PairingMode;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.config.BaseConfig;
import unii.draft.mtg.parings.util.helper.TourGuideMenuHelper;
import unii.draft.mtg.parings.view.fragments.SittingsFragment;
import unii.draft.mtg.parings.view.logic.SittingsLogic;

/**
 * Display Sittings
 */
public class SittingsActivity extends BaseActivity {
    private static final String TAG_FRAGMENT_SITTINGS = SittingsActivity.class
            .getName() + "TAG_FRAGMENT_SITTINGS";

    @Nullable
    private TourGuide mTutorialHandler = null;

    private SittingsLogic mSittingsLogic;
    @Inject
    ISharedPreferences mSharedPreferenceManager;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @Nullable
    @BindView(R.id.floating_action_button_next)
    FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cointainer);
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
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sittings, menu);
        setMenuActions((ImageView) menu.getItem(0).getActionView());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void injectDependencies(@NonNull ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initToolBar() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_header_path_game);
        mToolBar.setTitleTextColor(getSingleColor(R.color.white));
        getSupportActionBar().setTitle(R.string.app_header_path_game);
    }

    @Override
    protected void initView() {
        mSittingsLogic = new SittingsLogic(this);
        replaceFragments(new SittingsFragment(), TAG_FRAGMENT_SITTINGS, R.id.content_frame);
        mFloatingActionButton.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.floating_action_button_next)
    public void onActionButtonNextClicked() {
        Intent intent;
        if (mSharedPreferenceManager.getPairingType() == PairingMode.PAIRING_MANUAL) {
            intent = new Intent(this, ManualPlayerPairingActivity.class);
        } else {
            intent = new Intent(this, RoundActivity.class);

        }
        startActivity(intent);
        finish();
    }

    private void setMenuActions(@NonNull ImageView openManaCalculatorButton) {
        // just adding some padding to look better
        int padding = TourGuideMenuHelper.getHelperMenuPadding(getResources().getDisplayMetrics().density);

        openManaCalculatorButton.setPadding(padding, padding, padding, padding);
        // set an image
        openManaCalculatorButton.setImageDrawable(getSingleDrawable(R.drawable.ic_calculator));


        if (mSharedPreferenceManager.showGuideTourOnSittingsScreen()) {


            mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                    .setPointer(new Pointer())
                    .setToolTip(new ToolTip().setTitle(getString(R.string.help_title))
                            .setDescription(getString(R.string.action_calculate_mana))
                            .setBackgroundColor(getSingleColor(R.color.accent))
                            .setGravity(Gravity.START | Gravity.BOTTOM))
                    .setOverlay(new Overlay().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTutorialHandler.cleanUp();
                        }
                    }))
                    .playOn(openManaCalculatorButton);

            mSharedPreferenceManager.setGuideTourOnSittingsScreen(false);

        }

        openManaCalculatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent(BaseConfig.INTENT_PACKAGE_MANA_CALCULATOR_APP);
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Intent chooser = Intent.createChooser(sendIntent, getString(R.string.chooser_intent_title_mana_calculator));
                // Verify that the intent will resolve to an activity
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    //open app
                    startActivity(chooser);
                } else {
                    //display information about possibility of downloading new app
                    showInfoDialog(getString(R.string.chooser_dialog_title_mana_calculator), getString(R.string.chooser_dialog_body_mana_calculator), getString(R.string.chooser_dialog_button_name), mDownloadMTGManaCalculatorAppListener);
                }
            }
        });
    }


    @NonNull
    private MaterialDialog.SingleButtonCallback mDownloadMTGManaCalculatorAppListener = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            mSittingsLogic.openGooglePlayMTGManaCalculatorApp();
            dialog.dismiss();
        }
    };
}
