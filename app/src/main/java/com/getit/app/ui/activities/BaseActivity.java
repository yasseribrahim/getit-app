package com.getit.app.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.getit.app.Constants;
import com.getit.app.CustomApplication;
import com.getit.app.models.User;
import com.getit.app.persenters.firebase.FirebaseCallback;
import com.getit.app.persenters.firebase.FirebasePresenter;
import com.getit.app.ui.fragments.ProgressDialogFragment;
import com.getit.app.utilities.ToastUtils;
import com.getit.app.utilities.helpers.LocaleHelper;
import com.getit.app.utilities.helpers.StorageHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {
    protected SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkFirebase();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home -> finish();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void checkFirebase() {
        try {
            if (FirebaseAuth.getInstance().getCurrentUser() != null && StorageHelper.getCurrentUser() != null) {
                FirebasePresenter presenter = new FirebasePresenter(new FirebaseCallback() {
                    @Override
                    public void onFailure(String message, View.OnClickListener listener) {

                    }

                    @Override
                    public void onShowLoading() {

                    }

                    @Override
                    public void onHideLoading() {

                    }
                });
                presenter.saveToken(StorageHelper.getCurrentUser());
            }
        } catch (Exception ex) {
        }
    }

    protected Locale getCurrentLanguage() {
        try {
            if (preferences == null) {
                preferences = PreferenceManager.getDefaultSharedPreferences(CustomApplication.getApplication());
            }
            return new Locale(preferences.getString("language", Locale.getDefault().getLanguage()));
        } catch (Exception ex) {
        }
        return Locale.getDefault();
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleHelper.onAttach(context, getCurrentLanguage().getLanguage()));
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void openHome() {
        User user = StorageHelper.getCurrentUser();
        Intent intent = new Intent(this, LoginActivity.class);
        if (user != null) {
            switch (user.getType()) {
                case Constants.USER_TYPE_ADMIN ->
                        intent = new Intent(this, com.getit.app.ui.activities.admin.HomeActivity.class);
                case Constants.USER_TYPE_TEACHER ->
                        intent = new Intent(this, com.getit.app.ui.activities.teacher.HomeActivity.class);
                case Constants.USER_TYPE_STUDENT ->
                        intent = new Intent(this, com.getit.app.ui.activities.student.HomeActivity.class);
                default ->
                        Toast.makeText(this, "This user not support from app, Please contact with admin", Toast.LENGTH_LONG).show();
            }
        }
        startActivity(intent);
        finishAffinity();
    }

    public void onShowLoading() {
        ProgressDialogFragment.show(getSupportFragmentManager());
    }

    public void onHideLoading() {
        ProgressDialogFragment.hide(getSupportFragmentManager());
    }

    public void onFailure(String message, View.OnClickListener listener) {
        ToastUtils.longToast(message);
    }
}
