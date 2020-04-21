package com.tdillon.studentapp.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tdillon.studentapp.database.AppRepository;
import com.tdillon.studentapp.model.Assessment;
import com.tdillon.studentapp.model.AssessmentType;
import com.tdillon.studentapp.model.Course;
import com.tdillon.studentapp.model.CourseStatus;
import com.tdillon.studentapp.model.Mentor;
import com.tdillon.studentapp.model.Term;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditorVM extends AndroidViewModel {

    private AppRepository vmRepository;
    public LiveData<List<Assessment>> vmAssessments;
    public LiveData<List<Course>> vmCourses;
    public LiveData<List<Mentor>> vmMentors;
    public LiveData<List<Term>> vmTerms;
    public MutableLiveData<Assessment> vmLiveAssessment = new MutableLiveData<>();
    public MutableLiveData<Course> vmLiveCourse = new MutableLiveData<>();
    public MutableLiveData<Mentor> vmLiveMentor = new MutableLiveData<>();
    public MutableLiveData<Term> vmLiveTerm = new MutableLiveData<>();
    private Executor executor = Executors.newSingleThreadExecutor();

    public EditorVM(@NonNull Application application) {
        super(application);

        vmRepository = AppRepository.getInstance(getApplication());
        vmAssessments = vmRepository.rAssessments;
        vmCourses = vmRepository.rCourses;
        vmMentors = vmRepository.rMentors;
        vmTerms = vmRepository.rTerms;
    }

    //Assessment methods
    public void addAssessment(String assessmentTitle, Date date,
                              AssessmentType assessmentType, int courseId) {
        Assessment assessment = vmLiveAssessment.getValue();

        if(assessment == null) {
            if(TextUtils.isEmpty(assessmentTitle.trim())) {
                return;
            }
            assessment = new Assessment(assessmentTitle.trim(), date, assessmentType, courseId);
        }
        else {
            assessment.setTitle(assessmentTitle.trim());
            assessment.setDate(date);
            assessment.setAssessmentType(assessmentType);
            assessment.setCourseId(courseId);
        }
        vmRepository.addAssessment(assessment);
    }

    public void overwriteAssessment(Assessment assessment, int courseId) {
        assessment.setCourseId(courseId);
        vmRepository.addAssessment(assessment);
    }

    public void deleteAssessment() {
        vmRepository.deleteAssessment(vmLiveAssessment.getValue());
    }

    public void loadAssessment(final int assessmentId) {
        executor.execute(() -> {
            Assessment assessment = vmRepository.getAssessmentById(assessmentId);
            vmLiveAssessment.postValue(assessment);});
    }

    //Course methods
    public void addCourse(String courseTitle, Date startDate, Date endDate,
                          CourseStatus courseStatus, String note, int termId) {
        Course course = vmLiveCourse.getValue();

        if(course == null) {
            if(TextUtils.isEmpty(courseTitle.trim())) {
                return;
            }
            course = new Course(courseTitle.trim(), startDate, endDate, courseStatus, termId);
        }
        else {
            course.setTitle(courseTitle.trim());
            course.setStartDate(startDate);
            course.setExpectedEndDate(endDate);
            course.setCourseStatus(courseStatus);
            course.setNote(note);
            course.setTermId(termId);
        }
        vmRepository.addCourse(course);
    }

    public void overwriteCourse(Course course, int termId) {
        course.setTermId(termId);
        vmRepository.addCourse(course);
    }

    public void deleteCourse() {
        vmRepository.deleteCourse(vmLiveCourse.getValue());
    }

    public void loadCourse(final int courseId) {
        executor.execute(() -> {
            Course course = vmRepository.getCourseById(courseId);
            vmLiveCourse.postValue(course);});
    }

    //Mentor methods
    public void addMentor(String name, String email, String phone, int courseId) {
        Mentor mentor = vmLiveMentor.getValue();

        if(mentor == null) {
            if (TextUtils.isEmpty(name.trim())) {
                return;
            }
            mentor = new Mentor(name, email, phone, courseId);
        }
        else {
            mentor.setName(name);
            mentor.setEmail(email);
            mentor.setPhone(phone);
            mentor.setCourseId(courseId);
        }
        vmRepository.addMentor(mentor);
    }

    public void overwriteMentor(Mentor mentor, int courseId) {
        mentor.setCourseId(courseId);
        vmRepository.addMentor(mentor);
    }

    public void deleteMentor() {
        vmRepository.deleteMentor(vmLiveMentor.getValue());
    }

    public void loadMentor(final int mentorId) {
        executor.execute(() -> {
            Mentor mentor = vmRepository.getMentorById(mentorId);
            vmLiveMentor.postValue(mentor);});
    }

    //Term methods
    public void addTerm(String termTitle, Date startDate, Date endDate) {
        Term term = vmLiveTerm.getValue();

        if(term == null) {
            if(TextUtils.isEmpty(termTitle.trim())) {
                return;
            }
            term = new Term(termTitle.trim(), startDate, endDate);
        }
        else {
            term.setTitle(termTitle.trim());
            term.setStartDate(startDate);
            term.setEndDate(endDate);
        }
        vmRepository.addTerm(term);
    }

    public void deleteTerm() {
        vmRepository.deleteTerm(vmLiveTerm.getValue());
    }

    public void loadTerm(final int termId) {
        executor.execute(() -> {
            Term term = vmRepository.getTermById(termId);
            vmLiveTerm.postValue(term);});
    }

    //Getter methods
    public Term getTermById(int termId) {
        return vmRepository.getTermById(termId);
    }

    public LiveData<List<Assessment>> getAssessmentsInCourse(int courseId) {
        return (vmRepository.getAssessmentsByCourse(courseId));
    }

    public LiveData<List<Course>> getCoursesInTerm(int termId) {
        return (vmRepository.getCoursesByTerm(termId));
    }

    public LiveData<List<Mentor>> getMentorsInCourse(int courseId) {
        return (vmRepository.getMentorsByCourse(courseId));
    }

    public LiveData<List<Assessment>> getUnassignedAssessments() {
        return (vmRepository.getAssessmentsByCourse(-1));
    }

    public LiveData<List<Course>> getUnassignedCourses() {
        return (vmRepository.getCoursesByTerm(-1));
    }

    public LiveData<List<Mentor>> getUnassignedMentors() {
        return (vmRepository.getMentorsByCourse(-1));
    }
}