package com.getit.app.persenters.questions;

import androidx.annotation.NonNull;

import com.getit.app.Constants;
import com.getit.app.models.Question;
import com.getit.app.models.User;
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
import java.util.Collections;
import java.util.Comparator;
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

    public void save(Question question) {
        callback.onHideLoading();
        if (question.getId() == null) {
            question.setId(Calendar.getInstance().getTimeInMillis() + "");
        }
        reference.child(question.getId()).setValue(question).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (callback != null) {
                    callback.onSaveQuestionComplete();
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

    public void getQuestions() {
        callback.onShowLoading();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Question> questions = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Question question = dataSnapshot.getValue(Question.class);
                    questions.add(question);
                }

                if (callback != null) {
                    Collections.sort(questions, new Comparator<Question>() {
                        @Override
                        public int compare(Question o1, Question o2) {
                            return o1.getCourseId().compareTo(o2.getCourseId());
                        }
                    });
                    callback.onGetQuestionsComplete(questions);
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

    public void getQuestions(String courseId) {
        callback.onShowLoading();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Question> questions = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Question question = dataSnapshot.getValue(Question.class);
                    if (question.getCourseId().equalsIgnoreCase(courseId)) {
                        questions.add(question);
                    }
                }

                if (callback != null) {
                    callback.onGetQuestionsComplete(questions);
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
