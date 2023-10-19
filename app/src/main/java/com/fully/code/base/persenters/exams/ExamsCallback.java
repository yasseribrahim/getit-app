package com.fully.code.base.persenters.exams;

import com.fully.code.base.models.Exam;
import com.fully.code.base.persenters.BaseCallback;

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
