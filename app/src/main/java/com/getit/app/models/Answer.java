package com.getit.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Answer implements Parcelable {
    private Question question;
    private int selectedAnswerIndex;
    private Boolean tureAnswer;
    private String answer;
    private Boolean isRight;
    private boolean answered;

    public Answer() {
    }

    public Answer(Question question) {
        this(question, 0, null, null, null);
    }

    public Answer(Question question, int selectedAnswerIndex, String answer, Boolean isRight, Boolean tureAnswer) {
        this.question = question;
        this.selectedAnswerIndex = selectedAnswerIndex;
        this.answer = answer;
        this.isRight = isRight;
        this.tureAnswer = tureAnswer;
        this.answered = false;
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


    public void setTureAnswer(Boolean tureAnswer) {
        this.tureAnswer = tureAnswer;
    }

    public Boolean getTureAnswer() {
        return tureAnswer;
    }

    public void setRight(Boolean right) {
        isRight = right;
    }

    public Boolean getRight() {
        return isRight;
    }

    public void correct() {
        if (question.isMultiChoices()) {
            if (selectedAnswerIndex >= 0 && selectedAnswerIndex < question.getChoices().size()) {
                QuestionChoice rightChoice = question.getChoices().get(selectedAnswerIndex);
                isRight = rightChoice.isCorrectAnswer();
            } else {
                isRight = false;
            }
        } else if (question.isTrueFalse()) {
            isRight = question.isAnswerTrue() == tureAnswer;
        } else {
            isRight = null;
        }
        answered = true;
    }

    public void reset() {
        if (question.isMultiChoices()) {
            selectedAnswerIndex = 0;
        } else if (question.isTrueFalse()) {
            tureAnswer = null;
        } else {
            answer = null;
        }
        isRight = null;
        answered = false;
    }

    public void correct(boolean value) {
        isRight = value;
        answered = true;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isAnswered() {
        return answered;
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
                ", selectedAnswerIndex=" + selectedAnswerIndex +
                ", tureAnswer=" + tureAnswer +
                ", answer='" + answer + '\'' +
                ", isRight=" + isRight +
                ", answered=" + answered +
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
        dest.writeValue(this.tureAnswer);
        dest.writeString(this.answer);
        dest.writeValue(this.isRight);
        dest.writeByte(this.answered ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.question = source.readParcelable(Question.class.getClassLoader());
        this.selectedAnswerIndex = source.readInt();
        this.tureAnswer = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.answer = source.readString();
        this.isRight = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.answered = source.readByte() != 0;
    }

    protected Answer(Parcel in) {
        this.question = in.readParcelable(Question.class.getClassLoader());
        this.selectedAnswerIndex = in.readInt();
        this.tureAnswer = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.answer = in.readString();
        this.isRight = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.answered = in.readByte() != 0;
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
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
