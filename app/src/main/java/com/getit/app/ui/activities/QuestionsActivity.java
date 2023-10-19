package com.getit.app.ui.activities;

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
import com.getit.app.models.Lesson;
import com.getit.app.models.Question;
import com.getit.app.persenters.oldquestions.QuestionsCallback;
import com.getit.app.persenters.oldquestions.QuestionsPresenter;
import com.getit.app.ui.activities.admin.QuestionActivity;
import com.getit.app.ui.adptres.QuestionsAdapter;
import com.getit.app.utilities.helpers.LocaleHelper;
import com.getit.app.utilities.helpers.StorageHelper;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends BaseActivity implements QuestionsCallback, QuestionsAdapter.OnQuestionsClickListener {
    private ActivityQuestonsBinding binding;
    private QuestionsPresenter presenter;
    private QuestionsAdapter adapter;
    private List<Question> questions, searchedQuestions;
    private Lesson lesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityQuestonsBinding.inflate(getLayoutInflater());

        presenter = new QuestionsPresenter(this);
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
                openQuestionActivity(null);
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
    }

    private void load() {
        presenter.getQuestions();
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

//    @Override
//    public void onGetQuestionsComplete(List<Question> users) {
//        this.questions.clear();
//        this.questions.addAll(users);
//        search(binding.textSearch.getText().toString());
//    }

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
        Question user = searchedQuestions.get(position);
    }

    @Override
    public void onQuestionDeleteListener(int position) {
        if (position >= 0 && position < searchedQuestions.size()) {
            Question question = searchedQuestions.get(position);
            int index = questions.indexOf(question);
            if (index >= 0 && index < questions.size()) {
                questions.remove(index);
            }

           // presenter.delete(question);
        }
    }

    @Override
    public void onQuestionEditListener(int position) {
        if (position >= 0 && position < searchedQuestions.size()) {
            Question user = searchedQuestions.get(position);
            openQuestionActivity(user);
        }
    }

//    @Override
//    public void onDeleteQuestionComplete(Question Question) {
//        int index = searchedQuestions.indexOf(Question);
//        if (index != -1) {
//            searchedQuestions.remove(index);
//            adapter.notifyItemRemoved(index);
//        }
//        Toast.makeText(this, R.string.str_message_delete_successfully, Toast.LENGTH_LONG).show();
//    }

    private void openQuestionActivity(Question Question) {
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra(Constants.ARG_OBJECT, Question);
        startActivity(intent);
    }
}