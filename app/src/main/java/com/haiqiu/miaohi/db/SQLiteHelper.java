package com.haiqiu.miaohi.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;

import java.io.File;

/**
 * Created by zhandalin on 2016-12-19 15:40.
 * 说明:数据库
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "miaohi.db";
    private static final int VERSION = 1;
    private static SQLiteHelper helper;
    private static final String TAG = "SQLiteHelper";

    private SQLiteHelper(Context context) {
        this(context, DB_NAME, null, VERSION);
    }

    private SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        this(context, name, factory, version, null);
    }

    private SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + ConstantsValue.DB.TABLE_NAME_ATTENTION + "(id INTEGER primary key,user_id varchar(50),user_type varchar(10),user_name varchar(100),user_name_pinyin varchar(30),user_name_char varchar(10),user_name_head_char varchar(10),portrait_uri varchar(512),portrait_state varchar(10),attention_state varchar(10),attention_time varchar(20),attention_time_text varchar(20)," +
                "user_gender varchar(5),user_authentic varchar(1024),answer_auth INTEGER)";
        db.execSQL(sql);

        sql = "CREATE UNIQUE INDEX user_id_index_" + ConstantsValue.DB.TABLE_NAME_ATTENTION + " ON " + ConstantsValue.DB.TABLE_NAME_ATTENTION + " (user_id)";
        db.execSQL(sql);

        sql = "create table " + ConstantsValue.DB.TABLE_NAME_FANS + "(id INTEGER primary key,user_id varchar(50),user_type varchar(10),user_name varchar(100),user_name_pinyin varchar(30),user_name_char varchar(10),user_name_head_char varchar(10),portrait_uri varchar(512),portrait_state varchar(10),attention_state varchar(10),attention_time varchar(20),attention_time_text varchar(20)," +
                "user_gender varchar(5),user_authentic varchar(1024),answer_auth INTEGER)";
        db.execSQL(sql);

        sql = "CREATE UNIQUE INDEX user_id_index_" + ConstantsValue.DB.TABLE_NAME_FANS + " ON " + ConstantsValue.DB.TABLE_NAME_FANS + " (user_id)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * @return 如果没有登陆就会为null
     */
    public static SQLiteHelper getInstance(Context context) {
        if (helper == null) {
            String userId = UserInfoUtil.getUserId(context);
            if (MHStringUtils.isEmpty(userId)) {
                MHLogUtil.e(TAG, "没有登陆");
                return null;
            } else {
                DB_NAME = userId.replace("-", "") + ".db";
            }
            synchronized (SQLiteHelper.class) {
                helper = new SQLiteHelper(context);
            }
        }
        return helper;
    }

    /**
     * 切换数据库, 在登陆成功的时候执行
     */
    public static void switchDB() {
        helper = null;
    }


    public static void deleteDb(Context context) {
        getInstance(context);
        File file = new File(context.getDatabasePath(DB_NAME).getAbsolutePath());
        if (file.exists()) file.delete();
        helper = null;
        getInstance(context);
    }
}
