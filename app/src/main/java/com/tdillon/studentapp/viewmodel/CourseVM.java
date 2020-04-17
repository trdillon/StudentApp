package com.tdillon.studentapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tdillon.studentapp.database.AppRepository;
import com.tdillon.studentapp.model.Course;

import java.util.List;

public class CourseVM extends AndroidViewModel {

    private AppRepository vmRepository;
    public LiveData<List<Course>> vmCourses;

    public CourseVM(@NonNull Application application) {
        super(application);

        vmRepository = AppRepository.getInstance(application.getApplicationContext());
        vmCourses = vmRepository.rCourses;
    }
}