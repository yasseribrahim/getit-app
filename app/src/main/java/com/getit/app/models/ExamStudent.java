package com.getit.app.models;

import java.util.List;

public class ExamStudent {
    private Exam exam;
    private List<Answer> answers;

    public ExamStudent() {
    }

    public ExamStudent(Exam exam, List<Answer> answers) {
        this.exam = exam;
        this.answers = answers;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "ExamStudent{" +
                "exam=" + exam +
                ", answers=" + answers +
                '}';
    }
}
