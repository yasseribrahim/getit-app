package com.getit.app.models;

public class QuestionChoice {
    private String title;
    private boolean correctAnswer;

    public QuestionChoice() {
    }

    public QuestionChoice(String title, boolean correctAnswer) {
        this.title = title;
        this.correctAnswer = correctAnswer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String toString() {
        return "QuestionChoice{" +
                "title='" + title + '\'' +
                ", isRightAnswer=" + correctAnswer +
                '}';
    }
}
