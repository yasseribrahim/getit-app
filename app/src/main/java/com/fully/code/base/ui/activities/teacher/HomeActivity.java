package com.fully.code.base.ui.activities.teacher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.fully.code.base.Constants;
import com.fully.code.base.models.User;
import com.fully.code.base.ui.fragments.CoursesFragment;
import com.fully.code.base.ui.fragments.ExamsFragment;
import com.fully.code.base.ui.fragments.HomeFragment;
import com.fully.code.base.ui.fragments.MoreFragment;
import com.fully.code.base.ui.fragments.QuestionsFragment;
import com.fully.code.base.ui.fragments.UsersFragment;
import com.fully.code.base.utilities.helpers.StorageHelper;
import com.getit.app.R;
import com.getit.app.databinding.ActivityHomeTeacherBinding;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeTeacherBinding binding;
    private MenuItem previousItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeTeacherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpActionBar();
        setupNavDrawer();
    }

    @SuppressLint("WrongConstant")
    private void setupNavDrawer() {
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (previousItem == null || previousItem.getItemId() != item.getItemId()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            switch (item.getItemId()) {
                                case R.id.nav_home:
                                    showFragment(HomeFragment.newInstance(), R.id.container);
                                    binding.content.toolbar.setTitle(R.string.menu_home);
                                    break;
                                case R.id.nav_teachers:
                                    showFragment(UsersFragment.newInstance(Constants.USER_TYPE_TEACHER), R.id.container);
                                    binding.content.toolbar.setTitle(R.string.menu_teachers);
                                    break;
                                case R.id.nav_students:
                                    showFragment(UsersFragment.newInstance(Constants.USER_TYPE_STUDENT), R.id.container);
                                    binding.content.toolbar.setTitle(R.string.menu_students);
                                    break;
                                case R.id.nav_courses:
                                    showFragment(CoursesFragment.newInstance(), R.id.container);
                                    binding.content.toolbar.setTitle(R.string.menu_courses);
                                    break;
                                case R.id.nav_questions:
                                    showFragment(QuestionsFragment.newInstance(), R.id.container);
                                    binding.content.toolbar.setTitle(R.string.menu_questions);
                                    break;
                                case R.id.nav_exams:
                                    showFragment(ExamsFragment.newInstance(), R.id.container);
                                    binding.content.toolbar.setTitle(R.string.menu_exams);
                                    break;
                                case R.id.nav_more:
                                    showFragment(MoreFragment.newInstance(), R.id.container);
                                    binding.content.toolbar.setTitle(R.string.menu_more);
                                    break;
                            }
                        }
                    }, 300);
                    if (previousItem != null) {
                        previousItem.setChecked(false);
                    }
                    item.setChecked(true);
                    previousItem = item;
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        binding.navigationView.setCheckedItem(R.id.nav_home);
        showFragment(HomeFragment.newInstance(), R.id.container);
        binding.content.toolbar.setTitle(R.string.menu_home);

        CircleImageView profileImage = binding.navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        TextView name = binding.navigationView.getHeaderView(0).findViewById(R.id.full_name);
        TextView username = binding.navigationView.getHeaderView(0).findViewById(R.id.username);
        User user = StorageHelper.getCurrentUser();
        if (user != null) {
            name.setText(user.getFullName());
            username.setText(user.getUsername());
            Glide.with(this).load(user.getImageProfile()).placeholder(R.drawable.ic_profile).into(profileImage);
        }
    }

    //Set up Action Bar
    @SuppressLint("WrongConstant")
    private void setUpActionBar() {
        binding.content.toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(binding.content.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        binding.content.toolbar.setNavigationIcon(R.drawable.ic_menu);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (binding.drawerLayout.isDrawerOpen(Gravity.START)) {
                    binding.drawerLayout.closeDrawer(Gravity.START);
                } else {
                    binding.drawerLayout.openDrawer(Gravity.START);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showFragment(Fragment fragment, int fragmentContainer) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer, fragment);
        transaction.commitAllowingStateLoss();
    }
}