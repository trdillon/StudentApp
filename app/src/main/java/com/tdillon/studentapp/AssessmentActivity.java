package com.tdillon.studentapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdillon.studentapp.model.Assessment;
import com.tdillon.studentapp.ui.AssessmentAdapter;
import com.tdillon.studentapp.ui.RecyclerContext;
import com.tdillon.studentapp.viewmodel.AssessmentVM;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AssessmentActivity extends AppCompatActivity implements AssessmentAdapter.AssessmentListener {

    @BindView(R.id.assessment_recycler_view)
    RecyclerView aAssessmentRecyclerView;

    @OnClick(R.id.fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, AssessmentEditActivity.class);
        startActivity(intent);
    }

    private List<Assessment> assessmentData = new ArrayList<>();
    private AssessmentAdapter aAssessmentAdapter;
    private AssessmentVM aAssessmentVM;

    private void initViewModel() {
        final Observer<List<Assessment>> assessmentObserver =
                assessmentEntities -> {
                    assessmentData.clear();
                    assessmentData.addAll(assessmentEntities);

                    if(aAssessmentAdapter == null) {
                        aAssessmentAdapter = new AssessmentAdapter(assessmentData, AssessmentActivity.this, RecyclerContext.MAIN, this);
                        aAssessmentRecyclerView.setAdapter(aAssessmentAdapter);
                    } else {
                        aAssessmentAdapter.notifyDataSetChanged();
                    }
                };
        aAssessmentVM = new ViewModelProvider(this).get(AssessmentVM.class);
        aAssessmentVM.vmAssessments.observe(this, assessmentObserver);
    }

    private void initRecyclerView() {
        aAssessmentRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        aAssessmentRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onAssessmentSelected(int position, Assessment assessment) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }
}