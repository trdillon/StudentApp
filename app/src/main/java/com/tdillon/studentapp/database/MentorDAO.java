package com.tdillon.studentapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tdillon.studentapp.model.Mentor;

import java.util.List;

@Dao
public interface MentorDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMentor(Mentor mentor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAllMentors(List<Mentor> mentors);

    @Delete
    void deleteMentor(Mentor mentor);

    @Query("SELECT * FROM mentors ORDER BY name DESC")
    LiveData<List<Mentor>> getAllMentors();

    @Query("SELECT * FROM mentors WHERE id = :id")
    Mentor getMentorById(int id);

    @Query("Select * FROM mentors WHERE courseId = :courseId")
    LiveData<List<Mentor>> getMentorsByCourse(final int courseId);

    @Query("DELETE FROM mentors")
    int deleteAllMentors();

    @Query("SELECT COUNT(*) FROM mentors")
    int getMentorCount();
}