package com.getit.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.getit.app.Constants;
import com.getit.app.R;
import com.getit.app.databinding.BottomSheetGradeSelectorBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class GradeSelectorBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetGradeSelectorBinding binding;
    private ItemClickListener listener;
    private int selectedGrade;

    public static GradeSelectorBottomSheet newInstance(int selectedGrade) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ARG_ID, selectedGrade);
        GradeSelectorBottomSheet sheet = new GradeSelectorBottomSheet();
        sheet.setArguments(bundle);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetGradeSelectorBinding.inflate(inflater);
        selectedGrade = getArguments().getInt(Constants.ARG_ID, 0);
        switch (selectedGrade) {
            case Constants.GRADE_ONE ->
                    binding.txtGrade1.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorSecondary, null));
            case Constants.GRADE_TWO ->
                    binding.txtGrade2.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorSecondary, null));
            case Constants.GRADE_THREE ->
                    binding.txtGrade3.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorSecondary, null));
            case Constants.GRADE_FOUR ->
                    binding.txtGrade4.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorSecondary, null));
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.txtGrade1.setOnClickListener(this::onClick);
        binding.txtGrade2.setOnClickListener(this::onClick);
        binding.txtGrade3.setOnClickListener(this::onClick);
        binding.txtGrade4.setOnClickListener(this::onClick);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_grade_1 -> listener.onGradeClick(1);
            case R.id.txt_grade_2 -> listener.onGradeClick(2);
            case R.id.txt_grade_3 -> listener.onGradeClick(3);
            case R.id.txt_grade_4 -> listener.onGradeClick(4);
        }
        dismiss();
    }

    public interface ItemClickListener {
        void onGradeClick(int id);
    }
}