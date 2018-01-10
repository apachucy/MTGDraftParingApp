package unii.draft.mtg.parings.view.custom;


import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class RingtoneOperator {
    private static Ringtone ringtone = null;

    private RingtoneOperator() {
    }


    public static void playRingtone(Context context) {
        if (ringtone == null || !ringtone.isPlaying() && context != null) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
            ringtone.play();
        }
    }

    public static void stopRingtone() {
        if (ringtone != null) {
            ringtone.stop();
            ringtone = null;
        }
    }

    public static boolean isPlaying() {
        if (ringtone == null) {
            return false;
        }
        return ringtone.isPlaying();
    }
}
