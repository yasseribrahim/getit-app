package com.fully.code.base.persenters.questions;

import com.fully.code.base.models.Question;
import com.fully.code.base.persenters.BaseCallback;

import java.util.List;

public interface QuestionsCallback extends BaseCallback {
    default void onGetQuestionsComplete(List<Question> questions) {
    }
    default void onGetQuestionsCountComplete(long count) {
    }

    default void onSaveQuestionComplete() {
    }

    default void onDeleteQuestionComplete(Question question) {
    }

    default void onGetQuestionComplete(Question question) {
    }
}
