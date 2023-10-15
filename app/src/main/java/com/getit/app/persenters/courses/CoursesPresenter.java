package com.getit.app.persenters.courses;

import androidx.annotation.NonNull;

import com.getit.app.Constants;
import com.getit.app.models.Course;
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

public class CoursesPresenter implements BasePresenter {
    private DatabaseReference reference;
    private ValueEventListener listener;
    private CoursesCallback callback;

    public CoursesPresenter(CoursesCallback callback) {
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.NODE_NAME_COURSES).getRef();
        this.callback = callback;
    }

    public void getCoursesCount() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onGetCoursesCountComplete(snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void save(Course course) {
        callback.onHideLoading();
        if (course.getId() == null) {
            course.setId(Calendar.getInstance().getTimeInMillis() + "");
        }
        reference.child(course.getId()).setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (callback != null) {
                    callback.onSaveCourseComplete();
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

    public void getCourses() {
        callback.onShowLoading();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Course> courses = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Course course = dataSnapshot.getValue(Course.class);
                    courses.add(course);
                }

                if (callback != null) {
                    callback.onGetCoursesComplete(courses);
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

    public void getCourses(int grade) {
        callback.onShowLoading();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Course> courses = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Course course = dataSnapshot.getValue(Course.class);
                    if (course.getGrade() == grade) {
                        courses.add(course);
                    }
                }

                if (callback != null) {
                    callback.onGetCoursesComplete(courses);
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

    public void getCourseById(String id) {
        callback.onShowLoading();
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (callback != null) {
                    callback.onGetCourseComplete(snapshot.getValue(Course.class));
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
