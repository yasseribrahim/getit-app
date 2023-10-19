package com.getit.app.persenters.grade;

import com.getit.app.models.Grade;
import com.getit.app.persenters.BaseCallback;

import java.util.List;

public interface GradesCallback extends BaseCallback {
    default void onGetGradesComplete(List<Grade> grades) {
    }

    default void onGetGradesCountComplete(long count) {
    }

    default void onSaveGradeComplete() {
    }

    default void onDeleteGradeComplete(Grade grade) {
    }

    default void onGetGradeComplete(Grade grade) {
    }
}
