package com.tdillon.studentapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tdillon.studentapp.model.Course;

import java.util.List;

@Dao
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCourse(Course course);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAllCourses(List<Course> courses);

    @Delete
    void deleteCourse(Course course);

    @Query("SELECT * FROM courses ORDER BY startDate DESC")
    LiveData<List<Course>> getAllCourses();

    @Query("SELECT * FROM courses WHERE id = :id")
    Course getCourseById(int id);

    @Query("SELECT * FROM courses WHERE termId = :termId")
    LiveData<List<Course>> getCoursesByTerm(final int termId);

    @Query("DELETE FROM courses")
    int deleteAllCourses();

    @Query("SELECT COUNT(*) FROM courses")
    int getCourseCount();
}