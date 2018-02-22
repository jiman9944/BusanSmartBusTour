package kr.co.ubitec.smartbusanculturetourguide;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.ubitec.smartbusanculturetourguide.Location.GpsInfo;
import kr.co.ubitec.smartbusanculturetourguide.Location.RunnableGetGpsInfo;
import kr.co.ubitec.smartbusanculturetourguide.data.LocalDBHelper;
import kr.co.ubitec.smartbusanculturetourguide.data.LocalDBHelper2;
import kr.co.ubitec.smartbusanculturetourguide.data.LocalDBHelper3;
import kr.co.ubitec.smartbusanculturetourguide.data.LocalDBHelper4;
import kr.co.ubitec.smartbusanculturetourguide.data.RunnableGetTourContentsInfo;
import kr.co.ubitec.smartbusanculturetourguide.permission.PermissionCheckClass;

public class MainActivity extends Activity {

    PermissionCheckClass permissionCheckClass = new PermissionCheckClass(MainActivity.this);

    private RunnableGetGpsInfo runnableGetGpsInfo;
    private RunnableGetTourContentsInfo runnableGetTourContentsInfo;

    public static final String TAG = "BUSAN_SMART_CITY_LOG";

    public String URI =""; // 동영상의 경로 값
    public static String requestUrl;
    public String lang="ENG"; // 초기 언어값
    public double lng; //위치값
    public double lat;
    public boolean isPlay=false; //재생이 되고있는지 확인하는 함수
    public String title =""; //재생되는 컨텐츠의 제목을 가지고 있다가 중복재생이 되지않게 한다.
    public String listTitle1="";
    public String listTitle2="";
    public String listTitle3="";
    public String listTitle4="";
    public String listTitle5="";

    public static Context mContext;
    private GpsInfo gps;
    private TextView txtInfo;
    private VideoView videoView;

    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private ImageButton app_info;
    private ImageButton course_info;

    private Button button_kor;
    private Button button_eng;
    private Button button_chi;
    private Button button_jpn;

    private WindowManager.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        params = getWindow().getAttributes();

        setContentView(R.layout.activity_main);
        requestUrl = getString(R.string.web_path);

        getWindow().clearFlags(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mContext = this;
        ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(Integer.MAX_VALUE);

        for(ActivityManager.RunningTaskInfo taskInfo : tasks){
            if(taskInfo.baseActivity.getClassName().equals(kr.co.ubitec.smartbusanculturetourguide.MainActivity.this)&&taskInfo.numActivities>1){
                finish();
            }
        }



        try {

            this.registerReceiver(this.receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

            txtInfo = (TextView) findViewById(R.id.textView1);
            videoView = (VideoView) findViewById(R.id.VideoViewTourCont);

            LocalDBHelper.init(this);// 받아올 json 파일의 db테이블을 만들어 놓는다
            LocalDBHelper2.init(this);
            LocalDBHelper3.init(this);
            LocalDBHelper4.init(this);

            //권한체크
            permissionCheckClass.checkPermission();

            //관광 콘텐츠 DB 버전 확인 및 생성-----------------------------------------------------------------start
            runnableGetTourContentsInfo = new RunnableGetTourContentsInfo(requestUrl);
            runnableGetTourContentsInfo.start();
            //관광 콘텐츠 DB 버전 확인 및 생성-----------------------------------------------------------------end

            if (runnableGetGpsInfo != null && runnableGetGpsInfo.isAlive()) {
                runnableGetGpsInfo.interrupt();
            }
            if (gps == null) {
                gps = new GpsInfo(MainActivity.this);
            }
            runnableGetGpsInfo = new RunnableGetGpsInfo(MainActivity.this, gps, txtInfo, videoView);
            runnableGetGpsInfo.start(); //gps 탐색 시작

            lng=gps.getLongitude();//좌표를 받아온다.
            lat=gps.getLatitude();

            imageView = (ImageView) findViewById(R.id.imageButton);
            imageView2 = (ImageView) findViewById(R.id.imageButton2);
            imageView3 = (ImageView) findViewById(R.id.imageButton3);
            imageView4 = (ImageView) findViewById(R.id.imageButton4);
            imageView5 = (ImageView) findViewById(R.id.imageButton5);
            app_info = (ImageButton) findViewById(R.id.button_app);
            course_info = (ImageButton) findViewById(R.id.course_info);

            button_kor = (Button) findViewById(R.id.buttonKor);
            button_eng = (Button) findViewById(R.id.buttonEng);
            button_chi = (Button) findViewById(R.id.buttonChi);
            button_jpn = (Button) findViewById(R.id.buttonJpn);

            if(lang.equals("KOR")){//한국어
                imageView.setImageResource(R.drawable.main1_kor);
                imageView2.setImageResource(R.drawable.main2_kor);
                imageView3.setImageResource(R.drawable.main3_kor);
                imageView4.setImageResource(R.drawable.main4_kor);
                imageView5.setImageResource(R.drawable.main5_kor);
                button_kor.setTextColor(Color.RED);
                app_info.setImageResource(R.drawable.button_app_ko);
                course_info.setImageResource(R.drawable.course_ko);
                listTitle1="해운대/기장";
                listTitle2="광안리/수영";
                listTitle3="동래/금정/서면";
                listTitle4="영도/남포";
                listTitle5="서부산";
                lang="KOR";
            }else if(lang.equals("ENG")){//영어
                imageView.setImageResource(R.drawable.main1_eng);
                imageView2.setImageResource(R.drawable.main2_eng);
                imageView3.setImageResource(R.drawable.main3_eng);
                imageView4.setImageResource(R.drawable.main4_eng);
                imageView5.setImageResource(R.drawable.main5_eng);
                button_eng.setTextColor(Color.RED);
                app_info.setImageResource(R.drawable.button_app_en);
                course_info.setImageResource(R.drawable.course_en);
                listTitle1="Haeundae/Gijang";
                listTitle2="Gwangalli/Suyeong";
                listTitle3="Dongnae/GeumJeong/Seomyeon";
                listTitle4="Yeongdo/Nampo";
                listTitle5="West Busan";
                lang="ENG";
            }else if(lang.equals("CHN")){//중국어
                imageView.setImageResource(R.drawable.main1_cha);
                imageView2.setImageResource(R.drawable.main2_cha);
                imageView3.setImageResource(R.drawable.main3_cha);
                imageView4.setImageResource(R.drawable.main4_cha);
                imageView5.setImageResource(R.drawable.main5_cha);
                button_chi.setTextColor(Color.RED);
                app_info.setImageResource(R.drawable.button_app_ch);
                course_info.setImageResource(R.drawable.course_ch);
                listTitle1="海云台/机张";
                listTitle2="广安里/水营";
                listTitle3="东莱/金井/西面";
                listTitle4="影岛/南浦";
                listTitle5="西部釜山";
                lang="CHN";
            }else if(lang.equals("JPN")){//일본어
                imageView.setImageResource(R.drawable.main1_jpn);
                imageView2.setImageResource(R.drawable.main2_jpn);
                imageView3.setImageResource(R.drawable.main3jpn);
                imageView4.setImageResource(R.drawable.main4_jpn);
                imageView5.setImageResource(R.drawable.main5_jpn);
                button_jpn.setTextColor(Color.RED);
                app_info.setImageResource(R.drawable.button_app_jp);
                course_info.setImageResource(R.drawable.course_jp);
                listTitle1="海雲台/機張";
                listTitle2="広安里/水営";
                listTitle3="東莱/金井/西面";
                listTitle4="影島/ナンポ";
                listTitle5="西釜山";
                lang="JPN";
            }


        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PermissionCheckClass.MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            boolean locationPermissionGranted = true;
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = false;
                }
                if (permissions[i] == Manifest.permission.ACCESS_COARSE_LOCATION && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = false;
                }
            }

            if (!locationPermissionGranted) {
                Log.e(TAG, "위치 확인 권한이 없으면 앱의 일부 기능이 동작하지 않을 수 있습니다.");
            }
        }
    }

    public boolean setisPlay(){//재생중인지 확인
        return isPlay;
    } // 재생중이거나 아닐때 true , false 값을 가지고 있게 하는 메서드
    public void getisPlay(boolean temp){
        isPlay = temp;//현재 비디오 상태를 저장
    }
    public String setTitle(){ //재생 컨텐츠를 확인
        return title;
    } //이전에 재생했던 title을 가지고 있다가 return 해준다.
    public void getTitle(String temp){//현재 재생한 컨텐츠를 저장 (반복재생 방지)
        title = temp;
    } //재생하는 title을 가져와서 저장해둔다.
    public String putLang(){
        return lang;
    } // 언어정보를 보내준다.
    public void setLang(String language){
        lang = language;
    }
    public void getUri(String Uri){
        URI = Uri;
    }
    public void reStartVideo(){
        if(!title.equals("")) {
            String tmp = title;
            title = "";
            Intent intent = new Intent(this, VideoPlayActivity.class);
            intent.putExtra("playUri", URI);
            intent.putExtra("playTitle", tmp);
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick_button(View v){

        switch(v.getId()){
//            case R.id.video_test:
//                Intent test = new Intent(this,VideoPlayActivity.class);
//                startActivity(test);
//                break;
            case R.id.course_info://코스소개
                Intent courseinfo = new Intent(this,CourseActivity.class);
                startActivity(courseinfo);
                break;
            case R.id.imageButton: //명소 리스트 버튼
                Intent intent = new Intent(this, SubActivity.class);
                intent.putExtra("language",lang);
                intent.putExtra("gu","해운대구");
                intent.putExtra("gu2","기장군");
                intent.putExtra("title",listTitle1);
                startActivity(intent);
                break;
            case R.id.imageButton2: //명소 리스트 버튼
                Intent intent2 = new Intent(this, SubActivity.class);
                intent2.putExtra("language",lang);
                intent2.putExtra("gu","남구");
                intent2.putExtra("gu2","수영구");
                intent2.putExtra("title",listTitle2);
                startActivity(intent2);
                break;
            case R.id.imageButton3: //명소 리스트 버튼
                Intent intent3 = new Intent(this, SubActivity.class);
                intent3.putExtra("language",lang);
                intent3.putExtra("gu","동래구");
                intent3.putExtra("gu2","금정구");
                intent3.putExtra("gu3","부산진구");
                intent3.putExtra("title",listTitle3);
                startActivity(intent3);
                break;
            case R.id.imageButton4: //명소 리스트 버튼
                Intent intent4 = new Intent(this, SubActivity.class);
                intent4.putExtra("language",lang);
                intent4.putExtra("gu","영도구");
                intent4.putExtra("gu2","동구");
                intent4.putExtra("gu3","서구");
                intent4.putExtra("gu4","중구");
                intent4.putExtra("title",listTitle4);
                startActivity(intent4);
                break;
            case R.id.imageButton5: //명소 리스트 버튼
                Intent intent5 = new Intent(this, SubActivity.class);
                intent5.putExtra("language",lang);
                intent5.putExtra("gu","사상구");
                intent5.putExtra("gu2","강서구");
                intent5.putExtra("gu3","사하구");
                intent5.putExtra("gu4","북구");
                intent5.putExtra("title",listTitle5);
                startActivity(intent5);
                break;
            case R.id.button_app://앱소개
                Intent intent6 = new Intent(this,AppinfoActivity.class);
                startActivity(intent6);
                break;
            case R.id.buttonKor://한국어
                imageView.setImageResource(R.drawable.main1_kor);
                imageView2.setImageResource(R.drawable.main2_kor);
                imageView3.setImageResource(R.drawable.main3_kor);
                imageView4.setImageResource(R.drawable.main4_kor);
                imageView5.setImageResource(R.drawable.main5_kor);
                app_info.setImageResource(R.drawable.button_app_ko);
                course_info.setImageResource(R.drawable.course_ko);
                listTitle1="해운대/기장";
                listTitle2="광안리/수영";
                listTitle3="동래/금정/서면";
                listTitle4="영도/남포";
                listTitle5="서부산";
                button_kor.setTextColor(Color.RED);
                button_jpn.setTextColor(Color.WHITE);
                button_chi.setTextColor(Color.WHITE);
                button_eng.setTextColor(Color.WHITE);

                lang="KOR";
                break;
            case R.id.buttonEng://영어
                imageView.setImageResource(R.drawable.main1_eng);
                imageView2.setImageResource(R.drawable.main2_eng);
                imageView3.setImageResource(R.drawable.main3_eng);
                imageView4.setImageResource(R.drawable.main4_eng);
                imageView5.setImageResource(R.drawable.main5_eng);
                app_info.setImageResource(R.drawable.button_app_en);
                course_info.setImageResource(R.drawable.course_en);
                listTitle1="Haeundae/Gijang";
                listTitle2="Gwangalli/Suyeong";
                listTitle3="Dongnae/GeumJeong/Seomyeon";
                listTitle4="Yeongdo/Nampo";
                listTitle5="West Busan";
                button_kor.setTextColor(Color.WHITE);
                button_jpn.setTextColor(Color.WHITE);
                button_chi.setTextColor(Color.WHITE);
                button_eng.setTextColor(Color.RED);

                lang="ENG";
                break;
            case R.id.buttonChi://중국어
                imageView.setImageResource(R.drawable.main1_cha);
                imageView2.setImageResource(R.drawable.main2_cha);
                imageView3.setImageResource(R.drawable.main3_cha);
                imageView4.setImageResource(R.drawable.main4_cha);
                imageView5.setImageResource(R.drawable.main5_cha);
                app_info.setImageResource(R.drawable.button_app_ch);
                course_info.setImageResource(R.drawable.course_ch);
                listTitle1="海云台/机张";
                listTitle2="广安里/水营";
                listTitle3="东莱/金井/西面";
                listTitle4="影岛/南浦";
                listTitle5="西部釜山";
                button_kor.setTextColor(Color.WHITE);
                button_jpn.setTextColor(Color.WHITE);
                button_chi.setTextColor(Color.RED);
                button_eng.setTextColor(Color.WHITE);

                lang="CHN";
                break;
            case R.id.buttonJpn://일본어
                imageView.setImageResource(R.drawable.main1_jpn);
                imageView2.setImageResource(R.drawable.main2_jpn);
                imageView3.setImageResource(R.drawable.main3jpn);
                imageView4.setImageResource(R.drawable.main4_jpn);
                imageView5.setImageResource(R.drawable.main5_jpn);
                app_info.setImageResource(R.drawable.button_app_jp);
                course_info.setImageResource(R.drawable.course_jp);
                listTitle1="海雲台/機張";
                listTitle2="広安里/水営";
                listTitle3="東莱/金井/西面";
                listTitle4="影島/ナンポ";
                listTitle5="西釜山";
                button_kor.setTextColor(Color.WHITE);
                button_jpn.setTextColor(Color.RED);
                button_chi.setTextColor(Color.WHITE);
                button_eng.setTextColor(Color.WHITE);

                lang="JPN";
                break;
        }
    }



    @Override
    protected void onResume(){ //재시작시점
        super.onResume();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }

    public boolean checkGPSStatus() {//gps 상태 체크
        try {
            LocationManager locationManager;
            String context = Context.LOCATION_SERVICE;

            locationManager = (LocationManager) getSystemService(context);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
//                tvGpsStatus.setText("정상");
//                tvGpsStatus.setTextColor(Color.WHITE);
                return true;
            }
            else
            {
//                tvGpsStatus.setText("비정상");
//                tvGpsStatus.setTextColor(Color.RED);
//                alertCheckGPS();
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    protected void onDestroy() { //종료시점
        super.onDestroy();
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(Intent.ACTION_BATTERY_CHANGED.equals(action)){
                int health = intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);
                int level = intent.getIntExtra("level", 0);
                int plug = intent.getIntExtra("plugged", 0);
                int scale = intent.getIntExtra("scale", 100);
                int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
                String technology = intent.getStringExtra("technology");
                int temperature = intent.getIntExtra("temperature", 0);
                int voltage = intent.getIntExtra("voltage", 0);

                // health
                if(health == BatteryManager.BATTERY_HEALTH_COLD){
                    Log.i(TAG, "health cold");
                } else if(health == BatteryManager.BATTERY_HEALTH_DEAD){
                    Log.i(TAG, "health dead");
                } else if(health == BatteryManager.BATTERY_HEALTH_GOOD){
                    Log.i(TAG, "health good");
                } else if(health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){
                    Log.i(TAG, "health over voltage");
                } else if(health == BatteryManager.BATTERY_HEALTH_OVERHEAT){
                    Log.i(TAG, "health overheat");
                } else if(health == BatteryManager.BATTERY_HEALTH_UNKNOWN){
                    Log.i(TAG, "health unknown");
                } else if(health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){
                    Log.i(TAG, "health unspecified failure");
                }

                // 배터리 잔량 확인
                Log.i(TAG, "Charge : " + (level * 100 / scale));

                // 충전 방식
                if(plug == 0){
                    Log.i(TAG, "PlugType : unplugged");
                } else {
                    if((plug & BatteryManager.BATTERY_PLUGGED_AC) != 0){
                        Log.i(TAG, "PlugType : AC");
                    }

                    if((plug & BatteryManager.BATTERY_PLUGGED_USB) != 0){
                        Log.i(TAG, "PlugType : USB");
                    }

                    if((plug & BatteryManager.BATTERY_PLUGGED_WIRELESS) != 0){
                        Log.i(TAG, "PlugType : WIRELESS");
                    }
                }

                // 배터리 상태
                if(status == BatteryManager.BATTERY_STATUS_CHARGING){
                    Log.i(TAG, "Status : Charging");
                } else if(status == BatteryManager.BATTERY_STATUS_DISCHARGING){
                    Log.i(TAG, "Status : Discharging");
                } else if(status == BatteryManager.BATTERY_STATUS_FULL){
                    Log.i(TAG, "Status : Full");

                } else if(status == BatteryManager.BATTERY_STATUS_NOT_CHARGING){
                    Log.i(TAG, "Status : Not charging");
                } else if(status == BatteryManager.BATTERY_STATUS_UNKNOWN){
                    Log.i(TAG, "Status : Unknown");
                }
                // 배터리 기술에 대해 설명
                Log.i(TAG, "Technology : " + technology);

                // 배터리 온도
                Log.i(TAG, "Temperature : " + temperature);

                // 배터리 전압
                Log.i(TAG, "Voltage : " + voltage);
            }
        }
    };
    protected void onPause(){
        super.onPause();
        // receiver 해제
        unregisterReceiver(receiver);
    }


}
