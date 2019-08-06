package com.bage.view;

/**
 * Created by wangj on 2019/8/6.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bage.mybirds.R;
import com.bage.service.PhotoService;

/**
 * @author D10NG
 * @date on 2019-05-15 09:08
 */
public class PhotoView implements View.OnClickListener {
//    private Button btnTakePhoto;
//    private Button btnChoosePhoto;
    private ImageView imgPhoto;

    private Context mContext;
    private Handler mHandler;
    private View mView = null;
//    private ImageView avatar;

    public PhotoView(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mView = layoutInflater.inflate(R.layout.activity_take_photo, null);

        //调用本地图库的选择按钮
        imgPhoto = (ImageView)mView.findViewById(R.id.avatar);
        LinearLayout upload = (LinearLayout)mView.findViewById(R.id.local_select_button);
        upload.setOnClickListener(this);
//        btnTakePhoto = mView.findViewById(R.id.btn_take_photo);
//        btnChoosePhoto = mView.findViewById(R.id.btn_choose_photo);
//        imgPhoto = mView.findViewById(R.id.img_photo);
        //  拍照按钮
        LinearLayout takePhoto = (LinearLayout)mView.findViewById(R.id.take_photo_button);

        takePhoto.setOnClickListener(this);
    }

    public View getView() {
        return mView;
    }

    @Override
    public void onClick(View v) {
        Message message = new Message();
        message.what = PhotoService.CLICK_VIEW;
        message.arg1 = v.getId();
        mHandler.sendMessage(message);
    }

    /**
     * 显示图片
     * @param bitmap
     */
    public void setImgPhoto(Bitmap bitmap) {
        imgPhoto.setImageBitmap(bitmap);
    }
}