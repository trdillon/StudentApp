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

    private List<Course> courseData = new ArrayList<>();
    private CourseAdapter courseAdapter;

    @BindView(R.id.course_recycler_view)
    RecyclerView rvCourse;

    @OnClick(R.id.course_add_btn)
    void handleAddBtn() {
        Intent intent = new Intent(this, CourseEditActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_home)
    public void showHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initViewModel() {
        final Observer<List<Course>> courseObserver =
                courseEntities -> {
                    courseData.clear();
                    courseData.addAll(courseEntities);

                    if(courseAdapter == null) {
                        courseAdapter = new CourseAdapter(courseData, CourseActivity.this, RecyclerContext.MAIN, this);
                        rvCourse.setAdapter(courseAdapter);
                    }
                    else {
                        courseAdapter.notifyDataSetChanged();
                    }
                };
        CourseVM courseVM = new ViewModelProvider(this).get(CourseVM.class);
        courseVM.vmCourses.observe(this, courseObserver);
    }

    private void initRecyclerView() {
        rvCourse.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvCourse.setLayoutManager(layoutManager);
    }

    @Override
    public void onCourseSelected(int position, Course course) {
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