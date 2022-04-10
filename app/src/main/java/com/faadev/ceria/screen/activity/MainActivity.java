package com.faadev.ceria.screen.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.CategoryAdapter;
import com.faadev.ceria.adapter.ItemCLickListener;
import com.faadev.ceria.adapter.TabAdapter;
import com.faadev.ceria.model.CategoryModel;
import com.faadev.ceria.screen.ui.saldoku.SaldoFragment;
import com.faadev.ceria.screen.ui.home.HomeFragment;
import com.faadev.ceria.screen.ui.notifications.NotificationsFragment;
import com.faadev.ceria.utils.SlidingRootNavBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.yarolegovich.slidingrootnav.SlidingRootNav;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CardView menu;
    private SlidingRootNav sliding;
    private CardView home, saldo, postingan, notifikasi, pengaturan, dark_mode, logout;
    private boolean isDark = false;
    private ImageView dark_toggle;
    private FragmentTransaction ft;
    private String currentFragment = "home";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View main = findViewById(R.id.container);
            int flags = main.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                this.getWindow().setNavigationBarColor(Color.WHITE);
            }
            main.setSystemUiVisibility(flags);
            this.getWindow().setStatusBarColor(Color.WHITE);
        }


        sliding = new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.nav_header_main)
                .withDragDistance(250)
                .withRootViewElevation(25)
                .withRootViewScale(0.9f)
                .withRootViewXTranslation(100, this)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .inject();

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frame, new HomeFragment());
        ft.commit();

        _init();
        _prep();

    }

    private void _init() {
        menu = findViewById(R.id.menu_btn);
        home = findViewById(R.id.home);
        saldo = findViewById(R.id.saldo);
        postingan = findViewById(R.id.posting);
        notifikasi = findViewById(R.id.notifikasi);
        pengaturan = findViewById(R.id.pengaturan);
        dark_mode = findViewById(R.id.dark_mode);
        logout = findViewById(R.id.logout);
        dark_toggle = findViewById(R.id.dark_toggle);
    }

    private void _prep() {
        menu.setOnClickListener(view -> {
            sliding.openMenu();
        });

        home.setOnClickListener(v -> {
            navigateDrawer(new HomeFragment(), "home");
        });
        saldo.setOnClickListener(v -> {
            navigateDrawer(new SaldoFragment(), "saldo");
        });
        postingan.setOnClickListener(v -> {
            navigateDrawer(new NotificationsFragment(), "post");
        });
        notifikasi.setOnClickListener(v -> {
//            navigateDrawer();
        });
        pengaturan.setOnClickListener(v -> {
//            navigateDrawer();
        });
        dark_mode.setOnClickListener(v -> {
            isDark = !isDark;
            Handler hh = new Handler();
            hh.postDelayed(() -> {
                dark_toggle.setImageResource(isDark ? R.drawable.ic_toggle_on : R.drawable.ic_toggle_off);
            }, 500);
        });
        logout.setOnClickListener(v -> {

        });
    }

    private void navigateDrawer(Fragment fragment, String tag) {
        Handler hh1 = new Handler();
        hh1.postDelayed(() -> {
            sliding.closeMenu();
        }, 100);
        if(!currentFragment.equals(tag)) {
            currentFragment = tag;
            ft = getSupportFragmentManager().beginTransaction();
//            ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
            ft.replace(R.id.main_frame, fragment);
            Handler hh = new Handler();
            hh.postDelayed(() -> {
                ft.commit();
            }, 100);
        }
    }
}
