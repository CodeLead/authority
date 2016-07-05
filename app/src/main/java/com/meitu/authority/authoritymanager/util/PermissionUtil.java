package com.meitu.authority.authoritymanager.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限管理工具类
 * Created by meitu on 2016/7/1.
 */
public class PermissionUtil {
    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 构造函数
     * @param context
     */
    public PermissionUtil(Context context) {
        mContext = context;
    }

    /**
     * 判断系统版本是否为6.0及以上
     * @return
     */
    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 6.0及以上判断权限集合
     * @return
     */
    public boolean lackPermissions() {
        String [] permissions = getPermissionsAll();
        if (permissions != null) {
               for(String permission : permissions) {
                   if(lackPermission(permission)) {
                       return true;
                   }
               }
        }
        return false;
    }

    /**
     * 检查所有权限
     * @param permission
     * @return
     */
    private boolean lackPermission(String permission) {
        if(isOverMarshmallow()) {
            //6.0以上采用ContextCompat进行权限检查
            return ContextCompat.checkSelfPermission(mContext, permission)
                    != PackageManager.PERMISSION_GRANTED;
        }else {
            //6.0以下使用PermissiontChecker进行权限检查
            return PermissionChecker.checkSelfPermission(mContext, permission)
                    != PermissionChecker.PERMISSION_GRANTED;
        }
    }

    /**
     * 是否全部打开权限,在应用申请权限返回后用来判断其有没有打开全部应用
     * @param grantResults
     * @return
     */
    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取应用声明的全部权限
     * @return
     */
    public  String[] getPermissionsAll() {
        PackageManager pm = mContext.getPackageManager();
        String [] hashPermissions = null;
        try {
            PackageInfo pack = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_PERMISSIONS);
            hashPermissions = pack.requestedPermissions;
            for (String p : hashPermissions) {
                Log.d("permission == " , p);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return hashPermissions;
    }

    /**
     * 判断是否有存储权限
     * 如果能创建文件说明有权限，如果发生异常说明没有权限
     * @return
     */
    public boolean lackExtenalStoragePermission() {
        String testFileName = Environment.getExternalStorageDirectory() + "/meipai_test.txt" ;
        File testFile = new File(testFileName);
        if(!testFile.exists()) {
            try {
                testFile.createNewFile();
            } catch (IOException e) {
//                e.printStackTrace();
                return true;
            }
        }
        if(testFile.exists()) {
            testFile.delete();
        }
        return false;
    }

}
