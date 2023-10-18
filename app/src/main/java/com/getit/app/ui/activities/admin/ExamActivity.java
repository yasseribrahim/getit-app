package com.getit.app.ui.activities.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.getit.app.Constants;
import com.getit.app.R;
import com.getit.app.databinding.ActivityExamBinding;
import com.getit.app.models.Course;
import com.getit.app.models.Exam;
import com.getit.app.models.Question;
import com.getit.app.persenters.exams.ExamsCallback;
import com.getit.app.persenters.exams.ExamsPresenter;
import com.getit.app.persenters.questions.QuestionsCallback;
import com.getit.app.persenters.questions.QuestionsPresenter;
import com.getit.app.ui.activities.BaseActivity;
import com.getit.app.ui.adptres.QuestionSelectorAdapter;
import com.getit.app.ui.fragments.CourseSelectorBottomSheet;
import com.getit.app.ui.fragments.GradeSelectorBottomSheet;
import com.getit.app.utilities.ToastUtils;
import com.getit.app.utilities.UIUtils;
import com.getit.app.utilities.helpers.LocaleHelper;

import java.util.ArrayList;
import java.util.List;

public class ExamActivity extends BaseActivity implements ExamsCallback, QuestionsCallback, CourseSelectorBottomSheet.ItemClickListener, GradeSelectorBottomSheet.ItemClickListener, QuestionSelectorAdapter.OnItemClickListener {
    private ActivityExamBinding binding;
    private ExamsPresenter presenter;
    private QuestionsPresenter questionsPresenter;
    private QuestionSelectorAdapter adapter;
    private List<Question> questions;
    private Exam exam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityExamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new ExamsPresenter(this);
        questionsPresenter = new QuestionsPresenter(this);

        exam = getIntent().getParcelableExtra(Constants.ARG_OBJECT);
        if (exam == null) {
            exam = new Exam();
        }
        bind();

        binding.course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exam.getGrade() > 0) {
                    CourseSelectorBottomSheet.newInstance(exam.getCourseId(), exam.getGrade()).show(getSupportFragmentManager(), "");
                } else {
                    ToastUtils.longToast("Please select grade firstly!");
                }
            }
        });

        binding.grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradeSelectorBottomSheet.newInstance(exam.getGrade()).show(getSupportFragmentManager(), "");
            }
        });

        questions = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuestionSelectorAdapter(questions, this, exam.getQuestions());
        binding.recyclerView.setAdapter(adapter);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.title.getText().toString().trim();
                String totalScoreString = binding.totalScore.getText().toString();
                String passScoreString = binding.passScore.getText().toString();
                int totalScore = 0;
                int passScore = 0;
                try {
                    totalScore = Integer.parseInt(totalScoreString);
                    passScore = Integer.parseInt(passScoreString);
                } catch (Exception ex) {
                }

                if (title.isEmpty()) {
                    binding.title.setError(getString(R.string.str_title_hint));
                    binding.title.requestFocus();
                    return;
                }
                if (exam.getGrade() <= 0) {
                    binding.grade.setError(getString(R.string.str_grade_invalid));
                    ToastUtils.longToast(R.string.str_grade_invalid);
                    binding.grade.requestFocus();
                    return;
                }
                if (exam.getCourseId() == null) {
                    binding.course.setError(getString(R.string.str_course_hint));
                    ToastUtils.longToast(R.string.str_course_hint);
                    binding.course.requestFocus();
                    return;
                }
                if (totalScore <= 0) {
                    binding.totalScore.setError(getString(R.string.str_total_score_hint));
                    binding.totalScore.requestFocus();
                    return;
                }
                if (passScore <= 0) {
                    binding.passScore.setError(getString(R.string.str_pass_score_hint));
                    binding.passScore.requestFocus();
                    return;
                }
                if (exam.getQuestions().isEmpty()) {
                    ToastUtils.longToast(R.string.str_questions_list_hint);
                    return;
                }

                exam.setTitle(title);
                exam.setDescription(binding.description.getText().toString());
                exam.setActive(binding.isActive.isChecked());
                exam.setScoreTotal(totalScore);
                exam.setScorePass(passScore);
                presenter.save(exam);
            }
        });
    }

    @Override
    public void onGetQuestionsComplete(List<Question> questions) {
        exam.getQuestions().clear();
        this.questions.clear();
        this.questions.addAll(questions);
        binding.emptyMessage.setVisibility(questions.isEmpty() ? View.VISIBLE : View.GONE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveExamComplete() {
        Toast.makeText(this, R.string.str_message_added_successfully, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        Toast.makeText(this, getString(R.string.str_save_fail, message), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCourseClick(Course course) {
        exam.setCourseId(course.getId());
        exam.setCourseName(course.getName());
        binding.course.setText(course.getName());
        loadQuestion();
    }

    private void loadQuestion() {
        if (exam.getCourseId() != null && !exam.getCourseId().isEmpty()) {
            questionsPresenter.getQuestions(exam.getCourseId());
        }
    }

    private void bind() {
        binding.title.setText(exam.getTitle());
        binding.description.setText(exam.getDescription());
        binding.grade.setText(UIUtils.getGrade(exam.getGrade()));
        binding.course.setText(exam.getCourseName());
        binding.isActive.setChecked(exam.isActive());
        binding.totalScore.setText(exam.getScoreTotal() + "");
        binding.passScore.setText(exam.getScorePass() + "");

        loadQuestion();
    }

    @Override
    public void onGradeClick(int id) {
        exam.setGrade(id);
        binding.grade.setText(UIUtils.getGrade(id));
        exam.setCourseId(null);
        exam.setCourseName(null);
        binding.course.setText("");
    }

    @Override
    public void onCourseClickListener(int position) {

    }
}