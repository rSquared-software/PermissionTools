package com.rafalzajfert.permissiontools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 *
 *
 * @author Rafal Zajfert
 */
public class Permissions {

    private static int sMinRequestCode = 1;
    private static int sMaxRequestCode = 10000;

    private static SparseArray<PermissionRequest> sPermissionRequests = new SparseArray<>();
    private static final Object LOCK = new Object();

    /**
     * Determine whether you have been granted a particular {@code permissions}.
     * If all permissions are granted then {@link OnPermissionResultTask#onPermissionGranted(String[]) task.onPermissionGranted()} will be invoked,
     * otherwise asks user to grant this permissions and invoke {@link OnPermissionResultTask#onPermissionGranted(String[]) task.onPermissionGranted()} or {@link OnPermissionResultTask#onPermissionDenied(String[], String[]) task.onPermissionDenied()} depends on user response.
     *
     * @param task Task to execute after permission checks
     * @param permissions The requested permissions. Must be non-null and not empty.
     *
     * @see Activity#checkSelfPermission(String)
     * @see Activity#requestPermissions(String[], int)
     */
    public static void checkPermission(Activity activity, OnPermissionResultTask task, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> missingPermissions = getMissingPermissions(activity, permissions);
            if (!missingPermissions.isEmpty()) {
                requestPermissions(activity, new PermissionRequest(generateRequestCode(), task, permissions), missingPermissions);
                return;
            }
        }

        try {
            task.onPermissionGranted(permissions);
        } catch (SecurityException e) {
            //ignored, should never happen
        }
    }

    /**
     * Determine whether you have been granted a particular {@code permissions}.
     * If all permissions are granted then {@link OnPermissionResultTask#onPermissionGranted(String[]) task.onPermissionGranted()} will be invoked,
     * otherwise asks user to grant this permissions and invoke {@link OnPermissionResultTask#onPermissionGranted(String[]) task.onPermissionGranted()} or {@link OnPermissionResultTask#onPermissionDenied(String[], String[]) task.onPermissionDenied()} depends on user response.
     *
     * @param task Task to execute after permission checks
     * @param permissions The requested permissions. Must be non-null and not empty.
     *
     * @see Activity#checkSelfPermission(String)
     * @see Activity#requestPermissions(String[], int)
     */
    public static void checkPermission(android.app.Fragment fragment, OnPermissionResultTask task, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> missingPermissions = getMissingPermissions(fragment.getContext(), permissions);
            if (!missingPermissions.isEmpty()) {
                requestPermissions(fragment, new PermissionRequest(generateRequestCode(), task, permissions), missingPermissions);
                return;
            }
        }

        try {
            task.onPermissionGranted(permissions);
        } catch (SecurityException e) {
            //ignored, should never happen
        }
    }

    /**
     * Determine whether you have been granted a particular {@code permissions}.
     * If all permissions are granted then {@link OnPermissionResultTask#onPermissionGranted(String[]) task.onPermissionGranted()} will be invoked,
     * otherwise asks user to grant this permissions and invoke {@link OnPermissionResultTask#onPermissionGranted(String[]) task.onPermissionGranted()} or {@link OnPermissionResultTask#onPermissionDenied(String[], String[]) task.onPermissionDenied()} depends on user response.
     *
     * @param task Task to execute after permission checks
     * @param permissions The requested permissions. Must be non-null and not empty.
     *
     * @see Activity#checkSelfPermission(String)
     * @see Activity#requestPermissions(String[], int)
     */
    public static void checkPermission(android.support.v4.app.Fragment fragment, OnPermissionResultTask task, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> missingPermissions = getMissingPermissions(fragment.getContext(), permissions);
            if (!missingPermissions.isEmpty()) {
                requestPermissions(fragment, new PermissionRequest(generateRequestCode(), task, permissions), missingPermissions);
                return;
            }
        }

        try {
            task.onPermissionGranted(permissions);
        } catch (SecurityException e) {
            //ignored, should never happen
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void requestPermissions(Activity activity, PermissionRequest request, List<String> missingPermissions) {
        sPermissionRequests.put(request.mRequestCode, request);
        activity.requestPermissions(missingPermissions.toArray(new String[0]), request.mRequestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void requestPermissions(android.app.Fragment fragment, PermissionRequest request, List<String> missingPermissions) {
        sPermissionRequests.put(request.mRequestCode, request);
        fragment.requestPermissions(missingPermissions.toArray(new String[0]), request.mRequestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void requestPermissions(android.support.v4.app.Fragment fragment, PermissionRequest request, List<String> missingPermissions) {
        sPermissionRequests.put(request.mRequestCode, request);
        fragment.requestPermissions(missingPermissions.toArray(new String[0]), request.mRequestCode);
    }

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static List<String> getMissingPermissions(Context context, String[] permissions) {
        List<String> missingPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        return missingPermissions;
    }

    /**
     * Set minimal and maximal value of generated request code for permission requests
     * @param minRequestCode minimal value
     * @param maxRequestCode maximal value
     */
    public static void setRequestCodeRange(int minRequestCode, int maxRequestCode) {
        if (maxRequestCode < minRequestCode) {
            throw new IllegalArgumentException("maxRequestCode < minRequestCode");
        }
        if (maxRequestCode - minRequestCode < 10) {
            throw new IllegalArgumentException("Too small range (must be >= 10)");
        }
        sMinRequestCode = minRequestCode;
        sMaxRequestCode = maxRequestCode;
    }

    private static int generateRequestCode() {
        Random random = new Random();
        int code;
        do {
            code = random.nextInt(sMaxRequestCode - sMinRequestCode + 1) + sMinRequestCode;
        } while (sPermissionRequests.indexOfKey(code) >= 0);
        return code;
    }

    /**
     * Callback for the result from requesting permissions. This should be method is invoked in every {@code onRequestPermissionsResult(int, String[], int[])} method.
     *
     * @param requestCode The request code passed in requestPermissions(String[], int).
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     * @see android.app.Fragment#onRequestPermissionsResult(int, String[], int[])
     * @see android.support.v4.app.Fragment#onRequestPermissionsResult(int, String[], int[])
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionRequest request;
        synchronized (LOCK) {
            request = sPermissionRequests.get(requestCode);
            sPermissionRequests.delete(requestCode);
        }
        if (request != null) {
            List<String> missingPermissions = new ArrayList<>();
            List<String> grantedPermissions = new ArrayList<>(Arrays.asList(request.mPermissions));
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int result = grantResults[i];
                if (result != PackageManager.PERMISSION_GRANTED){
                    missingPermissions.add(permission);
                    grantedPermissions.remove(permission);
                }

            }
            if (missingPermissions.isEmpty()){
                try {
                    request.mTask.onPermissionGranted(grantedPermissions.toArray(new String[0]));
                } catch (SecurityException e) {
                    //ignored, should never happen
                }
            }else {
                request.mTask.onPermissionDenied(grantedPermissions.toArray(new String[0]), missingPermissions.toArray(new String[0]));
            }

        }
    }

    private static class PermissionRequest {
        int mRequestCode;
        OnPermissionResultTask mTask;
        String[] mPermissions;

        PermissionRequest(int requestCode, OnPermissionResultTask task, String[] permissions) {
            mRequestCode = requestCode;
            mTask = task;
            mPermissions = permissions;
        }
    }

}
