package com.bage.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bage.common.Commons;
import com.bage.domain.Event;
import com.bage.mybirds.R;
import com.bage.utils.JsonUtils;
import com.bage.utils.TimeHelper;
import com.bage.utils.UrlUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class TakePhotoActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 2;
    private static String[] PERMISSIONS_STORAGE = {
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION};

    //private OkHttpClient okHttpClient;
    private String pictureUrl = UrlUtils.getControllerUrl(TakePhotoActivity.this, "api/event", "upload/picture");
    //private String pictureUrl = "http://192.168.56.1:8080/MyBirds/api/event/upload/picture";
    //定位相关
    private double longitude;
    private double latitude;

    public final static int  PHOTO_ZOOM = 0;
    public final static int  TAKE_PHOTO = 1;
    public final static int  PHOTO_RESULT = 2;
    public final static String IMAGE_UNSPECIFIED = "image/*";
    public String LOG_TAG = "TakeAndUploadPhoto";
    public final static int MEDIA_TYPE_IMAGE = 3;
    public final static int OPEN_GPS_RETURN = 4;
    private String imageDir;
    private ImageView avatar;
    private ProgressBar pb;
    private LinearLayout ll_container;
    private TextView tv_locatin;
    private TextView tv_time;
    private EditText et_count;
    private Spinner et_countbirds_type;
    private EditText et_remark;
    private EditText et_question;

    private Long currentTime;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    public File myPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_take_photo);

        verifyStoragePermissions(TakePhotoActivity.this);
        verifyLocationPermissions(TakePhotoActivity.this);

        pictureUrl = UrlUtils.getControllerUrl(this, "api/event", "upload/picture");

        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        pb = (ProgressBar)findViewById(R.id.take_photo_progress_bar);
        tv_locatin = (TextView)findViewById(R.id.take_photo_textview_location);
        tv_time = (TextView)findViewById(R.id.take_photo_textview_time);
        et_count = (EditText)findViewById(R.id.take_photo_edittext_countbirds);
        et_countbirds_type = (Spinner)findViewById(R.id.take_photo_edittext_countbirds_type);
        et_question = (EditText)findViewById(R.id.take_photo_edittext_question);
        et_remark = (EditText)findViewById(R.id.take_photo_edittext_remark);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();//初始化百度定位信息
        mLocationClient.start();
        //调用本地图库的选择按钮
        avatar = (ImageView)findViewById(R.id.avatar);
        LinearLayout upload = (LinearLayout)findViewById(R.id.local_select_button);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(IMAGE_UNSPECIFIED);
                Intent wrapperIntent = Intent.createChooser(intent, null);
                startActivityForResult(wrapperIntent, PHOTO_ZOOM);
            }
        });
        //拍照按钮
        LinearLayout takePhoto = (LinearLayout)findViewById(R.id.take_photo_button);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDir = "temp.jpg";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), imageDir)));
                } catch (Exception e) {
                    Toast.makeText(TakePhotoActivity.this, "SD卡初始化失败", Toast.LENGTH_SHORT).show();
                }
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });
        //上传图片
        LinearLayout uploadPhoto = (LinearLayout)findViewById(R.id.upload_photo_button);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TakeAndUploadPhoto", "上传按钮点击");
                if(myPhotoFile==null){
                    Toast.makeText(TakePhotoActivity.this, "先拍照或选择本地文件再上传", Toast.LENGTH_SHORT).show();
                    return ;
                }
                else{
                    uploadPhoto("");
                }


            }
        });
    }
    //初始化定位
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=0;
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
    //图片缩放
//    public void photoZoom(Uri uri){
//        myPhotoFile = new File(uri.getPath());
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
//        //acpectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        //outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 320);
//        intent.putExtra("outputY", 320);
//        intent.putExtra("return-data",true);
//        startActivityForResult(intent, PHOTO_RESULT);
//    }
      //图片剪裁2
    public void photoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        //判断sdk版本，保证代码的高兼容性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String url=getPath(this,uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            myPhotoFile = new File(url);
        }else{
            intent.setDataAndType(uri, "image/*");
            myPhotoFile = new File(uri.getPath());
        }

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESULT);
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type)
    {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = null;
        try
        {
            // This location works best if you want the created images to be
            // shared
            // between applications and persist after your app has been
            // uninstalled.
            mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "MyCameraApp");

            Log.i(LOG_TAG, "Successfully created mediaStorageDir: "
                    + mediaStorageDir);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.i(LOG_TAG, "Error in Creating mediaStorageDir: "
                    + mediaStorageDir);
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                //申请WRITE_EXTERNAL_STORAGE权限
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
//            }
            if (!mediaStorageDir.mkdir())
            {
                // 在SD卡上创建文件夹需要权限：
                // <uses-permission
                // android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
                Log.i(LOG_TAG,
                        "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                //return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }

        else
        {
            return null;
        }


        return mediaFile;
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == PHOTO_ZOOM){
                photoZoom(data.getData());
            }
            if(requestCode == TAKE_PHOTO){
                File picture = new File(Environment.getExternalStorageDirectory()+"/"+imageDir);
                photoZoom(Uri.fromFile(picture));
            }
            if(requestCode == PHOTO_RESULT){
                Bundle extras = data.getExtras();
                if(extras!=null){
                    Bitmap photo = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    saveBitmapFile(photo, myPhotoFile);
                    avatar.setImageBitmap(photo);
                }
            }
            if(requestCode == OPEN_GPS_RETURN){
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void saveBitmapFile(Bitmap bitmap, File file){
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
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

            tv_locatin.setText("   地点:"+location.getLocationDescribe());
            tv_time.setText("   时间:"+location.getTime());


            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
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

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
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
            Log.i("TakeAndUploadPhoto", location.getLocType()+"\n"+sb.toString());
        }


    }

    protected void uploadPhoto(String urlPost) {
        //asyncHttp
        Log.i("TakeAndUploadPhoto", "开始上传");
        AsyncHttpClient client = new AsyncHttpClient();
       // client.setEnableRedirects(false);
        RequestParams params = new RequestParams();
        if (!myPhotoFile.exists() || myPhotoFile.length() <= 0) {
            Log.i("TakeAndUploadPhoto", "文件不存在");
            return;
        }
        try {
            //myPhotoFile.renameTo(new File(myPhotoFile.getParent() +  TimeHelper.getCurrentTime() + "pic.jpg"));
            params.put("pictureFile", myPhotoFile);
            System.out.println("-----------"+myPhotoFile.getPath().toString());
            int amount;
            if(et_count.getText().toString() == null || et_count.getText().toString() == ""){
                amount = 0;
            }else{
                amount = Integer.parseInt(et_count.getText().toString());
            }

            String dataTypeDesc = et_countbirds_type.getSelectedItem().toString();
            Event event = new Event(Commons.currentUser.getUse_id(), amount, 0, et_question.getText().toString(), TimeHelper.getCurrentTime(), (float) longitude, (float) latitude, 1, "", "", "", et_remark.getText().toString(), "暂无备注");
            if(Event.dataType_desc_bird.equals(dataTypeDesc)){ // 鸟
                event.setEve_datatype(Event.dataType_bird_pic);
            } else {
                event.setEve_datatype(Event.dataType_cicada_pic); // 禅
            }

            params.put("event", JsonUtils.BeantoJsonStr(event));
            //params.setHttpEntityIsRepeatable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("TakeAndUploadPhoto", pictureUrl + "==" + params.toString());

        client.post(pictureUrl, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode,Header[] headers,
                                  byte[] responseBody) {
                if (statusCode == 200) {
                    Log.i("TakeAndUploadPhoto", "上传成功");
                    Toast.makeText(TakePhotoActivity.this,"上传成功",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Log.i("TakeAndUploadPhoto", "上传失败1");
                    Toast.makeText(TakePhotoActivity.this,"上传失败",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                Toast.makeText(TakePhotoActivity.this,"上传失败",Toast.LENGTH_LONG);
                Log.i("TakeAndUploadPhoto", statusCode + "===" + error.toString() + "==");
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                // TODO Auto-generated method stub
                super.onProgress(bytesWritten, totalSize);
                int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                // 上传进度显示
                pb.setProgress(count);
                Log.e("上传 Progress>>>>>", bytesWritten + " / " + totalSize);
            }

        });

    }

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

}

