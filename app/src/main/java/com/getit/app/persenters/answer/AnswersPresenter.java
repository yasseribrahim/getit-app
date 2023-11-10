package com.getit.app.persenters.answer;

import androidx.annotation.NonNull;

import com.getit.app.Constants;
import com.getit.app.models.AnswerStudent;
import com.getit.app.models.Correction;
import com.getit.app.persenters.BasePresenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnswersPresenter implements BasePresenter {
    private DatabaseReference reference;
    private ValueEventListener listener;
    private AnswersCallback callback;

    public AnswersPresenter(AnswersCallback callback) {
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.NODE_NAME_ANSWERS).getRef();
        this.callback = callback;
    }

    public void search() {
        callback.onShowLoading();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Correction> corrections = new ArrayList<>();
                for (var lessonSnapshot : snapshot.getChildren()) {
                    for (var studentSnapshot : lessonSnapshot.getChildren()) {
                        var answers = studentSnapshot.getValue(AnswerStudent.class);
                        for (var answer : answers.getAnswers()) {
                            if (!answer.isAnswered() && answer.getQuestion().isArticle()) {
                                Correction correction = new Correction();
                                correction.setLessonId(lessonSnapshot.getKey());
                                correction.setStudentId(studentSnapshot.getKey());
                                correction.setQuestionsNumber(answers.getAnswers().size());
                                corrections.add(correction);
                            }
                        }
                    }
                }

                if (callback != null) {
                    callback.onSearchComplete(corrections);
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

    public void save(AnswerStudent answer) {
        callback.onHideLoading();
        reference.child(answer.getLessonId()).child(answer.getStudentId()).setValue(answer).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (callback != null) {
                    callback.onSaveAnswerComplete();
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

    public void getAnswer(String lessonId, String studentId) {
        callback.onShowLoading();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                AnswerStudent answer = null;
                if (snapshot.exists()) {
                    answer = snapshot.getValue(AnswerStudent.class);
                }

                if (callback != null) {
                    callback.onGetAnswerComplete(answer);
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
        reference.child(lessonId).child(studentId).addListenerForSingleValueEvent(listener);
    }

    public void delete(String lessonId, String studentId) {
        reference.child(lessonId).child(studentId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (callback != null) {
                    callback.onDeleteAnswerComplete(null);
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

    public void delete(String lessonId) {
        reference.child(lessonId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (callback != null) {
                    callback.onDeleteAnswerComplete(null);
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

    public void getAnswerById(String id) {
        callback.onShowLoading();
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (callback != null) {
                    callback.onGetAnswerComplete(snapshot.getValue(AnswerStudent.class));
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
