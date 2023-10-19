package com.getit.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Answer implements Parcelable {
    private Question question;
    private int selectedAnswerIndex;
    private String answer;
    private Boolean isRight;

    public Answer() {
    }

    public Answer(Question question) {
        this.question = question;
    }

    public Answer(Question question, int selectedAnswerIndex, String answer, Boolean isRight) {
        this.question = question;
        this.selectedAnswerIndex = selectedAnswerIndex;
        this.answer = answer;
        this.isRight = isRight;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question oldQuestion) {
        this.question = oldQuestion;
    }

    public int getSelectedAnswerIndex() {
        return selectedAnswerIndex;
    }

    public void setSelectedAnswerIndex(int selectedAnswerIndex) {
        this.selectedAnswerIndex = selectedAnswerIndex;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean isRight() {
        return isRight;
    }

    public void setIsRight(Boolean isRight) {
        this.isRight = isRight;
    }

    public void correct() {
        if (question.isMultiChoices()) {
            if (selectedAnswerIndex >= 0 && selectedAnswerIndex < question.getChoices().size()) {
                QuestionChoice rightChoice = question.getChoices().get(selectedAnswerIndex);
                isRight = rightChoice.isCorrectAnswer();
            } else {
                isRight = false;
            }
        } else {
            isRight = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return question.equals(answer.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "question=" + question +
                ", choice=" + selectedAnswerIndex +
                ", answer='" + answer + '\'' +
                ", isRight=" + isRight +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.question, flags);
        dest.writeInt(this.selectedAnswerIndex);
        dest.writeString(this.answer);
        dest.writeValue(this.isRight);
    }

    public void readFromParcel(Parcel source) {
        this.question = source.readParcelable(Question.class.getClassLoader());
        this.selectedAnswerIndex = source.readInt();
        this.answer = source.readString();
        this.isRight = (Boolean) source.readValue(Boolean.class.getClassLoader());
    }

    protected Answer(Parcel in) {
        this.question = in.readParcelable(Question.class.getClassLoader());
        this.selectedAnswerIndex = in.readInt();
        this.answer = in.readString();
        this.isRight = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Answer> CREATOR = new Parcelable.Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel source) {
            return new Answer(source);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };
}
