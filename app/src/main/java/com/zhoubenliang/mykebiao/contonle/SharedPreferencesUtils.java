package com.zhoubenliang.mykebiao.contonle;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作者:Administrator on 2016/3/21.
 * 邮箱:554524787@qq.com
 */
public class SharedPreferencesUtils {
    public static String getString(String key, Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        String string = sharedPreferences.getString(key, null);
        return string;
    }
    public static void putString(Context context,String key,String value){
        SharedPreferences sharedPreferences=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
}
