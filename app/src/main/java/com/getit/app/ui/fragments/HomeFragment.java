package com.getit.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.getit.app.R;
import com.getit.app.databinding.FragmentHomeBinding;
import com.getit.app.models.User;
import com.getit.app.persenters.courses.CoursesCallback;
import com.getit.app.persenters.courses.CoursesPresenter;
import com.getit.app.persenters.exams.ExamsCallback;
import com.getit.app.persenters.exams.ExamsPresenter;
import com.getit.app.persenters.oldquestions.QuestionsCallback;
import com.getit.app.persenters.oldquestions.QuestionsPresenter;
import com.getit.app.persenters.user.UsersCallback;
import com.getit.app.persenters.user.UsersPresenter;
import com.getit.app.utilities.ToastUtils;
import com.getit.app.utilities.helpers.StorageHelper;

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