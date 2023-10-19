package com.fully.code.base.ui.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.fully.code.base.Constants;
import com.fully.code.base.models.Answer;
import com.fully.code.base.models.Exam;
import com.fully.code.base.models.ExamStudent;
import com.fully.code.base.models.Question;
import com.fully.code.base.models.User;
import com.fully.code.base.persenters.user.UsersCallback;
import com.fully.code.base.persenters.user.UsersPresenter;
import com.fully.code.base.utilities.ToastUtils;
import com.fully.code.base.utilities.UIUtils;
import com.fully.code.base.utilities.helpers.LocaleHelper;
import com.fully.code.base.utilities.helpers.StorageHelper;
import com.getit.app.R;
import com.getit.app.databinding.ActivityExamDetailsBinding;

public class ExamDetailsActivity extends BaseActivity implements UsersCallback, View.OnClickListener {
    private ActivityExamDetailsBinding binding;
    private UsersPresenter presenter;
    private Exam exam;
    private User currentUser;
    private ExamStudent currentExamStudent;
    private Question currentQuestion;
    private Answer currentAnswer;
    private int currentQuestionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityExamDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new UsersPresenter(this);

        exam = getIntent().getParcelableExtra(Constants.ARG_OBJECT);
        currentUser = StorageHelper.getCurrentUser();
        currentExamStudent = currentUser.getExamStudent(exam);
        bind();

        binding.choice1.setOnClickListener(this);
        binding.choice2.setOnClickListener(this);
        binding.choice3.setOnClickListener(this);
        binding.choice4.setOnClickListener(this);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuestionIndex--;
                bindQuestion();
            }
        });
        binding.btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentExamStudent.setFinished(true);
                presenter.save(currentUser);
            }
        });
        binding.btnSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentAnswer.isRight() == null) {
                    var valid = false;
                    if (currentQuestion.isMultiChoices()) {
                        if (currentAnswer.getSelectedAnswerIndex() <= 0) {
                            ToastUtils.longToast(R.string.str_choices_hint);
                        } else {
                            valid = true;
                        }
                    } else {
                        String answer = binding.answer.getText().toString();
                        if (answer.isEmpty()) {
                            ToastUtils.longToast(R.string.str_your_answer_hint);
                        } else {
                            valid = true;
                        }
                    }
                    if (valid) {
                        presenter.save(currentUser);
                    }
                }
            }
        });
        binding.answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentAnswer.setAnswer(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (currentAnswer.isRight() != null) {
            if (view.getId() != R.id.is_right_1) {
                binding.check1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
            }
            if (view.getId() != R.id.is_right_2) {
                binding.check2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
            }
            if (view.getId() != R.id.is_right_3) {
                binding.check3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
            }
            if (view.getId() != R.id.is_right_4) {
                binding.check4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
            }
            ((ImageView) view).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, null));
            switch (view.getId()) {
                case R.id.is_right_1 -> currentAnswer.setSelectedAnswerIndex(1);
                case R.id.is_right_2 -> currentAnswer.setSelectedAnswerIndex(2);
                case R.id.is_right_3 -> currentAnswer.setSelectedAnswerIndex(3);
                case R.id.is_right_4 -> currentAnswer.setSelectedAnswerIndex(4);
            }
            currentAnswer.correct();
        }
    }

    @Override
    public void onSaveUserComplete() {
        Toast.makeText(this, R.string.str_message_added_successfully, Toast.LENGTH_LONG).show();
        if (canNext()) {
            currentQuestionIndex++;
            bindQuestion();
        }
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        Toast.makeText(this, getString(R.string.str_save_fail, message), Toast.LENGTH_LONG).show();
    }

    private void bind() {
        binding.title.setText(exam.getTitle());
        binding.description.setText(exam.getDescription());
        binding.grade.setText(UIUtils.getGrade(exam.getGrade()));
        binding.course.setText(exam.getCourseName());
        binding.totalScore.setText(exam.getScoreTotal() + "");
        binding.passScore.setText(exam.getScorePass() + "");
        currentQuestionIndex = 0;

        bindQuestion();
    }

    private boolean canNext() {
        return currentQuestionIndex >= 0 && currentQuestionIndex < exam.getQuestions().size();
    }

    private boolean canBack() {
        return currentQuestionIndex > 0 && currentQuestionIndex < exam.getQuestions().size();
    }

    private void bindQuestion() {
        currentQuestion = exam.getQuestions().get(currentQuestionIndex);
        currentAnswer = currentUser.getAnswer(currentExamStudent, currentQuestion);
        binding.btnSaveNext.setVisibility(canNext() ? View.VISIBLE : View.GONE);
        binding.btnBack.setVisibility(canBack() ? View.VISIBLE : View.GONE);
        binding.btnFinish.setVisibility((!canNext()) ? View.VISIBLE : View.GONE);

        binding.lblHeader.setText("Question #" + (currentQuestionIndex + 1));
        binding.containerAnswerArticle.setVisibility(!currentQuestion.isMultiChoices() ? View.VISIBLE : View.GONE);
        binding.containerAnswerMultiChoices.setVisibility(currentQuestion.isMultiChoices() ? View.VISIBLE : View.GONE);
        binding.answer.setEnabled(currentAnswer.isRight() != null && currentAnswer.isRight());

        if (currentQuestion.isMultiChoices()) {
            int index = 1;
            View correctAnswer = null, studentAnswer = null;
            ImageView check = null;
            for (var choice : currentQuestion.getChoices()) {
                switch (index) {
                    case 1 -> {
                        binding.choice1Text.setText(choice.getTitle());
                        check = choice.isCorrectAnswer() ? binding.check1 : null;
                        correctAnswer = choice.isCorrectAnswer() ? binding.check1 : null;
                        studentAnswer = currentAnswer.getSelectedAnswerIndex() == 1 ? binding.choice1 : null;
                    }
                    case 2 -> {
                        binding.choice2Text.setText(choice.getTitle());
                        check = choice.isCorrectAnswer() ? binding.check2 : null;
                        correctAnswer = choice.isCorrectAnswer() ? binding.check2 : null;
                        studentAnswer = currentAnswer.getSelectedAnswerIndex() == 1 ? binding.choice2 : null;
                    }
                    case 3 -> {
                        binding.choice3Text.setText(choice.getTitle());
                        check = choice.isCorrectAnswer() ? binding.check3 : null;
                        correctAnswer = choice.isCorrectAnswer() ? binding.check3 : null;
                        studentAnswer = currentAnswer.getSelectedAnswerIndex() == 1 ? binding.choice3 : null;
                    }
                    case 4 -> {
                        binding.choice4Text.setText(choice.getTitle());
                        check = choice.isCorrectAnswer() ? binding.check4 : null;
                        correctAnswer = choice.isCorrectAnswer() ? binding.check4 : null;
                        studentAnswer = currentAnswer.getSelectedAnswerIndex() == 1 ? binding.choice4 : null;
                    }
                }
                index++;
            }

            if (currentAnswer.isRight() != null) {
                if (check != null) {
                    binding.check1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                    binding.check2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                    binding.check3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                    binding.check4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unchecked, null));
                    check.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, null));
                }
                if (correctAnswer != null && studentAnswer != null) {
                    if (correctAnswer.getId() == studentAnswer.getId()) {
                        correctAnswer.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green_55, null));
                    } else {
                        studentAnswer.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.red_55, null));
                        correctAnswer.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green_55, null));
                    }
                }
            }
        } else {
            binding.answer.setText(currentAnswer.getAnswer());

            if (currentAnswer.isRight() != null) {
                if (currentAnswer.isRight() != null) {
                    binding.containerAnswerArticle.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.gray, null));
                } else {
                    binding.containerAnswerArticle.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.red_55, null));
                }
            }
        }
    }

    private void saveAnswer() {
        presenter.save(currentUser);
    }
}