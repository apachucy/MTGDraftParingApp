package unii.draft.mtg.parings.view.custom;

import unii.draft.mtg.parings.util.validation.ValidationHelper;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ToggleButton;

public class CustomTextWatcher implements TextWatcher {

	private CheckValueListener mCheckValueListener;
	private Button mButton;
	private ToggleButton mToggleButton;

	/**
	 * Changing state of button depending on saved value, higher element in
	 * hierarchy <br>
	 * and actual value in editText
	 * 
	 * @param checkValueListener
	 *            get saved long value and determine if button should change
	 *            state
	 * @param button
	 *            change state of this element - disable/enable
	 * @param toggleButton
	 *            element higher in hierarchy determine if button can change
	 *            state
	 */
	public CustomTextWatcher(CheckValueListener checkValueListener,
			Button button, ToggleButton toggleButton) {
		mCheckValueListener = checkValueListener;
		mButton = button;
		mToggleButton = toggleButton;
	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(@NonNull CharSequence s, int start, int before, int count) {
		/**
		 * Check if listener is not null <br>
		 * check if toggle button is set to be enabled <br>
		 * check if value is different <br>
		 * check if status of button is different<br>
		 * */
		if (mCheckValueListener != null
				&& mToggleButton.isEnabled()
				&& !ValidationHelper.isTextEmpty(s.toString())
				&& !s.toString().equals(
						mCheckValueListener.getCheckedValue() + "")) {
			mButton.setEnabled(true);
		} else if (mCheckValueListener != null
				&& mToggleButton.isEnabled()
				&& s.toString().equals(
						mCheckValueListener.getCheckedValue() + "")
				&& mButton.isEnabled()) {
			mButton.setEnabled(false);
		} else if (mCheckValueListener != null && !mToggleButton.isEnabled()) {
			mButton.setEnabled(false);
		} else {
			// sanity check
			mButton.setEnabled(false);
		}
	}
}
