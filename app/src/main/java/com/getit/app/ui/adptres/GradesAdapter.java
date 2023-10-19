package com.getit.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.getit.app.R;
import com.getit.app.databinding.ItemGradeBinding;
import com.getit.app.models.Grade;
import com.getit.app.models.User;
import com.getit.app.utilities.helpers.StorageHelper;

import java.util.List;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.ViewHolder> {
    private List<Grade> grades;
    private OnGradeClickListener listener;
    private User currentUser;

    public GradesAdapter(List<Grade> grades, OnGradeClickListener listener) {
        this.grades = grades;
        this.listener = listener;
        this.currentUser = StorageHelper.getCurrentUser();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grade, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Grade grade = grades.get(position);

        holder.binding.name.setText(grade.getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return grades.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemGradeBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemGradeBinding.bind(view);
            binding.containerActions.setVisibility(currentUser.isAdmin() ? View.VISIBLE : View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onGradeViewListener(grades.get(getAdapterPosition()));
                }
            });
            binding.containerRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onGradeDeleteViewListener(grades.get(getAdapterPosition()));
                }
            });
            binding.containerEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onGradeEditListener(grades.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnGradeClickListener {
        void onGradeViewListener(Grade grade);

        void onGradeEditListener(Grade grade);

        void onGradeDeleteViewListener(Grade grade);
    }
}