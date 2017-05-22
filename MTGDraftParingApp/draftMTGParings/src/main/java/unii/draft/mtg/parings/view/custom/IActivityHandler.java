package unii.draft.mtg.parings.view.custom;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;


public interface IActivityHandler {

    void showInfoDialog(String title, String body, String positiveText);

    void showInfoDialog(String title, String body, String positiveText, MaterialDialog.SingleButtonCallback positiveAction);

    void showSingleChoiceList(Context context, String title, List<String> list, String positiveText, MaterialDialog.ListCallbackSingleChoice singleListCallback);
}
