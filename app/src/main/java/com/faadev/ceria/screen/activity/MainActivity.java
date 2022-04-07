package com.faadev.ceria.screen.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.CategoryAdapter;
import com.faadev.ceria.adapter.ItemCLickListener;
import com.faadev.ceria.adapter.TabAdapter;
import com.faadev.ceria.screen.fragment.DisimpanFragment;
import com.faadev.ceria.screen.fragment.DisukaiFragment;
import com.faadev.ceria.screen.fragment.PopularFragment;
import com.faadev.ceria.screen.fragment.TerbaruFragment;
import com.faadev.ceria.model.CategoryModel;
import com.faadev.ceria.utils.LoginRequired;
import com.faadev.ceria.utils.SlidingRootNavBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.yarolegovich.slidingrootnav.SlidingRootNav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView menu;
    private NavigationView nav_view;
    private TabLayout tab;
    private TabAdapter adapter;
    private ViewPager vp;
    private RecyclerView rv_category;
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> cmod;
    private ItemCLickListener itemCLickListener;
    private String category = "";
    private FloatingActionButton fabAdd;
    private SlidingRootNav sliding;
    private CardView drawerLayout;


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
                this.getWindow().setNavigationBarColor(Color.TRANSPARENT);
            }
            main.setSystemUiVisibility(flags);
            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        sliding = new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.nav_header_main)
                .withDragDistance(140)
                .withRootViewElevation(25)
                .withRootViewScale(0.9f)
                .withRootViewXTranslation(100, this)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .inject();

        _init();
        _prep();

    }

    private void _init(){
        tab = findViewById(R.id.tab);
        vp = findViewById(R.id.vp);
//        nav_view = findViewById(R.id.nav_drawer);
        menu = findViewById(R.id.menu_btn);
        rv_category = findViewById(R.id.rv_category);
        fabAdd = findViewById(R.id.fab_add);
    }

    private void _prep(){
        menu.setOnClickListener(view -> {
            sliding.openMenu();
        });

//        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        menu.setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));
//
//        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    View main = findViewById(R.id.container);
//                    int flags = main.getSystemUiVisibility();
//                    flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
//                        getWindow().setNavigationBarColor(Color.WHITE);
//                    }
//                    main.setSystemUiVisibility(flags);
//                    getWindow().setStatusBarColor(Color.TRANSPARENT);
//                }
//            }
//
//            @Override
//            public void onDrawerOpened(@NonNull View drawerView) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    View main = findViewById(R.id.container);
//                    int flags = main.getSystemUiVisibility();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
//                        getWindow().setNavigationBarColor(Color.WHITE);
//                    }
//                    main.setSystemUiVisibility(flags);
//                    getWindow().setStatusBarColor(Color.TRANSPARENT);
//                }
//            }
//
//            @Override
//            public void onDrawerClosed(@NonNull View drawerView) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    View main = findViewById(R.id.container);
//                    int flags = main.getSystemUiVisibility();
//                    flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
//                        getWindow().setNavigationBarColor(Color.WHITE);
//                    }
//                    main.setSystemUiVisibility(flags);
//                    getWindow().setStatusBarColor(Color.WHITE);
//                }
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//
//            }
//        });

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new TerbaruFragment(), "Terbaru");
        adapter.addFragment(new PopularFragment(), "Popular");
        adapter.addFragment(new DisukaiFragment(), "Disukai");
        adapter.addFragment(new DisimpanFragment(), "Disimpan");
        vp.setAdapter(adapter);
        tab.setupWithViewPager(vp);

        cmod = new ArrayList<>();
        cmod.add(new CategoryModel("Cerpen", true));
        cmod.add(new CategoryModel("Novel", false));
        cmod.add(new CategoryModel("Puisi", false));
        cmod.add(new CategoryModel("Fabel", false));
        cmod.add(new CategoryModel("Dongeng", false));
        cmod.add(new CategoryModel("Cerita Rakyat", false));
        cmod.add(new CategoryModel("Agama", false));

        itemCLickListener = new ItemCLickListener() {
            @Override
            public void onItemClick(String param) {
                category = param;
            }
        };

        categoryAdapter = new CategoryAdapter(this, cmod, itemCLickListener);
        rv_category.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_category.setAdapter(categoryAdapter);

        fabAdd.setOnClickListener(v -> {
            LoginRequired.start(this, new Intent(this, CreatePostActivity.class));
        });
    }
}
