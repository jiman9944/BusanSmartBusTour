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

public class LocalDBHelper3 extends SQLiteOpenHelper {

    private static final String DB_NAME = "BusanSmartCity.sqlite";

    public static final String TAG = "BUSAN_SMART_CITY_LOG";

    private static SQLiteDatabase db;

    public LocalDBHelper3(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sql1 = new StringBuffer();
        //콘텐츠 DB생성
        try {
            sql1.append("CREATE TABLE TOUR_CONTENTS_DETAILS( ");
            sql1.append("	NO INTEGER ");
            sql1.append("   ,LANG INTEGER         ");
            sql1.append("   ,CONTENTS TEXT        ");
            sql1.append("   ,ADDRESS_KO TEXT        ");
            sql1.append("   ,ADDRESS_EN TEXT        ");
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
        LocalDBHelper3 instance = new LocalDBHelper3(context);
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


    public static int getTourContents(int lang,String no) {
        int result = 0;
        Cursor c = null;
        try {
            StringBuffer masterSql = new StringBuffer();
            c = db.rawQuery("SELECT * FROM TOUR_CONTENTS_DETAILS WHERE NO = "+"'" +no+"'" +"AND LANG ="+ "'" +lang+"'", null);
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

    public static void saveTourContents3(JSONObject jObject) throws JSONException {

        String insertQry = "INSERT INTO TOUR_CONTENTS_DETAILS (  NO, LANG, CONTENTS, ADDRESS_KO, ADDRESS_EN) "
                + " VALUES ( ?, ?, ?, ?, ?) ";
        try {

            JSONArray jArray = jObject.getJSONArray("tourContentsDTLList");
            db.beginTransaction();
            db.execSQL("DELETE FROM TOUR_CONTENTS_DETAILS");
            for (int i = 0; i < jArray.length(); i++) {
                db.execSQL(insertQry, new Object[]{
                        jArray.getJSONObject(i).get("no"),
                        jArray.getJSONObject(i).get("lang"),
                        jArray.getJSONObject(i).get("contents"),
                        jArray.getJSONObject(i).get("address_ko"),
                        jArray.getJSONObject(i).get("address_en")
                        //jArray.getJSONObject(i).get("beacon_no"),
                        //jArray.getJSONObject(i).get("air_view_yn")
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



    public static ArrayList<ContentsDetail> getContentsDetail(int lang , String no) {
        ArrayList<ContentsDetail> arrLst = new ArrayList<ContentsDetail>();

        StringBuffer masterSql = new StringBuffer();
//        masterSql.append("SELECT * FROM APP_TOUR_CONTENTS WHERE DISTRICT = ");
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT * FROM TOUR_CONTENTS_DETAILS WHERE NO = "+"'" +no+"'" +"AND LANG ="+ "'" +lang+"'", null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    ContentsDetail contentsDetail = new ContentsDetail();
                    contentsDetail.setNo(c.getInt(c.getColumnIndex("NO")));
                    contentsDetail.setLang(c.getInt(c.getColumnIndex("LANG")));
                    contentsDetail.setContents(c.getString(c.getColumnIndex("CONTENTS")));
                    contentsDetail.setAddress_ko(c.getString(c.getColumnIndex("ADDRESS_KO")));
                    contentsDetail.setAddress_en(c.getString(c.getColumnIndex("ADDRESS_EN")));

                    arrLst.add(contentsDetail);

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
}
