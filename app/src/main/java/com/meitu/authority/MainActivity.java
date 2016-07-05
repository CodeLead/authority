package com.meitu.authority;

import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.meitu.authority.authoritymanager.PermissionCheckActivity;
import com.meitu.authority.authoritymanager.util.PermissionUtil;


public class MainActivity extends AppCompatActivity {
 private static final int PERMISSION_REQUEST_CODE = 1024;


    private PermissionUtil mPermissionUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
       mPermissionUtil = new PermissionUtil(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
//        if (mPermissionUtil.lackPermissions()) {
//            startPermissionsActivity();
//        }
        //缺少存储卡写入权限，进入设置页面
        if(mPermissionUtil.lackExtenalStoragePermission()) {
            startPermissionsActivity();
        }
    }

    /**
     * 打开权限检查的Activity
     */
    private void startPermissionsActivity() {
        PermissionCheckActivity.startActivityForResult(this, PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PERMISSION_REQUEST_CODE && resultCode == PermissionCheckActivity.PERMISSIONS_DENIED) {
            //用户拒绝了权限
            Toast.makeText(getApplicationContext(),getString(R.string.thost_lack_permission_msg),Toast.LENGTH_LONG).show();
            finish();
        }
    }


}
