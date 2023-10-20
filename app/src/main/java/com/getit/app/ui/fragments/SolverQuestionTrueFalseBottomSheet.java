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
import com.getit.app.databinding.BottomSheetSolverQuestionTrueFalseBinding;
import com.getit.app.models.Answer;
import com.getit.app.utilities.ToastUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SolverQuestionTrueFalseBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetSolverQuestionTrueFalseBinding binding;
    private OnQuestionTrueFalseSolveCallback callback;
    private Answer answer;

    public static SolverQuestionTrueFalseBottomSheet newInstance(Answer answer) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.ARG_OBJECT, answer);
        SolverQuestionTrueFalseBottomSheet sheet = new SolverQuestionTrueFalseBottomSheet();
        sheet.setArguments(bundle);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetSolverQuestionTrueFalseBinding.inflate(inflater);
        answer = getArguments().getParcelable(Constants.ARG_OBJECT);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.title.setText(answer.getQuestion().getTitle());
        binding.description.setText(answer.getQuestion().getDescription());

        binding.isTrueAnswer.setOnClickListener(this);
        binding.isFalseAnswer.setOnClickListener(this);

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer.getTureAnswer() == null) {
                    ToastUtils.longToast(R.string.str_enter_value);
                    return;
                }

                if (callback != null) {
                    callback.onQuestionTrueFalseSolveCallback(answer);
                }
                dismiss();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.is_true_answer -> {
                binding.isTrueAnswer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, null));
                binding.isFalseAnswer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                answer.setTureAnswer(true);
            }
            case R.id.is_false_answer -> {
                binding.isTrueAnswer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.isFalseAnswer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, null));
                answer.setTureAnswer(false);
            }
        }
        answer.correct();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnQuestionTrueFalseSolveCallback) {
            callback = (OnQuestionTrueFalseSolveCallback) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnQuestionMultiChoicesSolveCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public interface OnQuestionTrueFalseSolveCallback {
        void onQuestionTrueFalseSolveCallback(Answer answer);
    }
}