package com.tdillon.studentapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tdillon.studentapp.model.Term;

import java.util.List;

@Dao
public interface TermDAO {

    //Term queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTerm(Term term);

    @Delete
    void deleteTerm(Term term);

    @Query("SELECT * FROM terms ORDER BY startDate DESC")
    LiveData<List<Term>> getAllTerms();

    @Query("SELECT * FROM terms WHERE id = :id")
    Term getTermById(int id);
}