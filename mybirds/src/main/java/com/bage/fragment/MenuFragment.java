package com.bage.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bage.activity.ImageCropActivity;
import com.bage.activity.MenuActivity;
import com.bage.common.Commons;
import com.bage.domain.Menu;
import com.bage.mybirds.R;
import com.bage.utils.FileUtils;
import com.bage.utils.LogUtils;
import com.meg7.widget.CustomShapeImageView;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment implements AdapterView.OnItemClickListener, PicModeSelectDialogFragment.IPicModeSelectListener {

    protected View mCurrentView;
    protected ListView lv_menus;
    protected CustomShapeImageView csiv_mypic;
    private String currentTitle;
    private List<Menu> menus;
    private List<Fragment> contents = new ArrayList<>();
    private static final int REQUEST_CODE = 6384; // onActivityResult request
    public static final int REQUEST_CODE_UPDATE_PIC = 0x1;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(R.layout.fragment_menu, null);
        TextView tv_username = (TextView) mCurrentView.findViewById(R.id.leftmenu_tv_username);
        tv_username.setText(Commons.currentUser.getUse_name());
        return mCurrentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv_menus = (ListView) mCurrentView.findViewById(R.id.fr_lv_menus);
        csiv_mypic = (CustomShapeImageView) mCurrentView.findViewById(R.id.leftmenu_csiv_mypic);

        contents.add(new NewsFragment());
        contents.add(new WhatIDoFragment());
        contents.add(new AboutMeFragment());
        contents.add(new SettingFragment());
        initMenus();

        MyAdapter myAdapter = new MyAdapter();
        lv_menus.setAdapter(myAdapter);
        lv_menus.setOnItemClickListener(this);
        csiv_mypic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAddProfilePicDialog();
                return false;
            }
        });
        currentTitle = "";
    }

    private void initMenus() {
        menus = new ArrayList<Menu>();
        Menu menu1 = new Menu();
        menu1.imgId = R.drawable.news;
        menu1.content = "新鲜事";
        Menu menu2 = new Menu();
        menu2.imgId = R.drawable.defaultmypic;
        menu2.content = "个人资料";
        Menu menu3 = new Menu();
        menu3.imgId = R.drawable.setting;
        menu3.content = "我的设置";
        Menu menu4 = new Menu();
        menu4.imgId = R.drawable.fabu;
        menu4.content = "退出";
        menus.add(menu1);
        menus.add(menu2);
        menus.add(menu3);
        menus.add(menu4);
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return menus.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.frmenu_lv_item, null);

            ImageView iv_menuimage = (ImageView) view.findViewById(R.id.frlvit_iv_menuimage);
            TextView tv_menucontent = (TextView) view.findViewById(R.id.frlvit_tv_menucontent);
            iv_menuimage.setImageResource(menus.get(position).imgId);
            tv_menucontent.setText(menus.get(position).content);
            return view;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Fragment newContent = null;
        //newContent = contents.get(position);
        MenuActivity menua = (MenuActivity) getActivity();
        menua.setAddVisable(View.GONE);
        switch (position) {
            case 0:
                menua.setAddVisable(View.VISIBLE);
                newContent = new NewsFragment();
                currentTitle = "新鲜事";
                break;

            case 1:
                currentTitle = "个人资料";
                newContent = new AboutMeFragment();
                break;
            case 2:
                currentTitle = "我的设置";
                newContent = new SettingFragment();
                break;
            case 3:
                // currentTitle = "我的动态";
                // newContent = new WhatIDoFragment();
                myExit();
                break;
        }
        if (newContent != null)
            switchFragment(newContent);
    }

    private void myExit() {

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

    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof MenuActivity) {
            MenuActivity fca = (MenuActivity) getActivity();
            fca.switchContent(fragment, currentTitle);
        }

    }

    private void showAddProfilePicDialog() {
        PicModeSelectDialogFragment dialogFragment = new PicModeSelectDialogFragment();
        dialogFragment.setiPicModeSelectListener(this);
        dialogFragment.show(getActivity().getFragmentManager(), "picModeSelector");
    }

    @Override
    public void onPicModeSelected(String mode) {
        String action = mode.equalsIgnoreCase(Constants.PicModes.CAMERA) ? Constants.IntentExtras.ACTION_CAMERA : Constants.IntentExtras.ACTION_GALLERY;
        actionProfilePic(action);
    }


    private void actionProfilePic(String action) {
        Intent intent = new Intent(getActivity(), ImageCropActivity.class);
        intent.putExtra("ACTION", action);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_PIC);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.sysoln("IMAGE_PATH：" + data.getStringExtra(Constants.IntentExtras.IMAGE_PATH));
        // If the file selection was successful
        if (resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                try {
                    LogUtils.shownToast(getActivity(), "进来了哦");
                    // Get the file path from the URI
                    final String path = data.getStringExtra(Constants.IntentExtras.IMAGE_PATH);
                    showInImageView(path);
                } catch (Exception e) {
                    LogUtils.shownToast(getActivity(), "File select error");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showInImageView(String path) {
        csiv_mypic.setImageBitmap(FileUtils.GetBitmapByFileName(path));
    }
}
