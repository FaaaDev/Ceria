package com.faadev.ceria.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

import com.faadev.ceria.R;
import com.yarolegovich.slidingrootnav.transform.RootTransformation;
import com.yarolegovich.slidingrootnav.util.SideNavUtils;

public class XTranslationTransformation implements RootTransformation {

    private static final float START_TRANSLATION = 0f;

    private final float endTranslation;
    private final Activity activity;

    public XTranslationTransformation(float endTranslation, Activity activity) {
        this.endTranslation = endTranslation;
        this.activity = activity;
    }

    @Override
    public void transform(float dragProgress, View rootView) {
        float translation = SideNavUtils.evaluate(dragProgress, START_TRANSLATION, endTranslation);
//        rootView.setTranslationX(translation);
        if(translation == 0) {
            rootView.setBackgroundResource(R.drawable.root_default);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int flags = rootView.getSystemUiVisibility();
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                    activity.getWindow().setNavigationBarColor(Color.WHITE);
                }
                rootView.setSystemUiVisibility(flags);
                activity.getWindow().setStatusBarColor(Color.WHITE);
            }
        } else {
            rootView.setBackgroundResource(R.drawable.root_slide);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int flags = rootView.getSystemUiVisibility();
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                    activity.getWindow().setNavigationBarColor(Color.argb(255, 58, 173, 177));
                }
                rootView.setSystemUiVisibility(flags);
                activity.getWindow().setStatusBarColor(Color.argb(255, 58, 173, 177));
            }
        }
    }
}