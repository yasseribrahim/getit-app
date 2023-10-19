package com.getit.app.ui.adptres;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.getit.app.R;
import com.getit.app.databinding.ItemCourseSelectorBinding;
import com.getit.app.models.Course;
import com.getit.app.utilities.UIUtils;

import java.util.List;

public class CoursesSelectorAdapter extends RecyclerView.Adapter<CoursesSelectorAdapter.ViewHolder> {
    private List<Course> courses;
    private OnItemClickListener listener;
    private List<Course> selectedCourses;

    // data is passed into the constructor
    public CoursesSelectorAdapter(List<Course> courses, OnItemClickListener listener, List<Course> selectedCourses) {
        this.courses = courses;
        this.listener = listener;
        this.selectedCourses = selectedCourses;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_selector, parent, false);
        return new ViewHolder(view);
    }

    public void handelSelectedCourses(Course course) {
        if (selectedCourses.contains(course)) {
            selectedCourses.remove(course);
        } else {
            selectedCourses.add(course);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Course course = courses.get(position);

        Drawable drawable = null;
        if (selectedCourses.contains(course)) {
            drawable = ResourcesCompat.getDrawable(holder.binding.check.getResources(), R.drawable.ic_check, null);
        } else {
            drawable = ResourcesCompat.getDrawable(holder.binding.check.getResources(), R.drawable.ic_unchecked, null);
        }
        holder.binding.check.setImageDrawable(drawable);
        holder.binding.name.setText(course.getName());
        holder.binding.grade.setText(UIUtils.getGrade(course.getGrade()));
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
                    if (getAdapterPosition() >= 0 && getAdapterPosition() < courses.size()) {
                        if (listener != null) listener.onCourseClickListener(courses.get(getAdapterPosition()));
                        handelSelectedCourses(courses.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onCourseClickListener(Course course);
    }
}