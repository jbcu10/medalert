package jbcu10.dev.medalert.config;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by jb on 20/08/2017.
 */

public class Permission {
    private static final int PERMISSION_RQ = 84;

    public void checkPermission(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
           this.requestSendSmsPermission(activity);
        }

    }

    public void requestSendSmsPermission(Activity activity){
        ActivityCompat.requestPermissions(
                activity, new String[] {Manifest.permission.SEND_SMS}, PERMISSION_RQ);
    }

}
