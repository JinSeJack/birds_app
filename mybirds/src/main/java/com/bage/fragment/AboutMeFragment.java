package com.bage.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bage.common.Commons;
import com.bage.domain.User;
import com.bage.mybirds.R;
import com.bage.utils.LogUtils;
import com.meg7.widget.CustomShapeImageView;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

public class AboutMeFragment extends Fragment {

    public static final int REQUEST_CODE_UPDATE_PIC = 0x1;

    private View view;
    private CustomShapeImageView csiv_icon;
    private TextView et_username;
    private TextView et_birthday;
    private TextView et_description;
    private TextView et_phone;
    private TextView et_gender;
    private Context context;
    private TextView tv_remark;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();


        csiv_icon = (CustomShapeImageView) view.findViewById(R.id.aboutme_csiv_icon);
        et_username = (TextView) view.findViewById(R.id.aboutme_tv_username);
        et_phone = (TextView) view.findViewById(R.id.aboutme_tv_phone);
        et_birthday = (TextView) view.findViewById(R.id.aboutme_tv_birthday);
        et_description = (TextView) view.findViewById(R.id.aboutme_tv_des);
        et_gender = (TextView) view.findViewById(R.id.aboutme_tv_sex);
        tv_remark = (TextView) view.findViewById(R.id.aboutme_tv_remark);

        setCurrentView();

    }

    public void setCurrentView() {
        // 初始化界面
        User currentUser = Commons.currentUser;
        if (currentUser != null) {
            //  展示图片
            et_username.setText(SetTextUtils.getText(currentUser.getUse_name()));
            et_phone.setText(SetTextUtils.getText(currentUser.getUse_phone()));
            et_gender.setText(SetTextUtils.getText(currentUser.getUse_sex()));
            et_birthday.setText(SetTextUtils.getText(currentUser.getUse_birthday()));
            et_description.setText(SetTextUtils.getText(currentUser.getUse_introduction()));
            tv_remark.setText(SetTextUtils.getText(currentUser.getUse_remark()));
        } else {
            LogUtils.shownToast(context, "请先登录");
        }
    }


    static class SetTextUtils {
        public static String getText(String str) {
            return str == null ? "" : str;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_aboutme, null);
        return view;
    }

}