package com.getit.app.persenters.lesson;

import com.getit.app.models.Lesson;
import com.getit.app.persenters.BaseCallback;

import java.util.List;

public interface LessonsCallback extends BaseCallback {
    default void onGetLessonsComplete(List<Lesson> lessons) {
    }

    default void onGetLessonsCountComplete(long count) {
    }

    default void onSaveLessonComplete() {
    }

    default void onDeleteLessonComplete(Lesson lesson) {
    }

    default void onGetLessonComplete(Lesson lesson) {
    }
}
