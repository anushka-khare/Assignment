package com.daffodil.assignment.utilities;

import android.content.Context;
import android.widget.EditText;

import com.daffodil.assignment.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "^(?=.*?[0-9]).{7,15}$";


    public static boolean isEmailAddress(Context context, EditText input, boolean required) {
        String text = input.getText().toString().trim();

        // text required and editText is blank, so return false
        if (required && !hasText(context, input, R.string.msg_enter_email)) {
            return false;
        }

        // text not required and text not empty and pattern mismatch
        if (required && text.length() > 0 && !Pattern.matches(EMAIL_REGEX, text)) {
            input.requestFocus();
            input.setError(context.getResources().getString(R.string.msg_email_invalid));
            return false;
        }
        return true;
    }


    // check the input field has any text or not
    // return true if it contains text otherwise false

    public static boolean hasText(Context context, EditText editText, int msg) {

        String text = editText.getText().toString().trim();
        if (text.length() == 0) {
            editText.requestFocus();
            editText.setError(context.getResources().getString(msg));
            return false;
        }
        return true;
    }

    public static boolean isValidMobile(Context context, EditText input, boolean required) {
        String text = input.getText().toString().trim();

        // text required and editText is blank, so return false
        if (required && !hasText(context, input, R.string.phone_empty)) {
            return false;
        }

        // text not required and text not empty and pattern mismatch
        if (required && text.length() > 0 && !Pattern.matches(PHONE_REGEX, text)) {
            input.requestFocus();
            input.setError(context.getResources().getString(R.string.invalid_contact));
            return false;
        }


        return true;
    }


}
