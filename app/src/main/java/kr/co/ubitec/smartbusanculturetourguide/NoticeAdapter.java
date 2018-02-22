package kr.co.ubitec.smartbusanculturetourguide;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import kr.co.ubitec.smartbusanculturetourguide.data.CultureContents;

/**
 * Created by Ubitec on 2018-01-05.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{

    Context context;
    ArrayList<HashMap<String,String>> contents;
    String lang="";


    public NoticeAdapter(Context context, ArrayList<HashMap<String,String>> contents){
        this.context = context;
        this.contents = contents;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler view에 반복될 아이템 레이아웃 연결
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,null); //레이아웃 연결
        return new ViewHolder(v);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    /** 정보 및 이벤트 처리는 이 메소드에서 구현 **/
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) { //holder는 가져온 하나하나의 아이템들을 칭하고, position은 count 라고 보면됨

        lang = ((MainActivity)MainActivity.mContext).putLang();
        final HashMap<String,String> item = contents.get(position); // 가져온 콘텐츠를 담는다
        String path = item.get("path"+position); //map에 있는 정보를 가져온다.
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); // 기기 내의 downloads 폴더의 경로 가져오기
        path = "file://"+file.getAbsolutePath() +path.replaceAll("\\\\","/"); //\ 를 / 로 변경

        holder.imageView.setImageURI(Uri.parse(path)); // 기기 내에 있는 image 를 url로 찾아서 가져와 뿌려줌
        holder.tv_content.setText(item.get("name"+position)); // 명소이름을 뿌린다.
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 이미지를 클릭햇을때 이벤트
                Intent intent = new Intent(view.getContext(),DetailActivity.class); //상세 페이지로 이동
                intent.putExtra("num",item.get("num"+position));//컨텐츠 번호
                intent.putExtra("name",item.get("name"+position));//명소이름
                view.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return this.contents.size();
    }

    /** item layout 불러오기 **/
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_content;
        CardView cv;
        ImageButton imageView;

        public ViewHolder(View v) {
            super(v);

            tv_content = (TextView) v.findViewById(R.id.tv_content);
            cv = (CardView) v.findViewById(R.id.cv);
            imageView = (ImageButton)v.findViewById(R.id.imageView);
        }
    }

}
