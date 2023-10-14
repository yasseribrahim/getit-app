package com.getit.app.persenters.questions;

import androidx.annotation.NonNull;

import com.getit.app.Constants;
import com.getit.app.models.Question;
import com.getit.app.persenters.BasePresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionsPresenter implements BasePresenter {
    private DatabaseReference reference;
    private ValueEventListener listener;
    private QuestionsCallback callback;

    public QuestionsPresenter(QuestionsCallback callback) {
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.NODE_NAME_QUESTIONS).getRef();
        this.callback = callback;
    }
    public void getQuestionsCount() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onGetQuestionsCountComplete(snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void save(Question... questions) {
        FirebaseDatabase dp = FirebaseDatabase.getInstance();
        DatabaseReference node = dp.getReference(Constants.NODE_NAME_USERS);
        for (Question question : questions) {
            node.child(question.getId()).setValue(question);
        }
        if (callback != null) {
            callback.onSaveQuestionComplete();
        }
    }

    public void getQuestions(String courseId) {
        callback.onShowLoading();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Question> Questions = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Question question = dataSnapshot.getValue(Question.class);
                    if (question.getCourseId().equalsIgnoreCase(courseId)) {
                        Questions.add(question);
                    }
                }

                if (callback != null) {
                    callback.onGetQuestionsComplete(Questions);
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

    public void getQuestionById(String id) {
        callback.onShowLoading();
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (callback != null) {
                    callback.onGetQuestionComplete(snapshot.getValue(Question.class));
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
