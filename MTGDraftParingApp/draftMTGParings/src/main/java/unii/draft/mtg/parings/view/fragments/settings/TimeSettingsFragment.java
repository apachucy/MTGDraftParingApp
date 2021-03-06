package unii.draft.mtg.parings.view.fragments.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.afollestad.materialdialogs.MaterialDialog;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.config.BaseConfig;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

public class TimeSettingsFragment extends BaseFragment {


    @Bind(R.id.settings_useCounter)
    CheckBox mUseTimeCounterCheckBox;

    @Bind(R.id.settings_useVibration)
    CheckBox mUseVibrationCheckBox;

    @Bind(R.id.settings_setTimeCounter)
    Button mSetTimeCounterTextView;

    @Bind(R.id.settings_firstVibration)
    Button mSetFirstVibrationTextView;

    @Bind(R.id.settings_secondVibration)
    Button mSetSecondVibrationTextView;

    @Bind(R.id.settings_durationVibration)
    Button mDurationVibrationTextView;

    @Inject
    ISharedPreferences mSharedPreferenceManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_time, container, false);
        ButterKnife.bind(this, view);
        injectDependencies();
        initFragmentView();
        initFragmentData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    @Override
    protected void initFragmentView() {

    }

    @Override
    protected void initFragmentData() {
        initDisplayCounter(mSharedPreferenceManager.displayCounterRound(),
                stringAsMinutes(mSharedPreferenceManager.getTimePerRound()));
        initVibration(
                mSharedPreferenceManager.useVibration(),
                stringAsMinutes(mSharedPreferenceManager.getFirstVibration()),
                stringAsMinutes(mSharedPreferenceManager.getSecondVibration()),
                stringAsSec(mSharedPreferenceManager.getVibrationDuration()));

    }


    @OnCheckedChanged(R.id.settings_useCounter)
    void onCheckUseTimeCounterChanged(boolean checked) {
        mSharedPreferenceManager.setDisplayCounterRound(checked);
        mSetTimeCounterTextView.setEnabled(checked);
    }

    @OnCheckedChanged(R.id.settings_useVibration)
    void onCheckUseVibrationChanged(boolean checked) {
        mSharedPreferenceManager.setUseVibration(checked);
        mSetFirstVibrationTextView.setEnabled(checked);
        mSetSecondVibrationTextView.setEnabled(checked);
        mDurationVibrationTextView.setEnabled(checked);

    }

    @OnClick(R.id.settings_setTimeCounter)
    void onSetTimeCounterClick() {
        String roundTime = stringAsMinutes(mSharedPreferenceManager.getTimePerRound());
        UpdateData updateDataTimeCounter = new UpdateData() {
            @Override
            public void updateView() {
                String roundTime = stringAsMinutes(mSharedPreferenceManager.getTimePerRound());
                mSetTimeCounterTextView.setText(getString(R.string.text_time_round, roundTime));
            }

            @Override
            public void updateSharedPreferences(String newData) {
                try {
                    long timePerRound = longAsMinutes(convertStringToLong(newData));
                    if (timePerRound != mSharedPreferenceManager.getTimePerRound()) {
                        mSharedPreferenceManager.setTimePerRound(timePerRound);
                    }
                } catch (NumberFormatException exception) {
                    return;
                } finally {

                }

            }
        };
        showEditTextDialog(getContext(), getString(R.string.settings_dialog_round_time_title), getString(R.string.settings_dialog_round_time_content),
                getString(R.string.settings_dialog_round_time_hint), roundTime, updateDataTimeCounter);
    }

    @OnClick(R.id.settings_firstVibration)
    void onSetFirstVibrationClick() {
        String firstVibration = stringAsMinutes(mSharedPreferenceManager.getFirstVibration());
        UpdateData updateDataFirstVibration = new UpdateData() {
            @Override
            public void updateView() {
                String firstVibrationTime = stringAsMinutes(mSharedPreferenceManager.getFirstVibration());
                mSetFirstVibrationTextView.setText(getString(R.string.text_first_vibration, firstVibrationTime));
            }

            @Override
            public void updateSharedPreferences(String newData) {
                try {
                    long newValue = longAsMinutes(convertStringToLong(newData));
                    if (newValue != mSharedPreferenceManager.getFirstVibration()) {
                        mSharedPreferenceManager.setFirstVibration(newValue);
                    }
                } catch (NumberFormatException exception) {
                    return;
                } finally {

                }
            }
        };
        showEditTextDialog(getContext(), getString(R.string.settings_dialog_round_first_vibration_title), getString(R.string.settings_dialog_round_first_vibration_content), getString(R.string.settings_dialog_round_first_vibration_hint),
                firstVibration, updateDataFirstVibration);
    }


    @OnClick(R.id.settings_secondVibration)
    void onSetSecondVibrationClick() {
        String secondVibration = stringAsMinutes(mSharedPreferenceManager.getSecondVibration());
        UpdateData updateDataSecondVibration = new UpdateData() {
            @Override
            public void updateView() {
                String secondVibrationTime = stringAsMinutes(mSharedPreferenceManager.getSecondVibration());
                mSetSecondVibrationTextView.setText(getString(R.string.text_second_vibration, secondVibrationTime));
            }

            @Override
            public void updateSharedPreferences(String newData) {
                try {
                    long newValue = longAsMinutes(convertStringToLong(newData));
                    if (newValue != mSharedPreferenceManager.getSecondVibration() && newValue < mSharedPreferenceManager.getFirstVibration()) {
                        mSharedPreferenceManager.setSecondVibration(newValue);
                    }
                } catch (NumberFormatException exception) {
                    return;
                } finally {

                }
            }
        };
        showEditTextDialog(getContext(), getString(R.string.settings_dialog_round_second_vibration_title), getString(R.string.settings_dialog_round_second_vibration_content), getString(R.string.settings_dialog_round_second_vibration_hint),
                secondVibration, updateDataSecondVibration);

    }

    @OnClick(R.id.settings_durationVibration)
    void onSetDurationVibrationClick() {
        String durationVibrationTimeInSec = stringAsSec(mSharedPreferenceManager.getVibrationDuration());
        UpdateData durationVibrationTime = new UpdateData() {
            @Override
            public void updateView() {
                String durationVibrationTimeInSec = stringAsSec(mSharedPreferenceManager.getVibrationDuration());
                mDurationVibrationTextView.setText(getString(R.string.text_duration_vibration, durationVibrationTimeInSec));

            }

            @Override
            public void updateSharedPreferences(String newData) {
                try {
                    long newValue = longAsSec(convertStringToLong(newData));
                    if (newValue != mSharedPreferenceManager.getVibrationDuration()) {
                        mSharedPreferenceManager.setVibrationDuration(newValue);
                    }
                } catch (NumberFormatException exception) {
                    return;
                } finally {

                }
            }

        };

        showEditTextDialog(getContext(), getString(R.string.settings_dialog_round_duration_vibration_title), getString(R.string.settings_dialog_round_duration_vibration_content), getString(R.string.settings_dialog_round_duration_vibration_hint),
                durationVibrationTimeInSec, durationVibrationTime);
    }

    protected void showEditTextDialog(Context context, String title, String content, String hint, String lastValue,
                                      final UpdateData updateData) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED)
                .backgroundColorRes(R.color.windowBackground)
                .input(hint, lastValue, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        int value = 0;

                        try {
                            value = Integer.parseInt(input.toString());
                        } catch (NumberFormatException e) {
                            value = 0;
                        } finally {
                            if (value < 0) {
                                //noinspection ReturnInsideFinallyBlock
                                return;
                            }
                            updateData.updateSharedPreferences(input.toString());
                            updateData.updateView();
                        }


                    }
                }).show();

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

    private String stringAsMinutes(long timeInMs) {
        return timeInMs / BaseConfig.DEFAULT_TIME_MINUT + "";
    }

    private void initDisplayCounter(boolean displayCounter, String text) {
        mUseTimeCounterCheckBox.setChecked(displayCounter);
        mSetTimeCounterTextView.setEnabled(displayCounter);
        mSetTimeCounterTextView.setText(getString(R.string.text_time_round, text));
    }

    private void initVibration(boolean vibration, String vib1, String vib2,
                               String duration) {
        mUseVibrationCheckBox.setChecked(vibration);
        mSetFirstVibrationTextView.setEnabled(vibration);
        mSetSecondVibrationTextView.setEnabled(vibration);
        mDurationVibrationTextView.setEnabled(vibration);

        mSetFirstVibrationTextView.setText(getString(R.string.text_first_vibration, vib1));
        mSetSecondVibrationTextView.setText(getString(R.string.text_second_vibration, vib2));
        mDurationVibrationTextView.setText(getString(R.string.text_duration_vibration, duration));
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }


    private interface UpdateData {
        void updateView();

        void updateSharedPreferences(String newData);
    }
}
