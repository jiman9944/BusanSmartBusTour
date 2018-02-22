package kr.co.ubitec.smartbusanculturetourguide;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.ubitec.smartbusanculturetourguide.Location.GpsInfo;
import kr.co.ubitec.smartbusanculturetourguide.Location.RunnableGetGpsInfo;

import kr.co.ubitec.smartbusanculturetourguide.data.ContentsDetail;
import kr.co.ubitec.smartbusanculturetourguide.data.ContentsPicture;
import kr.co.ubitec.smartbusanculturetourguide.data.LocalDBHelper3;
import kr.co.ubitec.smartbusanculturetourguide.data.LocalDBHelper4;
import kr.co.ubitec.smartbusanculturetourguide.data.RunnableGetTourContentsInfo;
import kr.co.ubitec.smartbusanculturetourguide.permission.PermissionCheckClass;

public class DetailActivity extends Activity {

    PermissionCheckClass permissionCheckClass = new PermissionCheckClass(DetailActivity.this);

    Context mContext;

    private RunnableGetGpsInfo runnableGetGpsInfo;
    private RunnableGetTourContentsInfo runnableGetTourContentsInfo;

    public static final String TAG = "BUSAN_SMART_CITY_LOG";

    public String URI =""; // 동영상의 경로 값
    public static String requestUrl;
    public String lang=""; // 초기 언어값
    public double lng; //위치값
    public double lat;
    public boolean isPlay=false; //재생이 되고있는지 확인하는 함수
    public String title =""; //재생되는 컨텐츠의 제목을 가지고 있다가 중복재생이 되지않게 한다.
    public String num;
    public String contentName;


    private GpsInfo gps;
    private TextView textView;
    private VideoView videoView;
    private TextView textTitle;
    private TextView add_text;
    ArrayList<HashMap<String,String>> noticeList;

    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        mContext = getApplicationContext();
        noticeList = new ArrayList<HashMap<String, String>>();

        getWindow().clearFlags(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //화면이 꺼지지 않고 계속 켜져있는 상태로 유지
        ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(Integer.MAX_VALUE);
        lang = ((MainActivity)MainActivity.mContext).putLang(); //언어정보를 가져온다

        Intent intent = getIntent();
        num=intent.getStringExtra("num");
        contentName = intent.getStringExtra("name");

        textTitle = (TextView)findViewById(R.id.detail_title);
        textTitle.setText(contentName);


        for(ActivityManager.RunningTaskInfo taskInfo : tasks){
            if(taskInfo.baseActivity.getClassName().equals(DetailActivity.this)&&taskInfo.numActivities>1){
                finish();
            }
        }

        try {
            //권한체크
            permissionCheckClass.checkPermission();
            detailInfo(); // 상세 정보 가져오기
            detailPicture(); // 상세 사진 가져오기

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    public void detailPicture(){ //상세내용의 이미지 set한다.
        try {
            int count=0;
            count = LocalDBHelper4.getTourContents(num); //갯수가져오기
            HashMap<String,String> result = new HashMap<String, String>();

            ArrayList<ContentsPicture> contents = new ArrayList<>();
            contents = LocalDBHelper4.getContents(num); //local DB 에서 이미지 정보를 가져온다.

            for(int i = 0 ; i <count-1; i++){
                int num =  contents.get(i).getContents_No(); //가져온 정보의 no값
                String url_addr = contents.get(i).getUrl_Addr(); // 가져온 정보의 img url 값

                result.put("num"+i, String.valueOf(num));
                result.put("url_addr"+i,url_addr);

                noticeList.add(result);

            }
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager); //슬라이드를 이용하여 이미지를 볼수있게 viewPager를 사용
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(),noticeList); //만든 viewPager 에 가져온 리스트를 담는다.

            viewPager.setAdapter(viewPagerAdapter); //img 연결
            viewPager.setCurrentItem(viewPager.getCurrentItem());

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onClick_button(View view){
        switch (view.getId()){
            case R.id.back_button_detail:
                finish();
                break;
            case R.id.home_button_detail:
                Intent intentHome = new Intent(this, MainActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentHome);
                finish();
                break;
        }
    }

    public void detailInfo(){
        int count=0;
        int lan = 0;
        if (lang.equals("KOR")){
            lan = 0;
        }
        else if (lang.equals("ENG")){
            lan = 1;
        }
        else if (lang.equals("JPN")){
            lan = 2;
        }
        else if (lang.equals("CHN")){
            lan = 3;
        }
        count = LocalDBHelper3.getTourContents(lan,num);
        HashMap<String,String> result = new HashMap<String, String>();
        ArrayList <ContentsDetail> contentsDetails =  new ArrayList<>();
        contentsDetails = LocalDBHelper3.getContentsDetail(lan,num);
        String contents = null;
        String address_ko = null;
        String address_en = null;
        for(int i = 0 ; i <count; i++){
            int num =  contentsDetails.get(i).getNo();
            contents = contentsDetails.get(i).getContents();
            address_ko = contentsDetails.get(i).getAddress_ko();
            address_en = contentsDetails.get(i).getAddress_en();

            result.put("num"+i, String.valueOf(num));
            result.put("contents"+i,contents);

        }

        textView = (TextView) findViewById(R.id.textView);
        add_text = (TextView) findViewById(R.id.add_text);


        textView.setText(contents); // 상세정보 text set
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setTextSize(22); //text 사이즈 설정

        if(lang.equals("KOR")) {
            add_text.setText("주소\n\n - "+address_ko); //주소 text 출력
            add_text.setTextSize(20); //text 사이즈 설정
        }
        else{
            add_text.setText("Address\n\n - "+address_en); // 외국어 주소 text 출력
            add_text.setTextSize(20);
        }
    }

    protected void onResume(){ //재시작시점
        super.onResume();

    }

    @Override
    protected void onDestroy() { //종료시점

        super.onDestroy();
    }
}
