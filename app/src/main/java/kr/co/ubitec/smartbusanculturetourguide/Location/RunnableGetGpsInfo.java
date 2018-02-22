package kr.co.ubitec.smartbusanculturetourguide.Location;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.co.ubitec.smartbusanculturetourguide.FunctionTestActivity;
import kr.co.ubitec.smartbusanculturetourguide.MainActivity;
import kr.co.ubitec.smartbusanculturetourguide.R;
import kr.co.ubitec.smartbusanculturetourguide.VideoPlayActivity;
import kr.co.ubitec.smartbusanculturetourguide.data.LocalDBHelper;
import kr.co.ubitec.smartbusanculturetourguide.data.MovieContents;

/**
 * Created by Administrator on 2017-09-20.
 */

public class RunnableGetGpsInfo extends Thread {
    Activity activity;
    GpsInfo gpsInfo;
    TextView txtInfo;
    VideoView videoView;
    int nowPlayMovieId = 0;
    Intent intent;

    public static final String TAG = "BUSAN_SMART_CITY_LOG";

    public RunnableGetGpsInfo(Activity activity, GpsInfo gpsInfo, TextView txtInfo, VideoView videoView) {
        this.activity = activity;
        this.gpsInfo = gpsInfo;
        this.txtInfo = txtInfo;
        this.videoView = videoView;
    }




    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(500);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (gpsInfo.isGetLocation()) {
                                gpsInfo.getLocation();
                                Log.i(TAG, "위성 갯수 : " + gpsInfo.getGpsSatiCnt());
                                double latitude = gpsInfo.getLatitude();
                                double longitude = gpsInfo.getLongitude();
                                double altitude = gpsInfo.getAltitude();
//                                ((MainActivity)MainActivity.mContext).setText(latitude,longitude);
                                float accuracy = gpsInfo.getAccuracy();
                                String provider = gpsInfo.getProvider();
                                int satiCnt = gpsInfo.getGpsSatiCnt();
                                String lang = ((MainActivity)MainActivity.mContext).putLang(); //현재 설정된 언어정보 받아오기
                                if(lang.equals("ko")){ //언어별로 title값을 다르게 받아오기 위함
                                    lang = "KOR";
                                }
                                else if(lang.equals("jp")){
                                    lang = "JPN";
                                }
                                else if(lang.equals("en")){
                                    lang = "ENG";
                                }
                                else if(lang.equals("ch")){
                                    lang = "CHN";
                                }

                                boolean check = ((MainActivity)MainActivity.mContext).checkGPSStatus();
                                if (check=true) { //위치값을 받았을때만

                                    //근처의 관광지를 로컬 DB로 받아옴
                                    ArrayList<MovieContents> arrLst = LocalDBHelper.getNearContents(latitude, longitude, lang);

                                    if (arrLst.size() > 0) {
                                        StringBuilder sb = new StringBuilder();
                                        for (MovieContents movieContent : arrLst) {
                                            sb.append("\n이름 : " + movieContent.getName() + "\n번호 : " + movieContent.getConNo() + "\n거리 : " + movieContent.getDistance() + ",");
                                            //Log.i(TAG, movieContent.toString());
                                        }
                                        if (txtInfo != null) {
                                            txtInfo.setText(sb.toString());

                                            txtInfo.append("\n\n위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude
                                                    + "\n고도 : " + altitude + "\n위성수 : " + satiCnt + "\n정확도 : " + accuracy); // 정확도란 받아온 gps값의 오차값을 말함
                                        }//위성수가 실내일때는 잡히지 않지만 외부로 나가면 안정적으로 잡힘

                                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                            //현재 재생되는 동영상이랑 같은지 확인 (같지 않을때만 재생)
                                            if (nowPlayMovieId != arrLst.get(0).getConNo()) { //(가장 가까운곳의 동영상이 재생됨)
                                                nowPlayMovieId = arrLst.get(0).getConNo();
                                                //파일 접근 권한이 없으면 동영상 플레이어를 띄우지 않음..

                                                //외부 파일 읽어서 동영상 재생하는 방법----------------------------------------------------------start
                                                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                                                File file2 = new File(file, activity.getString(R.string.video_path) + nowPlayMovieId); //읽어질 파일
                                                //
                                                if (intent == null) {
                                                    intent = new Intent(activity, VideoPlayActivity.class);
                                                }

                                                activity.stopService(intent);
                                                intent.putExtra("playUri", "file://" + file2.getAbsolutePath()); //파일 정보를 보낸다.
                                                intent.putExtra("playTitle", arrLst.get(0).getName()); //명소의 title보내기
                                                activity.startActivity(intent);
                                                //videoView.setVideoURI(Uri.parse("file://" + file2.getAbsolutePath()));
                                                //videoView.requestFocus();
                                                //videoView.start();
                                                //외부 파일 읽어서 동영상 재생하는 방법----------------------------------------------------------end
                                            }
                                        } else {
                                            Log.e(TAG, "파일 접근 권한이 없어 해설사 동영상을 플레이 할 수 없습니다.");
                                        }
                                    } else {
                                        Log.e(TAG, "주변에 등록된 관광명소가 없습니다.");
                                    }
                                }
                            } else {
                                if (!gpsInfo.isShowingMsg()) {
                                    gpsInfo.showSettingsAlert();
                                }
                            }
                        } catch (Exception ex) {
                            Log.e(TAG, ex.getMessage());
                            throw ex;
                        }
                    }
                });
            }
        } catch (InterruptedException e) {
            //interrupt() 메소드는 현재 수행하고 있는 명령을 바로 중지 시킨다. 만약 interrupt() 메소드를 호출하는 시점에 Object클래스의 wait(), wait(long), wait(long, int) 메소드나 Thread클래스의  join(), join(long), join(long, int), sleep(long), sleep(long, int) 메소드가 호출된 경우에는 InterruptedException을 발생 시킨다.
            e.printStackTrace();
        }
    }
}
