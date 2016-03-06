package unii.draft.mtg.parings.view.custom;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Unii on 2016-03-04.
 */
public interface IActivityHandler {

    void showInfoDialog(String title, String body, String positiveText);

    void showInfoDialog(String title, String body, String positiveText, MaterialDialog.SingleButtonCallback positiveAction);
}
