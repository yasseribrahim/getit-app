package com.getit.app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionChoice implements Parcelable {
    private String title;
    private boolean correctAnswer;

    public QuestionChoice() {
    }

    public QuestionChoice(String title, boolean correctAnswer) {
        this.title = title;
        this.correctAnswer = correctAnswer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String toString() {
        return "QuestionChoice{" +
                "title='" + title + '\'' +
                ", isRightAnswer=" + correctAnswer +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeByte(this.correctAnswer ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.title = source.readString();
        this.correctAnswer = source.readByte() != 0;
    }

    protected QuestionChoice(Parcel in) {
        this.title = in.readString();
        this.correctAnswer = in.readByte() != 0;
    }

    public static final Parcelable.Creator<QuestionChoice> CREATOR = new Parcelable.Creator<QuestionChoice>() {
        @Override
        public QuestionChoice createFromParcel(Parcel source) {
            return new QuestionChoice(source);
        }

        @Override
        public QuestionChoice[] newArray(int size) {
            return new QuestionChoice[size];
        }
    };
}
