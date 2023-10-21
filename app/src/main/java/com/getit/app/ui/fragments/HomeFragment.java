package com.getit.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.getit.app.R;
import com.getit.app.databinding.FragmentHomeBinding;
import com.getit.app.models.User;
import com.getit.app.persenters.lesson.LessonsCallback;
import com.getit.app.persenters.lesson.LessonsPresenter;
import com.getit.app.persenters.questions.QuestionsCallback;
import com.getit.app.persenters.questions.QuestionsPresenter;
import com.getit.app.persenters.unit.UnitsCallback;
import com.getit.app.persenters.unit.UnitsPresenter;
import com.getit.app.persenters.user.UsersCallback;
import com.getit.app.persenters.user.UsersPresenter;
import com.getit.app.utilities.ToastUtils;
import com.getit.app.utilities.helpers.StorageHelper;

public class HomeFragment extends Fragment implements UsersCallback, UnitsCallback, LessonsCallback, QuestionsCallback {
    private FragmentHomeBinding binding;
    private UsersPresenter usersPresenter;
    private UnitsPresenter unitsPresenter;
    private LessonsPresenter lessonsPresenter;
    private QuestionsPresenter questionsPresenter;
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
        unitsPresenter = new UnitsPresenter(this);
        lessonsPresenter = new LessonsPresenter(this);
        questionsPresenter = new QuestionsPresenter(this);
        currentUser = StorageHelper.getCurrentUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        usersPresenter.getUsersCount();
        unitsPresenter.getUnitsCount();
        lessonsPresenter.getLessonsCount();
        questionsPresenter.getQuestionsCount();
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
    public void onGetUnitsCountComplete(long count) {
        binding.unitsCounter.setText(count + "");
    }

    @Override
    public void onGetLessonsCountComplete(long count) {
        binding.lessonsCounter.setText(count + "");
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