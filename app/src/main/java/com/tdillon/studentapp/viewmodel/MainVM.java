package com.tdillon.studentapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tdillon.studentapp.database.AppRepository;
import com.tdillon.studentapp.model.Assessment;
import com.tdillon.studentapp.model.Course;
import com.tdillon.studentapp.model.Mentor;
import com.tdillon.studentapp.model.Term;

import java.util.List;

public class MainVM extends AndroidViewModel {

    private AppRepository vmRepository;
    public LiveData<List<Assessment>> vmAssessments;
    public LiveData<List<Course>> vmCourses;
    public LiveData<List<Mentor>> vmMentors;
    public LiveData<List<Term>> vmTerms;

    public MainVM(@NonNull Application application) {
        super(application);

        vmRepository = AppRepository.getInstance(application.getApplicationContext());
        vmAssessments = vmRepository.getAllAssessments();
        vmCourses = vmRepository.getAllCourses();
        vmMentors = vmRepository.getAllMentors();
        vmTerms = vmRepository.getAllTerms();
    }
}