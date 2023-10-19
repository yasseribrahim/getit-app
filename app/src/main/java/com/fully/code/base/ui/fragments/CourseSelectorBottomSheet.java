package com.fully.code.base.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fully.code.base.Constants;
import com.fully.code.base.models.Course;
import com.fully.code.base.persenters.courses.CoursesCallback;
import com.fully.code.base.persenters.courses.CoursesPresenter;
import com.fully.code.base.ui.adptres.CoursesSelectorAdapter;
import com.fully.code.base.utilities.ToastUtils;
import com.getit.app.databinding.BottomSheetCourseSelectorBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class CourseSelectorBottomSheet extends BottomSheetDialogFragment implements CoursesCallback, CoursesSelectorAdapter.OnItemClickListener {
    private BottomSheetCourseSelectorBinding binding;
    private CoursesPresenter presenter;
    private ItemClickListener listener;
    private CoursesSelectorAdapter adapter;
    private List<Course> courses;
    private String selectedCourse;
    private int selectedGrade;

    public static CourseSelectorBottomSheet newInstance(String selectedCourse, int selectedGrade) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARG_ID, selectedCourse);
        bundle.putInt(Constants.ARG_ID_2, selectedGrade);
        CourseSelectorBottomSheet sheet = new CourseSelectorBottomSheet();
        sheet.setArguments(bundle);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetCourseSelectorBinding.inflate(inflater);
        selectedCourse = getArguments().getString(Constants.ARG_ID);
        selectedGrade = getArguments().getInt(Constants.ARG_ID_2, 0);
        presenter = new CoursesPresenter(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        courses = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Course> selectedCourses = new ArrayList<>();
        selectedCourses.add(new Course(selectedCourse));
        adapter = new CoursesSelectorAdapter(courses, this, selectedCourses);
        binding.recyclerView.setAdapter(adapter);
        if (selectedGrade > 0) {
            presenter.getCourses(selectedGrade);
        } else {
            presenter.getCourses();
        }
    }

    @Override
    public void onGetCoursesComplete(List<Course> courses) {
        this.courses.clear();
        this.courses.addAll(courses);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            listener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        ToastUtils.longToast(message);
    }

    @Override
    public void onShowLoading() {
        binding.progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideLoading() {
        binding.progress.setVisibility(View.GONE);
    }

    @Override
    public void onCourseClickListener(Course course) {
        listener.onCourseClick(course);
        dismiss();
    }

    public interface ItemClickListener {
        void onCourseClick(Course course);
    }
}