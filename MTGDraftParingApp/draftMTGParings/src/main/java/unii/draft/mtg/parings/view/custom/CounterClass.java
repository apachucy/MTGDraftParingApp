package unii.draft.mtg.parings.view.custom;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import unii.draft.mtg.parings.util.config.BaseConfig;

public class CounterClass extends CountDownTimer {

    private TextView mTextView;
    private long mFirstVibration;
    private long mSecondVibration;
    private long mTimeVibration;
    private Vibrator mVibration;
    private long millisTillFinishWillBeCalled;

    /**
     * @param context           create an instance of vibration class
     * @param millisInFuture    The number of mills in the future from the call to start()
     *                          until the countdown is done and onFinish() is called.
     * @param countDownInterval The interval along the way to receive onTick(long) callback
     * @param textView          Display count down in text view
     * @param firstVibration    When first vibration should be start <br>
     *                          set to 0 to not run <br>
     *                          first vibration
     * @param secondVibration   When second vibration should be start<br>
     *                          set to 0 to not run <br>
     *                          second vibration
     * @param vibrationTime     Time for run each vibration <br>
     *                          set to 0 to not run vibrations <br>
     */
    public CounterClass(@Nullable Context context, long millisInFuture,
                        long countDownInterval, TextView textView, long firstVibration,
                        long secondVibration, long vibrationTime) {
        super(millisInFuture, countDownInterval);
        mTextView = textView;
        mFirstVibration = firstVibration;
        mSecondVibration = secondVibration;
        mTimeVibration = vibrationTime;

        // create Vibration instance only if
        // it will be used
        if (context != null && vibrationTime > 0
                && (firstVibration > 0 || secondVibration > 0)) {
            mVibration = (Vibrator) context
                    .getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    @Override
    public void onFinish() {
        // display formated string
        String hms = String.format(
                "%02d:%02d:%02d", 0, 0, 0
        );

        mTextView.setText(hms);
    }

    public long onPause() {
        return millisTillFinishWillBeCalled;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        millisTillFinishWillBeCalled = millisUntilFinished;
        // display formated string
        String hms = String.format(
                "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millisTillFinishWillBeCalled),
                TimeUnit.MILLISECONDS.toMinutes(millisTillFinishWillBeCalled)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                        .toHours(millisTillFinishWillBeCalled)),
                TimeUnit.MILLISECONDS.toSeconds(millisTillFinishWillBeCalled)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                        .toMinutes(millisTillFinishWillBeCalled)));

        mTextView.setText(hms);
        // instance of vibration exist
        if (mVibration != null) {

            if (mFirstVibration > 0
                    && BaseConfig.TIME_MARGIN_ERROR + mFirstVibration > millisUntilFinished
                    && mFirstVibration - BaseConfig.TIME_MARGIN_ERROR < millisUntilFinished) {
                mVibration.vibrate(mTimeVibration);
            }
            if (mSecondVibration > 0
                    && BaseConfig.TIME_MARGIN_ERROR + mSecondVibration > millisUntilFinished
                    && mSecondVibration - BaseConfig.TIME_MARGIN_ERROR < millisUntilFinished) {

                mVibration.vibrate(mTimeVibration);
            }

        }
    }
}
