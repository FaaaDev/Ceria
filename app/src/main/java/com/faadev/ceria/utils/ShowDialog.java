package com.faadev.ceria.utils;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import com.faadev.ceria.screen.fragment.ErrorFragment;

public class ShowDialog {

    public static void showError(FragmentManager fragmentManager,int code, String message){
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putString("code", String.valueOf(code));
        ErrorFragment errorFragment = new ErrorFragment();
        errorFragment.setArguments(bundle);
        errorFragment.show(fragmentManager, errorFragment.getTag());
    }
}
