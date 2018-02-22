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

public class LocalDBHelper2 extends SQLiteOpenHelper {

    private static final String DB_NAME = "BusanSmartCity.sqlite";

    public static final String TAG = "BUSAN_SMART_CITY_LOG";

    private static SQLiteDatabase db;

    public LocalDBHelper2(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sql1 = new StringBuffer();
        //콘텐츠 DB생성
        try {
            sql1.append("CREATE TABLE APP_TOUR_CONTENT( ");
            sql1.append("	NO INTEGER PRIMARY KEY ");
            sql1.append("   ,DISTRICT TEXT         ");
            sql1.append("   ,TITLE_KOR TEXT        ");
            sql1.append("   ,TITLE_ENG TEXT        ");
            sql1.append("   ,TITLE_JPN TEXT        ");
            sql1.append("   ,TITLE_CHN TEXT        ");
            sql1.append("   ,MAIN_IMAGE TEXT       ");
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
        LocalDBHelper2 instance = new LocalDBHelper2(context);
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
     * @param lang : KOR, ENG, JPN, CHN
     * @return
     */
    public static ArrayList<CultureContents> getContents(String lang) {
        ArrayList<CultureContents> arrLst = new ArrayList<CultureContents>();

        StringBuffer masterSql = new StringBuffer();
        masterSql.append("SELECT * FROM APP_TOUR_CONTENT");
        Cursor c = null;
        try {
            c = db.rawQuery(masterSql.toString(), new String[]{});
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    int no = c.getInt(c.getColumnIndex("NO"));
                    CultureContents cultureContents = new CultureContents();
                    cultureContents.setNo(c.getInt(c.getColumnIndex("NO")));
                    cultureContents.setName(c.getString(c.getColumnIndex("TITLE_" + lang)));
                    arrLst.add(cultureContents);

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

    public static void saveTourContents2(JSONObject jObject) throws JSONException {

        String insertQry = "INSERT INTO APP_TOUR_CONTENT (  NO, DISTRICT, TITLE_KOR, TITLE_ENG, TITLE_JPN, TITLE_CHN, MAIN_IMAGE) "
                + " VALUES ( ?, ?, ?, ?, ?, ?, ?) ";
        try {

            JSONArray jArray = jObject.getJSONArray("contentsLst");
            db.beginTransaction();
            db.execSQL("DELETE FROM APP_TOUR_CONTENT");
            for (int i = 0; i < jArray.length(); i++) {
                db.execSQL(insertQry, new Object[]{
                        jArray.getJSONObject(i).get("con_no"),
                        jArray.getJSONObject(i).get("district"),
                        jArray.getJSONObject(i).get("title_kor"),
                        jArray.getJSONObject(i).get("title_eng"),
                        jArray.getJSONObject(i).get("title_jpn"),
                        jArray.getJSONObject(i).get("title_chn"),
                        jArray.getJSONObject(i).get("main_image")
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

    public static int getTourContents(String gu) {
        int result = 0;
        Cursor c = null;
        try {
            StringBuffer masterSql = new StringBuffer();
            c = db.rawQuery("SELECT * FROM APP_TOUR_CONTENT WHERE DISTRICT = "+"'" +gu+"'", null);
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

    public static int getTourContents(String gu, String gu2) {
        int result = 0;
        Cursor c = null;
        try {
            StringBuffer masterSql = new StringBuffer();
            c = db.rawQuery("SELECT * FROM APP_TOUR_CONTENT WHERE DISTRICT = "+"'" +gu+"'" + "OR DISTRICT = "+"'" +gu2+"'", null);
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

    public static int getTourContents(String gu, String gu2,String gu3, String gu4) {
        int result = 0;
        Cursor c = null;
        try {
            StringBuffer masterSql = new StringBuffer();
            c = db.rawQuery("SELECT * FROM APP_TOUR_CONTENT WHERE DISTRICT = "+"'" +gu+"'" + "OR DISTRICT = "+"'" +gu2+"'" + "OR DISTRICT = "+"'" +gu3+"'" + "OR DISTRICT = "+"'" +gu4+"'", null);
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

    public static ArrayList<CultureContents> getContentslist(String lang , String gu) {
        ArrayList<CultureContents> arrLst = new ArrayList<CultureContents>();

        StringBuffer masterSql = new StringBuffer();
//        masterSql.append("SELECT * FROM APP_TOUR_CONTENT WHERE DISTRICT = ");
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT * FROM APP_TOUR_CONTENT WHERE DISTRICT = "+"'" +gu+"'", null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    int no = c.getInt(c.getColumnIndex("NO"));
                    double lat = c.getDouble(c.getColumnIndex("LATITUDE"));
                    double lng = c.getDouble(c.getColumnIndex("LONGITUDE"));
                    CultureContents cultureContents = new CultureContents();
                    cultureContents.setNo(c.getInt(c.getColumnIndex("NO")));
                    cultureContents.setName(c.getString(c.getColumnIndex("TITLE_" + lang)));
                    cultureContents.setMain_image(c.getString(c.getColumnIndex("MAIN_IMAGE")));
                    arrLst.add(cultureContents);

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

    public static ArrayList<CultureContents> getContentslist(String lang , String gu, String gu2) {
        ArrayList<CultureContents> arrLst = new ArrayList<CultureContents>();

        StringBuffer masterSql = new StringBuffer();
//        masterSql.append("SELECT * FROM APP_TOUR_CONTENT WHERE DISTRICT = ");
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT * FROM APP_TOUR_CONTENT WHERE DISTRICT = "+"'" +gu+"'" + "OR DISTRICT = "+"'" +gu2+"'" , null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    int no = c.getInt(c.getColumnIndex("NO"));
                    double lat = c.getDouble(c.getColumnIndex("LATITUDE"));
                    double lng = c.getDouble(c.getColumnIndex("LONGITUDE"));
                    CultureContents cultureContents = new CultureContents();
                    cultureContents.setNo(c.getInt(c.getColumnIndex("NO")));
                    cultureContents.setName(c.getString(c.getColumnIndex("TITLE_" + lang)));
                    cultureContents.setMain_image(c.getString(c.getColumnIndex("MAIN_IMAGE")));
                    arrLst.add(cultureContents);

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

    public static ArrayList<CultureContents> getContentslist(String lang , String gu, String gu2, String gu3, String gu4) {
        ArrayList<CultureContents> arrLst = new ArrayList<CultureContents>();

        StringBuffer masterSql = new StringBuffer();
//        masterSql.append("SELECT * FROM APP_TOUR_CONTENT WHERE DISTRICT = ");
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT * FROM APP_TOUR_CONTENT WHERE DISTRICT = "+"'" +gu+"'" + "OR DISTRICT = "+"'" +gu2+"'" + "OR DISTRICT = "+"'" +gu3+"'" + "OR DISTRICT = "+"'" +gu4+"'", null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    int no = c.getInt(c.getColumnIndex("NO"));
                    CultureContents cultureContents = new CultureContents();
                    cultureContents.setNo(c.getInt(c.getColumnIndex("NO")));
                    cultureContents.setName(c.getString(c.getColumnIndex("TITLE_" + lang)));
                    cultureContents.setMain_image(c.getString(c.getColumnIndex("MAIN_IMAGE")));
                    arrLst.add(cultureContents);

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
