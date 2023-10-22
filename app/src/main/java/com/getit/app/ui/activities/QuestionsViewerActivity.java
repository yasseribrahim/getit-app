package com.getit.app.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.getit.app.Constants;
import com.getit.app.R;
import com.getit.app.databinding.ActivityQuestonsViewerBinding;
import com.getit.app.models.Answer;
import com.getit.app.models.AnswerStudent;
import com.getit.app.models.Lesson;
import com.getit.app.models.Question;
import com.getit.app.models.User;
import com.getit.app.persenters.answer.AnswersCallback;
import com.getit.app.persenters.answer.AnswersPresenter;
import com.getit.app.persenters.questions.QuestionsCallback;
import com.getit.app.persenters.questions.QuestionsPresenter;
import com.getit.app.ui.adptres.QuestionsViewerAdapter;
import com.getit.app.ui.fragments.SolverQuestionArticleBottomSheet;
import com.getit.app.ui.fragments.SolverQuestionMultiChoicesBottomSheet;
import com.getit.app.ui.fragments.SolverQuestionTrueFalseBottomSheet;
import com.getit.app.utilities.ToastUtils;
import com.getit.app.utilities.helpers.LocaleHelper;
import com.getit.app.utilities.helpers.StorageHelper;

import java.util.ArrayList;
import java.util.List;

public class QuestionsViewerActivity extends BaseActivity implements
        QuestionsCallback, AnswersCallback,
        QuestionsViewerAdapter.OnQuestionsViewerClickListener,
        SolverQuestionMultiChoicesBottomSheet.OnQuestionMultiChoicesSolveCallback,
        SolverQuestionTrueFalseBottomSheet.OnQuestionTrueFalseSolveCallback,
        SolverQuestionArticleBottomSheet.OnQuestionArticleSolveCallback {
    private ActivityQuestonsViewerBinding binding;
    private QuestionsPresenter presenter;
    private AnswersPresenter answersPresenter;
    private QuestionsViewerAdapter solvedAdapter;
    private QuestionsViewerAdapter unsolvedAdapter;
    private List<Question> questions, searchedQuestions, solvedQuestions, unsolvedQuestions;
    private Lesson lesson;
    private User currentUser;
    private AnswerStudent answerStudent;
    private List<Answer> answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityQuestonsViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getExtras().containsKey(Constants.ARG_USER)) {
            currentUser = getIntent().getParcelableExtra(Constants.ARG_USER);
            binding.studentInfo.setVisibility(View.VISIBLE);
            binding.btnReset.setVisibility(View.GONE);
            binding.studentName.setText(currentUser.getFullName());
        } else {
            currentUser = StorageHelper.getCurrentUser();
            binding.btnReset.setVisibility(View.VISIBLE);
            binding.studentInfo.setVisibility(View.GONE);
        }
        presenter = new QuestionsPresenter(this);
        answersPresenter = new AnswersPresenter(this);
        lesson = getIntent().getParcelableExtra(Constants.ARG_OBJECT);

        binding.refreshLayout.setColorSchemeResources(R.color.refreshColor1, R.color.refreshColor2, R.color.refreshColor3, R.color.refreshColor4);
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });

        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (var answer : answers) {
                    answer.reset();
                    answerStudent.addAnswer(answer);
                }

                answersPresenter.save(answerStudent);
            }
        });

        binding.textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(binding.textSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        questions = new ArrayList<>();
        searchedQuestions = new ArrayList<>();
        solvedQuestions = new ArrayList<>();
        unsolvedQuestions = new ArrayList<>();
        answers = new ArrayList<>();
        binding.recyclerViewSolvedQuestions.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewUnsolvedQuestions.setLayoutManager(new LinearLayoutManager(this));
        solvedAdapter = new QuestionsViewerAdapter(solvedQuestions, answers, this);
        unsolvedAdapter = new QuestionsViewerAdapter(unsolvedQuestions, new ArrayList<>(), this);
        binding.recyclerViewSolvedQuestions.setAdapter(solvedAdapter);
        binding.recyclerViewUnsolvedQuestions.setAdapter(unsolvedAdapter);
        setUpActionBar();
    }

    @SuppressLint("WrongConstant")
    private void setUpActionBar() {
        binding.appBarLayout.toolbar.setTitle(lesson.getName());
        setSupportActionBar(binding.appBarLayout.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        binding.appBarLayout.toolbar.setNavigationIcon(R.drawable.back_to_home_button);
    }

    private void load() {
        presenter.getQuestions(lesson.getId());
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    public void onGetQuestionsComplete(List<Question> users) {
        this.questions.clear();
        this.questions.addAll(users);
        search(binding.textSearch.getText().toString());

        answersPresenter.getAnswer(lesson.getId(), currentUser.getId());
    }

    @Override
    public void onGetAnswerComplete(AnswerStudent answerStudent) {
        if (answerStudent == null) {
            answerStudent = new AnswerStudent(lesson.getId(), currentUser.getId());
        }

        for (Question question : questions) {
            var object = answerStudent.getAnswer(question);
        }

        this.answerStudent = answerStudent;
        answersPresenter.save(answerStudent);

        refresh();
    }

    @Override
    public void onSaveAnswerComplete() {
        ToastUtils.longToast(R.string.str_message_added_successfully);
        refresh();
    }

    @Override
    public void onShowLoading() {
        binding.refreshLayout.setRefreshing(true);
    }

    @Override
    public void onHideLoading() {
        binding.refreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void search(String searchedText) {
        searchedQuestions.clear();
        if (!searchedText.isEmpty()) {
            for (Question user : questions) {
                if (isMatched(user, searchedText)) {
                    searchedQuestions.add(user);
                }
            }
        } else {
            searchedQuestions.addAll(questions);
        }

        refresh();
    }

    private boolean isMatched(Question Question, String text) {
        String searchedText = text.toLowerCase();
        boolean result = Question.getTitle().toLowerCase().contains(searchedText) ||
                (Question.getDescription() != null && Question.getDescription().toLowerCase().contains(searchedText)) ||
                (Question.getLessonName() != null && Question.getLessonName().toLowerCase().contains(searchedText));
        return result;
    }

    private void refresh() {
        if (answerStudent != null) {
            int right = 0, wrong = 0;
            answers.clear();
            solvedQuestions.clear();
            unsolvedQuestions.clear();
            for (var question : searchedQuestions) {
                var answer = answerStudent.getAnswer(question);

                if (answer.isAnswered()) {
                    solvedQuestions.add(question);
                    answers.add(answer);
                } else {
                    unsolvedQuestions.add(question);
                }
            }

            binding.messageSolvedQuestionsEmpty.setVisibility(solvedQuestions.isEmpty() ? View.VISIBLE : View.GONE);
            binding.messageUnsolvedQuestionsEmpty.setVisibility(unsolvedQuestions.isEmpty() ? View.VISIBLE : View.GONE);
            solvedAdapter.notifyDataSetChanged();
            unsolvedAdapter.notifyDataSetChanged();
            summary();
        }
    }

    private void summary() {
        if (answerStudent != null) {
            int right = 0, wrong = 0, solved = 0, unsolved = 0;
            for (var question : questions) {
                var answer = answerStudent.getAnswer(question);

                if (answer.isAnswered()) {
                    solved++;
                } else {
                    unsolved++;
                }

                if (answer.getRight() != null) {
                    right += answer.getRight() ? 1 : 0;
                    wrong += !answer.getRight() ? 1 : 0;
                }
            }

            double result = (!questions.isEmpty() ? ((right * 1.0) / questions.size()) : 0) * 100;
            int decimalPlaces = 0;
            double factor = Math.pow(10, decimalPlaces);
            double roundedResult = (double) Math.round(result * factor) / factor;
            binding.questionsCounter.setText(getString(R.string.str_questions_counter, questions.size() + ""));
            binding.questionsSolvedCounter.setText(getString(R.string.str_questions_solved_counter, solved + ""));
            binding.questionsUnsolvedCounter.setText(getString(R.string.str_questions_unsolved_counter, unsolved + ""));
            binding.questionsRightCounter.setText(getString(R.string.str_questions_right_counter, right + ""));
            binding.questionsWrongCounter.setText(getString(R.string.str_questions_wrong_counter, wrong + ""));
            binding.result.setText(getString(R.string.str_questions_result, roundedResult + "%"));
        }
    }

    @Override
    public void onQuestionViewListener(Question question) {
        Answer answer = this.answerStudent.getAnswer(question);
        switch (question.getType()) {
            case Constants.QUESTION_TYPE_MULTI_CHOICE ->
                    SolverQuestionMultiChoicesBottomSheet.newInstance(answer, !answer.isAnswered() ? Constants.SHOW_MODE_EDIT : Constants.SHOW_MODE_VIEW).show(getSupportFragmentManager(), "");
            case Constants.QUESTION_TYPE_TRUE_FALSE ->
                    SolverQuestionTrueFalseBottomSheet.newInstance(answer, !answer.isAnswered() ? Constants.SHOW_MODE_EDIT : Constants.SHOW_MODE_VIEW).show(getSupportFragmentManager(), "");
            case Constants.QUESTION_TYPE_ARTICLE ->
                    SolverQuestionArticleBottomSheet.newInstance(answer, !answer.isAnswered() ? Constants.SHOW_MODE_EDIT : Constants.SHOW_MODE_VIEW).show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void onQuestionMultiChoicesSolveCallback(Answer answer) {
        answerStudent.addAnswer(answer);
        answersPresenter.save(answerStudent);
    }

    @Override
    public void onQuestionArticleSolveCallback(Answer answer) {
        answerStudent.addAnswer(answer);
        answersPresenter.save(answerStudent);
    }

    @Override
    public void onQuestionTrueFalseSolveCallback(Answer answer) {
        answerStudent.addAnswer(answer);
        answersPresenter.save(answerStudent);
    }

    @Override
    public void onQuestionCorrectionTrueListener(Question question) {
        Answer answer = this.answerStudent.getAnswer(question);
        answer.correct(true);
        answerStudent.addAnswer(answer);
        answersPresenter.save(answerStudent);
    }

    @Override
    public void onQuestionCorrectionFalseListener(Question question) {
        Answer answer = this.answerStudent.getAnswer(question);
        answer.correct(false);
        answerStudent.addAnswer(answer);
        answersPresenter.save(answerStudent);
    }
}