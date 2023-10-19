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
import com.getit.app.models.Course;
import com.getit.app.models.OldQuestion;
import com.getit.app.persenters.courses.CoursesCallback;
import com.getit.app.persenters.courses.CoursesPresenter;
import com.getit.app.persenters.oldquestions.QuestionsCallback;
import com.getit.app.persenters.oldquestions.QuestionsPresenter;
import com.getit.app.ui.activities.admin.QuestionActivity;
import com.getit.app.ui.adptres.OldQuestionsAdapter;
import com.getit.app.utilities.helpers.StorageHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class QuestionsFragment extends Fragment implements QuestionsCallback, CoursesCallback, OldQuestionsAdapter.OnQuestionsClickListener {
    private FragmentQuestonsBinding binding;
    private QuestionsPresenter presenter;
    private CoursesPresenter coursesPresenter;
    private OldQuestionsAdapter adapter;
    private List<OldQuestion> oldQuestions, searchedOldQuestions;

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

        oldQuestions = new ArrayList<>();
        searchedOldQuestions = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OldQuestionsAdapter(searchedOldQuestions, this);
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
    public void onGetQuestionsComplete(List<OldQuestion> users) {
        this.oldQuestions.clear();
        this.oldQuestions.addAll(users);
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
        searchedOldQuestions.clear();
        if (!searchedText.isEmpty()) {
            for (OldQuestion user : oldQuestions) {
                if (isMatched(user, searchedText)) {
                    searchedOldQuestions.add(user);
                }
            }
        } else {
            searchedOldQuestions.addAll(oldQuestions);
        }

        refresh();
    }

    private boolean isMatched(OldQuestion oldQuestion, String text) {
        String searchedText = text.toLowerCase();
        boolean result = oldQuestion.getTitle().toLowerCase().contains(searchedText) ||
                (oldQuestion.getDescription() != null && oldQuestion.getDescription().toLowerCase().contains(searchedText)) ||
                (oldQuestion.getCourseName() != null && oldQuestion.getCourseName().toLowerCase().contains(searchedText));
        return result;
    }

    private void refresh() {
        binding.message.setVisibility(View.GONE);
        if (searchedOldQuestions.isEmpty()) {
            binding.message.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onQuestionViewListener(int position) {
        OldQuestion user = searchedOldQuestions.get(position);
    }

    @Override
    public void onQuestionDeleteListener(int position) {
        if (position >= 0 && position < searchedOldQuestions.size()) {
            OldQuestion oldQuestion = searchedOldQuestions.get(position);
            int index = oldQuestions.indexOf(oldQuestion);
            if (index >= 0 && index < oldQuestions.size()) {
                oldQuestions.remove(index);
            }

            presenter.delete(oldQuestion);
        }
    }

    @Override
    public void onQuestionEditListener(int position) {
        if (position >= 0 && position < searchedOldQuestions.size()) {
            OldQuestion user = searchedOldQuestions.get(position);
            openQuestionActivity(user);
        }
    }

    @Override
    public void onDeleteQuestionComplete(OldQuestion oldQuestion) {
        int index = searchedOldQuestions.indexOf(oldQuestion);
        if (index != -1) {
            searchedOldQuestions.remove(index);
            adapter.notifyItemRemoved(index);
        }
        Toast.makeText(getContext(), R.string.str_message_delete_successfully, Toast.LENGTH_LONG).show();
    }

    private void openQuestionActivity(OldQuestion oldQuestion) {
        Intent intent = new Intent(getContext(), QuestionActivity.class);
        intent.putExtra(Constants.ARG_OBJECT, oldQuestion);
        startActivity(intent);
    }
}