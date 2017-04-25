package com.ystl.yysj.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Base64;

import com.alibaba.fastjson.JSONObject;
import com.ystl.yysj.ui.login.bean.UserInfo;

import java.io.UnsupportedEncodingException;

public class AppDataBaseHelper extends SQLiteOpenHelper {

    public static final String userID = "UID";//登录名字
    public static final String userINFO = "INFO";//保存 Base64Encoder编码后的userInfo JSON字符串
    public static final String TABLE_NAME = "USER";//表名
    // 数据库名称，开启注释把数据库放到Sdcard上
    private static final String DB_NAME = "yyssj.db";
    private static final String charset = "utf-8";
    /**
     * 数据库版本，升级时修改
     */
    private static final int DB_VERSION = 1;
    public static String SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( id INTEGER PRIMARY KEY AUTOINCREMENT ," + userID + " TEXT UNIQUE NOT NULL , " + userINFO + " TEXT  NOT NULL)";
    private static AppDataBaseHelper dbOpenHelper = null;

    private AppDataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * 得到数据库实例
     *
     * @param context
     * @return 数据库的SQLiteOpenHelper对象
     */
    public static synchronized AppDataBaseHelper getInstance(Context context) {
        if (dbOpenHelper == null) {
            dbOpenHelper = new AppDataBaseHelper(context);
            return dbOpenHelper;
        } else {
            return dbOpenHelper;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // db.execSQL(" ALTER TABLE TABLE_NAME ADD phone VARCHAR(12) NULL "); //往表中增加一列
        // DROP TABLE IF EXISTS TABLE_NAME 删除表
        db.execSQL("DROP TABLE  IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }


    /**
     * 插入UserInfo
     *
     * @param userInfo
     * @return
     */
    public synchronized long insertUser(UserInfo userInfo) {
        long id = 0;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.beginTransaction();
            String encodeName = encodeString(userInfo.uid + "");
            Cursor cursor = db.rawQuery("select " + userID + " from " + TABLE_NAME + " where " + userID + " = ?", new String[]{encodeName});
            ContentValues values = getContentValues(userInfo);
            if (cursor == null || cursor.getCount() <= 0) {
                //插入
                id = db.insert(TABLE_NAME, null, values);
            } else {
                //更新
                id = db.update(TABLE_NAME, values, userID + " = ?", new String[]{encodeName});
            }
            if (cursor != null) {
                cursor.close();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                db.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    public UserInfo getUserInfo(String userId) {
        UserInfo userInfo = null;
        SQLiteDatabase db = null;
        if (TextUtils.isEmpty(userId)) {
            return null;
        }
        try {
            String name = encodeString(userId);
            db = getReadableDatabase();
            db.beginTransaction();
            Cursor cursor = db.rawQuery("select " + userINFO + " from " + TABLE_NAME + " where  " + userID + " = ?", new String[]{name});
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int infoIndex = cursor.getColumnIndex(userINFO);
                    String userJsonStringBase64 = cursor.getString(infoIndex);
                    String userJson = decodeString(userJsonStringBase64);
                    userInfo = JSONObject.parseObject(userJson, UserInfo.class);
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                db.endTransaction();
            } catch (Exception e) {

            }
        }
        return userInfo;
    }


    public void delUsrInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.beginTransaction();
            db.delete(TABLE_NAME, userID + " = ? ", new String[]{encodeString(userInfo.uid + "")});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                db.endTransaction();
            } catch (Exception e) {

            }
        }
    }


    public ContentValues getContentValues(UserInfo userInfo) throws Exception {
        ContentValues values = new ContentValues();
        values.put(userID, encodeString(userInfo.uid + ""));
        String infoString = JSONObject.toJSONString(userInfo);
        values.put(userINFO, encodeString(infoString));
        return values;
    }


    /**
     * 编码
     *
     * @param text
     * @return
     * @throws UnsupportedEncodingException
     */
    public String encodeString(String text) throws UnsupportedEncodingException {
        return Base64.encodeToString(text.getBytes(charset), Base64.DEFAULT);
    }

    /**
     * 解码
     *
     * @param text
     * @return
     */
    public String decodeString(String text) throws UnsupportedEncodingException {
        return new String(Base64.decode(text.getBytes(charset), Base64.DEFAULT));

    }


}
