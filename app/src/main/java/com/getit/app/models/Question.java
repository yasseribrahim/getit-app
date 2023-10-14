package com.getit.app.models;

import com.getit.app.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Question {
    private String id;
    private String courseId;
    private String title;
    private String description;
    private int type;
    private List<QuestionChoice> choices;

    public Question() {
        this(null, null, null, null, 0);
    }

    public Question(String id, String courseId, String title, String description, int type) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.type = type;
        if (type == Constants.QUESTION_TYPE_MULTI_CHOICE) {
            choices = Arrays.asList(new QuestionChoice(), new QuestionChoice(), new QuestionChoice(), new QuestionChoice());
        } else {
            choices = new ArrayList<>();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<QuestionChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<QuestionChoice> choices) {
        this.choices = choices;
    }

    public boolean isMultiQuestion() {
        return type == Constants.QUESTION_TYPE_MULTI_CHOICE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id.equals(question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", courseId='" + courseId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", choices=" + choices +
                '}';
    }
}
