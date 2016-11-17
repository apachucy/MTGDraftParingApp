package unii.draft.mtg.parings.view.custom;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import unii.draft.mtg.parings.R;

public class SingleChoiceListCallback implements MaterialDialog.ListCallbackSingleChoice {

    private TextView mTextView;
    private String mName;
    private Context mContext;

    public SingleChoiceListCallback(Context context, TextView textView) {
        mTextView = textView;
        mName = "";
        mContext = context;
        textView.setText(noPlayerSelected());
    }

    @Override
    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
        if (text == null || text.length() == 0) {
            return true;
        }
        mName = text.toString();
        mTextView.setText(mContext.getString(R.string.select_player_name, mName));
        return true;
    }

    public String getCurrentName() {
        return mName;
    }

    public void cleanName() {
        mName = "";
        mTextView.setText(noPlayerSelected());
    }

    private String noPlayerSelected() {
        return mContext.getString(R.string.select_player_name, "-");
    }
}
