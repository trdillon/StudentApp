package com.tdillon.studentapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.tdillon.studentapp.model.Course;
import com.tdillon.studentapp.ui.RecyclerContext;
import com.tdillon.studentapp.util.TextFormatter;
import com.tdillon.studentapp.viewmodel.EditorVM;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tdillon.studentapp.util.Constants.EDITING_KEY;
import static com.tdillon.studentapp.util.Constants.TERM_ID_KEY;

public class TermEditActivity extends AppCompatActivity {

    @BindView(R.id.term_edit_title)
    EditText tvTermTitle;

    @BindView(R.id.term_edit_start)
    EditText tvTermStartDate;

    @BindView(R.id.term_edit_end)
    EditText tvTermEndDate;

    @BindView(R.id.term_edit_start_btn)
    ImageButton btnStartDate;

    @BindView(R.id.term_edit_end_btn)
    ImageButton btnEndDate;

    private EditorVM aViewModel;
    private boolean aNewTerm, aEditing;
    private List<Course> courseData = new ArrayList<>();
    int termId;

    private void initViewModel() {
        aViewModel = new ViewModelProvider(this).get(EditorVM.class);

        aViewModel.vmLiveTerm.observe(this, term -> {
            if(term != null && !aEditing) {
                tvTermTitle.setText(term.getTitle());
                tvTermStartDate.setText(TextFormatter.fullDateFormat.format(term.getStartDate()));
                tvTermEndDate.setText(TextFormatter.fullDateFormat.format(term.getEndDate()));
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            setTitle(getString(R.string.new_term));
            aNewTerm = true;
        } else {
            setTitle(getString(R.string.edit_term));
            termId = extras.getInt(TERM_ID_KEY);
            aViewModel.loadTerm(termId);
        }

        final Observer<List<Course>> courseObserver =
                courseEntities -> {
                    courseData.clear();
                    courseData.addAll(courseEntities);
                };

        aViewModel.getCoursesInTerm(termId).observe(this, courseObserver);
    }

    public void addTerm() {
        try {
            Date startDate = TextFormatter.fullDateFormat.parse(tvTermStartDate.getText().toString());
            Date endDate = TextFormatter.fullDateFormat.parse(tvTermEndDate.getText().toString());
            aViewModel.addTerm(tvTermTitle.getText().toString(), startDate, endDate);
            Log.v("Saved term",tvTermTitle.toString());

        } catch (ParseException e) {
            Log.v("Exception", Objects.requireNonNull(e.getLocalizedMessage()));
        }
        finish();
    }

    private void deleteTerm() {
        if(aViewModel.vmLiveTerm.getValue() != null) {
            String termTitle = aViewModel.vmLiveTerm.getValue().getTitle();
            if(courseData != null && courseData.size() != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete " + termTitle + "?");
                builder.setMessage("Are you sure you want to delete term '" + termTitle + "'?" +
                        "\nIt still has courses assigned to it. You will not delete the courses but " +
                        "they will not be assigned to any terms if you delete.\nDelete term anyway?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Yes", (dialog, id) -> {
                    dialog.dismiss();
                    aViewModel.deleteTerm();
                    finish();
                });
                builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
                builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete " + termTitle + "?");
                builder.setMessage("Are you sure you want to delete term '" + termTitle + "'?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Yes", (dialog, id) -> {
                    dialog.dismiss();
                    aViewModel.deleteTerm();
                    finish();
                });
                builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    @OnClick(R.id.term_edit_start_btn)
    public void termStartDatePicker() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            tvTermStartDate.setText(TextFormatter.fullDateFormat.format(myCalendar.getTime()));
        };
        new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.term_edit_end_btn)
    public void termEndDatePicker() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            tvTermEndDate.setText(TextFormatter.fullDateFormat.format(myCalendar.getTime()));
        };
        new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!aNewTerm) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            addTerm();
            return true;
        }
        else if (item.getItemId() == R.id.action_delete) {
            deleteTerm();
        }
        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        addTerm();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_term_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_save);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if(savedInstanceState != null) {
            aEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }
        initViewModel();
    }
}