package com.faadev.ceria.screen.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.CardAdapter;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.ProfileIdResponse;
import com.faadev.ceria.model.User;
import com.faadev.ceria.screen.ui.notification.NotificationFragment;
import com.faadev.ceria.screen.ui.profile.ProfileFragment;
import com.faadev.ceria.screen.ui.saldoku.SaldoFragment;
import com.faadev.ceria.screen.ui.home.HomeFragment;
import com.faadev.ceria.screen.ui.my_post.MyPostFragment;
import com.faadev.ceria.screen.ui.settings.SettingsFragment;
import com.faadev.ceria.utils.DismissListener;
import com.faadev.ceria.utils.GlideApp;
import com.faadev.ceria.utils.Preferences;
import com.faadev.ceria.utils.ShowDialog;
import com.faadev.ceria.utils.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.SlidingRootNav;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements DismissListener {

    private CardView menu, edit;
    private SlidingRootNav sliding;
    private CardView home, saldo, postingan, notifikasi, pengaturan, dark_mode, logout, profileInfo, profileImageContainer;
    private boolean isDark = false;
    private ImageView dark_toggle, profile_image, profile_image_small;
    private FragmentTransaction ft;
    private String currentFragment = "home";
    private TextView username, email;
    private ApiService apiService;
    private User user;


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
        profileInfo = findViewById(R.id.profile_info);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        profileImageContainer = findViewById(R.id.profile_image_container);
        edit = findViewById(R.id.edit_btn);
        profile_image = findViewById(R.id.profile_image);
        profile_image_small = findViewById(R.id.profile_image_small);
    }

    private void _prep() {
        if (Preferences.isLogedIn(getApplicationContext())) {
            logout.setVisibility(View.VISIBLE);
            profileInfo.setVisibility(View.VISIBLE);
            username.setText(Preferences.getUsername(getApplicationContext()));
            email.setText(Preferences.getEmail(getApplicationContext()));
            if (Preferences.getImage(getApplicationContext()) != null) {
                GlideApp.with(getApplicationContext())
                        .load(Preferences.getImage(getApplicationContext()))
                        .into(profile_image);
                GlideApp.with(getApplicationContext())
                        .load(Preferences.getImage(getApplicationContext()))
                        .into(profile_image_small);
            }
        } else {
            logout.setVisibility(View.GONE);
            profileInfo.setVisibility(View.GONE);
        }

        menu.setOnClickListener(view -> {
            sliding.openMenu();
        });
        edit.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
            intent.putExtra("data", user);
            startActivity(intent);
        });
        profileImageContainer.setOnClickListener(v -> {
            if (Preferences.isLogedIn(getApplicationContext())) {
                startActivity(new Intent(getApplicationContext(), ProfileDetailActivity.class));
            } else {
                startActivity(new Intent(this, AuthActivity.class));
            }
        });

        profileInfo.setOnClickListener(v -> {
            navigateDrawer(new ProfileFragment(), "profile", true);
        });
        home.setOnClickListener(v -> {
            navigateDrawer(new HomeFragment(), "home", false);
        });
        saldo.setOnClickListener(v -> {
            navigateDrawer(new SaldoFragment(), "saldo", true);
        });
        postingan.setOnClickListener(v -> {
            navigateDrawer(new MyPostFragment(), "post", true);
        });
        notifikasi.setOnClickListener(v -> {
            navigateDrawer(new NotificationFragment(), "notification", false);
        });
        pengaturan.setOnClickListener(v -> {
            navigateDrawer(new SettingsFragment(), "settings", true);
        });
        dark_mode.setOnClickListener(v -> {
            isDark = !isDark;
            Handler hh = new Handler();
            hh.postDelayed(() -> {
                dark_toggle.setImageResource(isDark ? R.drawable.ic_toggle_on : R.drawable.ic_toggle_off);
            }, 500);
        });
        logout.setOnClickListener(v -> {
            if (Preferences.isLogedIn(getApplicationContext())) {
                Handler hh1 = new Handler();
                hh1.postDelayed(() -> {
                    sliding.closeMenu();
                    ShowDialog.showConfirmCancel(getSupportFragmentManager(), "Yakin mau keluar ?");
                }, 100);
            }
        });
    }

    private void navigateDrawer(Fragment fragment, String tag, boolean loginRequired) {
        if (loginRequired) {
            if (Preferences.isLogedIn(this)) {
                Handler hh1 = new Handler();
                hh1.postDelayed(() -> {
                    sliding.closeMenu();
                }, 100);
                if (!currentFragment.equals(tag)) {
                    currentFragment = tag;
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_frame, fragment);
                    Handler hh = new Handler();
                    hh.postDelayed(() -> {
                        ft.commit();
                    }, 100);
                }
            } else {
                Handler hh1 = new Handler();
                hh1.postDelayed(() -> {
                    sliding.closeMenu();
                    startActivity(new Intent(this, AuthActivity.class));
                }, 100);
            }
        } else {
            Handler hh1 = new Handler();
            hh1.postDelayed(() -> {
                sliding.closeMenu();
            }, 100);
            if (!currentFragment.equals(tag)) {
                currentFragment = tag;
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_frame, fragment);
                Handler hh = new Handler();
                hh.postDelayed(() -> {
                    ft.commit();
                }, 100);
            }
        }

        if (currentFragment.equals("profile")) {
            profileImageContainer.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            getProfile();
        } else {
            profileImageContainer.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        _prep();
    }

    private void getProfile() {
        apiService = new ApiService(getApplicationContext());
        apiService.getUserId(Preferences.getId(getApplicationContext()), new Callback<ProfileIdResponse>() {
            @Override
            public void onResponse(Call<ProfileIdResponse> call, Response<ProfileIdResponse> response) {
                if (response.body().getCode() == 200) {
                    user = response.body().getData();
                } else {
                    ShowDialog.showError(getSupportFragmentManager(), response.body().getCode(), "Error " + response.code() + "-Gagal medapatkan data");
                }
            }

            @Override
            public void onFailure(Call<ProfileIdResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                ShowDialog.showError(getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
        });
    }

    @Override
    public void onDismissSheet(String from) {
        if (from.equals("confirmcancel")) {
            Preferences.clearLoggedInUser(getApplicationContext());
            logout.setVisibility(View.GONE);
            profileInfo.setVisibility(View.GONE);
        }
    }
}
