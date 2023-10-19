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
import com.getit.app.databinding.BottomSheetSolverQuestionMultiChoicesBinding;
import com.getit.app.models.Answer;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SolverQuestionMultiChoicesBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetSolverQuestionMultiChoicesBinding binding;
    private OnQuestionMultiChoicesSolveCallback callback;
    private Answer answer;

    public static SolverQuestionMultiChoicesBottomSheet newInstance(Answer answer) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.ARG_OBJECT, answer);
        SolverQuestionMultiChoicesBottomSheet sheet = new SolverQuestionMultiChoicesBottomSheet();
        sheet.setArguments(bundle);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetSolverQuestionMultiChoicesBinding.inflate(inflater);
        answer = getArguments().getParcelable(Constants.ARG_OBJECT);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.title.setText(answer.getQuestion().getTitle());
        binding.description.setText(answer.getQuestion().getDescription());

        binding.choice1.setOnClickListener(this);
        binding.choice2.setOnClickListener(this);
        binding.choice3.setOnClickListener(this);
        binding.choice4.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.is_right_1 -> {
                binding.check1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, null));
                binding.check2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.check3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.check4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                answer.setSelectedAnswerIndex(1);
            }
            case R.id.is_right_2 -> {
                binding.check1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.check2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, null));
                binding.check3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.check4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                answer.setSelectedAnswerIndex(2);
            }
            case R.id.is_right_3 -> {
                binding.check1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.check2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.check3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, null));
                binding.check4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                answer.setSelectedAnswerIndex(3);
            }
            case R.id.is_right_4 -> {
                binding.check1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.check2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.check3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.check4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, null));
                answer.setSelectedAnswerIndex(4);
            }
        }
        answer.correct();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnQuestionMultiChoicesSolveCallback) {
            callback = (OnQuestionMultiChoicesSolveCallback) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public interface OnQuestionMultiChoicesSolveCallback {
        void onQuestionMultiChoicesSolveCallback(Answer answer);
    }
}