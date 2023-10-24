package com.getit.app.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.getit.app.Constants;
import com.getit.app.R;
import com.getit.app.databinding.FragmentCorrectionBinding;
import com.getit.app.models.Correction;
import com.getit.app.models.Grade;
import com.getit.app.models.Lesson;
import com.getit.app.models.Unit;
import com.getit.app.models.User;
import com.getit.app.persenters.answer.AnswersCallback;
import com.getit.app.persenters.answer.AnswersPresenter;
import com.getit.app.persenters.grade.GradesCallback;
import com.getit.app.persenters.grade.GradesPresenter;
import com.getit.app.persenters.lesson.LessonsCallback;
import com.getit.app.persenters.lesson.LessonsPresenter;
import com.getit.app.persenters.unit.UnitsCallback;
import com.getit.app.persenters.unit.UnitsPresenter;
import com.getit.app.persenters.user.UsersCallback;
import com.getit.app.persenters.user.UsersPresenter;
import com.getit.app.ui.activities.QuestionsViewerActivity;
import com.getit.app.ui.adptres.CorrectionAdapter;

import java.util.ArrayList;
import java.util.List;

public class CorrectionFragment extends Fragment implements AnswersCallback, GradesCallback, UnitsCallback, LessonsCallback, UsersCallback, CorrectionAdapter.OnCorrectionClickListener {
    private FragmentCorrectionBinding binding;
    private UsersPresenter usersPresenter;
    private GradesPresenter gradesPresenter;
    private UnitsPresenter unitsPresenter;
    private LessonsPresenter lessonsPresenter;
    private AnswersPresenter answersPresenter;
    private CorrectionAdapter adapter;
    private List<Correction> corrections, searchedCorrections;
    private List<User> students;
    private List<Grade> grades;
    private List<Unit> units;
    private List<Lesson> lessons;

    public static CorrectionFragment newInstance() {
        Bundle args = new Bundle();
        CorrectionFragment fragment = new CorrectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCorrectionBinding.inflate(inflater);

        usersPresenter = new UsersPresenter(this);
        gradesPresenter = new GradesPresenter(this);
        unitsPresenter = new UnitsPresenter(this);
        lessonsPresenter = new LessonsPresenter(this);
        answersPresenter = new AnswersPresenter(this);

        students = new ArrayList<>();
        grades = new ArrayList<>();
        units = new ArrayList<>();
        lessons = new ArrayList<>();
        searchedCorrections = new ArrayList<>();
        corrections = new ArrayList<>();

        binding.refreshLayout.setColorSchemeResources(R.color.refreshColor1, R.color.refreshColor2, R.color.refreshColor3, R.color.refreshColor4);
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });

        binding.textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(binding.textSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CorrectionAdapter(searchedCorrections, this);
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        usersPresenter.getUsers(Constants.USER_TYPE_STUDENT);
    }

    @Override
    public void onGetUsersComplete(List<User> users) {
        students.clear();
        students.addAll(users);
        gradesPresenter.getGrades();
    }

    @Override
    public void onGetGradesComplete(List<Grade> grades) {
        this.grades.clear();
        this.grades.addAll(grades);
        unitsPresenter.getUnits();
    }

    @Override
    public void onGetUnitsComplete(List<Unit> units) {
        this.units.clear();
        this.units.addAll(units);
        lessonsPresenter.getLessons();
    }

    @Override
    public void onGetLessonsComplete(List<Lesson> lessons) {
        this.lessons.clear();
        this.lessons.addAll(lessons);
        answersPresenter.search();
    }

    @Override
    public void onSearchComplete(List<Correction> corrections) {
        this.corrections.clear();
        this.corrections.addAll(corrections);

        for (var correction : corrections) {
            var lesson = new Lesson();
            lesson.setId(correction.getLessonId());
            var index = lessons.indexOf(lesson);
            if (index != -1) {
                lesson = lessons.get(index);
                correction.setUnitId(lesson.getUnitId());
                correction.setLessonName(lesson.getName());
                correction.setLessonDate(lesson.getDate());
            }

            var unit = new Unit();
            unit.setId(correction.getUnitId());
            index = units.indexOf(unit);
            if (index != -1) {
                unit = units.get(index);
                correction.setGradeId(unit.getGradeId());
                correction.setUnitName(unit.getName());
            }
            index = grades.indexOf(new Grade(correction.getGradeId(), null));
            if (index != -1) {
                var garde = grades.get(index);
                correction.setGradeName(garde.getName());
            }
            index = students.indexOf(new User(correction.getStudentId()));
            if (index != -1) {
                var student = students.get(index);
                correction.setStudentName(student.getFullName());
            }
        }
        search(binding.textSearch.getText().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (gradesPresenter != null) {
            gradesPresenter.onDestroy();
        }
    }

    @Override
    public void onShowLoading() {
        binding.refreshLayout.setRefreshing(true);
    }

    @Override
    public void onHideLoading() {
        binding.refreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void search(String searchedText) {
        searchedCorrections.clear();
        if (!searchedText.isEmpty()) {
            for (Correction correction : corrections) {
                if (isMatched(correction, searchedText)) {
                    searchedCorrections.add(correction);
                }
            }
        } else {
            searchedCorrections.addAll(corrections);
        }

        refresh();
    }

    private boolean isMatched(Correction correction, String text) {
        String searchedText = text.toLowerCase();
        boolean result = (correction.getLessonName().toLowerCase().contains(searchedText) ||
                correction.getGradeName().toLowerCase().contains(searchedText) ||
                correction.getUnitName().toLowerCase().contains(searchedText) ||
                correction.getStudentName().toLowerCase().contains(searchedText));
        return result;
    }

    private void refresh() {
        binding.message.setVisibility(View.GONE);
        if (searchedCorrections.isEmpty()) {
            binding.message.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCorrectionClickListener(Correction correction) {
        var lesson = new Lesson();
        lesson.setId(correction.getLessonId());
        lesson.setName(correction.getLessonName());
        var user = new User(correction.getStudentId());
        user.setFullName(correction.getStudentName());
        Intent intent = new Intent(getContext(), QuestionsViewerActivity.class);
        intent.putExtra(Constants.ARG_OBJECT, lesson);
        intent.putExtra(Constants.ARG_USER, user);
        startActivity(intent);
    }
}