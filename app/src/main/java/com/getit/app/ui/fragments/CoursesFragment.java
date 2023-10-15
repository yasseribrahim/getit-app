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
import com.getit.app.databinding.FragmentCoursesBinding;
import com.getit.app.models.Course;
import com.getit.app.persenters.courses.CoursesCallback;
import com.getit.app.persenters.courses.CoursesPresenter;
import com.getit.app.ui.activities.CourseActivity;
import com.getit.app.ui.activities.UserActivity;
import com.getit.app.ui.adptres.CoursesAdapter;

import java.util.ArrayList;
import java.util.List;

public class CoursesFragment extends Fragment implements CoursesCallback, CoursesAdapter.OnItemClickListener {
    private FragmentCoursesBinding binding;
    private CoursesPresenter presenter;
    private CoursesAdapter coursesAdapter;
    private List<Course> courses, searchedCourses;

    public static CoursesFragment newInstance() {
        Bundle args = new Bundle();
        CoursesFragment fragment = new CoursesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCoursesBinding.inflate(inflater);

        presenter = new CoursesPresenter(this);

        binding.refreshLayout.setColorSchemeResources(R.color.refreshColor1, R.color.refreshColor2, R.color.refreshColor3, R.color.refreshColor4);
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCourseActivity(null);
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

        courses = new ArrayList<>();
        searchedCourses = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        coursesAdapter = new CoursesAdapter(searchedCourses, this);
        binding.recyclerView.setAdapter(coursesAdapter);

        return binding.getRoot();
    }

    private void load() {
        presenter.getCourses();
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    public void onGetCoursesComplete(List<Course> courses) {
        this.courses.clear();
        this.courses.addAll(courses);
        search(binding.textSearch.getText().toString());
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
        searchedCourses.clear();
        if (!searchedText.isEmpty()) {
            for (Course course : courses) {
                if (isMatched(course, searchedText)) {
                    searchedCourses.add(course);
                }
            }
        } else {
            searchedCourses.addAll(courses);
        }

        refresh();
    }

    private boolean isMatched(Course course, String text) {
        String searchedText = text.toLowerCase();
        int grade = 0;
        try {
            grade = Integer.parseInt(searchedText);
        } catch (Exception ex) {
        }
        boolean result = course.getName().toLowerCase().contains(searchedText) || (course.getGrade() == grade);
        return result;
    }

    private void refresh() {
        binding.message.setVisibility(View.GONE);
        if (searchedCourses.isEmpty()) {
            binding.message.setVisibility(View.VISIBLE);
        }

        coursesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemViewListener(int position) {
        Course course = searchedCourses.get(position);
    }

    @Override
    public void onDeleteItemViewListener(int position) {
        if (position >= 0 && position < searchedCourses.size()) {
            Course course = searchedCourses.get(position);
            int index = courses.indexOf(course);
            searchedCourses.remove(position);
            if (index >= 0 && index < courses.size()) {
                courses.remove(position);
            }

            presenter.save(course);
//            onDeleteUserComplete(position);
        }
    }

    @Override
    public void onItemEditListener(int position) {
        if (position >= 0 && position < searchedCourses.size()) {
            Course course = searchedCourses.get(position);
            openCourseActivity(course);
        }
    }

    @Override
    public void onDeleteCourseComplete(int position) {
        Toast.makeText(getContext(), R.string.str_message_delete_successfully, Toast.LENGTH_LONG).show();
        coursesAdapter.notifyItemRemoved(position);
    }

    private void openCourseActivity(Course course) {
        Intent intent = new Intent(getContext(), CourseActivity.class);
        intent.putExtra(Constants.ARG_OBJECT, course);
        startActivity(intent);
    }
}