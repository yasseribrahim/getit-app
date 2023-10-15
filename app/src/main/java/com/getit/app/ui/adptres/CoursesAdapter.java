package com.getit.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.getit.app.R;
import com.getit.app.databinding.ItemCourseBinding;
import com.getit.app.models.Course;
import com.getit.app.utilities.DatesUtils;
import com.getit.app.utilities.UIUtils;

import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {
    private List<Course> courses;
    private OnItemClickListener listener;

    public CoursesAdapter(List<Course> courses, OnItemClickListener listener) {
        this.courses = courses;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Course course = courses.get(position);

        holder.binding.name.setText(course.getName());
        holder.binding.grade.setText(UIUtils.getGrade(course.getGrade()));
        holder.binding.date.setText(DatesUtils.formatDateOnly(course.getCreatedAt()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return courses.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCourseBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemCourseBinding.bind(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onItemViewListener(getAdapterPosition());
                }
            });
            binding.containerRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onDeleteItemViewListener(getAdapterPosition());
                }
            });
            binding.containerEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onItemEditListener(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemViewListener(int position);

        void onItemEditListener(int position);

        void onDeleteItemViewListener(int position);
    }
}