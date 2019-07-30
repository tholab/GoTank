package com.example.black.go_tankuser.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.black.go_tankuser.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionHelper {
    private Activity mActivity;
    private final int REQUEST_PERMISSION = 99;
    private String TAG = "PermissionHelper";
    private PermissionListener listener;

    public PermissionHelper(Activity activity){
        mActivity = activity;
    }

    public void permissionListener (PermissionListener permissionListener){
        listener = permissionListener;
    }

    public boolean checkAndRequestPermission(){
        if (Build.VERSION.SDK_INT >= 24){
            int permissionCamera = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
            int permissionReadStorage = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionWriteStorage = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionCamera != PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
            if (permissionReadStorage != PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!listPermissionsNeeded.isEmpty()){
                ActivityCompat.requestPermissions(mActivity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_PERMISSION);
                return false;
            }

        }
        if (Build.VERSION.SDK_INT > 7){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        listener.onPermissionCheckDone();

        return true;
    }

    public void onRequestCallBack(int RequestCode, String[] permissions, int[] grantResults){
     switch (RequestCode){
         case REQUEST_PERMISSION: {
             Map<String,Integer> perms = new HashMap<>();
             //initialize the map with both permissions
             perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
             perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
             perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
             //fill with actual result from user
             if (grantResults.length > 0){
                 for (int i = 0; i < permissions.length; i++)
                     perms.put(permissions[i], grantResults[i]);
                 //check for both permissions
                 if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                         && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                         && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                     Log.e(TAG, "permission granted");

                     checkAndRequestPermission();
                 } else {
                     Log.e(TAG, "Some permisson are not granted ask again");
                     if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA) ||
                     ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                             ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                         showDialogOK(mActivity.getString(R.string.permission_dialog),
                                 new DialogInterface.OnClickListener(){
                                    @Override
                                     public void onClick(DialogInterface dialog, int which){
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                checkAndRequestPermission();
                                                break;
                                        }
                                    }
                                 });
                     }

                     else {
                         Toast.makeText(mActivity, R.string.go_to_permissions_settings, Toast.LENGTH_LONG).show();
                         Intent intent = new Intent();
                         intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                         Uri uri = Uri.fromParts("package", mActivity.getPackageName(),null);
                         intent.setData(uri);
                         mActivity.startActivity(intent);
                     }


                 }
             }
         }
     }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener){
        new AlertDialog.Builder(mActivity)
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .create()
                .show();
    }
    
    public interface PermissionListener{
        void onPermissionCheckDone();
    }
}



