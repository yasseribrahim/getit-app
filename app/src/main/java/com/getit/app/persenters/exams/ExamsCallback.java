package com.getit.app.persenters.exams;

import com.getit.app.models.Exam;
import com.getit.app.persenters.BaseCallback;

import java.util.List;

public interface ExamsCallback extends BaseCallback {
    default void onGetExamsComplete(List<Exam> exams) {
    }

    default void onGetExamsCountComplete(long count) {
    }

    default void onSaveExamComplete() {
    }

    default void onDeleteExamComplete(Exam exam) {
    }

    default void onGetExamComplete(Exam exam) {
    }
}
