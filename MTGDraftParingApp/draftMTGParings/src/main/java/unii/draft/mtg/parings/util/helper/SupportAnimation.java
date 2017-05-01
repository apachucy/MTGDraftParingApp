package unii.draft.mtg.parings.util.helper;

import android.os.Build;

public class SupportAnimation {
    public static boolean checkIfAnimationAreSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
