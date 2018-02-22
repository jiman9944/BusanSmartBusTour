package kr.co.ubitec.smartbusanculturetourguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import kr.co.ubitec.smartbusanculturetourguide.Location.GpsInfo;
import kr.co.ubitec.smartbusanculturetourguide.Location.RunnableGetGpsInfo;
import kr.co.ubitec.smartbusanculturetourguide.data.LocalDBHelper;
import kr.co.ubitec.smartbusanculturetourguide.data.RunnableGetTourContentsInfo;
import kr.co.ubitec.smartbusanculturetourguide.permission.PermissionCheckClass;

/**
 * 지우지 마세요
 */
public class FunctionTestActivity extends Activity {
    private Button btnCheckLocation;
    private Button btnGetNearContsStart;
    private Button btnGetNearContsStop;
    private Button btnCallVideoPop;
    private Button btnGetWeatherInfo;
    private TextView txtInfo;
    public static String requestUrl;

    PermissionCheckClass permissionCheckClass = new PermissionCheckClass(FunctionTestActivity.this);

    public static final String TAG = "BUSAN_SMART_CITY_LOG";

    private RunnableGetGpsInfo runnableGetGpsInfo;
    private RunnableGetTourContentsInfo runnableGetTourContentsInfo;



    private GpsInfo gps;

    private VideoView videoView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.
                    activity_function_test);

            //Sqlite 테이블 생성
            LocalDBHelper.init(this);

            //버튼 생성 및 이벤트 등록-------------------------------------------------------------------------start
            btnCheckLocation = (Button) findViewById(R.id.btnCheckLocation);
            btnGetNearContsStart = (Button) findViewById(R.id.btnGetNearContsStart);
            btnGetNearContsStop = (Button) findViewById(R.id.btnGetNearContsStop);
            btnCallVideoPop = (Button) findViewById(R.id.btnCallVideoPop);
            btnGetWeatherInfo = (Button) findViewById(R.id.btnGetWeather);

            txtInfo = (TextView) findViewById(R.id.textView1);

            //권한체크
            permissionCheckClass.checkPermission();


            //관광 콘텐츠 DB 버전 확인 및 생성-----------------------------------------------------------------start
            requestUrl = this.getString(R.string.web_path);
            runnableGetTourContentsInfo = new RunnableGetTourContentsInfo(requestUrl);
            runnableGetTourContentsInfo.start();
            //관광 콘텐츠 DB 버전 확인 및 생성-----------------------------------------------------------------end

            btnCheckLocation.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    gps = new GpsInfo(FunctionTestActivity.this);
                    // GPS 사용유무 가져오기
                    if (gps.isGetLocation()) {
                        double longitude = gps.getLongitude();
                        double latitude = gps.getLatitude();
                        double altitude = gps.getAltitude();
                        float accuracy = gps.getAccuracy();
                        String provider = gps.getProvider();
                        int satiCnt = gps.getGpsSatiCnt();
                        Toast.makeText(getApplicationContext(), "위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude
                                + "\n고도 : " + altitude + "\n위성수 : " + satiCnt + "\n정확도 : " + accuracy, Toast.LENGTH_LONG).show();
                    } else {
                        // GPS 를 사용할수 없으므로
                        gps.showSettingsAlert();
                    }
                }
            });

            //GPS 데이터를 0.5초 간격으로 받고 근방정보를 찾아서 뿌려줌 시작
            btnGetNearContsStart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    if (runnableGetGpsInfo != null && runnableGetGpsInfo.isAlive()) {
                        runnableGetGpsInfo.interrupt();
                    }
                    if (gps == null) {
                        gps = new GpsInfo(FunctionTestActivity.this);
                    }

                    runnableGetGpsInfo = new RunnableGetGpsInfo(FunctionTestActivity.this, gps, txtInfo, videoView);
                    runnableGetGpsInfo.start();

                }
            });
            //GPS 데이터를 0.5초 간격으로 받기 중단
            btnGetNearContsStop.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    if (runnableGetGpsInfo != null) {
                        runnableGetGpsInfo.interrupt();
                    }
                    if (gps == null) {
                        gps = new GpsInfo(FunctionTestActivity.this);
                    }
                    txtInfo.append(", 쓰레드 중단됨");
                }
            });
            btnCallVideoPop.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    try {
                        startActivity(new Intent(FunctionTestActivity.this, VideoPlayActivity.class));
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }
            });


            //버튼 생성 및 이벤트 등록-------------------------------------------------------------------------end

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        try {
            LocalDBHelper.destroy();
            if (runnableGetGpsInfo != null) {
                runnableGetGpsInfo.interrupt();
            }
        } catch (Exception e) {

        }
        super.onDestroy();
    }
}
