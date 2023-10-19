package com.fully.code.base.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.fully.code.base.models.User;
import com.fully.code.base.persenters.courses.CoursesCallback;
import com.fully.code.base.persenters.courses.CoursesPresenter;
import com.fully.code.base.persenters.exams.ExamsCallback;
import com.fully.code.base.persenters.exams.ExamsPresenter;
import com.fully.code.base.persenters.questions.QuestionsCallback;
import com.fully.code.base.persenters.questions.QuestionsPresenter;
import com.fully.code.base.persenters.user.UsersCallback;
import com.fully.code.base.persenters.user.UsersPresenter;
import com.fully.code.base.utilities.ToastUtils;
import com.fully.code.base.utilities.helpers.StorageHelper;
import com.getit.app.R;
import com.getit.app.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements UsersCallback, CoursesCallback, QuestionsCallback, ExamsCallback {
    private FragmentHomeBinding binding;
    private UsersPresenter usersPresenter;
    private CoursesPresenter coursesPresenter;
    private QuestionsPresenter questionsPresenter;
    private ExamsPresenter examsPresenter;
    private User currentUser;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersPresenter = new UsersPresenter(this);
        coursesPresenter = new CoursesPresenter(this);
        questionsPresenter = new QuestionsPresenter(this);
        examsPresenter = new ExamsPresenter(this);
        currentUser = StorageHelper.getCurrentUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        usersPresenter.getUsersCount();
        coursesPresenter.getCoursesCount();
        questionsPresenter.getQuestionsCount();
        examsPresenter.getExamsCount();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        binding.username.setText(getString(R.string.str_welcome_message, currentUser.getFullName()));
        return binding.getRoot();
    }

    @Override
    public void onGetUsersCountComplete(long teacherCount, long studentCount) {
        binding.teachersCounter.setText(teacherCount + "");
        binding.studentsCounter.setText(studentCount + "");
    }

    @Override
    public void onGetCoursesCountComplete(long count) {
        binding.coursesCounter.setText(count + "");
    }

    @Override
    public void onGetExamsCountComplete(long count) {
        binding.examsCounter.setText(count + "");
    }

    @Override
    public void onGetQuestionsCountComplete(long count) {
        binding.questionsCounter.setText(count + "");
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        ToastUtils.longToast(message);
    }

    @Override
    public void onShowLoading() {
        ProgressDialogFragment.show(getChildFragmentManager());
    }

    @Override
    public void onHideLoading() {
        ProgressDialogFragment.hide(getChildFragmentManager());
    }
}