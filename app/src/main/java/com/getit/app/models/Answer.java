package com.getit.app.models;

import java.util.Objects;

public class Answer {
    private Question question;
    private int selectedAnswerIndex;
    private String answer;
    private Boolean isRight;

    public Answer() {
    }

    public Answer(Question question, int selectedAnswerIndex, String answer, Boolean isRight) {
        this.question = question;
        this.selectedAnswerIndex = selectedAnswerIndex;
        this.answer = answer;
        this.isRight = isRight;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
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

    public Boolean getIsRight() {
        return isRight;
    }

    public void setIsRight(Boolean isRight) {
        this.isRight = isRight;
    }
    
    public void correct() {
        if (question.isMultiQuestion()) {
            if (selectedAnswerIndex >= 0 && selectedAnswerIndex < question.getChoices().size()) {
                QuestionChoice rightChoice = question.getChoices().get(selectedAnswerIndex);
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
        return question.equals(answer.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "question=" + question +
                ", choice=" + selectedAnswerIndex +
                ", answer='" + answer + '\'' +
                ", isRight=" + isRight +
                '}';
    }
}
