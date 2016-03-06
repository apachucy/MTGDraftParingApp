package unii.draft.mtg.parings.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import unii.draft.mtg.parings.HistoryScoreBoardActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.config.BaseConfig;
import unii.draft.mtg.parings.database.model.Draft;
import unii.draft.mtg.parings.database.model.DraftDao;
import unii.draft.mtg.parings.database.model.IDatabaseHelper;
import unii.draft.mtg.parings.database.model.PlayerDao;
import unii.draft.mtg.parings.sharedprefrences.ISettings;
import unii.draft.mtg.parings.sharedprefrences.SettingsPreferencesFactory;
import unii.draft.mtg.parings.validation.ValidationHelper;
import unii.draft.mtg.parings.view.custom.CheckValueListener;
import unii.draft.mtg.parings.view.custom.CustomTextWatcher;
import unii.draft.mtg.parings.view.custom.IActivityHandler;

/**
 * Created by apachucy on 2015-09-25.
 */

public class SettingsFragment extends BaseFragment {

    @Bind(R.id.settings_counterToggleButton)
    ToggleButton mCounterToggle;

    @OnCheckedChanged(R.id.settings_counterToggleButton)
    void onCounterToggleChecked(boolean checked) {
        // can only disable buttons
        // button can be enable only from editTexts
        if (!checked) {
            mCounterSaveButton.setEnabled(checked);
        }
        mCounterEditText.setEnabled(checked);
        mSettingsSharedPreferences.setDisplayCounterRound(checked);
        // if counter is not displayed
        // disable vibration
        if (!checked && mVibrationToggle.isChecked()) {
            setVibrationWidgetState(checked);
            mVibrationToggle.toggle();
        }
        mVibrationToggle.setEnabled(checked);
    }

    @Bind(R.id.settings_vibrationToggleButton)
    ToggleButton mVibrationToggle;

    @OnCheckedChanged(R.id.settings_vibrationToggleButton)
    void onVibrationToggleChecked(boolean checked) {
        setVibrationWidgetState(checked);
    }

    @Bind(R.id.settings_createParingsManualToggleButton)
    ToggleButton mManualParingToggle;

    @OnCheckedChanged(R.id.settings_createParingsManualToggleButton)
    void onManualParringToggleChecked(boolean checked) {
        mSettingsSharedPreferences.setManualParings(checked);
    }

    @Bind(R.id.settings_timeButton)
    Button mCounterSaveButton;

    @OnClick(R.id.settings_timeButton)
    void onCounterSaveButtonClicked(View view) {
        if (!ValidationHelper.isTextEmpty(mCounterEditText.getText()
                .toString())) {
            long value = longAsMinutes(convertStringToLong(mCounterEditText
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
    }

    @Bind(R.id.settings_durationVibrationButton)
    Button mVibrationDurationSaveButton;

    @OnClick(R.id.settings_durationVibrationButton)
    void onVibrationDurationButtonClicked(View view) {
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
    }

    @Bind(R.id.settings_fistrVibrationButton)
    Button mFirstVibrationSaveButton;

    @OnClick(R.id.settings_fistrVibrationButton)
    void onFirstVibrationButtonClicked(View view) {
        if (!ValidationHelper.isTextEmpty(mFirstVibrationEditText
                .getText().toString())) {
            long value = longAsMinutes(convertStringToLong(mFirstVibrationEditText
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
    }

    @Bind(R.id.settings_secondVibrationButton)
    Button mSecondVibrationSaveButton;

    @OnClick(R.id.settings_secondVibrationButton)
    void onSecondVibrationButtonClicked(View view) {
        if (!ValidationHelper.isTextEmpty(mSecondVibrationEditText
                .getText().toString())) {
            long value = longAsMinutes(convertStringToLong(mSecondVibrationEditText
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
    }

    @Bind(R.id.settings_timeEditText)
    EditText mCounterEditText;
    @Bind(R.id.settings_durationVibrationEditText)
    EditText mVibrationDurationEditText;
    @Bind(R.id.settings_firstVibrationEditText)
    EditText mFirstVibrationEditText;
    @Bind(R.id.settings_secondVibrationEditText)
    EditText mSecondVibrationEditText;

    @OnClick(R.id.settings_removeScoreBoardsButton)
    void onRemoveScoreBoardClicked(View view) {
        PlayerDao playerDao = ((IDatabaseHelper) mActivity.getApplication()).getDaoSession().getPlayerDao();
        DraftDao draftDao = ((IDatabaseHelper) mActivity.getApplication()).getDaoSession().getDraftDao();
        playerDao.deleteAll();
        draftDao.deleteAll();
        Toast.makeText(mActivity, getString(R.string.message_score_boards_removed), Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.settings_displayScoreBoardsInfoTextView)
    void onDisplayScoreBoardsClicked(View view) {
        DraftDao draftDao = ((IDatabaseHelper) mActivity.getApplication()).getDaoSession().getDraftDao();
        List<Draft> draftList = draftDao.loadAll();
        if (draftList == null || draftList.size() == 0) {
            Toast.makeText(mActivity, getString(R.string.message_score_board_not_exists), Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(mActivity, HistoryScoreBoardActivity.class);
            startActivity(intent);
        }
    }

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


        mCounterEditText.addTextChangedListener(mCounterTextWatcher);
        mVibrationDurationEditText
                .addTextChangedListener(mVibrationDurationTextWatcher);
        mFirstVibrationEditText
                .addTextChangedListener(mFirstVibraitonTextWatcher);
        mSecondVibrationEditText
                .addTextChangedListener(mSecondVibrationTextWatcher);
        return view;
    }


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

    private long longAsMinutes(long timeInMs) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
