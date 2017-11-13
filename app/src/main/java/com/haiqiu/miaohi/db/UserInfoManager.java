package com.haiqiu.miaohi.db;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.bean.UserData;
import com.haiqiu.miaohi.bean.UserGroupInfo;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.UserDataResponse;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by zhandalin on 2016-12-19 17:03.
 * 说明:处理用户自己的关注与粉丝信息
 */
public class UserInfoManager {
    private static final String TAG = "UserInfoManager";

    /**
     * 所有的可能的字母
     * A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
     */
    private final static HashSet<String> ALL_LETTER_SET = new HashSet<String>() {{
        //A B C D E
        add("A");
        add("B");
        add("C");
        add("D");
        add("E");
        //F G H I J
        add("F");
        add("G");
        add("H");
        add("I");
        add("J");
        //K L M N O
        add("K");
        add("L");
        add("M");
        add("N");
        add("O");
        //P Q R S T
        add("P");
        add("Q");
        add("R");
        add("S");
        add("T");
        //U V W X Y
        add("U");
        add("V");
        add("W");
        add("X");
        add("Y");
        //Z
        add("Z");
        add("#");
    }};
    private static final int pageSize = 10000;
    private static boolean fansIsSyncing;
    private static int fansSyncFailCount;
    private static int fans_page_index;

    private static boolean attentionIsSyncing;
    private static int attentionSyncFailCount;
    private static int attention_page_index;


    /**
     * 保存用户信息,会根据user_id 去重,建表的时候就是用user_id,attention_state做了唯一约束
     *
     * @param tableName    表名字
     * @param userDataList 用户集合
     * @return true 操作成功
     */
    public static boolean saveUserInfos(String tableName, Context context, List<UserData> userDataList) {
        if (null == userDataList) return false;

        synchronized (UserInfoManager.class) {
            SQLiteDatabase database = null;
            try {
                SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
                if (null == sqLiteHelper) return false;

                database = sqLiteHelper.getReadableDatabase();


                database.beginTransaction();
                String sql = "INSERT INTO " + tableName + " (user_id,user_type,user_name,user_name_pinyin,user_name_char,user_name_head_char," +
                        "portrait_uri,portrait_state,attention_state,attention_time,attention_time_text,user_gender,user_authentic ,answer_auth) VALUES " +
                        "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                UserData userData;
                for (int i = 0; i < userDataList.size(); i++) {
                    userData = userDataList.get(i);
                    if (null == userData) continue;

                    try {
                        database.execSQL(sql, new String[]{userData.user_id, userData.user_type + "", userData.user_name, userData.user_name_pinyin, userData.user_name_char, userData.user_name_head_char,
                                userData.portrait_uri, userData.portrait_state + "", userData.attention_state + "", userData.attention_time + "", userData.attention_time_text,
                                userData.user_gender + "", userData.user_authentic, userData.answer_auth + ""});
                    } catch (Exception e) {//高频率的不要打日志,太慢了
                        try {
                            //更新以前的
                            String format = String.format("UPDATE %s SET user_type = '%s', user_name = '%s', user_name_pinyin = '%s', user_name_char = '%s', user_name_head_char = '%s', " +
                                            "portrait_uri = '%s', portrait_state = '%s', attention_state = '%s', attention_time = '%s', attention_time_text = '%s', " +
                                            "user_gender = '%s', user_authentic = '%s', answer_auth = '%s' WHERE user_id = '%s'", tableName, userData.user_type + "", userData.user_name, userData.user_name_pinyin, userData.user_name_char, userData.user_name_head_char,
                                    userData.portrait_uri, userData.portrait_state + "", userData.attention_state + "", userData.attention_time + "", userData.attention_time_text,
                                    userData.user_gender + "", userData.user_authentic, userData.answer_auth + "", userData.user_id);
                            MHLogUtil.d("llll", "format=" + format);
                            database.execSQL(format);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                database.setTransactionSuccessful();
                database.endTransaction();
                return true;
            } catch (Exception e) {
                MHLogUtil.e(TAG,e);
                MHLogUtil.e(TAG, "saveUserInfos--" + e.getMessage());
            } finally {
                try {
                    if (null != database)
                        database.close();
                } catch (Exception e) {
                    MHLogUtil.e(TAG,e);
                    MHLogUtil.e(TAG, "saveUserInfos--close" + e.getMessage());
                }
            }
            return false;
        }

    }

    /**
     * @param tableName           表名字
     * @param user_name_head_char 用户名字每个字的首字母,传null,将全部返回,并且会过滤掉"#"号分组
     * @param limit               格式如0,99 表示从0开始取99条数据
     * @return 符合条件的用户集合
     */
    public static List<UserData> getUserInfos(String tableName, Context context, String user_name_head_char, String limit) {
        List<UserData> userDataList = new ArrayList<>();

        synchronized (UserInfoManager.class) {

            SQLiteDatabase database = null;
            try {
                database = SQLiteHelper.getInstance(context).getReadableDatabase();
                if (null == database) return userDataList;
                Cursor cursor;
                if (!MHStringUtils.isEmpty(user_name_head_char))
                    cursor = database.query(tableName, null, "user_name_head_char = ?", new String[]{user_name_head_char}, null, null, "user_name_char", limit);
                else
                    cursor = database.query(tableName, null, "user_name_head_char != ?", new String[]{"#"}, null, null, "user_name_char", limit);

                UserData userData;
                while (cursor.moveToNext()) {
                    userData = new UserData();
                    userData.attention_state = cursor.getInt(cursor.getColumnIndex("attention_state"));
                    userData.user_name_char = cursor.getString(cursor.getColumnIndex("user_name_char"));
                    userData.user_name_pinyin = cursor.getString(cursor.getColumnIndex("user_name_pinyin"));
                    userData.user_name = cursor.getString(cursor.getColumnIndex("user_name"));
                    userData.attention_time = cursor.getLong(cursor.getColumnIndex("attention_time"));
                    userData.attention_time_text = cursor.getString(cursor.getColumnIndex("attention_time_text"));
                    userData.portrait_state = cursor.getInt(cursor.getColumnIndex("portrait_state"));
                    userData.portrait_uri = cursor.getString(cursor.getColumnIndex("portrait_uri"));
                    userData.user_id = cursor.getString(cursor.getColumnIndex("user_id"));
                    userData.user_type = cursor.getInt(cursor.getColumnIndex("user_type"));
                    userData.user_name_head_char = cursor.getString(cursor.getColumnIndex("user_name_head_char"));
                    userData.user_gender = cursor.getInt(cursor.getColumnIndex("user_gender"));
                    userData.user_authentic = cursor.getString(cursor.getColumnIndex("user_authentic"));
                    userData.answer_auth = cursor.getInt(cursor.getColumnIndex("answer_auth"));
                    userDataList.add(userData);
                }
                cursor.close();
            } catch (Exception e) {
                MHLogUtil.e(TAG,e);
                MHLogUtil.e(TAG, "getUserInfos--" + e.getMessage());
            } finally {
                try {
                    if (null != database)
                        database.close();
                } catch (Exception e) {
                    MHLogUtil.e(TAG,e);
                    MHLogUtil.e(TAG, "getUserInfos--close" + e.getMessage());
                }
            }
            return userDataList;
        }
    }

    /**
     * @return 所有用户的数量
     */
    public static int getUserCount(Context context, String tableName) {
        synchronized (UserInfoManager.class) {
            SQLiteDatabase database = null;
            try {
                SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
                if (null == sqLiteHelper) return 0;

                database = sqLiteHelper.getReadableDatabase();

                String sql = "SELECT COUNT(user_id) FROM " + tableName;
                Cursor cursor = database.rawQuery(sql, null);
                cursor.moveToNext();
                int count = cursor.getInt(0);
                cursor.close();
                return count;
            } catch (Exception e) {
                MHLogUtil.e(TAG,e);
                MHLogUtil.e(TAG, "getUserCount--" + e.getMessage());
            } finally {
                try {
                    if (null != database)
                        database.close();
                } catch (Exception e) {
                    MHLogUtil.e(TAG,e);
                    MHLogUtil.e(TAG, "getUserCount--close" + e.getMessage());
                }
            }
            return 0;
        }
    }

    /**
     * @return 获取所有分类, 来作为联系人的索引
     */
    public static List<UserGroupInfo> getAllGroup(Context context, String tableName) {
        synchronized (UserInfoManager.class) {
            List<UserGroupInfo> arrayList = new ArrayList<>();
            MHLogUtil.d(TAG, "getAllGroup");
            SQLiteDatabase database = null;
            try {
                SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
                if (null == sqLiteHelper) return arrayList;

                database = sqLiteHelper.getReadableDatabase();


                String sql = "select user_name_head_char,count(user_name_head_char) from " + tableName + " group by user_name_head_char";
                Cursor cursor = database.rawQuery(sql, null);

                while (cursor.moveToNext()) {
                    String groupName = cursor.getString(0);
                    if (ALL_LETTER_SET.contains(groupName)) {
                        arrayList.add(new UserGroupInfo(groupName, cursor.getInt(1)));
                    }
                }
                //把#放在最后
                if (arrayList.size() > 0) {
                    UserGroupInfo groupInfo = arrayList.get(0);
                    if ("#".equals(groupInfo.getGroupName())) {
                        arrayList.remove(groupInfo);
                        arrayList.add(groupInfo);
                    }
                }
                cursor.close();
                return arrayList;
            } catch (Exception e) {
                MHLogUtil.e(TAG,e);
                MHLogUtil.e(TAG, "getAllGroup--" + e.getMessage());
            } finally {
                try {
                    if (null != database)
                        database.close();
                } catch (Exception e) {
                    MHLogUtil.e(TAG,e);
                    MHLogUtil.e(TAG, "getAllGroup--close" + e.getMessage());
                }
            }
            return arrayList;
        }
    }

    public static boolean updateAttentionState(Context context, String user_id, boolean attention_state) {
        if (null == user_id) return false;

        synchronized (UserInfoManager.class) {
            SQLiteDatabase database = null;
            try {
                SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
                if (null == sqLiteHelper) return false;

                database = sqLiteHelper.getReadableDatabase();

                //attention 表处理逻辑
                if (attention_state) {//要把这条数据加进去
//                    String sql = String.format("DELETE FROM %s WHERE user_id = '%s'", ConstantsValue.DB.TABLE_NAME_ATTENTION, userBaseInfo.user_id);
//                    database.execSQL(sql);
                    //这个没法处理,太多字段都没有,直接从服务端拿数据吧
                    syncData(context);
                } else {//取消关注的话要把这条数删除
                    String sql = String.format("DELETE FROM %s WHERE user_id = '%s'", ConstantsValue.DB.TABLE_NAME_ATTENTION, user_id);
                    database.execSQL(sql);
                }

                //fans表不能删,要更新状态
                String sql = String.format("UPDATE %s SET attention_state = '%s' WHERE user_id = '%s'", ConstantsValue.DB.TABLE_NAME_FANS, attention_state ? 1 : 0, user_id);
                database.execSQL(sql);
                MHLogUtil.d(TAG, "updateAttentionState--成功");
                return true;
            } catch (Exception e) {
                MHLogUtil.e(TAG,e);
                MHLogUtil.e(TAG, "updateAttentionState--" + e.getMessage());
            } finally {
                try {
                    if (null != database)
                        database.close();
                } catch (Exception e) {
                    MHLogUtil.e(TAG,e);
                    MHLogUtil.e(TAG, "saveUserInfos--close" + e.getMessage());
                }
            }
            return false;
        }

    }

    /**
     * 去服务端同步数据
     */
    public static void syncData(Context context) {
        if (!UserInfoUtil.isLogin()) {
            MHLogUtil.e(TAG, "没有登陆--不同步数据");
            return;
        }
        if (fansIsSyncing) {
            MHLogUtil.e(TAG, ConstantsValue.DB.TABLE_NAME_FANS + "正在同步数据,这次同步请求将丢失");
        } else {
            fansSyncFailCount = 0;
            fans_page_index = 0;
            syncFansData(context);
        }

        if (attentionIsSyncing) {
            MHLogUtil.e(TAG, ConstantsValue.DB.TABLE_NAME_ATTENTION + "正在同步数据,这次同步请求将丢失");
        } else {
            attentionSyncFailCount = 0;
            attention_page_index = 0;
            syncAttentionData(context);
        }
    }

    /**
     * 递归同步粉丝,直到全部拉取完
     */
    private static void syncFansData(final Context context) {
        MHLogUtil.d(TAG, "syncFansData--fans_page_index=" + fans_page_index);
        if (fansSyncFailCount > 3) {
            MHLogUtil.e(TAG, "syncFansData--重试次数大于3次--不再同步");
            fansIsSyncing = false;
            return;
        }
        fansIsSyncing = true;

        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_size", pageSize + "");
        requestParams.addParams("page_index", fans_page_index + "");
        requestParams.addParams("synch_time", SpUtils.getString(UserInfoUtil.getUserId(context) + "_" + ConstantsValue.DB.TABLE_NAME_FANS + "_last_synch_time"));

        MHHttpClient.getInstance().post(UserDataResponse.class, ConstantsValue.Url.USER_FANS_OWN, requestParams, new MHHttpHandler<UserDataResponse>() {
            @Override
            public void onSuccess(final UserDataResponse response) {
                fans_page_index++;
                if (null == response || null == response.data || null == response.data.page_result
                        || response.data.page_result.size() == 0) {
                    MHLogUtil.d(TAG, ConstantsValue.DB.TABLE_NAME_FANS + "--没有新的数据");
                    fansIsSyncing = false;
                    context.sendBroadcast(new Intent(ConstantsValue.IntentFilterAction.SYNC_FANS_DATA_SUCCESS));
                    if (null != response && null != response.data)
                        SpUtils.put(UserInfoUtil.getUserId(context) + "_" + ConstantsValue.DB.TABLE_NAME_FANS + "_last_synch_time", response.data.server_synch_time);
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long startTime = System.currentTimeMillis();
                        MHLogUtil.d(TAG, "size=" + response.data.page_result.size());
                        UserInfoManager.saveUserInfos(ConstantsValue.DB.TABLE_NAME_FANS, context.getApplicationContext(), response.data.page_result);
                        MHLogUtil.d(TAG, "耗时=" + (System.currentTimeMillis() - startTime));
                    }
                }).start();

                MHLogUtil.d(TAG, "syncFansData--继续请求数据--size=" + response.data.page_result.size());
                syncFansData(context);
            }

            @Override
            public void onFailure(String content) {
                fansSyncFailCount++;
                syncFansData(context);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                fansSyncFailCount++;
                syncFansData(context);
            }
        });
    }

    /**
     * 同步关注数据
     */
    private static void syncAttentionData(final Context context) {
        if (attentionSyncFailCount > 3) {
            MHLogUtil.e(TAG, "syncAttentionData--重试次数大于3次--不再同步");
            attentionIsSyncing = false;
            return;
        }
        MHLogUtil.d(TAG, "syncAttentionData");
        attentionIsSyncing = true;

        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_size", pageSize + "");
        requestParams.addParams("page_index", attention_page_index + "");
        requestParams.addParams("synch_time", SpUtils.getString(UserInfoUtil.getUserId(context) + "_" + ConstantsValue.DB.TABLE_NAME_ATTENTION + "_last_synch_time"));

        MHHttpClient.getInstance().post(UserDataResponse.class, ConstantsValue.Url.USER_ATTENTION_OWN, requestParams, new MHHttpHandler<UserDataResponse>() {
            @Override
            public void onSuccess(final UserDataResponse response) {
                attention_page_index++;
                if (null == response || null == response.data || null == response.data.page_result
                        || response.data.page_result.size() == 0) {
                    MHLogUtil.d(TAG, ConstantsValue.DB.TABLE_NAME_ATTENTION + "--没有新的数据");
                    attentionIsSyncing = false;

                    context.sendBroadcast(new Intent(ConstantsValue.IntentFilterAction.SYNC_ATTENTION_DATA_SUCCESS));
                    if (null != response && null != response.data)
                        SpUtils.put(UserInfoUtil.getUserId(context) + "_" + ConstantsValue.DB.TABLE_NAME_ATTENTION + "_last_synch_time", response.data.server_synch_time);
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long startTime = System.currentTimeMillis();
                        MHLogUtil.d(TAG, "size=" + response.data.page_result.size());
                        UserInfoManager.saveUserInfos(ConstantsValue.DB.TABLE_NAME_ATTENTION, context.getApplicationContext(), response.data.page_result);
                        MHLogUtil.d(TAG, "耗时=" + (System.currentTimeMillis() - startTime));
                    }
                }).start();
                MHLogUtil.d(TAG, "syncAttentionData--继续请求--size=" + response.data.page_result.size());
                syncAttentionData(context);
            }

            @Override
            public void onFailure(String content) {
                attentionSyncFailCount++;
                syncAttentionData(context);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                attentionSyncFailCount++;
                syncAttentionData(context);
            }
        });
    }


    /**
     * 删除本地数据库存储的数据信息
     */
    public static void deleteUserData(Context context) {
        SQLiteHelper.deleteDb(context);
        SpUtils.remove((UserInfoUtil.getUserId(context) + "_" + ConstantsValue.DB.TABLE_NAME_ATTENTION + "_last_synch_time"));
        SpUtils.remove((UserInfoUtil.getUserId(context) + "_" + ConstantsValue.DB.TABLE_NAME_FANS + "_last_synch_time"));
        syncData(context);
    }

}
