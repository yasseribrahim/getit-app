package com.getit.app.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.getit.app.Constants;
import com.getit.app.R;
import com.getit.app.databinding.ActivityQuestonsBinding;
import com.getit.app.models.Answer;
import com.getit.app.models.AnswerStudent;
import com.getit.app.models.Lesson;
import com.getit.app.models.Question;
import com.getit.app.models.User;
import com.getit.app.persenters.answer.AnswersCallback;
import com.getit.app.persenters.answer.AnswersPresenter;
import com.getit.app.persenters.questions.QuestionsCallback;
import com.getit.app.persenters.questions.QuestionsPresenter;
import com.getit.app.ui.activities.admin.QuestionActivity;
import com.getit.app.ui.adptres.QuestionsAdapter;
import com.getit.app.ui.fragments.SolverQuestionArticleBottomSheet;
import com.getit.app.ui.fragments.SolverQuestionMultiChoicesBottomSheet;
import com.getit.app.ui.fragments.SolverQuestionTrueFalseBottomSheet;
import com.getit.app.utilities.ToastUtils;
import com.getit.app.utilities.helpers.LocaleHelper;
import com.getit.app.utilities.helpers.StorageHelper;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends BaseActivity implements
        QuestionsCallback, AnswersCallback,
        QuestionsAdapter.OnQuestionsClickListener,
        SolverQuestionMultiChoicesBottomSheet.OnQuestionMultiChoicesSolveCallback,
        SolverQuestionTrueFalseBottomSheet.OnQuestionTrueFalseSolveCallback,
        SolverQuestionArticleBottomSheet.OnQuestionArticleSolveCallback {
    private ActivityQuestonsBinding binding;
    private QuestionsPresenter presenter;
    private AnswersPresenter answersPresenter;
    private QuestionsAdapter adapter;
    private List<Question> questions, searchedQuestions;
    private Lesson lesson;
    private User currentUser;
    private AnswerStudent answerStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityQuestonsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = StorageHelper.getCurrentUser();
        presenter = new QuestionsPresenter(this);
        answersPresenter = new AnswersPresenter(this);
        lesson = getIntent().getParcelableExtra(Constants.ARG_OBJECT);

        binding.btnAdd.setVisibility(StorageHelper.getCurrentUser().isAdmin() ? View.VISIBLE : View.GONE);
        binding.refreshLayout.setColorSchemeResources(R.color.refreshColor1, R.color.refreshColor2, R.color.refreshColor3, R.color.refreshColor4);
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question question = new Question();
                question.setLessonId(lesson.getId());
                question.setLessonName(lesson.getName());
                openQuestionActivity(question);
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
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuestionsAdapter(searchedQuestions, this);
        binding.recyclerView.setAdapter(adapter);
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
    public void onGetAnswerComplete(AnswerStudent answer) {
        if (answer == null) {
            answer = new AnswerStudent(lesson.getId(), currentUser.getId());
        }

        for (Question question : questions) {
            var object = answer.getAnswer(question);
        }

        this.answerStudent = answer;
        answersPresenter.save(answer);
    }

    @Override
    public void onSaveAnswerComplete() {
        ToastUtils.longToast(R.string.str_message_added_successfully);
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
        binding.message.setVisibility(View.GONE);
        if (searchedQuestions.isEmpty()) {
            binding.message.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onQuestionViewListener(int position) {
        Question question = searchedQuestions.get(position);
        Answer answer = this.answerStudent.getAnswer(question);
        if (!answer.isAnswered()) {
            switch (question.getType()) {
                case Constants.QUESTION_TYPE_MULTI_CHOICE ->
                        SolverQuestionMultiChoicesBottomSheet.newInstance(answer, Constants.SHOW_MODE_VIEW).show(getSupportFragmentManager(), "");
                case Constants.QUESTION_TYPE_TRUE_FALSE ->
                        SolverQuestionTrueFalseBottomSheet.newInstance(answer, Constants.SHOW_MODE_VIEW).show(getSupportFragmentManager(), "");
                case Constants.QUESTION_TYPE_ARTICLE ->
                        SolverQuestionArticleBottomSheet.newInstance(answer, Constants.SHOW_MODE_VIEW).show(getSupportFragmentManager(), "");
            }
        } else {
            ToastUtils.longToast("Question already answered");
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
    public void onQuestionDeleteListener(int position) {
        if (position >= 0 && position < searchedQuestions.size()) {
            Question question = searchedQuestions.get(position);
            int index = questions.indexOf(question);
            if (index >= 0 && index < questions.size()) {
                questions.remove(index);
            }

            presenter.delete(question);
        }
    }

    @Override
    public void onQuestionEditListener(int position) {
        if (position >= 0 && position < searchedQuestions.size()) {
            Question user = searchedQuestions.get(position);
            openQuestionActivity(user);
        }
    }

    @Override
    public void onDeleteQuestionComplete(Question Question) {
        int index = searchedQuestions.indexOf(Question);
        if (index != -1) {
            searchedQuestions.remove(index);
            adapter.notifyItemRemoved(index);
        }
        Toast.makeText(this, R.string.str_message_delete_successfully, Toast.LENGTH_LONG).show();
    }

    private void openQuestionActivity(Question Question) {
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra(Constants.ARG_OBJECT, Question);
        startActivity(intent);
    }
}