package com.bage.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by bage on 2016/6/6.
 */
public class MyAccountFragmentDialog extends DialogFragment {

    public static final String changeAccount = "切换账号";
    public static final String logout = "退出应用";
    private String[] picMode = {changeAccount, logout};

    private IPicModeSelectListener iPicModeSelectListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(picMode, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (iPicModeSelectListener != null)
                    iPicModeSelectListener.onPicModeSelected(picMode[which]);
            }
        });
        return builder.create();
    }

    public void setiPicModeSelectListener(IPicModeSelectListener iPicModeSelectListener) {
        this.iPicModeSelectListener = iPicModeSelectListener;
    }

    public interface IPicModeSelectListener {
        void onPicModeSelected(String mode);
    }
}
