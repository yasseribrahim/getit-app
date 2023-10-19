package com.fully.code.base.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.fully.code.base.models.Question;
import com.fully.code.base.models.User;
import com.fully.code.base.utilities.UIUtils;
import com.fully.code.base.utilities.helpers.StorageHelper;
import com.getit.app.R;
import com.getit.app.databinding.ItemQuestionBinding;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    private List<Question> questions;
    private OnQuestionsClickListener listener;
    private User currentUser;

    public QuestionsAdapter(List<Question> questions, OnQuestionsClickListener listener) {
        this.questions = questions;
        this.listener = listener;
        this.currentUser = StorageHelper.getCurrentUser();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_old, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question question = questions.get(position);

        holder.binding.title.setText(question.getTitle());
        holder.binding.course.setText(question.getCourseName());
        holder.binding.description.setText(question.getDescription());
        holder.binding.description.setVisibility(question.getDescription() != null && !question.getDescription().isEmpty() ? View.VISIBLE : View.GONE);
        holder.binding.type.setText(UIUtils.getQuestionType(question.getType()));
    }

    private int getSize(String id) {
        return questions.size();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return questions.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemQuestionBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemQuestionBinding.bind(view);
            binding.containerActions.setVisibility(currentUser.isAdmin() ? View.VISIBLE : View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onQuestionViewListener(getAdapterPosition());
                }
            });
            binding.containerRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onQuestionDeleteListener(getAdapterPosition());
                }
            });
            binding.containerEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onQuestionEditListener(getAdapterPosition());
                }
            });
        }
    }

    public interface OnQuestionsClickListener {
        void onQuestionViewListener(int position);

        void onQuestionEditListener(int position);

        void onQuestionDeleteListener(int position);
    }
}