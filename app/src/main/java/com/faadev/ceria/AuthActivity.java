package com.faadev.ceria;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.faadev.ceria.adapter.TabAdapter;
import com.faadev.ceria.databinding.ActivityAuthBinding;
import com.faadev.ceria.fragment.DisukaiFragment;
import com.faadev.ceria.fragment.LoginFragment;
import com.faadev.ceria.fragment.PopularFragment;
import com.faadev.ceria.fragment.RegisterFragment;
import com.faadev.ceria.fragment.TerbaruFragment;

public class AuthActivity extends AppCompatActivity {

   private ActivityAuthBinding binding;
   private boolean isLoading = false;
   private TabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View main = findViewById(R.id.container);
            int flags = main.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                this.getWindow().setNavigationBarColor(Color.WHITE);
            }
            main.setSystemUiVisibility(flags);
            this.getWindow().setStatusBarColor(Color.argb(255, 0, 0, 23));
        }

        implementation();
    }

    private void implementation(){
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginFragment(), "Masuk");
        adapter.addFragment(new RegisterFragment(), "Daftar");
        binding.vp.setAdapter(adapter);
        binding.tab.setupWithViewPager(binding.vp);


    }

}