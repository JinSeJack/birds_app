package com.bage.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.bage.activity.LoginActivity;
import com.bage.common.Commons;
import com.bage.domain.User;
import com.bage.mybirds.R;
import com.bage.utils.LogUtils;
import com.meg7.widget.CustomShapeImageView;

public class SettingFragment extends Fragment {


    private View view;
    private Context context;
    private ProgressDialog progressDialog;
    private CustomShapeImageView civ_img;
    private TextView tv_username;
    private TextView tv_phone;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();
        View rl_clear = view.findViewById(R.id.setting_rl_clear);
        View rl_account = view.findViewById(R.id.setting_rl_account);
        civ_img = (CustomShapeImageView) view.findViewById(R.id.setting_civ_img);
        tv_phone = (TextView) view.findViewById(R.id.setting_tv_phone);
        tv_username = (TextView) view.findViewById(R.id.setting_tv_username);
        View rl_account_setting = view.findViewById(R.id.setting_rl_account_setting);

        rl_account_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newDailog();
            }
        });

        rl_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAccount();
            }
        });
        rl_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setCurrentUser();
    }

    private void setCurrentUser() {
        User user = Commons.currentUser;
        if (user == null) {
            LogUtils.shownToast(context, "请先登录");
            return;
        }
//        String uri = UrlUtils.getFileUrl(context, user.getIcon());
//        ImageLoader.getInstance().displayImage(uri, civ_img, MyImageLoaderUtils.getUserOption());
        tv_phone.setText(user.getUse_phone());
        tv_username.setText(user.getUse_name());

    }

    private void newDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_account_setting, null);
        Switch sw_remenmber = (Switch) dialogView.findViewById(R.id.accountsetting_sw_remenmber);
        boolean checked = sw_remenmber.isChecked();
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();
        View ok = dialogView.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                showProgressDialog("正在保存。。。");
                new GetDataTask("保存成功").execute();
            }
        });
        View cancel = dialogView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    private void myAccount() {
        MyAccountFragmentDialog dialogFragment = new MyAccountFragmentDialog();
        dialogFragment.setiPicModeSelectListener(new MyAccountFragmentDialog.IPicModeSelectListener() {
            @Override
            public void onPicModeSelected(String mode) {
                if (mode.equals(MyAccountFragmentDialog.changeAccount)) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("logout", true);
                    startActivity(intent);
                    getActivity().finish();
                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle("您确定要退出么？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("logout", true);
                                    startActivity(intent);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                }

                            }).setNegativeButton("取消",

                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    }).create(); // 创建对话框
                    alertDialog.show(); // 显示对话框
                }
            }
        });
        dialogFragment.show(getActivity().getFragmentManager(), "MyAccountFragmentDialog");
    }

    public void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog(String text) {
        progressDialog = new ProgressDialog(context);
        // 实例化
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置进度条风格，风格为圆形，旋转的
        progressDialog.setMessage(text);
        // 设置ProgressDialog 提示信息
        // 设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        progressDialog.setCancelable(false);
        // 让ProgressDialog显示
        progressDialog.show();
    }

    /**
     * 模拟耗时操作
     */
    private class GetDataTask extends AsyncTask<Void, Void, String> {

        private String res = "";

        public GetDataTask(String res) {
            this.res = res;
        }

        @Override
        protected String doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            // Call onRefreshComplete when the list has been refreshed.
            dismissDialog();
            LogUtils.shownToast(context, result);
            super.onPostExecute(result);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, null);
        return view;
    }
}