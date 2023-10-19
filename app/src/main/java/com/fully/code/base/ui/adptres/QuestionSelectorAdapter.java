package com.fully.code.base.ui.adptres;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fully.code.base.models.Question;
import com.fully.code.base.utilities.UIUtils;
import com.getit.app.R;
import com.getit.app.databinding.ItemQuestionSelectorBinding;

import java.util.List;

public class QuestionSelectorAdapter extends RecyclerView.Adapter<QuestionSelectorAdapter.ViewHolder> {
    private List<Question> questions;
    private OnItemClickListener listener;
    private List<Question> selectedQuestions;

    // data is passed into the constructor
    public QuestionSelectorAdapter(List<Question> questions, OnItemClickListener listener, List<Question> selectedQuestions) {
        this.questions = questions;
        this.listener = listener;
        this.selectedQuestions = selectedQuestions;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_selector, parent, false);
        return new ViewHolder(view);
    }

    public void handelSelectedCourses(Question question) {
        if (selectedQuestions.contains(question)) {
            selectedQuestions.remove(question);
        } else {
            selectedQuestions.add(question);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question question = questions.get(position);

        Drawable drawable = null;
        if (selectedQuestions.contains(question)) {
            drawable = ResourcesCompat.getDrawable(holder.binding.check.getResources(), R.drawable.ic_check, null);
        } else {
            drawable = ResourcesCompat.getDrawable(holder.binding.check.getResources(), R.drawable.ic_unchecked, null);
        }
        holder.binding.check.setImageDrawable(drawable);
        holder.binding.name.setText(question.getTitle());
        holder.binding.type.setText(UIUtils.getQuestionType(question.getType()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return questions.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemQuestionSelectorBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemQuestionSelectorBinding.bind(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() >= 0 && getAdapterPosition() < questions.size()) {
                        if (listener != null) listener.onCourseClickListener(getAdapterPosition());
                        handelSelectedCourses(questions.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onCourseClickListener(int position);
    }
}