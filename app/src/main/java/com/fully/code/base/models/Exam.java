package com.fully.code.base.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Exam implements Parcelable {
    private String id;
    private String title;
    private String description;
    private Date date;
    private int scoreTotal;
    private int scorePass;
    private int grade;
    private String courseId;
    private String courseName;
    private String createdBy;
    private boolean isActive;
    private List<Question> questions;

    public Exam() {
        this(null, null, null, Calendar.getInstance().getTime(), 0, 0, 0, null, null, null);
    }

    public Exam(String id, String title, String description, Date date, int scoreTotal, int scorePass, int grade, String courseId, String courseName, String createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.scoreTotal = scoreTotal;
        this.scorePass = scorePass;
        this.grade = grade;
        this.courseId = courseId;
        this.courseName = courseName;
        this.createdBy = createdBy;
        this.isActive = true;
        this.questions = new ArrayList<>();
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

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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
        return "Exam{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", scoreTotal=" + scoreTotal +
                ", scorePass=" + scorePass +
                ", grade=" + grade +
                ", courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", isActive=" + isActive +
                ", questions=" + questions +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeInt(this.scoreTotal);
        dest.writeInt(this.scorePass);
        dest.writeInt(this.grade);
        dest.writeString(this.courseId);
        dest.writeString(this.courseName);
        dest.writeString(this.createdBy);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.questions);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.title = source.readString();
        this.description = source.readString();
        long tmpDate = source.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.scoreTotal = source.readInt();
        this.scorePass = source.readInt();
        this.grade = source.readInt();
        this.courseId = source.readString();
        this.courseName = source.readString();
        this.createdBy = source.readString();
        this.isActive = source.readByte() != 0;
        this.questions = source.createTypedArrayList(Question.CREATOR);
    }

    protected Exam(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.scoreTotal = in.readInt();
        this.scorePass = in.readInt();
        this.grade = in.readInt();
        this.courseId = in.readString();
        this.courseName = in.readString();
        this.createdBy = in.readString();
        this.isActive = in.readByte() != 0;
        this.questions = in.createTypedArrayList(Question.CREATOR);
    }

    public static final Creator<Exam> CREATOR = new Creator<Exam>() {
        @Override
        public Exam createFromParcel(Parcel source) {
            return new Exam(source);
        }

        @Override
        public Exam[] newArray(int size) {
            return new Exam[size];
        }
    };
}
