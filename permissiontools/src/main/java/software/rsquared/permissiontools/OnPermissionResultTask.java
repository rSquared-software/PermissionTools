package software.rsquared.permissiontools;

import android.support.annotation.NonNull;

/**
 * @author Rafal Zajfert
 */
public abstract class OnPermissionResultTask {

    /**
     * Called when all required permissions was granted and task can be invoked
     * @param permissions Array of all asked permissions
     * @throws SecurityException exception to disable permission check in this method. Should never be thrown.
     */
    public abstract void onPermissionGranted(@NonNull String[] permissions) throws SecurityException;

    /**
     * Called when user denied at least one of requested permission
     * @param grantedPermissions Array of all granted permissions
     * @param deniedPermissions Array of all denied permissions
     */
    public void onPermissionDenied(@NonNull String[] grantedPermissions, @NonNull String[] deniedPermissions) {
    }
}
