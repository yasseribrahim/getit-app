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
import com.getit.app.models.Exam;
import com.getit.app.persenters.exams.ExamsCallback;
import com.getit.app.persenters.exams.ExamsPresenter;
import com.getit.app.ui.activities.admin.ExamActivity;
import com.getit.app.ui.adptres.ExamsAdapter;
import com.getit.app.utilities.helpers.StorageHelper;

import java.util.ArrayList;
import java.util.List;

public class ExamsFragment extends Fragment implements ExamsCallback, ExamsAdapter.OnExamsClickListener {
    private FragmentQuestonsBinding binding;
    private ExamsPresenter presenter;
    private ExamsAdapter adapter;
    private List<Exam> exams, searchedExams;

    public static ExamsFragment newInstance() {
        Bundle args = new Bundle();
        ExamsFragment fragment = new ExamsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuestonsBinding.inflate(inflater);

        presenter = new ExamsPresenter(this);

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
                openExamActivity(null);
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

        exams = new ArrayList<>();
        searchedExams = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExamsAdapter(searchedExams, this);
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private void load() {
        if(StorageHelper.getCurrentUser().isAdmin()) {
            presenter.getExams();
        } else {
            presenter.getExams(StorageHelper.getCurrentUser().getGrade());
        }
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
    public void onGetExamsComplete(List<Exam> users) {
        this.exams.clear();
        this.exams.addAll(users);
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
        searchedExams.clear();
        if (!searchedText.isEmpty()) {
            for (Exam user : exams) {
                if (isMatched(user, searchedText)) {
                    searchedExams.add(user);
                }
            }
        } else {
            searchedExams.addAll(exams);
        }

        refresh();
    }

    private boolean isMatched(Exam question, String text) {
        String searchedText = text.toLowerCase();
        boolean result = question.getTitle().toLowerCase().contains(searchedText) || (question.getDescription() != null && question.getDescription().toLowerCase().contains(searchedText)) || (question.getCourseName() != null && question.getCourseName().toLowerCase().contains(searchedText));
        return result;
    }

    private void refresh() {
        binding.message.setVisibility(View.GONE);
        if (searchedExams.isEmpty()) {
            binding.message.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onExamViewListener(Exam exam) {

    }

    @Override
    public void onExamDeleteListener(int position) {
        if (position >= 0 && position < searchedExams.size()) {
            Exam exam = searchedExams.get(position);
            int index = exams.indexOf(exam);
            if (index >= 0 && index < exams.size()) {
                exams.remove(index);
            }

            presenter.delete(exam);
        }
    }

    @Override
    public void onExamEditListener(int position) {
        if (position >= 0 && position < searchedExams.size()) {
            Exam exam = searchedExams.get(position);
            openExamActivity(exam);
        }
    }

    @Override
    public void onDeleteExamComplete(Exam question) {
        int index = searchedExams.indexOf(question);
        if (index != -1) {
            searchedExams.remove(index);
            adapter.notifyItemRemoved(index);
        }
        Toast.makeText(getContext(), R.string.str_message_delete_successfully, Toast.LENGTH_LONG).show();
    }

    private void openExamActivity(Exam exam) {
        Intent intent = new Intent(getContext(), ExamActivity.class);
        intent.putExtra(Constants.ARG_OBJECT, exam);
        startActivity(intent);
    }
}