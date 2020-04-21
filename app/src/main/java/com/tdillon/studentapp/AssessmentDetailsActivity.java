package com.tdillon.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tdillon.studentapp.util.TextFormatter;
import com.tdillon.studentapp.viewmodel.EditorVM;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tdillon.studentapp.util.Constants.ASSESSMENT_ID_KEY;

public class AssessmentDetailsActivity extends AppCompatActivity {

    @BindView(R.id.asmt_detail_date)
    TextView tvAssessmentDate;

    @BindView(R.id.asmt_detail_type)
    TextView tvAssessmentType;

    private int assessmentId;
    private EditorVM aViewModel;

    private void initViewModel() {
        aViewModel = new ViewModelProvider(this).get(EditorVM.class);

        aViewModel.vmLiveAssessment.observe(this, assessment -> {
            tvAssessmentDate.setText(TextFormatter.getDateFormatted(assessment.getDate()));
            tvAssessmentType.setText(assessment.getAssessmentType().toString());
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            assessmentId = extras.getInt(ASSESSMENT_ID_KEY);
            aViewModel.loadAssessment(assessmentId);
        }
        else {
            finish();
        }
    }

    @OnClick(R.id.fab_edit_assessment)
    public void editAssessment() {
        Intent intent = new Intent(this, AssessmentEditActivity.class);
        intent.putExtra(ASSESSMENT_ID_KEY, assessmentId);
        this.startActivity(intent);
        finish();
    }

    @OnClick(R.id.button_home)
    public void showHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);
        ButterKnife.bind(this);
        initViewModel();
    }
}