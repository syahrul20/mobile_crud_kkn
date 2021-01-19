package com.lid.kkn.field;

import androidx.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
@Keep
public class MahasiswaField implements Serializable {
    private String uid;
    private String nim;
    private String stambuk;
    private String studentName;
    private String major;
    private String faculty;
    private String university;
    private String location;

    public MahasiswaField() {
    }

    public MahasiswaField(
            String uid,
            String nim,
            String stambuk,
            String studentName,
            String major,
            String faculty,
            String university,
            String location
    ) {
        this.uid = uid;
        this.nim = nim;
        this.stambuk = stambuk;
        this.studentName = studentName;
        this.major = major;
        this.faculty = faculty;
        this.university = university;
        this.location = location;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNim() {
        return nim;
    }

    public String getStambuk() {
        return stambuk;
    }

    public void setStambuk(String stambuk) {
        this.stambuk = stambuk;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
