package com.getit.app.persenters.unit;

import androidx.annotation.NonNull;

import com.getit.app.Constants;
import com.getit.app.models.Unit;
import com.getit.app.persenters.BasePresenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UnitsPresenter implements BasePresenter {
    private DatabaseReference reference;
    private ValueEventListener listener;
    private UnitsCallback callback;

    public UnitsPresenter(UnitsCallback callback) {
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.NODE_NAME_UNITS).getRef();
        this.callback = callback;
    }

    public void getUnitsCount() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<Long> counters = new ArrayList<>();
                snapshot.getChildren().forEach(e -> {
                    counters.add(e.getChildrenCount());
                });
                long counter = 0;
                for (Long value : counters) {
                    counter += value;
                }
                callback.onGetUnitsCountComplete(counter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void save(Unit unit) {
        callback.onHideLoading();
        if (unit.getId() == null) {
            unit.setId(Calendar.getInstance().getTimeInMillis() + "");
        }
        reference.child(unit.getGradeId()).child(unit.getId()).setValue(unit).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (callback != null) {
                    callback.onSaveUnitComplete();
                    callback.onHideLoading();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e.getMessage(), null);
                callback.onHideLoading();
            }
        });
    }

    public void getUnits(String gradeId) {
        callback.onShowLoading();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Unit> courses = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Unit course = dataSnapshot.getValue(Unit.class);
                    courses.add(course);
                }

                if (callback != null) {
                    callback.onGetUnitsComplete(courses);
                    callback.onHideLoading();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (callback != null) {
                    callback.onFailure("Unable to get message: " + databaseError.getMessage(), null);
                    callback.onHideLoading();
                }
            }
        };
        reference.child(gradeId).addListenerForSingleValueEvent(listener);
    }

    public void delete(Unit unit) {
        reference.child(unit.getGradeId()).child(unit.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (callback != null) {
                    callback.onDeleteUnitComplete(unit);
                    callback.onHideLoading();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e.getMessage(), null);
                callback.onHideLoading();
            }
        });
    }

    public void getUnitById(String gradeId, String id) {
        callback.onShowLoading();
        reference.child(gradeId).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (callback != null) {
                    callback.onGetUnitComplete(snapshot.getValue(Unit.class));
                }
                callback.onHideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onHideLoading();
            }
        });
    }

    @Override
    public void onDestroy() {
        if (reference != null && listener != null) {
            reference.removeEventListener(listener);
        }
    }
}