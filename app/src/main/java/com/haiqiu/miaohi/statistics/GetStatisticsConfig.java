package com.haiqiu.miaohi.statistics;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by LiuXiang on 2016/11/16.
 */
public class GetStatisticsConfig {
    private static final String a = "general_config";
    public static boolean ACTIVITY_DURATION_OPEN = true;

    private GetStatisticsConfig() {
    }

    public static SharedPreferences getNetConfig(Context context, String configName) {
        return context.getSharedPreferences(configName, 0);
    }

    public static SharedPreferences getGeneralConfig(Context context) {
        return context.getSharedPreferences("general_config", 0);
    }
}
