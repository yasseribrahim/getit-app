package com.getit.app.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.getit.app.Constants;
import com.getit.app.R;
import com.getit.app.databinding.ActivityCourseDetailsBinding;
import com.getit.app.models.Course;
import com.getit.app.models.Exam;
import com.getit.app.models.User;
import com.getit.app.persenters.exams.ExamsCallback;
import com.getit.app.persenters.exams.ExamsPresenter;
import com.getit.app.persenters.user.UsersCallback;
import com.getit.app.persenters.user.UsersPresenter;
import com.getit.app.ui.adptres.ExamsAdapter;
import com.getit.app.utilities.UIUtils;
import com.getit.app.utilities.helpers.LocaleHelper;
import com.getit.app.utilities.helpers.StorageHelper;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailsActivity extends BaseActivity implements ExamsCallback, UsersCallback, ExamsAdapter.OnExamsClickListener {
    private ActivityCourseDetailsBinding binding;
    private ExamsPresenter presenter;
    private UsersPresenter usersPresenter;
    private ExamsAdapter examsAdapter, examsSolvedAdapter;
    private List<Exam> exams, solvedExams;
    private User currentUser;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityCourseDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new ExamsPresenter(this);
        usersPresenter = new UsersPresenter(this);

        currentUser = StorageHelper.getCurrentUser();

        course = getIntent().getParcelableExtra(Constants.ARG_OBJECT);
        setUpActionBar();

        exams = new ArrayList<>();
        solvedExams = new ArrayList<>();

        binding.recyclerViewExams.setLayoutManager(new LinearLayoutManager(this));
        examsAdapter = new ExamsAdapter(exams, this);
        binding.recyclerViewExams.setAdapter(examsAdapter);

        binding.recyclerViewExamsSolved.setLayoutManager(new LinearLayoutManager(this));
        examsSolvedAdapter = new ExamsAdapter(solvedExams, this);
        binding.recyclerViewExamsSolved.setAdapter(examsSolvedAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    @SuppressLint("WrongConstant")
    private void setUpActionBar() {
        binding.appBarLayout.toolbar.setTitle(course.getName() + "-" + UIUtils.getGrade(course.getGrade()));
        setSupportActionBar(binding.appBarLayout.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        binding.appBarLayout.toolbar.setNavigationIcon(R.drawable.back_to_home_button);
    }

    @Override
    public void onGetExamsComplete(List<Exam> exams) {
        this.exams.clear();
        this.exams.addAll(exams);

        refreshLists();
    }

    private void refreshLists() {
        binding.messageExamsEmpty.setVisibility(exams.isEmpty() ? View.VISIBLE : View.GONE);
        examsAdapter.notifyDataSetChanged();

        binding.messageExamsSolvedEmpty.setVisibility(exams.isEmpty() ? View.VISIBLE : View.GONE);
        examsSolvedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetUserComplete(User user) {
        currentUser = user;
        StorageHelper.setCurrentUser(user);

        solvedExams.clear();
        currentUser.getExams().forEach(e -> {
            solvedExams.add(e.getExam());
        });

        refreshLists();
    }

    private void load() {
        usersPresenter.getUserById(currentUser.getId());
        presenter.getExams(course.getGrade());
    }

    @Override
    public void onExamViewListener(Exam exam) {
        Intent intent = new Intent(this, ExamDetailsActivity.class);
        intent.putExtra(Constants.ARG_OBJECT, exam);
        startActivity(intent);
    }

    @Override
    public void onExamEditListener(int position) {

    }

    @Override
    public void onExamDeleteListener(int position) {

    }
}