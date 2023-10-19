package com.getit.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.getit.app.Constants;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {
    private String id;
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String imageProfile;
    private int type;
    private int grade;
    private List<ExamStudent> exams;

    public User() {
        this(null, null, null, null, null, null, null, null, 0, 0);
    }

    public User(String id) {
        this(id, null, null, null, null, null, null, null, 0, 0);
    }

    public User(String username, String password) {
        this(null, username, password, null, null, username, null, null, 0, 0);
    }

    public User(String username, String password, String fullName, String phone) {
        this(null, username, password, fullName, phone, username, null, null, 0, 0);
    }

    public User(String id, String username, String password, String fullName, String phone, String email, String address, String imageProfile, int type, int grade) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.imageProfile = imageProfile;
        this.type = type;
        this.grade = grade;
        this.exams = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGrade() {
        return grade;
    }

    public void setExams(List<ExamStudent> exams) {
        this.exams = exams;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public List<ExamStudent> getExams() {
        return exams;
    }

    public boolean isStudent() {
        return Constants.USER_TYPE_STUDENT == type;
    }

    public boolean isTeacher() {
        return Constants.USER_TYPE_TEACHER == type;
    }

    public boolean isAdmin() {
        return Constants.USER_TYPE_ADMIN == type;
    }

    public ExamStudent getExamStudent(Exam exam) {
        if (exams == null) {
            exams = new ArrayList<>();
        }
        int index = exams.indexOf(new ExamStudent(exam));
        if (index != -1) {
            return exams.get(index);
        }
        var examStudent = new ExamStudent(exam);
        exams.add(examStudent);
        return examStudent;
    }

    public Answer getAnswer(ExamStudent exam, OldQuestion oldQuestion) {
        if (exam.getAnswers() == null) {
            exam.setAnswers(new ArrayList<>());
        }
        int index = exam.getAnswers().indexOf(new Answer(oldQuestion));
        if (index != -1) {
            return exam.getAnswers().get(index);
        }
        var answer = new Answer(oldQuestion);
        exam.getAnswers().add(answer);
        return answer;
    }

    public void addExamStudent(ExamStudent exam) {
        if (exams == null) {
            exams = new ArrayList<>();
        }
        int index = exams.indexOf(new ExamStudent(exam.getExam()));
        if (index != -1) {
            exams.set(index, exam);
        } else {
            exams.add(exam);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.fullName);
        dest.writeString(this.phone);
        dest.writeString(this.email);
        dest.writeString(this.address);
        dest.writeString(this.imageProfile);
        dest.writeInt(this.type);
        dest.writeInt(this.grade);
        dest.writeList(this.exams);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.username = source.readString();
        this.password = source.readString();
        this.fullName = source.readString();
        this.phone = source.readString();
        this.email = source.readString();
        this.address = source.readString();
        this.imageProfile = source.readString();
        this.type = source.readInt();
        this.grade = source.readInt();
        this.exams = new ArrayList<>();
        source.readList(this.exams, ExamStudent.class.getClassLoader());
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.fullName = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
        this.address = in.readString();
        this.imageProfile = in.readString();
        this.type = in.readInt();
        this.grade = in.readInt();
        this.exams = new ArrayList<>();
        in.readList(this.exams, ExamStudent.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
