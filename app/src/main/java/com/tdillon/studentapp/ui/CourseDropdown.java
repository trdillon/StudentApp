package com.tdillon.studentapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdillon.studentapp.R;
import com.tdillon.studentapp.model.Course;

import java.util.List;

public class CourseDropdown extends PopupWindow {

    private Context currContext;
    private List<Course> currCourses;
    private CoursePopup coursePopup;

    public CourseDropdown(Context currContext, List<Course> currCourses) {
        super(currContext);
        this.currContext = currContext;
        this.currCourses = currCourses;
        setupView();
    }

    private void setupView() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(currContext).inflate(R.layout.popup_view, null);

        RecyclerView rv = view.findViewById(R.id.rv_popup);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(currContext, LinearLayoutManager.VERTICAL, false));
        rv.addItemDecoration(new DividerItemDecoration(currContext, LinearLayoutManager.VERTICAL));

        coursePopup = new CoursePopup(currCourses);
        rv.setAdapter(coursePopup);

        setContentView(view);
    }

    public void setCourseListener(CoursePopup.CourseListener currCourseListener) {
        coursePopup.setCourseListener(currCourseListener);
    }
}