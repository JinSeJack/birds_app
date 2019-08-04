package com.bage.validator;

import android.widget.EditText;

import com.andreabaccega.formedittextvalidator.Validator;

/**
 * Created by bage on 2016/3/14.
 */

public class TestValidator extends Validator {

    public TestValidator(String customErrorMessage) {
        super(customErrorMessage);
    }

    @Override
    public boolean isValid(EditText et) {
        return true;
        //return TextUtils.equals(et.getText(), "test");
    }

}
