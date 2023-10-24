package com.getit.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.getit.app.R;
import com.getit.app.databinding.ItemCorrectionBinding;
import com.getit.app.models.Correction;
import com.getit.app.utilities.DatesUtils;

import java.util.List;

public class CorrectionAdapter extends RecyclerView.Adapter<CorrectionAdapter.ViewHolder> {
    private List<Correction> corrections;
    private OnCorrectionClickListener listener;

    public CorrectionAdapter(List<Correction> corrections, OnCorrectionClickListener listener) {
        this.corrections = corrections;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_correction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Correction correction = corrections.get(position);

        holder.binding.lessonName.setText(correction.getLessonName());
        holder.binding.gradeName.setText(correction.getGradeName());
        holder.binding.unitName.setText(correction.getUnitName());
        holder.binding.studentName.setText(correction.getStudentName());
        holder.binding.counter.setText(holder.binding.counter.getResources().getString(R.string.str_questions_formatted, correction.getQuestionsNumber() + ""));
        holder.binding.date.setText(DatesUtils.formatDateOnly(correction.getLessonDate()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return corrections.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCorrectionBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemCorrectionBinding.bind(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onCorrectionClickListener(corrections.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnCorrectionClickListener {
        void onCorrectionClickListener(Correction correction);
    }
}