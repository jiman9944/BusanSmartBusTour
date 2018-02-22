package kr.co.ubitec.smartbusanculturetourguide.Location;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Iterator;

import kr.co.ubitec.smartbusanculturetourguide.MainActivity;

/**
 * Created by Administrator on 2017-09-20.
 */

public class GpsInfo extends Service implements LocationListener {
    private final Context mContext;

    public static final String TAG = "BUSAN_SMART_CITY_LOG";

    // 현재 GPS 사용유무
    boolean isGPSEnabled = false;

    // 네트워크 사용유무
    boolean isNetworkEnabled = false;

    // GPS 상태값
    boolean isGetLocation = false;

    boolean showingMsg = false;

    public boolean isShowingMsg() {
        return showingMsg;
    }

    public void setShowingMsg(boolean showingMsg) {
        this.showingMsg = showingMsg;
    }

    Location location;
    //경도
    double longitude;
    //위도
    double latitude;
    //고도
    double altitude;
    //정확도
    float accuracy;
    /**
     * 위치제공자
     * Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
     * Network 위치제공자에 의한 위치변화
     * Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
     */
    String provider;

    // 최소 GPS 정보 업데이트 거리 10미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 5초
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    public GpsInfo(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            if (isGetLocation()) {
                // 네트워크 정보로 부터 위치값 가져오기
                getLocationProc();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    private void getLocationProc() {
        locationManager = (LocationManager) mContext
                .getSystemService(LOCATION_SERVICE);

        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        if (isNetworkEnabled) {
            if (ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //권한이 없어서 위치 정보를 받아올 수 없음
            } else {
                //일단 막음 위성 확인 해보기 위해..
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        // 위도 경도 저장
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
        }

        //위성 확인 실내에서는 위성이 감지되지 않는다고 함..(테스트 해봐야 함)
        if (isGPSEnabled) {
            if (ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //권한이 없어서 위치 정보를 받아올 수 없음
            } else {
//                if (location == null) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
//                            ((MainActivity)MainActivity.mContext).setText(latitude,longitude);

                        }
                    }
//                }
            }
        }
    }

    /**
     * GPS 종료
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    public int getGpsSatiCnt() {
        int count = -1;
        if (ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            GpsStatus status = locationManager.getGpsStatus(null);
            Iterable sats = status.getSatellites();
            Iterator satI = sats.iterator();

            while (satI.hasNext()) {
                GpsSatellite gpssatellite = (GpsSatellite) satI.next();
                if (gpssatellite.usedInFix()) {
                    count++;
                }
            }
        }
        return count;
    }


    /**
     * 위도값을 가져옵니다.
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    /**
     * 경도값을 가져옵니다.
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }


        return longitude;
    }

    public double getAltitude() {
        if (location != null) {
            altitude = location.getAltitude();
        }
        return altitude;
    }

    public float getAccuracy() {
        if (location != null) {
            accuracy = location.getAccuracy();
        }
        return accuracy;
    }

    public String getProvider() {
        if (location != null) {
            provider = location.getProvider();
        }
        return provider;
    }

    /**
     * GPS 나 wife 정보가 켜져있는지 확인합니다.
     */
    public boolean isGetLocation() {
        // GPS 정보 가져오기
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // 현재 네트워크 상태 값 알아오기
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // GPS 와 네트워크사용이 가능하지 않을때 소스 구현
            this.isGetLocation = false;
        } else {
            this.isGetLocation = true;
        }
        return this.isGetLocation;
    }

    /**
     * GPS 정보를 가져오지 못했을때
     * 설정값으로 갈지 물어보는 alert 창
     */
    public void showSettingsAlert() {
        showingMsg = true;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 셋팅이 되지 않았을수도 있습니다. 설정창으로 가시겠습니까 ? ");

        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });
        // Cancle 하면 종료 합니다.
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                showingMsg = false;
            }
        });

        alertDialog.show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        altitude = location.getAltitude();
        accuracy = location.getAccuracy();
        provider = location.getProvider();
        Log.i(TAG, "위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude
                + "\n고도 : " + altitude + "\n정확도 : "  + accuracy);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }
}
