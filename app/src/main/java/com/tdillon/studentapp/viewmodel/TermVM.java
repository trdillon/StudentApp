package com.tdillon.studentapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tdillon.studentapp.database.AppRepository;
import com.tdillon.studentapp.model.Term;

import java.util.List;

public class TermVM extends AndroidViewModel {

    private AppRepository vmRepository;
    public LiveData<List<Term>> vmTerms;

    public TermVM(@NonNull Application application) {
        super(application);

        vmRepository = AppRepository.getInstance(application.getApplicationContext());
        vmTerms = vmRepository.rTerms;
    }
}