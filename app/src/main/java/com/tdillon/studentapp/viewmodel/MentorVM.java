package com.tdillon.studentapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tdillon.studentapp.database.AppRepository;
import com.tdillon.studentapp.model.Mentor;

import java.util.List;

public class MentorVM extends AndroidViewModel {

    private AppRepository vmRepository;
    public LiveData<List<Mentor>> vmMentors;

    public MentorVM(@NonNull Application application) {
        super(application);

        vmRepository = AppRepository.getInstance(application.getApplicationContext());
        vmMentors = vmRepository.rMentors;
    }
}