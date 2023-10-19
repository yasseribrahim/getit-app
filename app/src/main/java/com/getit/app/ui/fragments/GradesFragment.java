package com.getit.app.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.getit.app.Constants;
import com.getit.app.R;
import com.getit.app.databinding.FragmentGradesBinding;
import com.getit.app.models.Grade;
import com.getit.app.persenters.grade.GradesCallback;
import com.getit.app.persenters.grade.GradesPresenter;
import com.getit.app.ui.activities.GradeDetailsActivity;
import com.getit.app.ui.adptres.GradesAdapter;
import com.getit.app.utilities.ToastUtils;
import com.getit.app.utilities.helpers.StorageHelper;

import java.util.ArrayList;
import java.util.List;

public class GradesFragment extends Fragment implements GradesCallback, GradesAdapter.OnGradeClickListener {
    private FragmentGradesBinding binding;
    private GradesPresenter presenter;
    private GradesAdapter adapter;
    private List<Grade> grades, searchedGrades;

    public static GradesFragment newInstance() {
        Bundle args = new Bundle();
        GradesFragment fragment = new GradesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGradesBinding.inflate(inflater);

        presenter = new GradesPresenter(this);

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
                addGrades(new Grade());
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

        grades = new ArrayList<>();
        searchedGrades = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GradesAdapter(searchedGrades, this);
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private void load() {
        presenter.getGrades();
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
    public void onGetGradesComplete(List<Grade> courses) {
        this.grades.clear();
        this.grades.addAll(courses);
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
        searchedGrades.clear();
        if (!searchedText.isEmpty()) {
            for (Grade course : grades) {
                if (isMatched(course, searchedText)) {
                    searchedGrades.add(course);
                }
            }
        } else {
            searchedGrades.addAll(grades);
        }

        refresh();
    }

    private boolean isMatched(Grade course, String text) {
        String searchedText = text.toLowerCase();
        boolean result = course.getName().toLowerCase().contains(searchedText);
        return result;
    }

    private void refresh() {
        binding.message.setVisibility(View.GONE);
        if (searchedGrades.isEmpty()) {
            binding.message.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGradeViewListener(Grade grade) {
        Intent intent = new Intent(getContext(), GradeDetailsActivity.class);
        intent.putExtra(Constants.ARG_OBJECT, grade);
        startActivity(intent);
    }

    @Override
    public void onGradeDeleteViewListener(Grade grade) {
        int index = grades.indexOf(grade);
        if (index >= 0 && index < grades.size()) {
            grades.remove(index);
        }

        presenter.delete(grade);
    }

    @Override
    public void onGradeEditListener(Grade grade) {
        addGrades(grade);
    }

    @Override
    public void onDeleteGradeComplete(Grade grade) {
        int index = searchedGrades.indexOf(grade);
        if (index != -1) {
            searchedGrades.remove(index);
            adapter.notifyItemRemoved(index);
        }
        Toast.makeText(getContext(), R.string.str_message_delete_successfully, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveGradeComplete() {
        ToastUtils.longToast(R.string.str_message_added_successfully);
        if (dialog != null) {
            dialog.dismiss();
        }
        load();
    }

    private AlertDialog dialog;

    private void addGrades(Grade grade) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.str_add_new);

        final EditText input = new EditText(getContext());
        input.setText(grade.getName());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.str_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                if (text.isEmpty()) {
                    ToastUtils.longToast(R.string.str_enter_value);
                    return;
                }
                var exist = grades.stream().anyMatch(grade -> {
                    return grade.getName().equalsIgnoreCase(text);
                });
                if (exist) {
                    ToastUtils.longToast("Sorry " + text + " already exist");
                } else {
                    grade.setName(text);
                    presenter.save(grade);
                }
            }
        });
        builder.setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog = builder.show();
    }
}