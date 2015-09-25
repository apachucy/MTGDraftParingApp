package unii.draft.mtg.parings.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.config.BaseConfig;
import unii.draft.mtg.parings.sharedprefrences.ISettings;
import unii.draft.mtg.parings.sharedprefrences.SettingsPreferencesFactory;
import unii.draft.mtg.parings.validation.ValidationHelper;
import unii.draft.mtg.parings.view.CheckValueListener;
import unii.draft.mtg.parings.view.CustomTextWatcher;

/**
 * Created by apachucy on 2015-09-25.
 */
public class SettingsFragment extends BaseFragment {
    @Bind(R.id.settings_counterToggleButton)
    ToggleButton mCounterToggle;
    @Bind(R.id.settings_vibrationToggleButton)
    ToggleButton mVibrationToggle;
    @Bind(R.id.settings_createParingsManualToggleButton)
    ToggleButton mManualParingToggle;
    @Bind(R.id.settings_timeButton)
    Button mCounterSaveButton;
    @Bind(R.id.settings_durationVibrationButton)
    Button mVibrationDurationSaveButton;
    @Bind(R.id.settings_fistrVibrationButton)
    Button mFirstVibrationSaveButton;
    @Bind(R.id.settings_secondVibrationButton)
    Button mSecondVibrationSaveButton;
    @Bind(R.id.settings_timeEditText)
    EditText mCounterEditText;
    @Bind(R.id.settings_durationVibrationEditText)
    EditText mVibrationDurationEditText;
    @Bind(R.id.settings_firstVibrationEditText)
    EditText mFirstVibrationEditText;
    @Bind(R.id.settings_secondVibrationEditText)
    EditText mSecondVibrationEditText;

    private ISettings mSettingsSharedPreferences;

    private CustomTextWatcher mCounterTextWatcher;
    private CustomTextWatcher mVibrationDurationTextWatcher;
    private CustomTextWatcher mFirstVibraitonTextWatcher;
    private CustomTextWatcher mSecondVibrationTextWatcher;
    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        mCounterTextWatcher = new CustomTextWatcher(mCounterListener,
                mCounterSaveButton, mCounterToggle);
        mFirstVibraitonTextWatcher = new CustomTextWatcher(
                mFirstVibrationListener, mFirstVibrationSaveButton,
                mVibrationToggle);
        mSecondVibrationTextWatcher = new CustomTextWatcher(
                mSecondVibrationListener, mSecondVibrationSaveButton,
                mVibrationToggle);
        mVibrationDurationTextWatcher = new CustomTextWatcher(
                mVibrationDurationListener, mVibrationDurationSaveButton,
                mVibrationToggle);

        mSettingsSharedPreferences = SettingsPreferencesFactory.getInstance();
        // init data from shPerferences
        initDisplayCounter(mSettingsSharedPreferences.displayCounterRound(),
                stringAsMinuts(mSettingsSharedPreferences.getTimePerRound()));
        initVibration(
                mSettingsSharedPreferences.useVibration(),
                stringAsMinuts(mSettingsSharedPreferences.getFirstVibration()),
                stringAsMinuts(mSettingsSharedPreferences.getSecondVibration()),
                stringAsSec(mSettingsSharedPreferences.getVibrationDuration()));

        mManualParingToggle.setChecked(mSettingsSharedPreferences.areManualParings());

        mCounterToggle.setOnCheckedChangeListener(mToggleListener);
        mVibrationToggle.setOnCheckedChangeListener(mToggleListener);
        mManualParingToggle.setOnCheckedChangeListener(mToggleListener);

        mCounterSaveButton.setOnClickListener(mOnButtonClickListener);
        mFirstVibrationSaveButton.setOnClickListener(mOnButtonClickListener);
        mSecondVibrationSaveButton.setOnClickListener(mOnButtonClickListener);
        mVibrationDurationSaveButton.setOnClickListener(mOnButtonClickListener);

        mCounterEditText.addTextChangedListener(mCounterTextWatcher);
        mVibrationDurationEditText
                .addTextChangedListener(mVibrationDurationTextWatcher);
        mFirstVibrationEditText
                .addTextChangedListener(mFirstVibraitonTextWatcher);
        mSecondVibrationEditText
                .addTextChangedListener(mSecondVibrationTextWatcher);
        return view;
    }


    private View.OnClickListener mOnButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.settings_timeButton:
                    if (!ValidationHelper.isTextEmpty(mCounterEditText.getText()
                            .toString())) {
                        long value = longAsMinuts(convertStringToLong(mCounterEditText
                                .getText().toString()));
                        // save only if value is different from previous
                        if (value != mSettingsSharedPreferences.getTimePerRound()) {
                            mSettingsSharedPreferences.setTimePerRound(value);
                            Toast.makeText(mActivity,
                                    getString(R.string.toast_saved),
                                    Toast.LENGTH_SHORT).show();
                            mCounterSaveButton.setEnabled(false);
                        }
                    }
                    break;
                case R.id.settings_fistrVibrationButton:
                    if (!ValidationHelper.isTextEmpty(mFirstVibrationEditText
                            .getText().toString())) {
                        long value = longAsMinuts(convertStringToLong(mFirstVibrationEditText
                                .getText().toString()));
                        // save only if value is different from previous
                        if (value != mSettingsSharedPreferences.getFirstVibration()) {
                            mSettingsSharedPreferences.setFirstVibration(value);
                            Toast.makeText(mActivity,
                                    getString(R.string.toast_saved),
                                    Toast.LENGTH_SHORT).show();
                            mFirstVibrationSaveButton.setEnabled(false);
                        }
                    }

                    break;
                case R.id.settings_secondVibrationButton:
                    if (!ValidationHelper.isTextEmpty(mSecondVibrationEditText
                            .getText().toString())) {
                        long value = longAsMinuts(convertStringToLong(mSecondVibrationEditText
                                .getText().toString()));
                        // save only if value is different from previous
                        if (value != mSettingsSharedPreferences
                                .getSecondVibration()) {
                            mSettingsSharedPreferences.setSecondVibration(value);
                            Toast.makeText(mActivity,
                                    getString(R.string.toast_saved),
                                    Toast.LENGTH_SHORT).show();
                            mSecondVibrationSaveButton.setEnabled(false);
                        }
                    }
                    break;

                case R.id.settings_durationVibrationButton:
                    if (!ValidationHelper.isTextEmpty(mVibrationDurationEditText
                            .getText().toString())) {
                        long value = longAsSec(convertStringToLong(mVibrationDurationEditText
                                .getText().toString()));
                        // save only if value is different from previous
                        if (value != mSettingsSharedPreferences
                                .getVibrationDuration()) {
                            mSettingsSharedPreferences
                                    .setVibrationDuration(longAsSec(convertStringToLong(mVibrationDurationEditText
                                            .getText().toString())));
                            Toast.makeText(mActivity,
                                    getString(R.string.toast_saved),
                                    Toast.LENGTH_SHORT).show();
                            mVibrationDurationSaveButton.setEnabled(false);
                        }
                    }
                    break;

            }

        }
    };
    /***
     * Change state of widgets depending on state of <br>
     * ToggleButtons<br>
     * if ToggleButton <br>
     * is enable then all depending widgets are enable <br>
     */
    private CompoundButton.OnCheckedChangeListener mToggleListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {

            switch (buttonView.getId()) {
                case R.id.settings_counterToggleButton:
                    // can only disable buttons
                    // button can be enable only from editTexts
                    if (!isChecked) {
                        mCounterSaveButton.setEnabled(isChecked);
                    }
                    mCounterEditText.setEnabled(isChecked);
                    mSettingsSharedPreferences.setDisplayCounterRound(isChecked);
                    // if counter is not displayed
                    // disable vibration
                    if (!isChecked && mVibrationToggle.isChecked()) {
                        setVibrationWidgetState(isChecked);
                        mVibrationToggle.toggle();

                    }
                    mVibrationToggle.setEnabled(isChecked);
                    Toast.makeText(mActivity,
                            getString(R.string.toast_saved), Toast.LENGTH_SHORT)
                            .show();

                    break;

                case R.id.settings_vibrationToggleButton:
                    setVibrationWidgetState(isChecked);
                    Toast.makeText(mActivity,
                            getString(R.string.toast_saved), Toast.LENGTH_SHORT)
                            .show();

                    break;

                case R.id.settings_createParingsManualToggleButton:
                    mSettingsSharedPreferences.setManualParings(isChecked);
                    break;
            }

        }
    };

    private void setVibrationWidgetState(boolean useVibration) {

        mVibrationDurationEditText.setEnabled(useVibration);

        mFirstVibrationEditText.setEnabled(useVibration);
        mSecondVibrationEditText.setEnabled(useVibration);
        // can only disable buttons
        // button can be enable only from editTexts
        if (!useVibration) {
            mFirstVibrationSaveButton.setEnabled(useVibration);
            mVibrationDurationSaveButton.setEnabled(useVibration);
            mSecondVibrationSaveButton.setEnabled(useVibration);
        }
        mSettingsSharedPreferences.setUseVibration(useVibration);
    }

    private void initDisplayCounter(boolean displayCounter, String text) {

        mCounterToggle.setChecked(displayCounter);
        mCounterSaveButton.setEnabled(false);

        mCounterEditText.setEnabled(displayCounter);
        mCounterEditText.setText(text);

    }

    private long convertStringToLong(String text) {
        return Long.parseLong(text);
    }

    private long longAsMinuts(long timeInMs) {
        return timeInMs * BaseConfig.DEFAULT_TIME_MINUT;
    }

    private long longAsSec(long timeInMs) {
        return timeInMs * BaseConfig.DEFAULT_TIME_SECOND;
    }

    private String stringAsSec(long timeInMs) {
        return timeInMs / BaseConfig.DEFAULT_TIME_SECOND + "";
    }

    private String stringAsMinuts(long timeInMs) {
        return timeInMs / BaseConfig.DEFAULT_TIME_MINUT + "";
    }

    private void initVibration(boolean vibration, String vib1, String vib2,
                               String duration) {

        mVibrationToggle.setChecked(vibration);

        mVibrationDurationSaveButton.setEnabled(false);
        mVibrationDurationEditText.setText(duration);
        mVibrationDurationEditText.setEnabled(vibration);

        mFirstVibrationEditText.setText(vib1);
        mFirstVibrationEditText.setEnabled(vibration);
        mFirstVibrationSaveButton.setEnabled(false);

        mSecondVibrationEditText.setText(vib2);
        mSecondVibrationEditText.setEnabled(vibration);
        mSecondVibrationSaveButton.setEnabled(false);

    }

    private CheckValueListener mCounterListener = new CheckValueListener() {

        @Override
        public String getCheckedValue() {

            return stringAsMinuts(mSettingsSharedPreferences.getTimePerRound());
        }
    };

    private CheckValueListener mVibrationDurationListener = new CheckValueListener() {

        @Override
        public String getCheckedValue() {
            return stringAsSec(mSettingsSharedPreferences
                    .getVibrationDuration());
        }
    };

    private CheckValueListener mFirstVibrationListener = new CheckValueListener() {

        @Override
        public String getCheckedValue() {
            return stringAsMinuts(mSettingsSharedPreferences
                    .getFirstVibration());
        }
    };

    private CheckValueListener mSecondVibrationListener = new CheckValueListener() {

        @Override
        public String getCheckedValue() {
            return stringAsMinuts(mSettingsSharedPreferences
                    .getSecondVibration());
        }
    };

}
