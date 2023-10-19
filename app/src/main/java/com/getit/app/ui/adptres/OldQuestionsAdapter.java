package com.getit.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.getit.app.R;
import com.getit.app.databinding.ItemQuestionBinding;
import com.getit.app.models.OldQuestion;
import com.getit.app.models.User;
import com.getit.app.utilities.UIUtils;
import com.getit.app.utilities.helpers.StorageHelper;

import java.util.List;

public class OldQuestionsAdapter extends RecyclerView.Adapter<OldQuestionsAdapter.ViewHolder> {
    private List<OldQuestion> oldQuestions;
    private OnQuestionsClickListener listener;
    private User currentUser;

    public OldQuestionsAdapter(List<OldQuestion> oldQuestions, OnQuestionsClickListener listener) {
        this.oldQuestions = oldQuestions;
        this.listener = listener;
        this.currentUser = StorageHelper.getCurrentUser();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_old, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OldQuestion oldQuestion = oldQuestions.get(position);

        holder.binding.title.setText(oldQuestion.getTitle());
        holder.binding.course.setText(oldQuestion.getCourseName());
        holder.binding.description.setText(oldQuestion.getDescription());
        holder.binding.description.setVisibility(oldQuestion.getDescription() != null && !oldQuestion.getDescription().isEmpty() ? View.VISIBLE : View.GONE);
        holder.binding.type.setText(UIUtils.getQuestionType(oldQuestion.getType()));
    }

    private int getSize(String id) {
        return oldQuestions.size();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return oldQuestions.size();
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