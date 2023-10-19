package com.fully.code.base.ui.fragments;

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

import com.fully.code.base.Constants;
import com.fully.code.base.models.Course;
import com.fully.code.base.models.Question;
import com.fully.code.base.persenters.courses.CoursesCallback;
import com.fully.code.base.persenters.courses.CoursesPresenter;
import com.fully.code.base.persenters.questions.QuestionsCallback;
import com.fully.code.base.persenters.questions.QuestionsPresenter;
import com.fully.code.base.ui.activities.admin.QuestionActivity;
import com.fully.code.base.ui.adptres.QuestionsAdapter;
import com.fully.code.base.utilities.helpers.StorageHelper;
import com.getit.app.R;
import com.getit.app.databinding.FragmentQuestonsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class QuestionsFragment extends Fragment implements QuestionsCallback, CoursesCallback, QuestionsAdapter.OnQuestionsClickListener {
    private FragmentQuestonsBinding binding;
    private QuestionsPresenter presenter;
    private CoursesPresenter coursesPresenter;
    private QuestionsAdapter adapter;
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
        coursesPresenter = new CoursesPresenter(this);

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
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new QuestionsAdapter(searchedQuestions, this);
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private void load() {
        if (StorageHelper.getCurrentUser().isAdmin()) {
            presenter.getQuestions();
        } else {
            coursesPresenter.getCourses(StorageHelper.getCurrentUser().getGrade());
        }
    }

    @Override
    public void onGetCoursesComplete(List<Course> courses) {
        List<String> coursesIds = new ArrayList<>();
        courses.forEach(new Consumer<Course>() {
            @Override
            public void accept(Course course) {
                coursesIds.add(course.getId());
            }
        });

        presenter.getQuestions(coursesIds.toArray(new String[]{}));
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
    public void onDeleteQuestionComplete(Question question) {
        int index = searchedQuestions.indexOf(question);
        if (index != -1) {
            searchedQuestions.remove(index);
            adapter.notifyItemRemoved(index);
        }
        Toast.makeText(getContext(), R.string.str_message_delete_successfully, Toast.LENGTH_LONG).show();
    }

    private void openQuestionActivity(Question question) {
        Intent intent = new Intent(getContext(), QuestionActivity.class);
        intent.putExtra(Constants.ARG_OBJECT, question);
        startActivity(intent);
    }
}