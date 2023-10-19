package com.getit.app.models;

import java.util.Objects;

public class Answer {
    private OldQuestion oldQuestion;
    private int selectedAnswerIndex;
    private String answer;
    private Boolean isRight;

    public Answer() {
    }

    public Answer(OldQuestion oldQuestion) {
        this.oldQuestion = oldQuestion;
    }

    public Answer(OldQuestion oldQuestion, int selectedAnswerIndex, String answer, Boolean isRight) {
        this.oldQuestion = oldQuestion;
        this.selectedAnswerIndex = selectedAnswerIndex;
        this.answer = answer;
        this.isRight = isRight;
    }

    public OldQuestion getQuestion() {
        return oldQuestion;
    }

    public void setQuestion(OldQuestion oldQuestion) {
        this.oldQuestion = oldQuestion;
    }

    public int getSelectedAnswerIndex() {
        return selectedAnswerIndex;
    }

    public void setSelectedAnswerIndex(int selectedAnswerIndex) {
        this.selectedAnswerIndex = selectedAnswerIndex;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean isRight() {
        return isRight;
    }

    public void setIsRight(Boolean isRight) {
        this.isRight = isRight;
    }

    public void correct() {
        if (oldQuestion.isMultiChoices()) {
            if (selectedAnswerIndex >= 0 && selectedAnswerIndex < oldQuestion.getChoices().size()) {
                QuestionChoice rightChoice = oldQuestion.getChoices().get(selectedAnswerIndex);
                isRight = rightChoice.isCorrectAnswer();
            } else {
                isRight = false;
            }
        } else {
            isRight = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return oldQuestion.equals(answer.oldQuestion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldQuestion);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "question=" + oldQuestion +
                ", choice=" + selectedAnswerIndex +
                ", answer='" + answer + '\'' +
                ", isRight=" + isRight +
                '}';
    }
}
