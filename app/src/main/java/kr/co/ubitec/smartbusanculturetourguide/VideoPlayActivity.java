package kr.co.ubitec.smartbusanculturetourguide;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.VideoView;

import java.util.List;

public class VideoPlayActivity extends Activity {

    public static final String TAG = "BUSAN_SMART_CITY_LOG";

    private VideoView videoView;
    private Button button;
    private SeekBar seekBar;
    private String lang=""; //현재 설정된 언어 저장
    private String alreadyPlay=""; //방금 재생한 컨텐츠 저장
    private String playTitle="";//가져온 컨텐츠 이름 저장
    private boolean isPlay; //재생상태 저장


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_video_play);
            Log.i("TAG",lang);


            ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(Integer.MAX_VALUE);
            for(ActivityManager.RunningTaskInfo taskInfo : tasks){
                if(taskInfo.baseActivity.getClassName().equals(kr.co.ubitec.smartbusanculturetourguide.VideoPlayActivity.this)&&taskInfo.numActivities>1){
                    finish();
                }
            }

            videoView = (VideoView) findViewById(R.id.VideoViewTourCont);
            button = (Button) findViewById(R.id.button2) ;
            seekBar = (SeekBar) findViewById(R.id.seekBar);


            button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)MainActivity.mContext).getisPlay(false);
                    videoView.stopPlayback();
                    VideoPlayActivity.this.finish();
                }
            });

            Intent myIntent = getIntent();
            //외부 파일 읽어서 동영상 재생하는 방법----------------------------------------------------------start
            //File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            //File file2 = new File(file, getString(R.string.video_path) + "71" + ".mp4");
            //videoView.setVideoURI(Uri.parse("file://" + file2.getAbsolutePath()));
            lang = ((MainActivity)MainActivity.mContext).putLang();
            if(lang.equals("KOR")){
                lang="ko";
            }
            else if(lang.equals("ENG")){
                lang="en";
            }
            else if(lang.equals("JPN")){
                lang="jp";
            }
            else if(lang.equals("CHN")){
                lang="ch";
            }

            playTitle = myIntent.getStringExtra("playTitle");//조회된 근처 관광지 이름 가져오기


            isPlay = ((MainActivity)MainActivity.mContext).setisPlay();
            if(isPlay==false&&!playTitle.equals("")) {
                alreadyPlay = ((MainActivity)MainActivity.mContext).setTitle();//이전에 재생한 컨텐츠 이름 가져오기


                if (!alreadyPlay.equals(playTitle)&&!playTitle.equals("")) {//방금 가져온 관광지와 이전에 재생한 컨텐츠가 중복되는지 확인
                    ((MainActivity)MainActivity.mContext).getisPlay(true); //재생상태를 true로 변경

                    //-------------------음량 설정 및 video 설정 시작----------------

                    final AudioManager audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);//기기 내의 음량을 제어하기위함
                    int nMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//최대음량을 가져온다
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,AudioManager.FLAG_PLAY_SOUND);
                    int nCurrentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//현재 음량을 가져옴
                    seekBar.setMax(nMax);//레이아웃내의 seekBar의 최대음량 설정
                    seekBar.setProgress(nCurrentVolumn);//현재음량에 따라 위치조정
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {//음량에따라 바뀌는 이벤트 리스너
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);//seekBar를 움직임에 따라 음량설정
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
                    MediaController mediaController = new MediaController(this); //videoView 를 제어하기 위해 선언
                    mediaController.setPadding(0,0,320,0);//video 컨트롤러를 중앙으로 오게하기위함
                    videoView.setMediaController(mediaController); //위에서 만든 컨트롤러와 videoView를 연결
                    videoView.setOnCompletionListener(completionListener); //완료 상태를 확인하기위해 설정


                    //----------------------음량 및 video 컨트롤러 설정 끝--------------


                    String playUri = myIntent.getStringExtra("playUri");//재생할 컨텐츠경로 가져오기
                    ((MainActivity)MainActivity.mContext).getUri(playUri); //다시재생하기위해 경로 저장
                    ((MainActivity)MainActivity.mContext).getTitle(playTitle);//재생한 컨텐츠 중복방지(컨텐츠 이름 저장)
                    setTitle(playTitle);//컨텐츠 이름을 넣어준다.
                    Log.i("URL", "----------------------------------------------");
                    Log.i("URL", playUri);
                    videoView.setVideoURI(Uri.parse(playUri+lang+".mp4"));
                    videoView.requestFocus();
                    videoView.start();
                    //외부 파일 읽어서 동영상 재생하는 방법----------------------------------------------------------end
                }
                else{
                    videoView.stopPlayback();//영상 중지
                    VideoPlayActivity.this.finish(); // 영상 종료
                }
            }
            else{
                videoView.stopPlayback();//영상 중지
                VideoPlayActivity.this.finish();//이미 실행중인경우 새로운 액티비티 실행을 하지않고 바로 종료시킨다

            }
        }catch(Exception ex){ // contents 가 없는데 실행하려고 하는경우
                Log.e(TAG, ex.getMessage());
                videoView.stopPlayback(); // 영상중지
                VideoPlayActivity.this.finish(); // activity 종료
            }

    }



    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {

            VideoPlayActivity.this.finish(); //재생이 완료되면 종료
            ((MainActivity)MainActivity.mContext).getisPlay(false); //재생이 완료되면 true인 현재상태를 false 로 변경해준다.
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

}
