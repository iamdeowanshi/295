package com.abacus.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.abacus.android.base.BaseActivity;
import com.abacus.android.util.MarshmallowPermissions;

public class CameraActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if(!MarshmallowPermissions.checkPermissionForCamera(this)) {
            MarshmallowPermissions.requestPermissionForCamera(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}