package com.bage.validator;

import android.text.TextUtils;
import android.widget.EditText;

import com.andreabaccega.formedittextvalidator.Validator;

/**
 * Created by bage on 2016/3/14.
 */

public class Test2Validator extends Validator {

    public Test2Validator(String customErrorMessage) {
        super(customErrorMessage);
    }

    @Override
    public boolean isValid(EditText et) {
        return true;
    }

}
