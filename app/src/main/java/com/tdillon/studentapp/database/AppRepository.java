package com.tdillon.studentapp.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.tdillon.studentapp.model.Assessment;
import com.tdillon.studentapp.model.Course;
import com.tdillon.studentapp.model.Mentor;
import com.tdillon.studentapp.model.Term;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {

    private static AppRepository currInstance;

    public LiveData<List<Assessment>> rAssessments;
    public LiveData<List<Course>> rCourses;
    public LiveData<List<Mentor>> rMentors;
    public LiveData<List<Term>> rTerms;

    private AppDatabase rDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    private AppRepository(Context context) {
        rDb = AppDatabase.getInstance(context);
        rAssessments = getAllAssessments();
        rCourses = getAllCourses();
        rMentors = getAllMentors();
        rTerms = getAllTerms();
    }

    //Get the database instance
    public static AppRepository getInstance(Context context) {
        if(currInstance == null) {
            currInstance = new AppRepository(context);
        }
        return currInstance;
    }

    //Assessment methods
    public void addAssessment(final Assessment assessment) {
        executor.execute(() -> rDb.assessmentDAO().addAssessment(assessment));
    }

    public void deleteAssessment(final Assessment assessment) {
        executor.execute(() -> rDb.assessmentDAO().deleteAssessment(assessment));
    }

    public LiveData<List<Assessment>> getAllAssessments() {
        return rDb.assessmentDAO().getAllAssessments();
    }

    public Assessment getAssessmentById(int assessmentId) {
        return rDb.assessmentDAO().getAssessmentById(assessmentId);
    }

    public LiveData<List<Assessment>> getAssessmentsByCourse(final int courseId) {
        return rDb.assessmentDAO().getAssessmentsByCourse(courseId);
    }

    //Course methods
    public void addCourse(final Course course) {
        executor.execute(() -> rDb.courseDAO().addCourse(course));
    }

    public void deleteCourse(final Course course) {
        executor.execute(() -> rDb.courseDAO().deleteCourse(course));
    }

    public LiveData<List<Course>> getAllCourses() {
        return rDb.courseDAO().getAllCourses();
    }

    public Course getCourseById(int courseId) {
        return rDb.courseDAO().getCourseById(courseId);
    }

    public LiveData<List<Course>> getCoursesByTerm(final int termId) {
        return rDb.courseDAO().getCoursesByTerm(termId);
    }

    //Mentor methods
    public void addMentor(final Mentor mentor) {
        executor.execute(() -> rDb.mentorDAO().addMentor(mentor));
    }

    public void deleteMentor(final Mentor mentor) {
        executor.execute(() -> rDb.mentorDAO().deleteMentor(mentor));
    }

    public LiveData<List<Mentor>> getAllMentors() {
        return rDb.mentorDAO().getAllMentors();
    }

    public Mentor getMentorById(int mentorId) {
        return rDb.mentorDAO().getMentorById(mentorId);
    }

    public LiveData<List<Mentor>> getMentorsByCourse(final int courseId) {
        return rDb.mentorDAO().getMentorsByCourse(courseId);
    }

    //Term methods
    public void addTerm(final Term term) {
        executor.execute(() -> rDb.termDAO().addTerm(term));
    }

    public void deleteTerm(final Term term) {
        executor.execute(() -> rDb.termDAO().deleteTerm(term));
    }

    public LiveData<List<Term>> getAllTerms() {
        return rDb.termDAO().getAllTerms();
    }

    public Term getTermById(int termId) {
        return rDb.termDAO().getTermById(termId);
    }
}