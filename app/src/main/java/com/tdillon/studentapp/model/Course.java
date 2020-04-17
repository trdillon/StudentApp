package com.tdillon.studentapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "courses")
public class Course {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private Date startDate;
    private Date expectedEndDate;
    private CourseStatus courseStatus;
    private String note;
    private int termId;

    @Ignore
    public Course() {
    }

    @Ignore
    public Course(int id, String title, Date startDate, Date expectedEndDate, CourseStatus courseStatus, String note, int termId) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.courseStatus = courseStatus;
        this.note = note;
        this.termId = termId;
    }

    @Ignore
    public Course(String title, Date startDate, Date expectedEndDate, CourseStatus courseStatus, int termId) {
        this.title = title;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.courseStatus = courseStatus;
        this.termId = termId;
    }

    public Course(String title, Date startDate, Date expectedEndDate, CourseStatus courseStatus) {
        this.title = title;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.courseStatus = courseStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpectedEndDate() {
        return expectedEndDate;
    }

    public void setExpectedEndDate(Date expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public CourseStatus getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(CourseStatus courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }
}