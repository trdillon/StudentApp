package com.tdillon.studentapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.tdillon.studentapp.model.Assessment;
import com.tdillon.studentapp.model.Course;
import com.tdillon.studentapp.model.Mentor;
import com.tdillon.studentapp.model.Term;

@Database(entities = {Term.class, Course.class, Assessment.class, Mentor.class}, version = 8)
@TypeConverters({DateConverter.class, CourseStatusConverter.class, AssessmentTypeConverter.class})

public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "AppDatabase.db";
    private static volatile AppDatabase instance;
    private static final Object LOCK = new Object();

    //Model DAOs
    public abstract AssessmentDAO assessmentDAO();
    public abstract CourseDAO courseDAO();
    public abstract MentorDAO mentorDAO();
    public abstract TermDAO termDAO();

    public static AppDatabase getInstance(Context context) {
        if(instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }
}