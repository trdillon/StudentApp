package com.tdillon.studentapp.ui;

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

    private Context uContext;
    private RecyclerView rPopup;
    private List<Assessment> uAssessments;
    private AssessmentPopup assessmentPopup;

    public AssessmentDropdown(Context uContext, List<Assessment> uAssessments) {
        super(uContext);
        this.uContext = uContext;
        this.uAssessments = uAssessments;
        setupView();
    }

    private void setupView() {
        View view = LayoutInflater.from(uContext).inflate(R.layout.popup_view, null);

        rPopup = view.findViewById(R.id.rv_popup);
        rPopup.setHasFixedSize(true);
        rPopup.setLayoutManager(new LinearLayoutManager(uContext, LinearLayoutManager.VERTICAL, false));
        rPopup.addItemDecoration(new DividerItemDecoration(uContext, LinearLayoutManager.VERTICAL));

        assessmentPopup = new AssessmentPopup(uAssessments);
        rPopup.setAdapter(assessmentPopup);

        setContentView(view);
    }

    public void setAssessmentListener(AssessmentPopup.AssessmentListener currAssessmentListener) {
        assessmentPopup.setAssessmentListener(currAssessmentListener);
    }
}
