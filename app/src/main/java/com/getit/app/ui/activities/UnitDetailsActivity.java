package com.getit.app.ui.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.getit.app.Constants;
import com.getit.app.R;
import com.getit.app.databinding.ActivityUnitDetailsBinding;
import com.getit.app.models.Lesson;
import com.getit.app.models.Unit;
import com.getit.app.models.User;
import com.getit.app.persenters.lesson.LessonsCallback;
import com.getit.app.persenters.lesson.LessonsPresenter;
import com.getit.app.ui.adptres.LessonsAdapter;
import com.getit.app.utilities.ToastUtils;
import com.getit.app.utilities.helpers.LocaleHelper;
import com.getit.app.utilities.helpers.StorageHelper;

import java.util.ArrayList;
import java.util.List;

public class UnitDetailsActivity extends BaseActivity implements LessonsCallback, LessonsAdapter.OnLessonClickListener {
    private ActivityUnitDetailsBinding binding;
    private LessonsPresenter presenter;
    private LessonsAdapter adapter;
    private List<Lesson> lessons, searchedLessons;
    private User currentUser;
    private Unit unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityUnitDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new LessonsPresenter(this);

        currentUser = StorageHelper.getCurrentUser();

        unit = getIntent().getParcelableExtra(Constants.ARG_OBJECT);
        setUpActionBar();

        binding.btnAdd.setVisibility(StorageHelper.getCurrentUser().isAdmin() ? View.VISIBLE : View.GONE);
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLesson(new Lesson());
            }
        });

        lessons = new ArrayList<>();
        searchedLessons = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LessonsAdapter(searchedLessons, this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    @SuppressLint("WrongConstant")
    private void setUpActionBar() {
        binding.appBarLayout.toolbar.setTitle(unit.getName());
        setSupportActionBar(binding.appBarLayout.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        binding.appBarLayout.toolbar.setNavigationIcon(R.drawable.back_to_home_button);
    }

    @Override
    public void onGetLessonsComplete(List<Lesson> lessons) {
        this.lessons.clear();
        this.lessons.addAll(lessons);

        search(binding.textSearch.getText().toString());
    }

    private void load() {
        presenter.getLessons(unit.getId());
    }

    private void search(String searchedText) {
        searchedLessons.clear();
        if (!searchedText.isEmpty()) {
            for (Lesson lesson : lessons) {
                if (isMatched(lesson, searchedText)) {
                    searchedLessons.add(lesson);
                }
            }
        } else {
            searchedLessons.addAll(lessons);
        }

        refresh();
    }

    private void refresh() {
        binding.messageExamsEmpty.setVisibility(View.GONE);
        if (searchedLessons.isEmpty()) {
            binding.messageExamsEmpty.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }

    private boolean isMatched(Lesson lesson, String text) {
        String searchedText = text.toLowerCase();
        boolean result = lesson.getName().toLowerCase().contains(searchedText);
        return result;
    }

    @Override
    public void onLessonViewListener(Lesson lesson) {
        Intent intent = null;
        if (currentUser.isAdmin()) {
            intent = new Intent(this, QuestionsActivity.class);
        } else if (currentUser.isTeacher()) {
            intent = new Intent(this, StudentsActivity.class);
        } else {
            intent = new Intent(this, QuestionsViewerActivity.class);
        }

        intent.putExtra(Constants.ARG_OBJECT, lesson);
        startActivity(intent);
    }

    @Override
    public void onLessonEditListener(Lesson lesson) {
        addLesson(lesson);
    }

    @Override
    public void onLessonDeleteListener(Lesson lesson) {
        presenter.delete(lesson);
    }

    @Override
    public void onDeleteLessonComplete(Lesson lesson) {
        int index = lessons.indexOf(lesson);
        if (index != -1) {
            lessons.remove(index);
            adapter.notifyItemRemoved(index);
        }
        ToastUtils.longToast(R.string.str_message_delete_successfully);
        load();
    }

    @Override
    public void onSaveLessonComplete() {
        ToastUtils.longToast(R.string.str_message_added_successfully);
        if (dialog != null) {
            dialog.dismiss();
        }
        load();
    }

    private AlertDialog dialog;

    private void addLesson(Lesson lesson) {
        lesson.setUnitId(this.unit.getId());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.str_add_new);

        final EditText input = new EditText(this);
        input.setText(lesson.getName());
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
                var exist = lessons.stream().anyMatch(grade -> {
                    return grade.getName().equalsIgnoreCase(text);
                });
                if (exist) {
                    ToastUtils.longToast("Sorry " + text + " already exist");
                } else {
                    lesson.setName(text);
                    presenter.save(lesson);
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