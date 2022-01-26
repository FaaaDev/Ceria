package com.faadev.ceria.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.faadev.ceria.http.response.UserData;
import com.google.gson.Gson;

public class Preferences {

    static final String KEY_USER_TEREGISTER ="ikikeykanggousersengweslogin!siaulahkepo";


    private static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveUser(UserData user, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();

        Gson gson = new Gson();

        String json = gson.toJson(user);

        editor.putString(KEY_USER_TEREGISTER, json);
        editor.apply();

        System.out.println("JSON ==== "+json);
    }

    public static String getUsername(Context context){
        Gson gson = new Gson();
        UserData user = gson.fromJson(getSharedPreference(context).getString(KEY_USER_TEREGISTER,""), UserData.class);

        return user.getUser().getName();
    }

    public static String getRole(Context context){
        Gson gson = new Gson();
        UserData user = gson.fromJson(getSharedPreference(context).getString(KEY_USER_TEREGISTER,""), UserData.class);

        return user.getUser().getRole();
    }

    public static String getImage(Context context){
        Gson gson = new Gson();
        UserData user = gson.fromJson(getSharedPreference(context).getString(KEY_USER_TEREGISTER,""), UserData.class);

        return user.getUser().getImage();
    }

    public static String getEmail(Context context){
        Gson gson = new Gson();
        UserData user = gson.fromJson(getSharedPreference(context).getString(KEY_USER_TEREGISTER,""), UserData.class);

        return user.getUser().getEmail();
    }

    public static String getToken(Context context){
        Gson gson = new Gson();
        UserData user = gson.fromJson(getSharedPreference(context).getString(KEY_USER_TEREGISTER,""), UserData.class);

        return user.getToken();
    }

    public static int getId(Context context) {
        Gson gson = new Gson();
        UserData user = gson.fromJson(getSharedPreference(context).getString(KEY_USER_TEREGISTER,""), UserData.class);

        return user.getUser().getId();
    }

    public static Boolean isLogedIn(Context context){
        return !TextUtils.isEmpty(getSharedPreference(context).getString(KEY_USER_TEREGISTER,""));
    }


    public static void clearLoggedInUser (Context context){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(KEY_USER_TEREGISTER);
        editor.apply();
    }
}