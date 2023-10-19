package com.getit.app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Unit implements Parcelable {
    private String id;
    private String name;
    private String gradeId;

    public Unit() {
        this(null, null, null);
    }

    public Unit(String gradeId) {
        this.gradeId = gradeId;
    }

    public Unit(String id, String gradeId, String name) {
        this.id = id;
        this.gradeId = gradeId;
        this.name = name;
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

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gradeId='" + gradeId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.gradeId);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.name = source.readString();
        this.gradeId = source.readString();
    }

    protected Unit(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.gradeId = in.readString();
    }

    public static final Parcelable.Creator<Unit> CREATOR = new Parcelable.Creator<Unit>() {
        @Override
        public Unit createFromParcel(Parcel source) {
            return new Unit(source);
        }

        @Override
        public Unit[] newArray(int size) {
            return new Unit[size];
        }
    };
}
