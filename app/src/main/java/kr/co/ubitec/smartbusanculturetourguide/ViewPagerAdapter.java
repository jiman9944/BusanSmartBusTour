package kr.co.ubitec.smartbusanculturetourguide;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ubitec on 2018-01-10.
 */

public class ViewPagerAdapter extends PagerAdapter {
    ArrayList<HashMap<String,String>> images;
    private LayoutInflater inflater;
    private Context context;

    public ViewPagerAdapter (Context context,  ArrayList<HashMap<String,String>> images){
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) { //container 는 사진을 넣는 곳, position 은 갯수
        inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.viewpager, container, false);
        int count = getCount();
        ImageView imageView = (ImageView)v.findViewById(R.id.img_view) ;
        TextView imageText = (TextView)v.findViewById(R.id.imageText);

        final HashMap<String,String> item = images.get(position);
        String path = item.get("url_addr"+position); // 이미지 경로 가져오기
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); // 내부 download 폴더 경로 가져오기
        path = "file://"+file.getAbsolutePath() +path.replaceAll("\\\\","/"); //이미지 경로 설정

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//이미지를 화면에 꽉차게 설정
        imageText.setText(position+1+"/"+count); //이미지 아래에 몇번째 사진인지 알려주는 TEXT
        imageView.setImageURI(Uri.parse(path)); //url 로 가져온 이미지를 찾아서 넣는다.
        container.addView(v); //viewPager 에 추가한다.

        return v;
    }

    @Override

    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub

        //ViewPager에서 보이지 않는 View는 제거
        //세번째 파라미터가 View 객체 이지만 데이터 타입이 Object여서 형변환 실시
        container.removeView((View)object);
    }
}
