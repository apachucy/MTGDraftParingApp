package unii.draft.mtg.parings;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

/**
 * Created by apachucy on 2015-09-25.
 */
public class BaseActivity extends ActionBarActivity {


    protected void replaceFragments(Fragment fragment, String tag, int container) {
        Fragment fragmentFound = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragmentFound != null) {
            getSupportFragmentManager().beginTransaction().show(fragmentFound).commit();
        } else {
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
