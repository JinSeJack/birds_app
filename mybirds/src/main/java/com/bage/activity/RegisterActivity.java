package com.bage.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bage.mybirds.R;
import com.bage.utils.LogUtils;
import com.bage.utils.ProgressGenerator;
import com.bage.utils.UrlUtils;
import com.dd.processbutton.iml.ActionProcessButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements ProgressGenerator.OnCompleteListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private ProgressGenerator progressGenerator;
    private ActionProcessButton btnSignIn;
    private RadioGroup radioGroup;
    private String user_sex = "男";
    private EditText nikeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.phone);
        nikeName = (EditText) findViewById(R.id.et_nikename);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                final View view = textView;
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegister(view);
                    return true;
                }
                return false;
            }
        });
        radioGroup = (RadioGroup) findViewById(R.id.rg_gender);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_boy:
                        user_sex = "男";
                        break;
                    case R.id.rb_girl:
                        user_sex = "女";
                        break;
                    default:
                        break;
                }
            }
        });
        progressGenerator = new ProgressGenerator(this);
        btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        // btnSignIn.setMode(ActionProcessButton.Mode.PROGRESS);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister(v);
            }
        });
    }

    private void attemptRegister(View view) {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isPhoneValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_phone));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // 关闭软键盘
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            progressGenerator.start(btnSignIn);
            btnSignIn.setEnabled(false);
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("use_phone", email);
            params.put("use_password", password);
            params.put("use_sex", user_sex);
            params.put("use_name", nikeName.getText().toString());
            String registerUrl = UrlUtils.getControllerUrl(this, "api/user", "register");
            asyncHttpClient.post(this, registerUrl, params, new AsyncHttpResponseHandler(){

                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String responseText = new String(bytes);
                    if(responseText.equals("注册成功")){
                        LogUtils.shownToast(RegisterActivity.this, "注册成功");
                        finish();
                    }else{
                        LogUtils.shownToast(RegisterActivity.this, responseText);
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                }
            });

        }
    }

    private boolean isPhoneValid(String email) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    @Override
    public void onComplete() {
        LogUtils.shownToast(this, "注册成功");
        startActivity(new Intent(this,LoginActivity.class));

    }
}

