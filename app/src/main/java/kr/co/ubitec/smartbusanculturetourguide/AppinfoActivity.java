package kr.co.ubitec.smartbusanculturetourguide;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

/**
 * Created by Ubitec on 2018-01-12.
 */

public class AppinfoActivity extends Activity {

    public String lang = "";
    public ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.appinfo);
        lang = ((MainActivity)MainActivity.mContext).putLang();
        imageView = (ImageView) findViewById(R.id.appinfo);


        if(lang.equals("ENG")){//언어별로 다른이미지로 설정
            imageView.setImageResource(R.drawable.walkingapp_en);
        }
        else if (lang.equals("JPN")){
            imageView.setImageResource(R.drawable.walkingapp_jp);
        }
        else if (lang.equals("CHN")){
            imageView.setImageResource(R.drawable.walkingapp_ch);
        }
        else {
            imageView.setImageResource(R.drawable.walkingapp_ko);
        }

    }
}
