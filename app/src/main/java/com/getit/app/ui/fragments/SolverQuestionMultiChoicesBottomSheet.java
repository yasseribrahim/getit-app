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
import com.getit.app.models.QuestionChoice;
import com.getit.app.utilities.ToastUtils;
import com.getit.app.utilities.helpers.StorageHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SolverQuestionMultiChoicesBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetSolverQuestionMultiChoicesBinding binding;
    private OnQuestionMultiChoicesSolveCallback callback;
    private Answer answer;
    private int mode;

    public static SolverQuestionMultiChoicesBottomSheet newInstance(Answer answer, int mode) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.ARG_OBJECT, answer);
        bundle.putInt(Constants.ARG_SHOW_MODE, mode);
        SolverQuestionMultiChoicesBottomSheet sheet = new SolverQuestionMultiChoicesBottomSheet();
        sheet.setArguments(bundle);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetSolverQuestionMultiChoicesBinding.inflate(inflater);
        answer = getArguments().getParcelable(Constants.ARG_OBJECT);
        mode = getArguments().getInt(Constants.ARG_SHOW_MODE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.title.setText(answer.getQuestion().getTitle());
        binding.description.setText(answer.getQuestion().getDescription());
        binding.containerDescription.setVisibility(answer.getQuestion().getDescription() == null || answer.getQuestion().getDescription().isEmpty() ? View.GONE : View.VISIBLE);
        binding.containerButtons.setVisibility(Constants.SHOW_MODE_EDIT == mode ? View.VISIBLE : View.GONE);

        if (Constants.SHOW_MODE_EDIT == mode) {
            binding.choice1.setOnClickListener(this);
            binding.choice2.setOnClickListener(this);
            binding.choice3.setOnClickListener(this);
            binding.choice4.setOnClickListener(this);
        }

        if (answer.isAnswered()) {
            switch (answer.getSelectedAnswerIndex()) {
                case 1 -> onClick(binding.choice1);
                case 2 -> onClick(binding.choice2);
                case 3 -> onClick(binding.choice3);
                case 4 -> onClick(binding.choice4);
            }
        }

        if (!StorageHelper.getCurrentUser().isStudent()) {
            for (int i = 0; i < answer.getQuestion().getChoices().size(); i++) {
                if (answer.getQuestion().getChoices().get(i).isCorrectAnswer()) {
                    switch (i + 1) {
                        case 1 ->
                                binding.choice1Text.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
                        case 2 ->
                                binding.choice2Text.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
                        case 3 ->
                                binding.choice3Text.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
                        case 4 ->
                                binding.choice4Text.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
                    }
                }
            }
        }

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer.getSelectedAnswerIndex() <= 0) {
                    ToastUtils.longToast(R.string.str_enter_value);
                    return;
                }

                if (callback != null) {
                    callback.onQuestionMultiChoicesSolveCallback(answer);
                }
                dismiss();
            }
        });

        for (int i = 0; i < answer.getQuestion().getChoices().size(); i++) {
            QuestionChoice choice = answer.getQuestion().getChoices().get(i);
            switch (i) {
                case 0 -> binding.choice1Text.setText(choice.getTitle());
                case 1 -> binding.choice2Text.setText(choice.getTitle());
                case 2 -> binding.choice3Text.setText(choice.getTitle());
                case 3 -> binding.choice4Text.setText(choice.getTitle());
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choice_1 -> {
                binding.check1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, null));
                binding.check2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.check3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.check4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                answer.setSelectedAnswerIndex(1);
            }
            case R.id.choice_2 -> {
                binding.check1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.check2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, null));
                binding.check3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.check4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                answer.setSelectedAnswerIndex(2);
            }
            case R.id.choice_3 -> {
                binding.check1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.check2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                binding.check3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, null));
                binding.check4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                answer.setSelectedAnswerIndex(3);
            }
            case R.id.choice_4 -> {
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
            throw new RuntimeException(context.toString() + " must implement OnQuestionMultiChoicesSolveCallback");
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