package com.fully.code.base.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.fully.code.base.models.Course;
import com.fully.code.base.models.User;
import com.fully.code.base.utilities.DatesUtils;
import com.fully.code.base.utilities.UIUtils;
import com.fully.code.base.utilities.helpers.StorageHelper;
import com.getit.app.R;
import com.getit.app.databinding.ItemCourseBinding;

import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {
    private List<Course> courses;
    private OnItemClickListener listener;
    private User currentUser;

    public CoursesAdapter(List<Course> courses, OnItemClickListener listener) {
        this.courses = courses;
        this.listener = listener;
        this.currentUser = StorageHelper.getCurrentUser();
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
            binding.containerActions.setVisibility(currentUser.isAdmin() ? View.VISIBLE : View.GONE);
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