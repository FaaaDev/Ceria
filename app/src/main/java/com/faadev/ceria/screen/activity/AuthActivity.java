package com.faadev.ceria.screen.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.TabAdapter;
import com.faadev.ceria.databinding.ActivityAuthBinding;
import com.faadev.ceria.screen.fragment.LoginFragment;
import com.faadev.ceria.screen.fragment.RegisterFragment;

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
        binding.close.setOnClickListener(v -> {
            if(getCurrentFocus()!=null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            finish();
        });
    }

}