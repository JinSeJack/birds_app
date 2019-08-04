package com.bage.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


/**
 * 添加新闻对话框
 */
public class AddNewsSelectDialogFragment extends DialogFragment {

    public static final String picture = "图片";
    public static final String audio = "音频";
    public static final String puretext = "文本";

    private String[] dataTypes = {picture,audio,puretext};

    private DataTypeSelectListener dataTypeSelectListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(dataTypes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (dataTypeSelectListener != null)
                    dataTypeSelectListener.onDataTypeSelected(dataTypes[which]);
            }
        });
        return builder.create();
    }

    public void setDataTypeSelectListener(DataTypeSelectListener dataTypeSelectListener) {
        this.dataTypeSelectListener = dataTypeSelectListener;
    }

    /**
     *
     */
    public interface DataTypeSelectListener {
        void onDataTypeSelected(String dataType);
    }
}
