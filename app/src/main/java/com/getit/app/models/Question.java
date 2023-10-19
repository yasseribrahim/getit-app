package com.getit.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.getit.app.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Question implements Parcelable {
    private String id;
    private String lessonId;
    private String lessonName;
    private String title;
    private String description;
    private int type;
    private List<QuestionChoice> choices;
    private boolean isAnswerTrue;

    public Question() {
        this(null, null, null, null, 0);
    }

    public Question(String id, String lessonId, String title, String description, int type) {
        this.id = id;
        this.lessonId = lessonId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.isAnswerTrue = false;
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

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getLessonName() {
        return lessonName;
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

    public boolean isTrueFalse() {
        return type == Constants.QUESTION_TYPE_TRUE_FALSE;
    }

    public boolean isArticle() {
        return type == Constants.QUESTION_TYPE_ARTICLE;
    }

    public void setAnswerTrue(boolean answerTrue) {
        isAnswerTrue = answerTrue;
    }

    public boolean isAnswerTrue() {
        return isAnswerTrue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question oldQuestion = (Question) o;
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
                ", lessonId='" + lessonId + '\'' +
                ", lessonName='" + lessonName + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", choices=" + choices +
                ", isTrue=" + isAnswerTrue +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.lessonId);
        dest.writeString(this.lessonName);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeInt(this.type);
        dest.writeTypedList(this.choices);
        dest.writeByte(this.isAnswerTrue ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.lessonId = source.readString();
        this.lessonName = source.readString();
        this.title = source.readString();
        this.description = source.readString();
        this.type = source.readInt();
        this.choices = source.createTypedArrayList(QuestionChoice.CREATOR);
        this.isAnswerTrue = source.readByte() != 0;
    }

    protected Question(Parcel in) {
        this.id = in.readString();
        this.lessonId = in.readString();
        this.lessonName = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.type = in.readInt();
        this.choices = in.createTypedArrayList(QuestionChoice.CREATOR);
        this.isAnswerTrue = in.readByte() != 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
