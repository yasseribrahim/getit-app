package com.getit.app.persenters.answer;

import com.getit.app.models.AnswerStudent;
import com.getit.app.models.Correction;
import com.getit.app.persenters.BaseCallback;

import java.util.List;

public interface AnswersCallback extends BaseCallback {
    default void onGetAnswersComplete(List<AnswerStudent> answers) {
    }

    default void onGetAnswersCountComplete(long count) {
    }

    default void onSaveAnswerComplete() {
    }

    default void onDeleteAnswerComplete(AnswerStudent answer) {
    }

    default void onGetAnswerComplete(AnswerStudent answer) {
    }

    default void onSearchComplete(List<Correction> corrections) {
    }
}
