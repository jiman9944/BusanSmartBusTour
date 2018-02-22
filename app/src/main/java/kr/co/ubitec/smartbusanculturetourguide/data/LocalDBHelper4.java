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

public class LocalDBHelper4 extends SQLiteOpenHelper {

    private static final String DB_NAME = "BusanSmartCity.sqlite";

    public static final String TAG = "BUSAN_SMART_CITY_LOG";

    private static SQLiteDatabase db;

    public LocalDBHelper4(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sql1 = new StringBuffer();
        //콘텐츠 DB생성
        try {
            sql1.append("CREATE TABLE APP_CONTENTS_PICTURE( ");
            sql1.append("	CONTENTS_NO INTEGER  ");
            sql1.append("   ,TITLE TEXT         ");
            sql1.append("   ,URL_ADDR TEXT        ");
            sql1.append("   ,PICTURE_NO INTEGER        ");
            //sql1.append("   ,BEACON_NO INTEGER     ");
            //sql1.append("   ,AIR_VIEW_YN TEXT      ");
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
        LocalDBHelper4 instance = new LocalDBHelper4(context);
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

    public static int getTourContents(String no) {
        int result = 0;
        Cursor c = null;
        try {
            StringBuffer masterSql = new StringBuffer();
            c = db.rawQuery("SELECT * FROM APP_CONTENTS_PICTURE WHERE CONTENTS_NO = "+"'" +no+"'" , null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    Log.i(TAG, c.getString(0));
                    result++;
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

    public static ArrayList<ContentsPicture> getContents(String no) {
        ArrayList<ContentsPicture> arrLst = new ArrayList<ContentsPicture>();

        StringBuffer masterSql = new StringBuffer();
       // masterSql.append("SELECT * FROM APP_TOUR_CONTENTS_PICTURE");
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT * FROM APP_CONTENTS_PICTURE WHERE CONTENTS_NO = "+"'" +no+"'" , null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    String title1 = c.getString(c.getColumnIndex("TITLE"));
                    if(!title1.equals("thum_1.jpg")) {
                        ContentsPicture contentsPicture = new ContentsPicture();
                        contentsPicture.setContents_No(c.getInt(c.getColumnIndex("CONTENTS_NO")));
                        contentsPicture.setTitle(c.getString(c.getColumnIndex("TITLE")));
                        contentsPicture.setUrl_Addr(c.getString(c.getColumnIndex("URL_ADDR")));
                        contentsPicture.setPicture_No(c.getInt(c.getColumnIndex("PICTURE_NO")));
                        arrLst.add(contentsPicture);
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

    public static void saveTourContentsVer2(String id, int version) throws Exception {
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

    public static void saveTourContents4(JSONObject jObject) throws JSONException {

        String insertQry = "INSERT INTO APP_CONTENTS_PICTURE (  CONTENTS_NO, TITLE, URL_ADDR, PICTURE_NO) "
                + " VALUES ( ?, ?, ?, ?) ";
        try {

            JSONArray jArray = jObject.getJSONArray("tourpicture");
            db.beginTransaction();
            db.execSQL("DELETE FROM APP_CONTENTS_PICTURE");
            for (int i = 0; i < jArray.length(); i++) {
                db.execSQL(insertQry, new Object[]{
                        jArray.getJSONObject(i).get("contents_no"),
                        jArray.getJSONObject(i).get("title"),
                        jArray.getJSONObject(i).get("url_addr"),
                        jArray.getJSONObject(i).get("picture_no")
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


}
