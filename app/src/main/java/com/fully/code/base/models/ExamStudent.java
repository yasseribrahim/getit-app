package com.fully.code.base.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExamStudent {
    private Exam exam;
    private List<Answer> answers;
    private boolean finished;

    public ExamStudent() {
        this(null);
    }

    public ExamStudent(Exam exam) {
        this.exam = exam;
        this.answers = new ArrayList<>();
        this.finished = false;
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

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExamStudent that = (ExamStudent) o;
        return exam.equals(that.exam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exam);
    }

    @Override
    public String toString() {
        return "ExamStudent{" +
                "exam=" + exam +
                ", answers=" + answers +
                '}';
    }
}
