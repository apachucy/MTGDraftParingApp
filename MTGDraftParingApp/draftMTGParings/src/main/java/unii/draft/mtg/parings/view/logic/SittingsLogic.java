package unii.draft.mtg.parings.view.logic;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import unii.draft.mtg.parings.util.config.BaseConfig;

public class SittingsLogic {
    private Context mContext;

    public SittingsLogic(Context context) {
        mContext = context;
    }

    public void openGooglePlayMTGManaCalculatorApp() {
        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BaseConfig.INTENT_OPEN_GOOGLE_PLAY + BaseConfig.INTENT_PACKAGE_MANA_CALCULATOR_APP_UNII)));
        } catch (android.content.ActivityNotFoundException anfe) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BaseConfig.INTENT_OPEN_GOOGLE_PLAY_WWW + BaseConfig.INTENT_PACKAGE_MANA_CALCULATOR_APP_UNII)));
        }
    }
}
