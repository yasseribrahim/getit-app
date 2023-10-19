package com.getit.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.getit.app.R;
import com.getit.app.databinding.ItemLessonBinding;
import com.getit.app.models.Lesson;
import com.getit.app.models.User;
import com.getit.app.utilities.DatesUtils;
import com.getit.app.utilities.helpers.StorageHelper;

import java.util.List;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.ViewHolder> {
    private List<Lesson> lessons;
    private OnLessonClickListener listener;
    private User currentUser;

    public LessonsAdapter(List<Lesson> lessons, OnLessonClickListener listener) {
        this.lessons = lessons;
        this.listener = listener;
        this.currentUser = StorageHelper.getCurrentUser();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);

        holder.binding.name.setText(lesson.getName());
        holder.binding.date.setText(DatesUtils.formatDateOnly(lesson.getDate()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return lessons.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemLessonBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemLessonBinding.bind(view);
            binding.containerActions.setVisibility(currentUser.isAdmin() ? View.VISIBLE : View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onLessonViewListener(lessons.get(getAdapterPosition()));
                }
            });
            binding.containerRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onLessonDeleteListener(lessons.get(getAdapterPosition()));
                }
            });
            binding.containerEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onLessonEditListener(lessons.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnLessonClickListener {
        void onLessonViewListener(Lesson lesson);

        void onLessonEditListener(Lesson lesson);

        void onLessonDeleteListener(Lesson lesson);
    }
}