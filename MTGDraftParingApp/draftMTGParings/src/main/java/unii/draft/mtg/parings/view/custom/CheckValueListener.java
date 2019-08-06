package unii.draft.mtg.parings.view.custom;

import androidx.annotation.NonNull;

public interface CheckValueListener {
	/**
	 * get value that should be checked
	 * 
	 * @return
	 */
    @NonNull
    String getCheckedValue();
}
