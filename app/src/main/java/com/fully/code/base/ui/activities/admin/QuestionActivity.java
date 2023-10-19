package com.fully.code.base.ui.activities.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fully.code.base.Constants;
import com.fully.code.base.models.Course;
import com.fully.code.base.models.Question;
import com.fully.code.base.models.QuestionChoice;
import com.fully.code.base.persenters.questions.QuestionsCallback;
import com.fully.code.base.persenters.questions.QuestionsPresenter;
import com.fully.code.base.ui.activities.BaseActivity;
import com.fully.code.base.ui.fragments.CourseSelectorBottomSheet;
import com.fully.code.base.utilities.ToastUtils;
import com.fully.code.base.utilities.helpers.LocaleHelper;
import com.getit.app.R;
import com.getit.app.databinding.ActivityQuestionBinding;

import java.util.ArrayList;

public class QuestionActivity extends BaseActivity implements QuestionsCallback, CourseSelectorBottomSheet.ItemClickListener, View.OnClickListener {
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
        if (question == null) {
            question = new Question();
        }
        bind();

        binding.course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseSelectorBottomSheet.newInstance(question.getCourseId(), 0).show(getSupportFragmentManager(), "");
            }
        });

        binding.questionTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.question_type_multi_choices ->
                            question.setType(Constants.QUESTION_TYPE_MULTI_CHOICE);
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

                if (title.isEmpty()) {
                    binding.title.setError(getString(R.string.str_title_hint));
                    binding.title.requestFocus();
                    return;
                }
                if (question.getCourseId() == null) {
                    binding.course.setError(getString(R.string.str_course_hint));
                    binding.course.requestFocus();
                    return;
                }
                if (!binding.questionTypeArticle.isChecked() && !binding.questionTypeMultiChoices.isChecked()) {
                    ToastUtils.longToast(R.string.str_question_type_hint);
                    return;
                }
                if (question.isMultiChoices()) {
                    if (!validateMultiChoices()) {
                        return;
                    }
                }

                question.setTitle(title);
                question.setDescription(binding.description.getText().toString());
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
    public void onCourseClick(Course course) {
        question.setCourseId(course.getId());
        question.setCourseName(course.getName());
        binding.course.setText(course.getName());
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
        binding.course.setText(question.getCourseName());
        binding.questionTypeArticle.setChecked(!question.isMultiChoices());
        binding.questionTypeMultiChoices.setChecked(question.isMultiChoices());
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
        } else {
            question.setChoices(new ArrayList<>());
        }
        binding.choices.setVisibility(question.isMultiChoices() ? View.VISIBLE : View.GONE);
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