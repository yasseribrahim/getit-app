package com.fully.code.base.ui.activities.admin;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fully.code.base.Constants;
import com.fully.code.base.models.Course;
import com.fully.code.base.persenters.courses.CoursesCallback;
import com.fully.code.base.persenters.courses.CoursesPresenter;
import com.fully.code.base.ui.activities.BaseActivity;
import com.fully.code.base.ui.fragments.GradeSelectorBottomSheet;
import com.fully.code.base.utilities.UIUtils;
import com.fully.code.base.utilities.helpers.LocaleHelper;
import com.getit.app.R;
import com.getit.app.databinding.ActivityCourseBinding;

public class CourseActivity extends BaseActivity implements CoursesCallback, GradeSelectorBottomSheet.ItemClickListener {
    private ActivityCourseBinding binding;

    private CoursesPresenter presenter;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new CoursesPresenter(this);

        course = getIntent().getParcelableExtra(Constants.ARG_OBJECT);
        if(course == null) {
            course = new Course();
        }
        bind();

        binding.grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradeSelectorBottomSheet.newInstance(course.getGrade()).show(getSupportFragmentManager(), "");
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.name.getText().toString().trim();

                if (name.isEmpty()) {
                    binding.name.setError(getString(R.string.str_name_invalid));
                    binding.name.requestFocus();
                    return;
                }
                if (course.getGrade() <= 0) {
                    binding.grade.setError(getString(R.string.str_grade_invalid));
                    binding.grade.requestFocus();
                    return;
                }

                course.setName(name);
                course.setCreatedAt(Calendar.getInstance().getTime());
                presenter.save(course);
            }
        });
    }

    @Override
    public void onSaveCourseComplete() {
        Toast.makeText(this, R.string.str_message_added_successfully, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        Toast.makeText(this, getString(R.string.str_save_fail, message), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGradeClick(int id) {
        course.setGrade(id);
        binding.grade.setText(UIUtils.getGrade(id));
    }

    private void bind() {
        binding.name.setText(course.getName());
        binding.grade.setText(UIUtils.getGrade(course.getGrade()));
    }
}