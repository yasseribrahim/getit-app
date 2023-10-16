package com.getit.app.persenters.courses;

import com.getit.app.models.Course;
import com.getit.app.persenters.BaseCallback;

import java.util.List;

public interface CoursesCallback extends BaseCallback {
    default void onGetCoursesComplete(List<Course> courses) {
    }
    default void onGetCoursesCountComplete(long count) {
    }

    default void onSaveCourseComplete() {
    }

    default void onDeleteCourseComplete(Course course) {
    }

    default void onGetCourseComplete(Course course) {
    }
}
