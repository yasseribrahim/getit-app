package com.getit.app.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.getit.app.Constants;
import com.getit.app.R;
import com.getit.app.databinding.FragmentQuestonsBinding;
import com.getit.app.models.Question;
import com.getit.app.persenters.questions.QuestionsCallback;
import com.getit.app.persenters.questions.QuestionsPresenter;
import com.getit.app.ui.activities.QuestionActivity;
import com.getit.app.ui.adptres.QuestionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuestionsFragment extends Fragment implements QuestionsCallback, QuestionsAdapter.OnQuestionsClickListener {
    private FragmentQuestonsBinding binding;
    private QuestionsPresenter presenter;
    private QuestionsAdapter usersAdapter;
    private List<Question> questions, searchedQuestions;

    public static QuestionsFragment newInstance() {
        Bundle args = new Bundle();
        QuestionsFragment fragment = new QuestionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuestonsBinding.inflate(inflater);

        presenter = new QuestionsPresenter(this);

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
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersAdapter = new QuestionsAdapter(searchedQuestions, this);
        binding.recyclerView.setAdapter(usersAdapter);

        return binding.getRoot();
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
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    public void onGetQuestionsComplete(List<Question> users) {
        this.questions.clear();
        this.questions.addAll(users);
        search(binding.textSearch.getText().toString());
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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

    private boolean isMatched(Question question, String text) {
        String searchedText = text.toLowerCase();
        boolean result = question.getTitle().toLowerCase().contains(searchedText) ||
                (question.getDescription() != null && question.getDescription().toLowerCase().contains(searchedText)) ||
                (question.getCourseName() != null && question.getCourseName().toLowerCase().contains(searchedText));
        return result;
    }

    private void refresh() {
        binding.message.setVisibility(View.GONE);
        if (searchedQuestions.isEmpty()) {
            binding.message.setVisibility(View.VISIBLE);
        }

        usersAdapter.notifyDataSetChanged();
    }

    @Override
    public void onQuestionViewListener(int position) {
        Question user = searchedQuestions.get(position);
    }

    @Override
    public void onQuestionDeleteListener(int position) {
        if (position >= 0 && position < searchedQuestions.size()) {
            Question user = searchedQuestions.get(position);
            int index = questions.indexOf(user);
            searchedQuestions.remove(position);
            if (index >= 0 && index < questions.size()) {
                questions.remove(position);
            }

            presenter.save(user);
            onDeleteQuestionComplete(position);
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
    public void onDeleteQuestionComplete(int position) {
        Toast.makeText(getContext(), R.string.str_message_delete_successfully, Toast.LENGTH_LONG).show();
        usersAdapter.notifyItemRemoved(position);
    }

    private void openQuestionActivity(Question question) {
        Intent intent = new Intent(getContext(), QuestionActivity.class);
        intent.putExtra(Constants.ARG_OBJECT, question);
        startActivity(intent);
    }
}