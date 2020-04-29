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
import com.tdillon.studentapp.model.Assessment;

import java.util.List;

public class AssessmentDropdown extends PopupWindow {

    private Context currContext;
    private List<Assessment> currAssessments;
    private AssessmentPopup assessmentPopup;

    public AssessmentDropdown(Context currContext, List<Assessment> currAssessments) {
        super(currContext);
        this.currContext = currContext;
        this.currAssessments = currAssessments;
        setupView();
    }

    private void setupView() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(currContext).inflate(R.layout.popup_view, null);

        RecyclerView rv = view.findViewById(R.id.rv_popup);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(currContext, LinearLayoutManager.VERTICAL, false));
        rv.addItemDecoration(new DividerItemDecoration(currContext, LinearLayoutManager.VERTICAL));

        assessmentPopup = new AssessmentPopup(currAssessments);
        rv.setAdapter(assessmentPopup);

        setContentView(view);
    }

    public void setAssessmentListener(AssessmentPopup.AssessmentListener currAssessmentListener) {
        assessmentPopup.setAssessmentListener(currAssessmentListener);
    }
}