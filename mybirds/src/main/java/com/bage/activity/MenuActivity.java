package com.bage.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bage.anim.CircularProgressDrawable;
import com.bage.anim.CustomAnimation;
import com.bage.common.Commons;
import com.bage.domain.Event;
import com.bage.fragment.AddNewsSelectDialogFragment;
import com.bage.fragment.MenuFragment;
import com.bage.fragment.NewsFragment;
import com.bage.mybirds.R;
import com.bage.utils.CacheUtils;
import com.bage.utils.FileProviderUtil;
import com.bage.utils.FileUtils;
import com.bage.utils.JsonUtils;
import com.bage.utils.LogUtils;
import com.bage.utils.TimeHelper;
import com.bage.utils.UrlUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import at.markushi.ui.ActionView;
import at.markushi.ui.action.BackAction;
import at.markushi.ui.action.DrawerAction;

public class MenuActivity extends CustomAnimation {

    public static String currentSpecies = "鸟";

    private String puretextUrl;
    private String audioUrl;

    private static final int FILE_SELECT_CODE = 2;
    private Fragment mContent;
    private TextView tv_title;
    private ActionView action;
    private AsyncHttpClient client;

    private String[] eve;
    private ProgressDialog progressDialog;

    //定位相关
    //定位相关
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private double longitude;
    private double latitude;
    private Long currentTime;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private long mExitTime = 0;

    //定位相关
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    public static void verifyLocationPermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    //实现BDLocationListener接口
    //BDLocationListener接口有1个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());

            //更新位置信息
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("TakeAndUploadPhoto", location.getLocType() + "\n" + sb.toString());
        }


    }


    //初始化定位
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
//定位相关


    public MenuActivity() {
        // see the class CustomAnimation for how to attach
        // the CanvasTransformer to the SlidingMenu
        super(R.string.anim_zoom, new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.25 + 0.75);
                canvas.scale(scale, scale, canvas.getWidth() / 2, canvas.getHeight() / 2);
            }

        });
    }
    private View iv_add;

    public void setAddVisable(int visibiale) {
        if(iv_add != null){
            iv_add.setVisibility(visibiale);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(Commons.currentUser == null){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
            return ;
        };
        //定位相关
        verifyStoragePermissions(this);
        verifyLocationPermissions(this);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();//初始化百度定位信息
        mLocationClient.start();
        //定位相关


        puretextUrl = UrlUtils.getControllerUrl(MenuActivity.this, "api/event", "upload/puretext");
        audioUrl = UrlUtils.getControllerUrl(MenuActivity.this, "api/event", "upload/audio");
        //audioUrl = "http://www.aunnyair.top:8080/MyBirds/api/event/upload/audio";
        /// 设置滑动菜单
        intSlidingMenu(savedInstanceState);
        // 将滑动菜单和菜单图片绑定
        getSlidingMenu().setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                action.setAction(new DrawerAction());
            }
        });
        getSlidingMenu().setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                action.setAction(new BackAction());
            }
        });
        // findViews
        tv_title = (TextView) findViewById(R.id.acmenu_tv_title);
        action = (ActionView) findViewById(R.id.action);
        iv_add = (View) findViewById(R.id.acmenu_iv_add);

        // 硬編碼，簡單處理4.4與5.0之間的差異
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        View bar_helper = (View) findViewById(R.id.bar_helper);
        if (currentapiVersion <= 19) {
            bar_helper.setVisibility(View.GONE);
        }
        // 添加相应事件
        action.setAction(new DrawerAction());
        // new 一个AsyncHttpClient
        client = new AsyncHttpClient();
    }

    private void intSlidingMenu(Bundle savedInstanceState) {

        //  Constant.displayHeight = displayMetrics.heightPixels;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_offset);
        getSlidingMenu().setBehindWidth((int) (displayMetrics.widthPixels * 0.8));


        // set the Above View
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        // 默认放置新闻页面
        if (mContent == null) {
            mContent = new NewsFragment();
        }
        // set the Above View
        setContentView(R.layout.activity_menu);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.acmenu_fl_content, mContent)
                .commit();

        // set the Behind View
        setBehindContentView(R.layout.activity_basemenu);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, new MenuFragment())
                .commit();

        // customize the SlidingMenu
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);


    }

    /**
     * 切换菜单
     *
     * @param fragment
     * @param currentTitle
     */
    public void switchContent(Fragment fragment, String currentTitle) {
        mContent = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.acmenu_fl_content, fragment)
                .commit();
        getSlidingMenu().showContent();
        tv_title.setText(currentTitle);
    }

    public void showMyMenu(View view) {
        SlidingMenu menu = getSlidingMenu();
        menu.toggle();
    }

    //从这里开始是上传图片
    public void addNews(View v) {
        AddNewsSelectDialogFragment dialogFragment = new AddNewsSelectDialogFragment();
        dialogFragment.setDataTypeSelectListener(new AddNewsSelectDialogFragment.DataTypeSelectListener() {
            @Override
            public void onDataTypeSelected(String dataType) {
                if (dataType.equals(AddNewsSelectDialogFragment.picture)) {
                    addPictureNews();
                }
                if (dataType.equals(AddNewsSelectDialogFragment.audio)) {
                    addAudioNews();
                }
                if (dataType.equals(AddNewsSelectDialogFragment.puretext)) {
                    addPuretextNews();
                }
            }
        });
        dialogFragment.show(MenuActivity.this.getFragmentManager(), "addNewsSelectDialogFragment");
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                LogUtils.shownToast(this, "再按一次退出程序");
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //从这里开始是上传图片
    public void addPictureNews() {
        Intent photoIntent = new Intent(this, TakePhotoActivity.class);
        startActivity(photoIntent);
    }

    public void addPuretextNews() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
       // builder.setIcon(R.drawable.logo);
        // 填充view
        View viewDialog = getLayoutInflater().inflate(R.layout.activity_meun_dialog_input_textnews, null);
        final EditText fet_question = (EditText) viewDialog.findViewById(R.id.acmenu_fet_question);
        final EditText fet_description = (EditText) viewDialog.findViewById(R.id.acmenu_fet_description);
        final Spinner spi_type = (Spinner) viewDialog.findViewById(R.id.acmenu_fet_content_type);
        final EditText fet_content = (EditText) viewDialog.findViewById(R.id.acmenu_fet_content);
        final EditText et_remark = (EditText) viewDialog.findViewById(R.id.acmenu_et_remark);
        final Button btn_cancel = (Button) viewDialog.findViewById(R.id.acmenu_btn_cancel);
        final Button btn_send = (Button) viewDialog.findViewById(R.id.acmenu_btn_send);
        // 设置对话框
        builder.setView(viewDialog);
       // builder.setTitle("我要提问");
        final AlertDialog dialog = builder.show();
        // 增加点击事件
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取参数
                String question = fet_question.getText().toString();
                String description = fet_description.getText().toString();
                String content = fet_content.getText().toString();
                String remark = et_remark.getText().toString();
                String dataTypeDesc = spi_type.getSelectedItem().toString();

                Event event = new Event(0, Commons.currentUser.getUse_id(), question, TimeHelper.getCurrentTime(), (float) longitude, (float) latitude, 0, content, "", "", description, remark);

                if(Event.dataType_desc_bird.equals(dataTypeDesc)){ // 鸟
                    event.setEve_datatype(Event.dataType_bird_text);
                } else {
                    event.setEve_datatype(Event.dataType_cicada_text); // 禅
                }
                // 上传
                uploadPureTextNews(event);
                dialog.dismiss();
            }

        });
        // 点击取消直接关闭即可
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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

    private void uploadPureTextNews(Event event) {
        event.setUse_id(Commons.currentUser.getUse_id());
        showProgressDialog("正在操作。。。");
        RequestParams params = new RequestParams();
        System.out.println("event.toString():" + JsonUtils.BeantoJsonStr(event));
        params.put("event", JsonUtils.BeantoJsonStr(event));
        params.put("puretextUrl",puretextUrl);
        client.post(puretextUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                if (statusCode == 200) {
                    if ("true".equals(new String(bytes))) {
                        LogUtils.shownToast(MenuActivity.this, "操作成功");
                    } else {
                        LogUtils.shownToast(MenuActivity.this, "操作失败");
                    }
                } else {
                    LogUtils.shownToast(MenuActivity.this, "操作失败");
                }
                dismissDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                dismissDialog();
                LogUtils.shownToast(MenuActivity.this, "操作失败;错误码："+statusCode);
            }
        });
    }


    //这里是录音专区变量，请勿随意使用
    private ImageView mIv_AddAudio;
    private MediaRecorder mMediaRecorder01;
    private boolean sdCardExit;
    private File myRecAudioFile;
    private String myRecAudioDir = "sdcard";// 得到Sd卡path
    private boolean isStopRecord;
    private EditText et_dec;
    private EditText et_count;
    private EditText et_que;
    private Spinner spi_audioBird_type;

    public void addAudioNews() {

              /* 判断SD Card是否插入 */
        sdCardExit = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view1 = inflater.inflate(R.layout.recording, null);
        final ImageView ivDrawable = (ImageView) view1.findViewById(R.id.iv_drawable);
        final ImageView ivStartRecord = (ImageView) view1.findViewById(R.id.iv_startRecord);

        final CircularProgressDrawable drawable;
        drawable = new CircularProgressDrawable.Builder()
                .setRingWidth(getResources().getDimensionPixelSize(R.dimen.drawable_ring_size))
                .setOutlineColor(getResources().getColor(android.R.color.darker_gray))
                .setRingColor(getResources().getColor(android.R.color.holo_green_light))
                .setCenterColor(getResources().getColor(android.R.color.white))
                .create();
        ivDrawable.setImageDrawable(drawable);

        ivStartRecord.setOnClickListener(new View.OnClickListener() {
            boolean isRecording = false;

            @Override
            public void onClick(View v) {
                if (isRecording) {
                    if (myRecAudioFile != null) {
                                 /* 停止录音 */
//                        mMediaRecorder01.stop();
//                        mMediaRecorder01.release();
//
//                        mMediaRecorder01 = null;
                        if (mMediaRecorder01 != null) {
                            try {
                                mMediaRecorder01.stop();
                            } catch (IllegalStateException e) {
                                // TODO 如果当前java状态和jni里面的状态不一致，
                                //e.printStackTrace();
                                mMediaRecorder01 = null;
                                mMediaRecorder01 = new MediaRecorder();
                            }
                            mMediaRecorder01.release();
                            mMediaRecorder01 = null;
                        }


                        ivStartRecord.setImageResource(R.drawable.play);
                        isStopRecord = true;
                    }
                    isRecording = false;
                } else {
                    //animation.start();
                    isRecording = true;
                    ivStartRecord.setImageResource(R.drawable.pause);
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            record();
                        }
                    });
                    thread.start();
                }
            }

            public void record() {
                try {
                    if (!sdCardExit) {
                        Toast.makeText(MenuActivity.this, "请插入SD Card",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
                    Date date = new Date();
                    String audioName = format.format(date);

                    String path = CacheUtils.getAndioCachePath(MenuActivity.this, audioName + ".amr");
                    myRecAudioFile = new File(path);

                    mMediaRecorder01 = new MediaRecorder();

                    PackageManager pm = getPackageManager();
                    boolean permission_caremera = (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission("android.permission.RECORD_AUDIO", "com.bage.mybirds"));

                    if (!(permission_caremera)) {
                        LogUtils.shownToast(MenuActivity.this, "获得权限后，重试");
                        ActivityCompat.requestPermissions(MenuActivity.this, new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO,
                        }, 0x01);

                        return ;
                    }
                    /* 设置录音来源为麦克风 */
                    mMediaRecorder01
                            .setAudioSource(MediaRecorder.AudioSource.MIC);

                    mMediaRecorder01
                            .setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);

                    mMediaRecorder01
                            .setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    //文件保存位置
                    mMediaRecorder01.setOutputFile(myRecAudioFile
                            .getAbsolutePath());

                    mMediaRecorder01.prepare();
                    mMediaRecorder01.start();


                    isStopRecord = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        et_dec = (EditText) view1.findViewById(R.id.et_audioDescription);
        et_count = (EditText) view1.findViewById(R.id.et_audioBirdAmount);
        et_que = (EditText) view1.findViewById(R.id.et_audioQuestion);
        spi_audioBird_type = (Spinner) view1.findViewById(R.id.et_audioBird_type);
        new AlertDialog.Builder(this).setView(view1)
                .setPositiveButton("上传", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inform = "";
                        if (myRecAudioFile == null){
                            inform = "请点击图标录音或者选择一个音频文件";
                        }
                        eve = new String[]{et_count.getText().toString(), et_dec.getText().toString(), et_que.getText().toString()};

                        if(!isStopRecord){
                            if (mMediaRecorder01 != null) {
                                try {
                                    mMediaRecorder01.stop();
                                } catch (IllegalStateException e) {
                                    // TODO 如果当前java状态和jni里面的状态不一致，
                                    //e.printStackTrace();
                                    mMediaRecorder01 = null;
                                    mMediaRecorder01 = new MediaRecorder();
                                }
                                mMediaRecorder01.release();
                                mMediaRecorder01 = null;
                            }

                            ivStartRecord.setImageResource(R.drawable.play);
                            isStopRecord = true;
                        }
                        if( myRecAudioFile.exists()) {
                            uploadAudio("", eve);
                        } else {
                            Toast.makeText(getApplicationContext(), inform, Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("本地上传", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eve = new String[]{et_count.getText().toString(), et_dec.getText().toString(), et_que.getText().toString()};
                        showFileChooser();
                        //获取结果
                    }
                }).show();

    }

    private void  checkPermission()
    {
        // 检查权限是否获取（android6.0及以上系统可能默认关闭权限，且没提示）
        PackageManager pm = getPackageManager();
        boolean permission_readStorage = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.READ_EXTERNAL_STORAGE", "com.bage.Activity"));
        boolean permission_writeStorage = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "com.bage.Activity"));
        boolean permission_caremera = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.RECORD_AUDIO", "com.bage.Activity"));

        if (!(permission_readStorage && permission_writeStorage && permission_caremera)) {
            ActivityCompat.requestPermissions(MenuActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
            }, 0x01);
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "选择一个音频文件"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = FileProviderUtil.getFilePathByUri(this, uri);
//                    String path = FileUtils.getPath(this, uri); //The old api does not work now.
//                    System.out.println(uri.toString()+"aaaa+++++++++++++"+path);
                    myRecAudioFile = new File(path);
                    uploadAudio("", eve);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void uploadAudio(String urlPost, String[] eve) {

        showProgressDialog("正在上传。。。");

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("audioFile", myRecAudioFile);
            System.out.println("-------------"+myRecAudioFile.getPath().toString());
            //eve中含有三个元素 eve[0] 数量 eve[1] 是描述 eve[2] 是问题

            /*int use_id, int eve_count, int eve_positivecount, String eve_question, String eve_time,
            float eve_longitude, float eve_latitude, int eve_datatype, String eve_puretext, String eve_audiourl,
			String eve_pictureurl, String eve_description, String eve_remark*/

            if(eve[0] == "" || eve[0] == null){
                eve[0] = "0";
            }
            String dataTypeDesc = spi_audioBird_type.getSelectedItem().toString();
            //System.out.println(eve[0] + " --- " + eve[1] + " --- " +eve[2] + " --- " );
            Event event = new Event(Commons.currentUser.getUse_id(), Integer.parseInt(eve[0]), 0, eve[2], TimeHelper.getCurrentTime(),
                    (float) longitude, (float) latitude, 2, "", myRecAudioDir, "", "", "备注");

            if(Event.dataType_desc_bird.equals(dataTypeDesc)){ // 鸟
                event.setEve_datatype(Event.dataType_bird_voice);
            } else {
                event.setEve_datatype(Event.dataType_cicada_voice); // 禅
            }

            String eventString = JsonUtils.BeantoJsonStr(event);
            params.put("event", eventString);

        } catch (Exception e) {
            e.printStackTrace();
        }

        client.post(audioUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                if (statusCode == 200) {
                    if ("true".equals(new String(responseBody))) {
                        LogUtils.shownToast(MenuActivity.this, "操作成功");
                    } else {
                        LogUtils.shownToast(MenuActivity.this, "操作失败");
                    }
                } else {
                    LogUtils.shownToast(MenuActivity.this, "操作失败");
                }
                dismissDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                dismissDialog();
                LogUtils.shownToast(MenuActivity.this, "操作失败");
            }
        });
    }
}