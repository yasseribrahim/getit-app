package com.getit.app.persenters.exams;

import androidx.annotation.NonNull;

import com.getit.app.Constants;
import com.getit.app.models.Exam;
import com.getit.app.persenters.BasePresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExamsPresenter implements BasePresenter {
    private DatabaseReference reference;
    private ValueEventListener listener;
    private ExamsCallback callback;

    public ExamsPresenter(ExamsCallback callback) {
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.NODE_NAME_EXAMS).getRef();
        this.callback = callback;
    }
    public void getExamsCount() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onGetExamsCountComplete(snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void save(Exam... exams) {
        FirebaseDatabase dp = FirebaseDatabase.getInstance();
        DatabaseReference node = dp.getReference(Constants.NODE_NAME_USERS);
        for (Exam exam : exams) {
            node.child(exam.getId()).setValue(exam);
        }
        if (callback != null) {
            callback.onSaveExamComplete();
        }
    }

    public void getExams(int grade) {
        callback.onShowLoading();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Exam> exams = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Exam exam = dataSnapshot.getValue(Exam.class);
                    if (exam.getGrade() == grade) {
                        exams.add(exam);
                    }
                }

                if (callback != null) {
                    callback.onGetExamsComplete(exams);
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
        reference.addListenerForSingleValueEvent(listener);
    }

    public void getExamById(String id) {
        callback.onShowLoading();
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (callback != null) {
                    callback.onGetExamComplete(snapshot.getValue(Exam.class));
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
