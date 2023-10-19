package com.fully.code.base.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.fully.code.base.utilities.helpers.LocaleHelper;
import com.getit.app.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends BaseActivity {
    private static final int SPLASH_DELAY_MILLIS = 2000;
    private ActivitySplashBinding binding;
    private Handler handler;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isOpenHome()) {
                openHome();
            } else {
                openLogin();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        handler = new Handler();
        openApp();
    }

    private boolean isOpenHome() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        return user != null;
    }

    private void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private void openApp() {
        handler.postDelayed(runnable, SPLASH_DELAY_MILLIS);
    }
}