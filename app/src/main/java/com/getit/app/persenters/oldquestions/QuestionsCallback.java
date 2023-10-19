package com.getit.app.persenters.oldquestions;

import com.getit.app.models.OldQuestion;
import com.getit.app.persenters.BaseCallback;

import java.util.List;

public interface QuestionsCallback extends BaseCallback {
    default void onGetQuestionsComplete(List<OldQuestion> oldQuestions) {
    }
    default void onGetQuestionsCountComplete(long count) {
    }

    default void onSaveQuestionComplete() {
    }

    default void onDeleteQuestionComplete(OldQuestion oldQuestion) {
    }

    default void onGetQuestionComplete(OldQuestion oldQuestion) {
    }
}
