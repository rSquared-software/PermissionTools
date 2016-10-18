package com.rafalzajfert.permissiontools.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rafalzajfert.permissiontools.OnPermissionResultTask;
import com.rafalzajfert.permissiontools.Permissions;

import java.util.Arrays;


public class MainSupportFragment extends Fragment {
    private static final String PERMISSION_DENIED_MSG = "Denied: %s \nGranted: %s";
    private static final String PERMISSION_GRANTED_MSG = "Take a photo!";

    public static Fragment newInstance() {
        return new MainSupportFragment();
    }

    public MainSupportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions.checkPermission(MainSupportFragment.this, new OnPermissionResultTask() {
                    @Override
                    public void onPermissionGranted(@NonNull String[] permissions) throws SecurityException {
                        takePhoto();
                    }

                    @Override
                    public void onPermissionDenied(@NonNull String[] grantedPermissions, @NonNull String[] deniedPermissions) {
                        Toast.makeText(getActivity(), String.format(PERMISSION_DENIED_MSG, Arrays.toString(deniedPermissions), Arrays.toString(grantedPermissions)) , Toast.LENGTH_LONG).show();
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    private void takePhoto() {
        Toast.makeText(getActivity(), PERMISSION_GRANTED_MSG, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
