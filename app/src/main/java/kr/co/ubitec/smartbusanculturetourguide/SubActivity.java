package kr.co.ubitec.smartbusanculturetourguide;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.ubitec.smartbusanculturetourguide.Location.GpsInfo;
import kr.co.ubitec.smartbusanculturetourguide.Location.RunnableGetGpsInfo;

import kr.co.ubitec.smartbusanculturetourguide.data.CultureContents;
import kr.co.ubitec.smartbusanculturetourguide.data.LocalDBHelper;
import kr.co.ubitec.smartbusanculturetourguide.data.LocalDBHelper2;
import kr.co.ubitec.smartbusanculturetourguide.data.MovieContents;
import kr.co.ubitec.smartbusanculturetourguide.data.RunnableGetTourContentsInfo;
import kr.co.ubitec.smartbusanculturetourguide.permission.PermissionCheckClass;

public class SubActivity extends Activity {

    PermissionCheckClass permissionCheckClass = new PermissionCheckClass(SubActivity.this);

    Context mContext;

    private RunnableGetGpsInfo runnableGetGpsInfo;
    private RunnableGetTourContentsInfo runnableGetTourContentsInfo;

    public static final String TAG = "BUSAN_SMART_CITY_LOG";

    public String gu = "";
    public String gu2 = "";
    public String gu3 = "";
    public String gu4 = "";
    public String listTitle = "";
    public String URI =""; // 동영상의 경로 값
    public static String requestUrl;
    public String lang=""; // 초기 언어값
    public double lng; //위치값
    public double lat;
    public boolean isPlay=false; //재생이 되고있는지 확인하는 함수
    public String title =""; //재생되는 컨텐츠의 제목을 가지고 있다가 중복재생이 되지않게 한다.


    private GpsInfo gps;
    private TextView txtInfo;
    private VideoView videoView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView imageView;
    private ListView listView;
    ArrayList<HashMap<String,String>> noticeList;

    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_activity);

        mContext = getApplicationContext();
        noticeList = new ArrayList<HashMap<String, String>>();
        recyclerView = (RecyclerView) findViewById(R.id.rv);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);


        getWindow().clearFlags(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(Integer.MAX_VALUE);
        lang = ((MainActivity)MainActivity.mContext).putLang();

        Intent intent = getIntent();// 조회할 구,군을 받아온다
        gu = intent.getStringExtra("gu");
        gu2 = intent.getStringExtra("gu2");
        gu3 = intent.getStringExtra("gu3");
        gu4 = intent.getStringExtra("gu4");
        listTitle = intent.getStringExtra("title"); // 리스트의 제목을 받아온다
        txtInfo = (TextView) findViewById(R.id.list_title);
        txtInfo.setText(listTitle); // 제목text 바꾸기


        for(ActivityManager.RunningTaskInfo taskInfo : tasks){ //1개의 activity 만 실행
            if(taskInfo.baseActivity.getClassName().equals(SubActivity.this)&&taskInfo.numActivities>1){
                finish();
            }
        }


        try {
            //권한체크
            permissionCheckClass.checkPermission();
            selectContents();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    public void onClick_button(View view){
        switch (view.getId()){
            case R.id.back_button://뒤로가기
                finish();
                break;
            case R.id.home_button://홈버튼
                finish();
                break;
//            case R.id.lang_button:
//                DialogSelectOption();
//                break;
        }
    }

    public void selectContents(){//콘텐츠들을 뿌려준다
        try {
            int count=0;
            count = LocalDBHelper2.getTourContents(gu,gu2,gu3,gu4); //local DB 에 있는 정보를 select 해온다. (갯수)
            HashMap<String,String> result = new HashMap<String, String>();

            ArrayList<CultureContents> contents = new ArrayList<>();
            contents = LocalDBHelper2.getContentslist(lang,gu,gu2,gu3,gu4); // 리스트 조회해서 가져오기

            for(int i = 0 ; i <count; i++){//조회한 갯수만큼 뿌려준다.
                int num =  contents.get(i).getNo(); // item 들을 담아서
                String name = contents.get(i).getName();
                String path = contents.get(i).getMain_image();

                result.put("num"+i, String.valueOf(num));
                result.put("name"+i,name); //Map에 put
                result.put("path"+i,path);

                noticeList.add(result); //저장한 map을 담는다.


            }
            NoticeAdapter adapter = new NoticeAdapter(getApplicationContext(),noticeList); // 만들어 놓은 adapter에 저장한 map 을 보낸다.

            recyclerView.setAdapter(adapter); //뷰에 만든 형태로 뿌려준다.
            adapter.notifyDataSetChanged(); //데이터 변경시 바꿔줌


        }
        catch (Exception e){

        }
    }

//    public void DialogSelectOption(){//안드로이드 내에서 언어별 변경할 수 있게 dialog창 생성
//        final String items[] = {"한국어", "English", "Japanese", "Chinese"}; // dialog 창 내에 목록
//        final int[] temp = new int[1];
//        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//        ab.setTitle("Language");
//        ab.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                temp[0] = arg1;
//            }
//        })
//                // Set the action buttons
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // user clicked OK, so save the mSelectedItems results somewhere
//                        // or return them to the component that opened the dialog
//                        switch (temp[0]){
//                            case 0:
//                                dialog.dismiss(); //다이얼로그창 닫기
//                                lang = "KOR"; //언어 설정
//                                ((MainActivity)MainActivity.mContext).setLang(lang);
//                                adapter.notifyDataSetChanged();
//                                break;
//                            case 1:
//                                dialog.dismiss();
//                                lang="ENG";
//                                ((MainActivity)MainActivity.mContext).setLang(lang);
//                                adapter.notifyDataSetChanged();
//                                break;
//                            case 2:
//                                dialog.dismiss();
//                                lang="JPN";
//                                ((MainActivity)MainActivity.mContext).setLang(lang);
//                                adapter.notifyDataSetChanged();
//                                break;
//                            case 3:
//                                dialog.dismiss();
//                                lang="CHN";
//                                ((MainActivity)MainActivity.mContext).setLang(lang);
//                                adapter.notifyDataSetChanged();
//                                break;
//                        }
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // removes the dialog from the screen
//                    }
//                })
//                .show(); // 위에서 생성한 다이얼로그 창을 보여준다.
//    }




    protected void onResume(){ //재시작시점
        super.onResume();

    }

    @Override
    protected void onDestroy() { //종료시점

        super.onDestroy();
    }
}
