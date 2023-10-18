package com.getit.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.getit.app.R;
import com.getit.app.databinding.ItemExamBinding;
import com.getit.app.models.Exam;
import com.getit.app.utilities.DatesUtils;
import com.getit.app.utilities.UIUtils;

import java.util.List;

public class ExamsAdapter extends RecyclerView.Adapter<ExamsAdapter.ViewHolder> {
    private List<Exam> exams;
    private OnExamsClickListener listener;

    public ExamsAdapter(List<Exam> exams, OnExamsClickListener listener) {
        this.exams = exams;
        this.listener = listener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Exam exam = exams.get(position);

        holder.binding.title.setText(exam.getTitle());
        holder.binding.grade.setText(UIUtils.getGrade(exam.getGrade()));
        holder.binding.createdBy.setText(exam.getCreatedBy());
        holder.binding.course.setText(exam.getCourseName());
        holder.binding.description.setText(exam.getDescription());
        holder.binding.description.setVisibility(exam.getDescription() != null && !exam.getDescription().isEmpty() ? View.VISIBLE : View.GONE);
        holder.binding.date.setText(DatesUtils.formatDateOnly(exam.getDate()));
        int questionsNumber = exam.getQuestions() != null ? exam.getQuestions().size() : 0;
        holder.binding.questions.setText(holder.binding.questions.getResources().getString(R.string.str_questions_formatted, questionsNumber + ""));
        holder.binding.passScore.setText(holder.binding.passScore.getResources().getString(R.string.str_score_pass_formatted, exam.getScorePass() + ""));
        holder.binding.totalScore.setText(holder.binding.totalScore.getResources().getString(R.string.str_score_total_formatted, exam.getScoreTotal() + ""));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return exams.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemExamBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemExamBinding.bind(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onExamViewListener(getAdapterPosition());
                }
            });
            binding.containerRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onExamDeleteListener(getAdapterPosition());
                }
            });
            binding.containerEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onExamEditListener(getAdapterPosition());
                }
            });
        }
    }

    public interface OnExamsClickListener {
        void onExamViewListener(int position);

        void onExamEditListener(int position);

        void onExamDeleteListener(int position);
    }
}