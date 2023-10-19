package com.getit.app.ui.adptres;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.getit.app.R;
import com.getit.app.databinding.ItemQuestionSelectorBinding;
import com.getit.app.models.OldQuestion;
import com.getit.app.utilities.UIUtils;

import java.util.List;

public class QuestionSelectorAdapter extends RecyclerView.Adapter<QuestionSelectorAdapter.ViewHolder> {
    private List<OldQuestion> oldQuestions;
    private OnItemClickListener listener;
    private List<OldQuestion> selectedOldQuestions;

    // data is passed into the constructor
    public QuestionSelectorAdapter(List<OldQuestion> oldQuestions, OnItemClickListener listener, List<OldQuestion> selectedOldQuestions) {
        this.oldQuestions = oldQuestions;
        this.listener = listener;
        this.selectedOldQuestions = selectedOldQuestions;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_selector, parent, false);
        return new ViewHolder(view);
    }

    public void handelSelectedCourses(OldQuestion oldQuestion) {
        if (selectedOldQuestions.contains(oldQuestion)) {
            selectedOldQuestions.remove(oldQuestion);
        } else {
            selectedOldQuestions.add(oldQuestion);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OldQuestion oldQuestion = oldQuestions.get(position);

        Drawable drawable = null;
        if (selectedOldQuestions.contains(oldQuestion)) {
            drawable = ResourcesCompat.getDrawable(holder.binding.check.getResources(), R.drawable.ic_check, null);
        } else {
            drawable = ResourcesCompat.getDrawable(holder.binding.check.getResources(), R.drawable.ic_unchecked, null);
        }
        holder.binding.check.setImageDrawable(drawable);
        holder.binding.name.setText(oldQuestion.getTitle());
        holder.binding.type.setText(UIUtils.getQuestionType(oldQuestion.getType()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return oldQuestions.size();
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
                    if (getAdapterPosition() >= 0 && getAdapterPosition() < oldQuestions.size()) {
                        if (listener != null) listener.onCourseClickListener(getAdapterPosition());
                        handelSelectedCourses(oldQuestions.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onCourseClickListener(int position);
    }
}