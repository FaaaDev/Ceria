package com.faadev.ceria.utils;

import android.content.Context;
import android.content.Intent;

import com.faadev.ceria.screen.activity.AuthActivity;

public class LoginRequired {

    public static void start(Context context, Intent intent){
        if (Preferences.isLogedIn(context)){
            context.startActivity(intent);
        } else {
            context.startActivity(new Intent(context, AuthActivity.class));
        }
    }

}
