package com.rafalzajfert.permissiontools.sample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rafalzajfert.permissiontools.OnPermissionResultTask;
import com.rafalzajfert.permissiontools.Permissions;

import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String PERMISSION_DENIED_MSG = "Denied: %s \nGranted: %s";
    private static final String PERMISSION_GRANTED_MSG = "Take a photo!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Permissions.checkPermission(MainActivity.this, new OnPermissionResultTask() {
                    @Override
                    public void onPermissionGranted(@NonNull String[] permissions) throws SecurityException {
                        takePhoto();
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            }
        });

        findViewById(R.id.button_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainFragmentActivity.class));
            }
        });

        findViewById(R.id.button_support_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainFragmentActivity.class);
                intent.putExtra(MainFragmentActivity.EXTRA_SUPPORT, true);
                startActivity(intent);
            }
        });
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    private void takePhoto() {
        Toast.makeText(this, PERMISSION_GRANTED_MSG, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
