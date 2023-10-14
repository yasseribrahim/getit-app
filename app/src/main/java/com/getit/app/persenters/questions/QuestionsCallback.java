package com.getit.app.persenters.questions;

import com.getit.app.models.Question;
import com.getit.app.persenters.BaseCallback;

import java.util.List;

public interface QuestionsCallback extends BaseCallback {
    default void onGetQuestionsComplete(List<Question> questions) {
    }
    default void onGetQuestionsCountComplete(long count) {
    }

    default void onSaveQuestionComplete() {
    }

    default void onDeleteQuestionComplete(int position) {
    }

    default void onGetQuestionComplete(Question question) {
    }
}
