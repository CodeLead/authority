package com.meitu.authority.authoritymanager;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.meitu.authority.R;
import com.meitu.authority.authoritymanager.util.PermissionUtil;

public class PermissionCheckActivity extends AppCompatActivity {

//    public static final int PERMISSIONS_GRANTED = 0;          //点击设置返回码
    public static final int PERMISSIONS_DENIED = -1;            //取消返回码
//    private static final int PERMISSION_REQUEST_CODE = 0x1000;  //请求权限请求码
    private static final String PACKAGE_URL_SCHEME = "package:"; //package scheme
    /**
     * 权限工具
     */
    private PermissionUtil mPermissionUtil;
    /**
     * 是否需要检查权限
     */
    private boolean isRequireCheck;


    public static void startActivityForResult(Activity activity, int requestCode){
        Intent intent = new Intent(activity,PermissionCheckActivity.class);
//        intent.putExtra(EXTRA_PERMISSIONS,permissions);
        ActivityCompat.startActivityForResult(activity,intent,requestCode,null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent() == null) {
            throw  new RuntimeException("AuthorityManagerActivity需要使用静态startActivityForResult打开");
        }
        setContentView(R.layout.authority_manager_activity);
        mPermissionUtil = new PermissionUtil(this);
        isRequireCheck = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isRequireCheck) {
            /**
             * 判断是否有缺少外部存储卡读写权限
             */
            if(mPermissionUtil.lackPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showMissingPermissionDialog();
            }else {
                extenalStoragePermissionGranted();
            }
        }
    }


    /**
     * 存储权限已经授权
     */
    private void extenalStoragePermissionGranted() {
        //TODO
    }



    /**
     * 显示引导对话框
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionCheckActivity.this);
        builder.setTitle(R.string.dialog_help);
        builder.setMessage(R.string.string_help_text);

        // 拒绝, 提示缺少关键应用，退出
        builder.setNegativeButton(R.string.dialog_cancel_btn_text, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                setResult(PERMISSIONS_DENIED);
                finish();
            }
        });

        builder.setPositiveButton(R.string.dialog_positive_btn_text, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
                finish();
            }
        });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 跳转到设置页面，以打开权限
     */
    private void startAppSettings() {
        if(PermissionUtil.isOverMarshmallow()) {
            //6.0以上跳转到应用的设置界面
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
            startActivity(intent);
        }else {
            //6.0之前跳转到设置页面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }
    }


}
