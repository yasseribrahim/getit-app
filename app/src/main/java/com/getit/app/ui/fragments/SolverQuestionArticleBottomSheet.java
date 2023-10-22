package com.getit.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.getit.app.Constants;
import com.getit.app.R;
import com.getit.app.databinding.BottomSheetSolverQuestionArticleBinding;
import com.getit.app.models.Answer;
import com.getit.app.utilities.ToastUtils;
import com.getit.app.utilities.helpers.StorageHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SolverQuestionArticleBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetSolverQuestionArticleBinding binding;
    private OnQuestionArticleSolveCallback callback;
    private Answer answer;
    private int mode;

    public static SolverQuestionArticleBottomSheet newInstance(Answer answer, int mode) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.ARG_OBJECT, answer);
        bundle.putInt(Constants.ARG_SHOW_MODE, mode);
        SolverQuestionArticleBottomSheet sheet = new SolverQuestionArticleBottomSheet();
        sheet.setArguments(bundle);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetSolverQuestionArticleBinding.inflate(inflater);
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
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                answer.setAnswer(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer.getAnswer() == null || answer.getAnswer().isEmpty()) {
                    ToastUtils.longToast(R.string.str_enter_value);
                    return;
                }

                if (callback != null) {
                    answer.correct();
                    callback.onQuestionArticleSolveCallback(answer);
                }
                dismiss();
            }
        });

        if (answer.isAnswered()) {
            binding.answer.setText(answer.getAnswer());
            binding.answer.setEnabled(false);
        }

        if (!StorageHelper.getCurrentUser().isStudent()) {
            binding.containerCorrectAnswer.setVisibility(View.VISIBLE);
            binding.correctAnswer.setText(answer.getQuestion().getCorrectAnswer());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnQuestionArticleSolveCallback) {
            callback = (OnQuestionArticleSolveCallback) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnQuestionArticleSolveCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public interface OnQuestionArticleSolveCallback {
        void onQuestionArticleSolveCallback(Answer answer);
    }
}