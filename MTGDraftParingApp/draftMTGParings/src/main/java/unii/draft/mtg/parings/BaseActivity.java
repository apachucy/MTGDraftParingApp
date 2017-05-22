package unii.draft.mtg.parings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.logic.dagger.ActivityModule;
import unii.draft.mtg.parings.logic.dagger.ApplicationComponent;
import unii.draft.mtg.parings.logic.dagger.HasComponent;
import unii.draft.mtg.parings.view.custom.IActivityHandler;


public abstract class BaseActivity extends ActionBarActivity implements IActivityHandler, HasComponent<ActivityComponent> {

    private ActivityComponent mActivityComponent;
    private MaterialDialog mMaterialDialogInstance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityComponent activityComponent = initActivityComponent();
        injectDependencies(activityComponent);
        mMaterialDialogInstance = null;
    }


    @Override
    public void showInfoDialog(String title, String body, String positiveText) {
        mMaterialDialogInstance = new MaterialDialog.Builder(this)
                .title(title)
                .content(body)
                .positiveText(positiveText).backgroundColorRes(R.color.windowBackground)
                .show();
    }

    @Override
    public void showInfoDialog(String title, String body, String positiveText, MaterialDialog.SingleButtonCallback positiveAction) {
        mMaterialDialogInstance = new MaterialDialog.Builder(this)
                .title(title)
                .content(body)
                .positiveText(positiveText).onPositive(positiveAction).backgroundColorRes(R.color.windowBackground)
                .show();
    }
    @Override
    public void showSingleChoiceList(Context context, String title, List<String> list, String positiveText, MaterialDialog.ListCallbackSingleChoice singleListCallback) {
        mMaterialDialogInstance = new MaterialDialog.Builder(context)
                .title(title).items(list)
                .itemsCallbackSingleChoice(-1, singleListCallback).backgroundColorRes(R.color.windowBackground)
                .positiveText(positiveText)
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMaterialDialogInstance != null) {
            mMaterialDialogInstance.dismiss();
            mMaterialDialogInstance = null;
        }
    }

    @Override
    public ActivityComponent getComponent() {
        return mActivityComponent;
    }

    protected abstract void injectDependencies(final ActivityComponent activityComponent);

    protected abstract void initToolBar();

    protected abstract void initView();

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }


    protected void replaceFragments(Fragment fragment, String tag, int container) {
        Fragment fragmentFound = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragmentFound != null) {
            getSupportFragmentManager().beginTransaction().show(fragmentFound).commit();
        } else {
            hideKeyboard();
            getSupportFragmentManager().beginTransaction()
                    .replace(container, fragment, tag)
                    .commit();
        }
    }

    protected TourGuide bindTourGuideButton(String bodyText, ImageView imageView) {
        ToolTip toolTip = new ToolTip()
                .setTitle(getString(R.string.help_title))
                .setDescription(bodyText).setBackgroundColor(getSingleColor(R.color.accent))
                .setGravity(Gravity.START | Gravity.BOTTOM);// Gravity.LEFT |

        return TourGuide.init(this)
                .setToolTip(toolTip).playLater(imageView);
    }


    private ActivityComponent initActivityComponent() {
        MTGDraftParingsApplication application = (MTGDraftParingsApplication) getApplication();
        final ApplicationComponent mApplicationComponent = application.getComponent();

        mActivityComponent = mApplicationComponent.plus(new ActivityModule(this, this));
        return mActivityComponent;
    }

    protected Drawable getSingleDrawable(int drawableRes) {
        return ContextCompat.getDrawable(this, drawableRes);
    }

    protected int getSingleColor(int colorRes) {
        return ContextCompat.getColor(this, colorRes);
    }
}
