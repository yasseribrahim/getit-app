package com.getit.app.ui.activities.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.getit.app.Constants;
import com.getit.app.R;
import com.getit.app.databinding.ActivityQuestionBinding;
import com.getit.app.models.Question;
import com.getit.app.models.QuestionChoice;
import com.getit.app.persenters.questions.QuestionsCallback;
import com.getit.app.persenters.questions.QuestionsPresenter;
import com.getit.app.ui.activities.BaseActivity;
import com.getit.app.utilities.ToastUtils;
import com.getit.app.utilities.helpers.LocaleHelper;

import java.util.ArrayList;

public class QuestionActivity extends BaseActivity implements QuestionsCallback, View.OnClickListener {
    private ActivityQuestionBinding binding;
    private QuestionsPresenter presenter;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new QuestionsPresenter(this);

        question = getIntent().getParcelableExtra(Constants.ARG_OBJECT);
        bind();

        binding.questionTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.question_type_multi_choices ->
                            question.setType(Constants.QUESTION_TYPE_MULTI_CHOICE);
                    case R.id.question_type_true_false ->
                            question.setType(Constants.QUESTION_TYPE_TRUE_FALSE);
                    case R.id.question_type_article ->
                            question.setType(Constants.QUESTION_TYPE_ARTICLE);
                }
                handelQuestionType();
            }
        });

        binding.isRight1.setOnClickListener(this);
        binding.isRight2.setOnClickListener(this);
        binding.isRight3.setOnClickListener(this);
        binding.isRight4.setOnClickListener(this);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.title.getText().toString().trim();
                String correctAnswer = null;

                if (title.isEmpty()) {
                    binding.title.setError(getString(R.string.str_title_hint));
                    binding.title.requestFocus();
                    return;
                }
                if (!binding.questionTypeArticle.isChecked() && !binding.questionTypeMultiChoices.isChecked() && !binding.questionTypeTrueFalse.isChecked()) {
                    ToastUtils.longToast(R.string.str_question_type_hint);
                    return;
                }
                if (question.isMultiChoices()) {
                    if (!validateMultiChoices()) {
                        return;
                    }
                } else if (question.isTrueFalse()) {
                    if (!binding.isTrueAnswer.isChecked() && !binding.isFalseAnswer.isChecked()) {
                        ToastUtils.longToast(R.string.str_enter_value);
                        return;
                    }
                    question.setAnswerTrue(binding.isTrueAnswer.isChecked());
                } else if (question.isArticle()) {
                    if (binding.correctAnswer.getText().toString().isEmpty()) {
                        ToastUtils.longToast(R.string.str_enter_value);
                        return;
                    } else {
                        correctAnswer = binding.correctAnswer.getText().toString();
                    }
                }

                question.setTitle(title);
                question.setDescription(binding.description.getText().toString());
                question.setCorrectAnswer(correctAnswer);
                presenter.save(question);
            }
        });
    }

    @Override
    public void onSaveQuestionComplete() {
        Toast.makeText(this, R.string.str_message_added_successfully, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        Toast.makeText(this, getString(R.string.str_save_fail, message), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.is_right_1) {
            binding.isRight1.setChecked(false);
        }
        if (view.getId() != R.id.is_right_2) {
            binding.isRight2.setChecked(false);
        }
        if (view.getId() != R.id.is_right_3) {
            binding.isRight3.setChecked(false);
        }
        if (view.getId() != R.id.is_right_4) {
            binding.isRight4.setChecked(false);
        }
        ((RadioButton) view).setChecked(true);
    }

    private void bind() {
        binding.title.setText(question.getTitle());
        binding.description.setText(question.getDescription());
        binding.questionTypeArticle.setChecked(question.isArticle());
        binding.questionTypeMultiChoices.setChecked(question.isMultiChoices());
        binding.questionTypeTrueFalse.setChecked(question.isTrueFalse());
        handelQuestionType();
    }

    private void handelQuestionType() {
        if (question.isMultiChoices()) {
            if (question.getChoices() == null) {
                question.setChoices(new ArrayList<>());
            }

            // Prepare Choices
            int index = question.getChoices().size();
            while (index < 4) {
                question.getChoices().add(new QuestionChoice());
                index++;
            }

            // Fill Choices
            index = 1;
            for (QuestionChoice choice : question.getChoices()) {
                switch (index) {
                    case 1 -> {
                        binding.choice1.setText(choice.getTitle());
                        binding.isRight1.setChecked(choice.isCorrectAnswer());
                    }
                    case 2 -> {
                        binding.choice2.setText(choice.getTitle());
                        binding.isRight2.setChecked(choice.isCorrectAnswer());
                    }
                    case 3 -> {
                        binding.choice3.setText(choice.getTitle());
                        binding.isRight3.setChecked(choice.isCorrectAnswer());
                    }
                    case 4 -> {
                        binding.choice4.setText(choice.getTitle());
                        binding.isRight4.setChecked(choice.isCorrectAnswer());
                    }
                }
                index++;
            }
        } else if (question.isTrueFalse()) {
            if (question.getId() != null) {
                binding.isTrueAnswer.setChecked(question.isAnswerTrue());
                binding.isFalseAnswer.setChecked(!question.isAnswerTrue());
            }
        } else {
            question.setChoices(new ArrayList<>());
            binding.correctAnswer.setText(question.getCorrectAnswer());
        }
        binding.choices.setVisibility(question.isMultiChoices() ? View.VISIBLE : View.GONE);
        binding.questionTrueFalseElements.setVisibility(question.isTrueFalse() ? View.VISIBLE : View.GONE);
        binding.containerCorrectAnswerArticle.setVisibility(question.isArticle() ? View.VISIBLE : View.GONE);
    }

    private boolean validateMultiChoices() {
        int index = 1;
        String choiceText;
        boolean isCorrectAnswer, correctAnswerSelected = false;
        for (QuestionChoice choice : question.getChoices()) {
            choiceText = "";
            isCorrectAnswer = false;
            switch (index) {
                case 1 -> {
                    choiceText = binding.choice1.getText().toString();
                    isCorrectAnswer = binding.isRight1.isChecked();
                }
                case 2 -> {
                    choiceText = binding.choice2.getText().toString();
                    isCorrectAnswer = binding.isRight2.isChecked();
                }
                case 3 -> {
                    choiceText = binding.choice3.getText().toString();
                    isCorrectAnswer = binding.isRight3.isChecked();
                }
                case 4 -> {
                    choiceText = binding.choice4.getText().toString();
                    isCorrectAnswer = binding.isRight4.isChecked();
                }
            }

            if (choiceText.isEmpty()) {
                ToastUtils.longToast("Please Fill Choice# " + index);
                return false;
            }
            correctAnswerSelected = correctAnswerSelected || isCorrectAnswer;
            question.getChoices().get(index - 1).setTitle(choiceText);
            question.getChoices().get(index - 1).setCorrectAnswer(isCorrectAnswer);
            index++;
        }

        if (!correctAnswerSelected) {
            ToastUtils.longToast("Please select is right answer");
            return false;
        }

        return true;
    }
}