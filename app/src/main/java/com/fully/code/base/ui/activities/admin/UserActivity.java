package com.fully.code.base.ui.activities.admin;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.fully.code.base.Constants;
import com.fully.code.base.models.User;
import com.fully.code.base.persenters.user.UsersCallback;
import com.fully.code.base.persenters.user.UsersPresenter;
import com.fully.code.base.ui.activities.BaseActivity;
import com.fully.code.base.ui.fragments.GradeSelectorBottomSheet;
import com.fully.code.base.utilities.UIUtils;
import com.fully.code.base.utilities.helpers.LocaleHelper;
import com.getit.app.R;
import com.getit.app.databinding.ActivityUserBinding;

public class UserActivity extends BaseActivity implements UsersCallback, GradeSelectorBottomSheet.ItemClickListener {
    private ActivityUserBinding binding;

    private UsersPresenter presenter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new UsersPresenter(this);

        if(getIntent().getExtras().containsKey(Constants.ARG_OBJECT)) {
            user = getIntent().getParcelableExtra(Constants.ARG_OBJECT);
        } else {
            user = new User();
            int userType = getIntent().getIntExtra(Constants.ARG_ID, Constants.USER_TYPE_STUDENT);
            user.setType(userType);
        }
        bind();

        binding.grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradeSelectorBottomSheet.newInstance(user.getGrade()).show(getSupportFragmentManager(), "");
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = binding.password.getText().toString().trim();
                String username = binding.username.getText().toString().trim();
                String phone = binding.phone.getText().toString().trim();
                String address = binding.address.getText().toString().trim();
                String fullName = binding.fullName.getText().toString().trim();

                if (user.getId() == null) {
                    if (password.isEmpty() || password.length() < 6) {
                        binding.password.setError(getString(R.string.str_password_length_invalid));
                        binding.password.requestFocus();
                        return;
                    }

                    if (username.isEmpty()) {
                        binding.username.setError(getString(R.string.str_username_invalid));
                        binding.username.requestFocus();
                        return;
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                        binding.username.setError(getString(R.string.str_username_invalid));
                        binding.username.requestFocus();
                        return;
                    }
                }

                if (fullName.isEmpty()) {
                    binding.phone.setError(getString(R.string.str_full_name_invalid));
                    binding.phone.requestFocus();
                    return;
                }
                if (phone.isEmpty()) {
                    binding.fullName.setError(getString(R.string.str_phone_invalid));
                    binding.fullName.requestFocus();
                    return;
                }
                if (address.isEmpty()) {
                    binding.address.setError(getString(R.string.str_address_invalid));
                    binding.address.requestFocus();
                    return;
                }
                if (user.getGrade() <= 0) {
                    binding.grade.setError(getString(R.string.str_grade_invalid));
                    binding.grade.requestFocus();
                    return;
                }

                user.setUsername(username);
                user.setPassword(password);
                user.setFullName(fullName);
                user.setPhone(phone);
                user.setAddress(address);

                if(user.getId() == null) {
                    presenter.signup(user);
                } else {
                    presenter.save(user);
                }
            }
        });
    }

    @Override
    public void onSaveUserComplete() {
        Toast.makeText(this, R.string.str_message_added_successfully, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onSignupUserComplete() {
        Toast.makeText(this, R.string.str_message_added_successfully, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onSignupUserFail(String message) {
        Toast.makeText(this, getString(R.string.str_save_fail, message), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        Toast.makeText(this, getString(R.string.str_save_fail, message), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGradeClick(int id) {
        user.setGrade(id);
        binding.grade.setText(UIUtils.getGrade(id));
    }

    private void bind() {
        binding.type.setText(UIUtils.getAccountType(user.getType()));
        binding.username.setEnabled(user.getId() == null);
        binding.password.setEnabled(user.getId() == null);

        binding.username.setText(user.getUsername());
        binding.password.setText(user.getPassword());
        binding.fullName.setText(user.getFullName());
        binding.address.setText(user.getAddress());
        binding.phone.setText(user.getPhone());
        binding.grade.setText(UIUtils.getGrade(user.getGrade()));
    }
}