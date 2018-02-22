package kr.co.ubitec.smartbusanculturetourguide.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-09-20.
 */

public class PermissionCheckClass {
    public final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    Activity activity;

    public PermissionCheckClass(Activity activity) {
        this.activity = activity;
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> arrLst = new ArrayList<String>();

            //------------위치권한 요청---------------------- 여기에 권한만 추가하면 됨---------------start
            addReqPermissionToArrayLst(arrLst, Manifest.permission.INTERNET);
            addReqPermissionToArrayLst(arrLst, Manifest.permission.ACCESS_FINE_LOCATION);
            addReqPermissionToArrayLst(arrLst, Manifest.permission.ACCESS_COARSE_LOCATION);
            addReqPermissionToArrayLst(arrLst, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            addReqPermissionToArrayLst(arrLst, Manifest.permission.WRITE_SETTINGS);
            addReqPermissionToArrayLst(arrLst, Manifest.permission.WAKE_LOCK);
            //------------위치권한 요청---------------------- 여기에 권한만 추가하면 됨---------------end

            if (arrLst.size() > 0) {
                String[] arrStrReqPermissions = new String[arrLst.size()];
                for (int i = 0; i < arrLst.size(); i++) {
                    arrStrReqPermissions[i] = arrLst.get(i);
                }
                ActivityCompat.requestPermissions(activity, arrStrReqPermissions, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
    }

    /**
     * 권한을 확인해서 없으면 ArrayList에 추가해서 반환함
     * @param arrLst
     * @param permission
     * @return
     */
    private ArrayList<String> addReqPermissionToArrayLst(ArrayList<String> arrLst, String permission) {
        if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            arrLst.add(permission);
        }
        return arrLst;
    }
}
