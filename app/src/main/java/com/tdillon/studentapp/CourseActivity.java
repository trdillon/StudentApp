package com.tdillon.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdillon.studentapp.model.Course;
import com.tdillon.studentapp.ui.CourseAdapter;
import com.tdillon.studentapp.ui.RecyclerContext;
import com.tdillon.studentapp.viewmodel.CourseVM;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CourseActivity extends AppCompatActivity implements CourseAdapter.CourseListener {

    @BindView(R.id.course_recycler_view)
    RecyclerView aCourseRecyclerView;

    @OnClick(R.id.fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, CourseEditActivity.class);
        startActivity(intent);
    }

    private List<Course> courseData = new ArrayList<>();
    private CourseAdapter aCourseAdapter;

    private void initViewModel() {
        final Observer<List<Course>> courseObserver =
                courseEntities -> {
                    courseData.clear();
                    courseData.addAll(courseEntities);

                    if(aCourseAdapter == null) {
                        aCourseAdapter = new CourseAdapter(courseData, CourseActivity.this, RecyclerContext.MAIN, this);
                        aCourseRecyclerView.setAdapter(aCourseAdapter);
                    }
                    else {
                        aCourseAdapter.notifyDataSetChanged();
                    }
                };
        CourseVM aCourseVM = new ViewModelProvider(this).get(CourseVM.class);
        aCourseVM.vmCourses.observe(this, courseObserver);
    }

    private void initRecyclerView() {
        aCourseRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        aCourseRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onCourseSelected(int position, Course course) {
    }

    @OnClick(R.id.button_home)
    public void showHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_main);
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }
}