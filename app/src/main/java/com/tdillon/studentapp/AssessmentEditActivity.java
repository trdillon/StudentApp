package com.tdillon.studentapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tdillon.studentapp.model.AssessmentType;
import com.tdillon.studentapp.util.AlertReceiver;
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

    @BindView(R.id.text_goal_millis)
    EditText tvGoalMillis;

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

    private void deleteAssessment() {
        if(aViewModel.vmLiveAssessment.getValue() != null) {
            String assessmentTitle = aViewModel.vmLiveAssessment.getValue().getTitle();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete " + assessmentTitle + "?");
            builder.setMessage("Are you sure you want to delete assessment '" + assessmentTitle + "'?");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setPositiveButton("Yes", (dialog, id) -> {
                dialog.dismiss();
                aViewModel.deleteAssessment();
                finish();
            });
            builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void alertAssessment() {
        Intent intent = new Intent(AssessmentEditActivity.this, AlertReceiver.class);
        intent.putExtra("key", "You have an assessment due today!");
        PendingIntent sender = PendingIntent.getBroadcast(AssessmentEditActivity.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        long goalDateAlert = Long.parseLong(tvGoalMillis.getText().toString());
        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC_WAKEUP, goalDateAlert, sender);
        Log.v("INFO", "Added alert");
    }

    @OnClick(R.id.asmt_edit_date_btn)
    public void assessmentDatePicker() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            tvAssessmentDate.setText(TextFormatter.getDateFormatted(myCalendar.getTime()));
            tvGoalMillis.setText(Long.toString(myCalendar.getTimeInMillis()));
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

    @OnClick(R.id.fab_alert_assessment)
    public void handleAlertBtn(View view) {
        alertAssessment();
    }

    @OnClick(R.id.fab_delete_assessment)
    public void handleDeleteBtn(View view) {
        deleteAssessment();
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