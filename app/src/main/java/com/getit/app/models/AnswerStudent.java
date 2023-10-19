package com.getit.app.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnswerStudent {
    private String lessonId;
    private List<Answer> answers;
    private boolean finished;

    public AnswerStudent() {
        this(null);
    }

    public AnswerStudent(String lessonId) {
        this.lessonId = lessonId;
        this.answers = new ArrayList<>();
        this.finished = false;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
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
        AnswerStudent that = (AnswerStudent) o;
        return lessonId.equals(that.lessonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId);
    }

    @Override
    public String toString() {
        return "ExamStudent{" +
                "lessonId=" + lessonId +
                ", answers=" + answers +
                '}';
    }
}
