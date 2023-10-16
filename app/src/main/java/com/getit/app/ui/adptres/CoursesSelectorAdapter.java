package com.getit.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.getit.app.R;
import com.getit.app.databinding.ItemCourseSelectorBinding;
import com.getit.app.models.Course;
import com.getit.app.utilities.UIUtils;

import java.util.List;

public class CoursesSelectorAdapter extends RecyclerView.Adapter<CoursesSelectorAdapter.ViewHolder> {
    private List<Course> courses;
    private OnItemClickListener listener;
    private String selectedCourse;

    // data is passed into the constructor
    public CoursesSelectorAdapter(List<Course> courses, OnItemClickListener listener, String selectedCourse) {
        this.courses = courses;
        this.listener = listener;
        this.selectedCourse = selectedCourse;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_selector, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.binding.check.setVisibility(course.getId().equalsIgnoreCase(selectedCourse) ? View.VISIBLE : View.GONE);
        holder.binding.name.setText(course.getName());
        holder.binding.grade.setText(UIUtils.getGrade(course.getGrade()));
    }

    private int getSize(String id) {
        return courses.size();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return courses.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCourseSelectorBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemCourseSelectorBinding.bind(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onCourseClickListener(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onCourseClickListener(int position);
    }
}