package com.getit.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.getit.app.R;
import com.getit.app.databinding.ItemQuestionViewerBinding;
import com.getit.app.models.Answer;
import com.getit.app.models.Question;
import com.getit.app.models.User;
import com.getit.app.utilities.UIUtils;
import com.getit.app.utilities.helpers.StorageHelper;

import java.util.List;

public class QuestionsViewerAdapter extends RecyclerView.Adapter<QuestionsViewerAdapter.ViewHolder> {
    private List<Question> questions;
    private List<Answer> answers;
    private OnQuestionsViewerClickListener listener;
    private User currentUser;

    public QuestionsViewerAdapter(List<Question> questions, List<Answer> answers, OnQuestionsViewerClickListener listener) {
        this.questions = questions;
        this.answers = answers;
        this.listener = listener;
        this.currentUser = StorageHelper.getCurrentUser();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_viewer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question question = questions.get(position);

        holder.binding.title.setText(question.getTitle());
        holder.binding.description.setText(question.getDescription());
        holder.binding.description.setVisibility(question.getDescription() != null && !question.getDescription().isEmpty() ? View.VISIBLE : View.GONE);
        holder.binding.type.setText(UIUtils.getQuestionType(question.getType()));

        int index = answers.indexOf(new Answer(question));
        if (index != -1) {
            Answer answer = answers.get(index);
            if (answer.getRight() != null) {
                if (answer.getRight()) {
                    holder.binding.getRoot().setBackgroundColor(ResourcesCompat.getColor(holder.binding.getRoot().getResources(), R.color.green_55, null));
                } else {
                    holder.binding.getRoot().setBackgroundColor(ResourcesCompat.getColor(holder.binding.getRoot().getResources(), R.color.red_55, null));
                }
            } else {
                holder.binding.getRoot().setBackgroundColor(ResourcesCompat.getColor(holder.binding.getRoot().getResources(), R.color.background, null));
            }
        } else {
            holder.binding.getRoot().setBackgroundColor(ResourcesCompat.getColor(holder.binding.getRoot().getResources(), R.color.background, null));
        }
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
        ItemQuestionViewerBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemQuestionViewerBinding.bind(view);
            binding.containerCorrection.setVisibility((currentUser.isAdmin() || currentUser.isTeacher()) ? View.VISIBLE : View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onQuestionViewListener(questions.get(getAdapterPosition()));
                }
            });
            binding.containerCorrectionTrue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onQuestionCorrectionTrueListener(questions.get(getAdapterPosition()));
                }
            });
            binding.containerCorrectionFalse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onQuestionCorrectionFalseListener(questions.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnQuestionsViewerClickListener {
        void onQuestionViewListener(Question question);

        void onQuestionCorrectionTrueListener(Question question);

        void onQuestionCorrectionFalseListener(Question question);
    }
}