package com.tdillon.studentapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tdillon.studentapp.model.AssessmentType;
import com.tdillon.studentapp.util.TextFormatter;
import com.tdillon.studentapp.viewmodel.EditorVM;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tdillon.studentapp.util.Constants.ASSESSMENT_ID_KEY;
import static com.tdillon.studentapp.util.Constants.COURSE_ID_KEY;
import static com.tdillon.studentapp.util.Constants.EDITING_KEY;

public class AssessmentEditActivity extends AppCompatActivity {

    @BindView(R.id.asmt_edit_title)
    EditText tvAssessmentTitle;

    @BindView(R.id.asmt_edit_date)
    EditText tvAssessmentDate;

    @BindView(R.id.asmt_edit_type_dropdown)
    Spinner spAssessmentType;

    private EditorVM aViewModel;
    private boolean aNewAssessment, aEditing;
    private int courseId = -1;
    private ArrayAdapter<AssessmentType> assessmentTypeAdapter;

    private void initViewModel() {
        aViewModel = new ViewModelProvider(this).get(EditorVM.class);

        aViewModel.vmLiveAssessment.observe(this, assessment -> {
            if(assessment != null && !aEditing) {
                tvAssessmentTitle.setText(assessment.getTitle());
                tvAssessmentDate.setText(TextFormatter.getDateFormatted(assessment.getDate()));
                int position = getSpinnerPosition(assessment.getAssessmentType());
                spAssessmentType.setSelection(position);
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            setTitle(getString(R.string.new_assessment));
            aNewAssessment = true;
        } else if (extras.containsKey(COURSE_ID_KEY)) {
            courseId = extras.getInt(COURSE_ID_KEY);
            Log.v("DEBUG", "Extras course ID: " + courseId);
            setTitle(getString(R.string.new_assessment));
        } else {
            setTitle(R.string.edit_assessment);
            int assessmentId = extras.getInt(ASSESSMENT_ID_KEY);
            aViewModel.loadAssessment(assessmentId);
        }
    }

    public void addAssessment() {
        try {
            Date date = TextFormatter.getDateFormattedString(tvAssessmentDate.getText().toString());
            aViewModel.addAssessment(tvAssessmentTitle.getText().toString(), date, getSpinnerValue(), courseId);
            Log.v("Saved Assessment", tvAssessmentTitle.toString());
        } catch (ParseException e) {
            Log.v("Exception", Objects.requireNonNull(e.getLocalizedMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    @OnClick(R.id.asmt_edit_date_btn)
    public void assessmentDatePicker() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            tvAssessmentDate.setText(TextFormatter.getDateFormatted(myCalendar.getTime()));
        };
        new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void addSpinnerItems() {
        assessmentTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, AssessmentType.values());
        spAssessmentType.setAdapter(assessmentTypeAdapter);
    }

    private AssessmentType getSpinnerValue() {
        return (AssessmentType) spAssessmentType.getSelectedItem();
    }

    private int getSpinnerPosition(AssessmentType assessmentType) {
        return assessmentTypeAdapter.getPosition(assessmentType);
    }

    /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!aNewAssessment) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            addAssessment();
            return true;
        } else if(item.getItemId() == R.id.action_delete) {
            aViewModel.deleteAssessment();
            finish();
        }
        return super.onOptionsItemSelected(item);
    } */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        addAssessment();
    }

    @OnClick(R.id.fab_save_assessment)
    public void handleSaveBtn(View view) {
        addAssessment();
    }

    @OnClick(R.id.button_home)
    public void showHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_edit);
        ButterKnife.bind(this);

        if(savedInstanceState != null) {
            aEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        initViewModel();
        addSpinnerItems();
    }
}