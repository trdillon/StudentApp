package com.tdillon.studentapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tdillon.studentapp.model.Assessment;

import java.util.List;

@Dao
public interface AssessmentDAO {

    //Assessment queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAssessment(Assessment assessment);
    
    @Delete
    void deleteAssessment(Assessment assessment);

    @Query("SELECT * FROM assessments ORDER BY date DESC")
    LiveData<List<Assessment>> getAllAssessments();

    @Query("SELECT * FROM assessments WHERE id = :id")
    Assessment getAssessmentById(int id);

    @Query("SELECT * FROM assessments WHERE courseId = :courseId")
    LiveData<List<Assessment>> getAssessmentsByCourse(final int courseId);
}