package com.getit.app.models;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Course {
    private String id;
    private String name;
    private int grade;
    private Date createdAt;

    public Course() {
    }

    public Course(String id, String name, int grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.createdAt = Calendar.getInstance().getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id.equals(course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                ", createdAt=" + createdAt +
                '}';
    }
}
