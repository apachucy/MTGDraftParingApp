package unii.draft.mtg.parings.view.fragments;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.validation.ValidationHelper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialogFragment extends DialogFragment {

    private final static String BUNDLE_TITLE = CustomDialogFragment.class
            .getName() + "BUNDLE_TITLE";
    private final static String BUNDLE_MESSAGE = CustomDialogFragment.class
            .getName() + "BUNDLE_MESSAGE";
    private final static String BUNDLE_BUTTON_NAME = CustomDialogFragment.class
            .getName() + "BUNDLE_BUTTON_NAME";
    private static OnClickListener mOnClickListener;

    private String mTitle;
    private String mMessage;
    private String mButtonName;

    /**
     * Create a new instance of CustomDialogFragment
     *
     * @param title         - text for title cannot be null or empty
     * @param message       - text for message cannot be null or empty
     * @param buttonName    - button label
     * @param buttonOnClick - action that should be taken after click on button can be
     *                      null
     * @return in case of title or message to be null or empty return null <br>
     * else return instance of {@link CustomDialogFragment}
     */
    public static CustomDialogFragment newInstance(String title,
                                                   String message, String buttonName, OnClickListener buttonOnClick) {
        CustomDialogFragment dialogFragment = null;
        // no text provided return null
        if (ValidationHelper.isTextEmpty(title)
                || ValidationHelper.isTextEmpty(message)
                || ValidationHelper.isTextEmpty(buttonName)) {
            return dialogFragment;
        }

        if (buttonOnClick != null) {
            mOnClickListener = buttonOnClick;
            dialogFragment.setOnClickListener(buttonOnClick);
        }
        dialogFragment = new CustomDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(BUNDLE_TITLE, title);
        args.putString(BUNDLE_MESSAGE, message);
        args.putString(BUNDLE_BUTTON_NAME, buttonName);
        dialogFragment.setArguments(args);
        // request a window without the title
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return dialogFragment;
    }

    /**
     * Create a new instance of CustomDialogFragment
     *
     * @param title      - text for title cannot be null or empty
     * @param message    - text for message cannot be null or empty
     * @param buttonName - button label
     * @return in case of title or message to be null or empty return null <br>
     * else return instance of {@link CustomDialogFragment}
     */
    public static CustomDialogFragment newInstance(String title,
                                                   String message, String buttonName) {
        return CustomDialogFragment.newInstance(title, message, buttonName,
                null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString(BUNDLE_TITLE);
        mMessage = getArguments().getString(BUNDLE_MESSAGE);
        mButtonName = getArguments().getString(BUNDLE_BUTTON_NAME);
    }

    public static void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);

        TextView titleTextView = (TextView) v
                .findViewById(R.id.dialog_titleTextView);
        TextView messageTextView = (TextView) v
                .findViewById(R.id.dialog_bodyTextView);
        Button dismissButton = (Button) v
                .findViewById(R.id.dialog_dismissButton);
        titleTextView.setText(mTitle);
        messageTextView.setText(mMessage);

        if (mOnClickListener == null) {
            mOnClickListener = new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismissAllowingStateLoss();
                }
            };
        }

        dismissButton.setOnClickListener(mOnClickListener);
        dismissButton.setText(mButtonName);
        return v;
    }

    /**
     * @param manager
     * @param tag
     * @param onClick action when click on button
     * @overload
     */
    public void show(FragmentManager manager, String tag,
                     OnClickListener onClick) {
        mOnClickListener = onClick;
        super.show(manager, tag);
    }

}
