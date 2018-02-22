package kr.co.ubitec.smartbusanculturetourguide.data;

import android.app.Activity;
import android.util.Log;

import org.json.JSONObject;

import kr.co.ubitec.smartbusanculturetourguide.MainActivity;
import kr.co.ubitec.smartbusanculturetourguide.R;

/**
 * Created by Administrator on 2017-09-21.
 */

public class RunnableGetTourContentsInfo extends Thread {
    public static String requestUrl;
    public static final String TAG = "BUSAN_SMART_CITY_LOG";

    public RunnableGetTourContentsInfo(String requestUrl){
        this.requestUrl = requestUrl;
    }

    @Override
    public void run() {
        JSONParser jParser = new JSONParser();
        JSONObject jsonObjectVersion = jParser.getJSONFromUrl(requestUrl + "/city/mAppFunc/getContentsVer.json?id=tourcont"); //json으로 만들어진 version파일을 읽어온다.
        try {
            if(jsonObjectVersion.getInt("contentsVer") == LocalDBHelper.getTourContentsVer("TOURCONT")){
                Log.i(TAG, "문화관광 DB 버전이 같음");
            }
//            if(jsonObjectVersion.getInt("appcontentsVer") == LocalDBHelper2.getTourContentsVer("APPCONT")){
//                Log.i(TAG, "문화관광 DB 버전이 같음");
//            }
            else{
                Log.i(TAG, "문화관광 DB 버전이 다름");
                JSONObject jsonObjectContentsLst = jParser.getJSONFromUrl(requestUrl + "/city/mAppFunc/getContentsLst.json"); //컨텐츠json 파일을 읽어온다.
                JSONObject jsonObjectAppContentsLst = jParser.getJSONFromUrl(requestUrl+"/city/mAppFunc/getTourContentsLst.json");
                JSONObject jsonObjectAppContentsDetail = jParser.getJSONFromUrl(requestUrl+"/city/mAppFunc/getContentsDetail.json");
                JSONObject jsonObjectAppContentsPicture = jParser.getJSONFromUrl(requestUrl+"/city/mAppFunc/getPictureLst.json");
                LocalDBHelper.saveTourContents(jsonObjectContentsLst); //컨텐츠 정보들을 읽어들여서 localDB에 저장
                LocalDBHelper2.saveTourContents2(jsonObjectAppContentsLst); //컨텐츠 정보들을 읽어들여서 localDB에 저장
                LocalDBHelper3.saveTourContents3(jsonObjectAppContentsDetail); //컨텐츠 정보들을 읽어들여서 localDB에 저장
                LocalDBHelper4.saveTourContents4(jsonObjectAppContentsPicture);//컨텐츠 정보들을 읽어들여서 localDB에 저장

                LocalDBHelper.saveTourContentsVer("TOURCONT", jsonObjectVersion.getInt("contentsVer")); //최신 버전을 저장한다
                LocalDBHelper2.saveTourContentsVer2("TOURCONT", jsonObjectVersion.getInt("appcontentsVer")); //최신 버전을 저장한다
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
