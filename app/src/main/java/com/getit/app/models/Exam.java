package com.getit.app.models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Exam {
    private String id;
    private String title;
    private String description;
    private Date date;
    private int scoreTotal;
    private int scorePass;
    private int grade;
    private String createdBy;
    private List<Question> questions;

    public Exam() {
        this(null, null, null, Calendar.getInstance().getTime(), 0, 0, 0, null);
    }

    public Exam(String id, String title, String description, Date date, int scoreTotal, int scorePass, int grade, String createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.scoreTotal = scoreTotal;
        this.scorePass = scorePass;
        this.grade = grade;
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getScoreTotal() {
        return scoreTotal;
    }

    public void setScoreTotal(int scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public int getScorePass() {
        return scorePass;
    }

    public void setScorePass(int scorePass) {
        this.scorePass = scorePass;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return id.equals(exam.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", scoreTotal=" + scoreTotal +
                ", scorePass=" + scorePass +
                ", grade=" + grade +
                ", createdBy='" + createdBy + '\'' +
                ", questions=" + questions +
                '}';
    }
}
