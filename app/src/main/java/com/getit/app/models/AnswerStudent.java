package com.getit.app.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnswerStudent {
    private String lessonId;
    private String studentId;
    private List<Answer> answers;
    private boolean finished;

    public AnswerStudent() {
        this(null, null);
    }

    public AnswerStudent(String lessonId, String studentId) {
        this.lessonId = lessonId;
        this.studentId = studentId;
        this.answers = new ArrayList<>();
        this.finished = false;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public Answer getAnswer(Question question) {
        if (answers == null) {
            answers = new ArrayList<>();
        }
        int index = answers.indexOf(new Answer(question));
        if (index != -1) {
            return answers.get(index);
        }
        var answer = new Answer(question);
        answers.add(answer);
        return answer;
    }

    public void addAnswer(Answer answer) {
        if (answers == null) {
            answers = new ArrayList<>();
        }
        int index = answers.indexOf(new Answer(answer.getQuestion()));
        if (index != -1) {
            answers.set(index, answer);
        } else {
            answers.add(answer);
        }
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
        return "AnswerStudent{" +
                "lessonId='" + lessonId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", answers=" + answers +
                ", finished=" + finished +
                '}';
    }
}
