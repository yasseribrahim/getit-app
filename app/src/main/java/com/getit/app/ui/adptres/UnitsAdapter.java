package com.getit.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.getit.app.R;
import com.getit.app.databinding.ItemGradeBinding;
import com.getit.app.models.Unit;
import com.getit.app.models.User;
import com.getit.app.utilities.helpers.StorageHelper;

import java.util.List;

public class UnitsAdapter extends RecyclerView.Adapter<UnitsAdapter.ViewHolder> {
    private List<Unit> units;
    private OnUnitClickListener listener;
    private User currentUser;

    public UnitsAdapter(List<Unit> units, OnUnitClickListener listener) {
        this.units = units;
        this.listener = listener;
        this.currentUser = StorageHelper.getCurrentUser();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Unit unit = units.get(position);

        holder.binding.name.setText(unit.getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return units.size();
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
                        listener.onUnitViewListener(units.get(getAdapterPosition()));
                }
            });
            binding.containerRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onUnitDeleteListener(units.get(getAdapterPosition()));
                }
            });
            binding.containerEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onUnitEditListener(units.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnUnitClickListener {
        void onUnitViewListener(Unit unit);

        void onUnitEditListener(Unit unit);

        void onUnitDeleteListener(Unit unit);
    }
}