package com.getit.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.getit.app.Constants;
import com.getit.app.databinding.BottomSheetCourseSelectorBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SolverQuestionMultiChoicesBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetCourseSelectorBinding binding;
    private ItemClickListener listener;
    private String selectedCourse;
    private int selectedGrade;

    public static SolverQuestionMultiChoicesBottomSheet newInstance(String selectedCourse, int selectedGrade) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARG_ID, selectedCourse);
        bundle.putInt(Constants.ARG_ID_2, selectedGrade);
        SolverQuestionMultiChoicesBottomSheet sheet = new SolverQuestionMultiChoicesBottomSheet();
        sheet.setArguments(bundle);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetCourseSelectorBinding.inflate(inflater);
        selectedCourse = getArguments().getString(Constants.ARG_ID);
        selectedGrade = getArguments().getInt(Constants.ARG_ID_2, 0);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        courses = new ArrayList<>();
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        List<Course> selectedCourses = new ArrayList<>();
//        selectedCourses.add(new Course(selectedCourse));
//        adapter = new CoursesSelectorAdapter(courses, this, selectedCourses);
//        binding.recyclerView.setAdapter(adapter);
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

    public interface ItemClickListener {
    }
}