package com.getit.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.getit.app.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OldQuestion implements Parcelable {
    private String id;
    private String courseId;
    private String courseName;
    private String title;
    private String description;
    private int type;
    private List<QuestionChoice> choices;

    public OldQuestion() {
        this(null, null, null, null, 0);
    }

    public OldQuestion(String id, String courseId, String title, String description, int type) {
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

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
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

    public boolean isMultiChoices() {
        return type == Constants.QUESTION_TYPE_MULTI_CHOICE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OldQuestion oldQuestion = (OldQuestion) o;
        return id.equals(oldQuestion.id);
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
                ", courseName='" + courseName + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", choices=" + choices +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.courseId);
        dest.writeString(this.courseName);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeInt(this.type);
        dest.writeList(this.choices);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.courseId = source.readString();
        this.courseName = source.readString();
        this.title = source.readString();
        this.description = source.readString();
        this.type = source.readInt();
        this.choices = new ArrayList<QuestionChoice>();
        source.readList(this.choices, QuestionChoice.class.getClassLoader());
    }

    protected OldQuestion(Parcel in) {
        this.id = in.readString();
        this.courseId = in.readString();
        this.courseName = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.type = in.readInt();
        this.choices = new ArrayList<QuestionChoice>();
        in.readList(this.choices, QuestionChoice.class.getClassLoader());
    }

    public static final Parcelable.Creator<OldQuestion> CREATOR = new Parcelable.Creator<OldQuestion>() {
        @Override
        public OldQuestion createFromParcel(Parcel source) {
            return new OldQuestion(source);
        }

        @Override
        public OldQuestion[] newArray(int size) {
            return new OldQuestion[size];
        }
    };
}
