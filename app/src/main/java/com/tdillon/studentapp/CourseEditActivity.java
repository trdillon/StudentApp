package com.tdillon.studentapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.tdillon.studentapp.model.CourseStatus;
import com.tdillon.studentapp.util.TextFormatter;
import com.tdillon.studentapp.viewmodel.EditorVM;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tdillon.studentapp.util.Constants.COURSE_ID_KEY;
import static com.tdillon.studentapp.util.Constants.EDITING_KEY;
import static com.tdillon.studentapp.util.Constants.TERM_ID_KEY;

public class CourseEditActivity extends AppCompatActivity {

    @BindView(R.id.course_edit_title)
    EditText tvCourseTitle;

    @BindView(R.id.course_edit_start)
    EditText tvCourseStartDate;

    @BindView(R.id.course_edit_end)
    EditText tvCourseEndDate;

    @BindView(R.id.course_edit_start_btn)
    ImageButton btnStartDate;

    @BindView(R.id.course_edit_end_btn)
    ImageButton btnEndDate;

    @BindView(R.id.course_edit_status_dropdown)
    Spinner spCourseStatus;

    @BindView(R.id.course_edit_note)
    EditText tvNote;

    private EditorVM aViewModel;
    private boolean aNewCourse, aEditing;
    private int termId = -1;
    private ArrayAdapter<CourseStatus> courseStatusAdapter;

    private void initViewModel() {
        aViewModel = new ViewModelProvider(this).get(EditorVM.class);

        aViewModel.vmLiveCourse.observe(this, course -> {
            if(course != null && !aEditing) {
                tvCourseTitle.setText(course.getTitle());
                tvCourseStartDate.setText(TextFormatter.fullDateFormat.format(course.getStartDate()));
                tvCourseEndDate.setText(TextFormatter.fullDateFormat.format(course.getExpectedEndDate()));
                tvNote.setText(course.getNote());
                int position = getSpinnerPosition(course.getCourseStatus());
                spCourseStatus.setSelection(position);
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            setTitle(getString(R.string.new_course));
            aNewCourse = true;
        } else if (extras.containsKey(TERM_ID_KEY)){ // Check if this is adding a course to a term
            termId = extras.getInt(TERM_ID_KEY);
            Log.v("DEBUG", "Extras term ID: " + termId);
            setTitle(getString(R.string.new_course));
        } else {
            setTitle(getString(R.string.edit_course));
            int courseId = extras.getInt(COURSE_ID_KEY);
            aViewModel.loadCourse(courseId);
        }
    }

    public void addCourse() {
        try {
            Date startDate = TextFormatter.fullDateFormat.parse(tvCourseStartDate.getText().toString());
            Date endDate = TextFormatter.fullDateFormat.parse(tvCourseEndDate.getText().toString());
            aViewModel.addCourse(tvCourseTitle.getText().toString(), startDate, endDate, getSpinnerValue(), tvNote.getText().toString(), termId);
            Log.v("Saved Course", tvCourseTitle.toString());
        } catch (ParseException e) {
            Log.v("Exception", Objects.requireNonNull(e.getLocalizedMessage()));
        }
        finish();
    }

    @OnClick(R.id.course_edit_start_btn)
    public void courseStartDatePicker() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            tvCourseStartDate.setText(TextFormatter.fullDateFormat.format(myCalendar.getTime()));
        };
        new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.course_edit_end_btn)
    public void courseEndDatePicker() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            tvCourseEndDate.setText(TextFormatter.fullDateFormat.format(myCalendar.getTime()));
        };
        new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void addSpinnerItems() {
        courseStatusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CourseStatus.values());
        spCourseStatus.setAdapter(courseStatusAdapter);
    }

    private CourseStatus getSpinnerValue() {
        return (CourseStatus) spCourseStatus.getSelectedItem();
    }

    private int getSpinnerPosition(CourseStatus courseStatus) {
        return courseStatusAdapter.getPosition(courseStatus);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!aNewCourse) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            addCourse();
            return true;
        } else if(item.getItemId() == R.id.action_delete) {
            aViewModel.deleteCourse();
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
        addCourse();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_save);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if(savedInstanceState != null) {
            aEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }
        initViewModel();
        // Set up spinner object
        addSpinnerItems();
    }
}