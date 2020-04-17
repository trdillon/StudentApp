package com.tdillon.studentapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tdillon.studentapp.database.AppRepository;
import com.tdillon.studentapp.model.Assessment;

import java.util.List;

public class AssessmentVM extends AndroidViewModel {

    private AppRepository vmRepository;
    public LiveData<List<Assessment>> vmAssessments;

    public AssessmentVM(@NonNull Application application) {
        super(application);

        vmRepository = AppRepository.getInstance(application.getApplicationContext());
        vmAssessments = vmRepository.rAssessments;
    }
}