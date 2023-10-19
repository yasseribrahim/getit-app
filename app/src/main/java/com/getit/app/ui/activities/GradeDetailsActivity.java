package com.getit.app.ui.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.getit.app.Constants;
import com.getit.app.R;
import com.getit.app.databinding.ActivityGradeDetailsBinding;
import com.getit.app.models.Grade;
import com.getit.app.models.Unit;
import com.getit.app.models.User;
import com.getit.app.persenters.unit.UnitsCallback;
import com.getit.app.persenters.unit.UnitsPresenter;
import com.getit.app.ui.adptres.UnitsAdapter;
import com.getit.app.utilities.ToastUtils;
import com.getit.app.utilities.helpers.LocaleHelper;
import com.getit.app.utilities.helpers.StorageHelper;

import java.util.ArrayList;
import java.util.List;

public class GradeDetailsActivity extends BaseActivity implements UnitsCallback, UnitsAdapter.OnUnitClickListener {
    private ActivityGradeDetailsBinding binding;
    private UnitsPresenter presenter;
    private UnitsAdapter adapter;
    private List<Unit> units, searchedUnits;
    private User currentUser;
    private Grade grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityGradeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new UnitsPresenter(this);

        currentUser = StorageHelper.getCurrentUser();

        grade = getIntent().getParcelableExtra(Constants.ARG_OBJECT);
        setUpActionBar();

        binding.btnAdd.setVisibility(StorageHelper.getCurrentUser().isAdmin() ? View.VISIBLE : View.GONE);
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUnit(new Unit());
            }
        });

        units = new ArrayList<>();
        searchedUnits = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UnitsAdapter(searchedUnits, this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    @SuppressLint("WrongConstant")
    private void setUpActionBar() {
        binding.appBarLayout.toolbar.setTitle(grade.getName());
        setSupportActionBar(binding.appBarLayout.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        binding.appBarLayout.toolbar.setNavigationIcon(R.drawable.back_to_home_button);
    }

    @Override
    public void onGetUnitsComplete(List<Unit> units) {
        this.units.clear();
        this.units.addAll(units);

        search(binding.textSearch.getText().toString());
    }

    private void load() {
        presenter.getUnits(grade.getId());
    }

    private void search(String searchedText) {
        searchedUnits.clear();
        if (!searchedText.isEmpty()) {
            for (Unit unit : units) {
                if (isMatched(unit, searchedText)) {
                    searchedUnits.add(unit);
                }
            }
        } else {
            searchedUnits.addAll(units);
        }

        refresh();
    }

    private void refresh() {
        binding.messageExamsEmpty.setVisibility(View.GONE);
        if (searchedUnits.isEmpty()) {
            binding.messageExamsEmpty.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }

    private boolean isMatched(Unit unit, String text) {
        String searchedText = text.toLowerCase();
        boolean result = unit.getName().toLowerCase().contains(searchedText);
        return result;
    }

    @Override
    public void onUnitViewListener(Unit unit) {
        Intent intent = new Intent(this, UnitDetailsActivity.class);
        intent.putExtra(Constants.ARG_OBJECT, unit);
        startActivity(intent);
    }

    @Override
    public void onUnitEditListener(Unit unit) {
        addUnit(unit);
    }

    @Override
    public void onUnitDeleteListener(Unit unit) {
       presenter.delete(unit);
    }

    @Override
    public void onDeleteUnitComplete(Unit unit) {
        int index = units.indexOf(unit);
        if (index != -1) {
            units.remove(index);
            adapter.notifyItemRemoved(index);
        }
        ToastUtils.longToast(R.string.str_message_delete_successfully);
        load();
    }

    @Override
    public void onSaveUnitComplete() {
        ToastUtils.longToast(R.string.str_message_added_successfully);
        if (dialog != null) {
            dialog.dismiss();
        }
        load();
    }

    private AlertDialog dialog;

    private void addUnit(Unit unit) {
        unit.setGradeId(grade.getId());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.str_add_new);

        final EditText input = new EditText(this);
        input.setText(unit.getName());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.str_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                if (text.isEmpty()) {
                    ToastUtils.longToast(R.string.str_enter_value);
                    return;
                }
                var exist = units.stream().anyMatch(grade -> {
                    return grade.getName().equalsIgnoreCase(text);
                });
                if (exist) {
                    ToastUtils.longToast("Sorry " + text + " already exist");
                } else {
                    unit.setName(text);
                    presenter.save(unit);
                }
            }
        });
        builder.setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog = builder.show();
    }
}