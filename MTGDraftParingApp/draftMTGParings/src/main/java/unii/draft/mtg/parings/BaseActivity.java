package unii.draft.mtg.parings;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

/**
 * Created by apachucy on 2015-09-25.
 */
public class BaseActivity extends ActionBarActivity {

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
            getSupportFragmentManager().beginTransaction().replace(container, fragment, tag).commit();
        }
    }

    protected TourGuide bindTourGuideButton(String bodyText, ImageView imageView) {
        ToolTip toolTip = new ToolTip()
                .setTitle(getString(R.string.help_title))
                .setDescription(bodyText).setBackgroundColor(getResources().getColor(R.color.accent))
                .setGravity(Gravity.LEFT | Gravity.BOTTOM);

        return TourGuide.init(this)
                .setToolTip(toolTip).playLater(imageView);
    }

    protected TourGuide bindTourGuideButton(String bodyText, TextView textView, int gravity) {
        ToolTip toolTip = new ToolTip()
                .setTitle(getString(R.string.help_title))
                .setDescription(bodyText).setBackgroundColor(getResources().getColor(R.color.accent))
                .setGravity(gravity);

        return TourGuide.init(this)
                .setToolTip(toolTip).playLater(textView);
    }
}
