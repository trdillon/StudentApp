package com.tdillon.studentapp.ui;

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

    private Context uContext;
    private RecyclerView rPopup;
    private List<Course> uCourses;
    private CoursePopup coursePopup;

    public CourseDropdown(Context uContext, List<Course> uCourses) {
        super(uContext);
        this.uContext = uContext;
        this.uCourses = uCourses;
        setupView();
    }

    private void setupView() {
        View view = LayoutInflater.from(uContext).inflate(R.layout.popup_view, null);

        rPopup = view.findViewById(R.id.rv_popup);
        rPopup.setHasFixedSize(true);
        rPopup.setLayoutManager(new LinearLayoutManager(uContext, LinearLayoutManager.VERTICAL, false));
        rPopup.addItemDecoration(new DividerItemDecoration(uContext, LinearLayoutManager.VERTICAL));

        coursePopup = new CoursePopup(uCourses);
        rPopup.setAdapter(coursePopup);

        setContentView(view);
    }

    public void setCourseListener(CoursePopup.CourseListener currCourseListener) {
        coursePopup.setCourseListener(currCourseListener);
    }
}