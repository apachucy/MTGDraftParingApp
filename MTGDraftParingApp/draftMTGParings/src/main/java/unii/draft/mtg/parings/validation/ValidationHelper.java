package unii.draft.mtg.parings.validation;

import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

/**
 * @author Arkadiusz Pachucy <br>
 *         Validation class for validating<br>
 *         fields and displaying a warning<br>
 */
public class ValidationHelper {
    /**
     * Check if editText is empty <br>
     * in case of:<br>
     * -null reference <br>
     * -no text in editText<br>
     * return true can set warning in editText <br>
     * if reference to it is not null <br>
     * and string was provided <br>
     *
     * @param editText field to be validated
     * @param warning  additional string to display a warning
     * @return true if reference is null or when there are no text <br>
     * in other case return false<br>
     */
    public static boolean isEditTextEmpty(EditText editText, String warning) {
        return isEditTextEmpty(editText, warning, null);
    }

    /**
     * Check if editText is empty <br>
     * in case of:<br>
     * -null reference <br>
     * -no text in editText<br>
     * return true can set warning in editText <br>
     * if reference to it is not null <br>
     * and string was provided <br>
     *
     * @param editText field to be validated
     * @param warning  additional string to display a warning
     * @param drawable icon to be displayed in editText (only if warning is provided)
     *                 in case of null use default icon
     * @return true if reference is null or when there are no text <br>
     * in other case return false<br>
     */
    public static boolean isEditTextEmpty(EditText editText, String warning,
                                          Drawable drawable) {
        // reference is null
        if (editText == null) {
            return true;
        }
        // this is emptyfield
        if (isTextEmpty(editText.getText().toString())) {
            // display warning in editText
            if (!isTextEmpty(warning)) {
                if (drawable == null) {
                    editText.setError(warning);
                } else {
                    editText.setError(warning, drawable);
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isTextInputFieldEmpty(TextInputLayout textInputLayout, String warning) {
        // reference is null
        if (textInputLayout == null || textInputLayout.getEditText() == null) {
            return true;
        }
        if (isTextEmpty(textInputLayout.getEditText().getText().toString())) {
            // display warning in editText
            if (!isTextEmpty(warning)) {
                textInputLayout.setError(warning);
            }
            return true;
        }
        textInputLayout.setError(null);
        return false;
    }

    public static boolean isTextEmpty(String text) {
        return text == null || text.trim().equals("")
                || text.trim().equals(" ");
    }
}
