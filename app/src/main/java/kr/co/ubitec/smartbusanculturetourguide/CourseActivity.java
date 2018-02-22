package kr.co.ubitec.smartbusanculturetourguide;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by Ubitec on 2018-01-25.
 */

public class CourseActivity extends Activity {

    Context mContext;
    public String lang=""; // 초기 언어값
    TextView title;
    LinearLayout courseimg;
    Intent intent;
    File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour_course);

        mContext = getApplicationContext();

        getWindow().clearFlags(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(Integer.MAX_VALUE);

        title = (TextView)findViewById(R.id.course_title) ;
        courseimg = (LinearLayout)findViewById(R.id.courseimg);

        intent = new Intent(this, VideoPlayActivity.class);
        lang = ((MainActivity)MainActivity.mContext).putLang(); //언어정보를 가져온다
        if (lang.equals("ENG")){
            title.setText("Course Introduction (Redline)");
            courseimg.setBackground(getResources().getDrawable(R.drawable.courseinfo_en));
        }
        else if(lang.equals("CHN")){
            title.setText("路线简介 (红线)");
            courseimg.setBackground(getResources().getDrawable(R.drawable.courseinfo_ch));
        }
        else if(lang.equals("JPN")){
            title.setText("コース紹介 (レッドライン)");
            courseimg.setBackground(getResources().getDrawable(R.drawable.courseinfo_jp));
        }
        else{
            title.setText("코스소개 (레드라인)");
            courseimg.setBackground(getResources().getDrawable(R.drawable.courseinfo_ko));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick_button(View v){

        switch(v.getId()){
            case R.id.back_button_course:
                finish();
                break;
            case R.id.home_button_course:
                finish();
                break;
            case R.id.arpina:
                File file1 = new File(file, this.getString(R.string.video_path) + "7"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file1.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "ARPINA"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "ARPINA"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "ARPINA"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "아르피나"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.busanCinema:
                File file2 = new File(file, this.getString(R.string.video_path) + "5"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file2.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "Busan Cinema Center"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "电影的殿堂"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "映画の殿堂"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "영화의 전당"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.busanHarbor:
                File file3 = new File(file, this.getString(R.string.video_path) + "109"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file3.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "Busanghangdaegyo Bridge"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "釜山港大桥"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "釜山港大橋"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "부산항대교"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.busanMuseumOfArt:
                File file5 = new File(file, this.getString(R.string.video_path) + "6"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file5.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "BUSAN Museum of Art"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "釜山市立美术馆"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "釜山市立美術館"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "시립미술관"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.busanHarbor2:
                File file4 = new File(file, this.getString(R.string.video_path) + "109"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file4.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "Busanghangdaegyo Bridge"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "釜山港大桥"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "釜山港大橋"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "부산항대교"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.busanMuseum:
                File file6 = new File(file, this.getString(R.string.video_path) + "51"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file6.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "Busan Museum"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "釜山市立博物馆"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "釜山市立博物館"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "부산박물관"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.busanStation:
                File file7 = new File(file, this.getString(R.string.video_path) + "38"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file7.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "Busan Station"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "釜山火车站"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "釜山駅"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "부산역"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.centum:
                File file8 = new File(file, this.getString(R.string.video_path) + "4"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file8.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "Centum City"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "Centum City"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "センタムシティ"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "센텀시티"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.dongBaek:
                File file9 = new File(file, this.getString(R.string.video_path) + "2"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file9.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "Dongbaek Island"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "冬柏岛"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "冬柏島"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "동백섬"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.gwangalli:
                File file10 = new File(file, this.getString(R.string.video_path) + "62"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file10.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "Gwangalli Beach"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "广安里海水浴场"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "広安里海水浴場"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "광안리해수욕장"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.gwanganBridge:
                File file11 = new File(file, this.getString(R.string.video_path) + "67"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file11.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "Gwangan Bridge(Diamond Bridge)"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "广安大桥"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "広安大橋（ダイヤモンドブリッジ）"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "광안대교"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.gwangBok:
                File file17 = new File(file, this.getString(R.string.video_path) + "210"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file17.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "Gwangbok-ro"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "光复路"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "光復路通り"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "광복로"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.haeundea:
                File file12 = new File(file, this.getString(R.string.video_path) + "3"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file12.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "Haeundae Beach"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "海云台海水浴场"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "海雲台海水浴場"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "해운대해수욕장"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.peacePark:
                File file13 = new File(file, this.getString(R.string.video_path) + "53"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file13.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "Peace Park"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "和平公园"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "平和公園"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "평화공원"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.un:
                File file14 = new File(file, this.getString(R.string.video_path) + "50"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file14.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "UN Memorial Cemetery"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "UN纪念公园"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "UN記念公園"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "UN기념공원"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.marineCity:
                File file15 = new File(file, this.getString(R.string.video_path) + "1"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file15.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "Haeundae Marine City"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "海云台海上城市"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "海雲台マリンシティ"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "마린시티"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
            case R.id.yongHoman:
                File file16 = new File(file, this.getString(R.string.video_path) + "52"); //읽어질 파일
                intent.putExtra("playUri", "file://" + file16.getAbsolutePath()); //파일 정보를 보낸다.
                if (lang.equals("ENG")) {
                    intent.putExtra("playTitle", "Yongho Bay Cruise Ship Terminal"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("CHN")){
                    intent.putExtra("playTitle", "龙湖湾游轮客运站"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else if(lang.equals("JPN")){
                    intent.putExtra("playTitle", "龍湖湾遊覧船ターミナル"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                else{
                    intent.putExtra("playTitle", "용호만유람선터미널"); //명소의 title보내기
                    ((MainActivity)MainActivity.mContext).getTitle("");
                }
                this.startActivity(intent);
                break;
        }
    }
}
