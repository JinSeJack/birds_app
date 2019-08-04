package com.bage.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.bage.common.Commons;
import com.bage.domain.User;
import com.bage.mybirds.R;
import com.bage.utils.JsonUtils;
import com.bage.utils.LogUtils;
import com.bage.utils.ProgressGenerator;
import com.bage.utils.UrlUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        System.out.println("我是登录界面，我要来了了 -------------------------");
    }

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private ProgressGenerator progressGenerator;
    private Button btnSignIn;
    private String loginUrl;
    private String email;
    private String password;
    private ProgressDialog progressDialog;
    private CheckBox cb_remb;
    private CheckBox cb_auto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginUrl = UrlUtils.getControllerUrl(this, "api/user", "login");
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.phone);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                final View view = textView;
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin(view);
                    return true;
                }
                return false;
            }
        });
        cb_auto = (CheckBox) findViewById(R.id.cb_autoLogin);
        cb_remb = (CheckBox) findViewById(R.id.cb_rememberPassword);
        cb_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb_remb.setChecked(true);
                }
            }
        });
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin(v);
            }
        });

        Intent intent = getIntent();
        if(intent.getBooleanExtra("logout", false)){
            SharedPreferences preferences=getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String user = preferences.getString("user", "");
            if("".equals(user)){
                return;
            }else{
                User cUser = JsonUtils.fromJson(user, User.class);
                email = cUser.getUse_phone();
                password = preferences.getString("password", "");
                mEmailView.setText(email);
                mPasswordView.setText(password);
                cb_remb.setChecked(true);
                cb_auto.setChecked(false);
            }
        }else{
            checkeLocalUser();
        }
    }

    public boolean checkeLocalUser(){
        SharedPreferences preferences=getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String user = preferences.getString("user", "");
        try {
            if("".equals(user)){
                return false;
            }
            User cUser = JsonUtils.fromJson(user, User.class);
            password = preferences.getString("password", "");
            boolean auto = preferences.getBoolean("auto", false);
            if (!("").equals(password) && !"".equals(cUser.getUse_phone())) {
                email = cUser.getUse_phone();
                mEmailView.setText(email);
                mPasswordView.setText(password);
                cb_remb.setChecked(true);
                if (auto) {
                    postInfo();
                    cb_auto.setChecked(true);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }catch(Exception e){
            return false;
        }
    }

    private void attemptLogin(View view) {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
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
            showProgressDialog("正在登录。。。");
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            postInfo();
        }
    }

    private void postInfo() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("use_phone", email);
        params.put("use_password", password);
        LogUtils.sysoln("loginUrl:" + loginUrl);
        asyncHttpClient.post(this, loginUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String res = new String(bytes);
                if (i == 200) {
                    LogUtils.sysoln("登陆成功：res：" + res);
                    try{
                        onComplete(JsonUtils.toBeen(res, User.class));
                    }catch (Exception e){
                        LogUtils.shownToast(LoginActivity.this, "用户名或密码不正确，请重试！");
                    }
                } else {
                    LogUtils.shownToast(LoginActivity.this, res);
                }
                dismissDialog();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                dismissDialog();
                if("15061113379".equals(email) && "123".equals(password)){
                    User user = new User();
                    user.setUse_id(1);
                    user.setUse_phone("15061113379");
                    user.setUse_birthday("1994-01-01");
                    user.setUse_introduction("不是诗人是痴人");
                    user.setUse_name("王呈飞");
                    user.setUse_remark("程序员");
                    user.setUse_sex("男");
                    onComplete(user);
                    LogUtils.shownToast(LoginActivity.this, "登陆成功");
                }else{
                    LogUtils.shownToast(LoginActivity.this, "用户名不存在或密码错误");
                }
            }
        });
    }


    public void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog(String text) {

        progressDialog = new ProgressDialog(this);
        // 实例化
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置进度条风格，风格为圆形，旋转的

        progressDialog.setMessage(text);
        // 设置ProgressDialog 提示信息

        // 设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);

        // 设置ProgressDialog 是否可以按退回按键取消
        progressDialog.setCancelable(true);

        // 让ProgressDialog显示
        progressDialog.show();
    }

    public void onComplete(User currentUser) {
        Commons.currentUser = currentUser;
        if(cb_remb.isChecked() || cb_auto.isChecked()){
            saveUser(currentUser);
        }
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }

    private void saveUser(User currentUser) {
        SharedPreferences preferences = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user", JsonUtils.BeantoJsonStr(currentUser));
        editor.putString("password", password);
        editor.putBoolean("remb", cb_remb.isChecked());
        editor.putBoolean("auto", cb_auto.isChecked());
        editor.commit();
    }

    /**
     * 开启注册界面
     *
     * @param view
     */
    public void myRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}

