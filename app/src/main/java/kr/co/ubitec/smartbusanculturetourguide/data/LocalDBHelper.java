package kr.co.ubitec.smartbusanculturetourguide.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class LocalDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "BusanSmartCity.sqlite";

    public static final String TAG = "BUSAN_SMART_CITY_LOG";

    private static SQLiteDatabase db;

    public LocalDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sql1 = new StringBuffer();
        //콘텐츠 DB생성
        try {
            sql1.append("CREATE TABLE TOUR_CONTENTS( ");
            sql1.append("	NO INTEGER PRIMARY KEY ");
            sql1.append("   ,DISTRICT TEXT         ");
            sql1.append("   ,TITLE_KOR TEXT        ");
            sql1.append("   ,TITLE_ENG TEXT        ");
            sql1.append("   ,TITLE_JPN TEXT        ");
            sql1.append("   ,TITLE_CHN TEXT        ");
            sql1.append("   ,LATITUDE INTEGER      ");
            sql1.append("   ,LONGITUDE INTEGER     ");
            sql1.append("   ,MAIN_IMAGE TEXT       ");
            //sql1.append("   ,BEACON_NO INTEGER     ");
            sql1.append("   ,AIR_VIEW_YN TEXT      ");
            sql1.append(")                         ");
            System.out.println(sql1.toString());
            db.execSQL(sql1.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.getLocalizedMessage());
        }
        //콘텐츠 DB Version 관리생성
        try {
            sql1.delete(0, sql1.length());
            sql1.append("CREATE TABLE TOUR_CONTENTS_VER( ");
            sql1.append("   ID TEXT PRIMARY KEY ");
            sql1.append("   ,VER INTEGER         ");
            sql1.append(")                      ");
            System.out.println(sql1.toString());
            db.execSQL(sql1.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

    public static void init(Context context) {
        LocalDBHelper instance = new LocalDBHelper(context);
        db = instance.getReadableDatabase();
        instance.onCreate(db);


    }

    public static void destroy() {
        try {
            db.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        db = null;
    }

    private static Cursor getTourContentsVerCursor(String id) {
        StringBuffer masterSql = new StringBuffer();
        masterSql.append("SELECT VER FROM TOUR_CONTENTS_VER WHERE ID = ? ");
        Cursor c = db.rawQuery(masterSql.toString(), new String[]{id});
        return c;
    }

    /**
     *
     * @param nowLat
     * @param nowLng
     * @param lang : KOR, ENG, JPN, CHN
     * @return
     */
    public static ArrayList<MovieContents> getNearContents(double nowLat, double nowLng, String lang) {

        ArrayList<MovieContents> arrLst = new ArrayList<MovieContents>();

        StringBuffer masterSql = new StringBuffer();
        masterSql.append("SELECT * FROM TOUR_CONTENTS");
        Cursor c = null;
        try {
            c = db.rawQuery(masterSql.toString(), null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    double lat = c.getDouble(c.getColumnIndex("LATITUDE"));
                    double lng = c.getDouble(c.getColumnIndex("LONGITUDE"));
                    double distance = Math.sqrt(Math.pow(nowLat - lat, 2) + Math.pow(nowLng - lng, 2));
                    if (distance <= 0.0015) {
                        MovieContents movieContents = new MovieContents();
                        movieContents.setConNo(c.getInt(c.getColumnIndex("NO")));
                        movieContents.setName(c.getString(c.getColumnIndex("TITLE_" + lang)));
                        movieContents.setLat(lat);
                        movieContents.setLng(lng);
                        movieContents.setDistance(distance);
                        arrLst.add(movieContents);
                    }
                    c.moveToNext();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.getLocalizedMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        Collections.sort(arrLst, new MoviceContentsComparatorImpl());
        return arrLst;
    }

    public static int getTourContentsVer(String id) {
        int result = 0;
        Cursor c = null;
        try {
            c = getTourContentsVerCursor(id);
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    result = c.getInt(0);
                    c.moveToNext();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.getLocalizedMessage());
        } finally {
            if (c != null) {
                c.close();
            }
            return result;
        }
    }

    public static void saveTourContentsVer(String id, int version) throws Exception {
        Cursor c = null;
        try {
            StringBuffer masterSql = new StringBuffer();
            c = getTourContentsVerCursor(id);
            if (c.getCount() > 0) {
                masterSql.append("UPDATE TOUR_CONTENTS_VER SET ");
                masterSql.append("   VER      = ?           ");
                masterSql.append("   WHERE ID = ?           ");

            } else {
                masterSql.append("INSERT INTO TOUR_CONTENTS_VER(   ");
                masterSql.append("   VER                    ");
                masterSql.append("   ,ID                    ");
                masterSql.append(") VALUES (                ");
                masterSql.append("    ? ,?                  ");
                masterSql.append(")                         ");
            }

            db.beginTransaction();
            db.execSQL(masterSql.toString(), new Object[]{version, id});
            db.setTransactionSuccessful();

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.getLocalizedMessage());
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
            db.endTransaction();
        }
    }

    public static void saveTourContents(JSONObject jObject) throws JSONException {

        String insertQry = "INSERT INTO TOUR_CONTENTS (  NO, DISTRICT, TITLE_KOR, TITLE_ENG, TITLE_JPN, TITLE_CHN, LATITUDE, LONGITUDE, MAIN_IMAGE, AIR_VIEW_YN) "
                + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
        try {

            JSONArray jArray = jObject.getJSONArray("contentsLst");
            db.beginTransaction();
            db.execSQL("DELETE FROM TOUR_CONTENTS");
            for (int i = 0; i < jArray.length(); i++) {
                db.execSQL(insertQry, new Object[]{
                        jArray.getJSONObject(i).get("no"),
                        jArray.getJSONObject(i).get("district"),
                        jArray.getJSONObject(i).get("title_kor"),
                        jArray.getJSONObject(i).get("title_eng"),
                        jArray.getJSONObject(i).get("title_jpn"),
                        jArray.getJSONObject(i).get("title_chn"),
                        jArray.getJSONObject(i).get("latitude"),
                        jArray.getJSONObject(i).get("longitude"),
                        jArray.getJSONObject(i).get("main_image"),
                        //jArray.getJSONObject(i).get("beacon_no"),
                        jArray.getJSONObject(i).get("air_view_yn")
                });
            }
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.getLocalizedMessage());
            throw ex;
        } finally {
            db.endTransaction();
        }
    }

    public static int getTourContents(String BeaconNo) {
        int result = 0;
        Cursor c = null;
        try {
            StringBuffer masterSql = new StringBuffer();
            c = db.rawQuery("SELECT * FROM TOUR_CONTENTS WHERE BEACON_NO = ?", new String[]{BeaconNo});
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    Log.i(TAG, c.getString(0));
                    c.moveToNext();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.getLocalizedMessage());
        } finally {
            if (c != null) {
                c.close();
            }
            return result;
        }
    }
}
