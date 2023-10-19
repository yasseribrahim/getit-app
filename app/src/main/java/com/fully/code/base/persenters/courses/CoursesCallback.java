package com.fully.code.base.persenters.courses;

import com.fully.code.base.models.Course;
import com.fully.code.base.persenters.BaseCallback;

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
