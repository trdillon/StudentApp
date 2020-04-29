package com.tdillon.studentapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tdillon.studentapp.model.Course;
import com.tdillon.studentapp.ui.CourseAdapter;
import com.tdillon.studentapp.ui.CourseDropdown;
import com.tdillon.studentapp.ui.RecyclerContext;
import com.tdillon.studentapp.util.TextFormatter;
import com.tdillon.studentapp.viewmodel.EditorVM;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tdillon.studentapp.util.Constants.TERM_ID_KEY;

public class TermDetailsActivity extends AppCompatActivity implements CourseAdapter.CourseListener {

    private List<Course> courseData = new ArrayList<>();
    private List<Course> unassignedCourses = new ArrayList<>();
    private int termID;
    private CourseAdapter courseAdapter;
    private EditorVM editorVM;

    @BindView(R.id.term_detail_start)
    TextView tvTermStartDate;

    @BindView(R.id.term_detail_end)
    TextView tvTermEndDate;

    @BindView(R.id.rview_term_detail_course)
    RecyclerView rvCourse;

    @BindView(R.id.fab_add_course)
    FloatingActionButton fabAddCourse;

    @OnClick(R.id.button_home)
    public void showHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initViewModel() {
        editorVM = new ViewModelProvider(this).get(EditorVM.class);

        editorVM.vmLiveTerm.observe(this, term -> {
            tvTermStartDate.setText(TextFormatter.getDateFormatted(term.getStartDate()));
            tvTermEndDate.setText(TextFormatter.getDateFormatted(term.getEndDate()));
        });

        final Observer<List<Course>> courseObserver =
                courseEntities -> {
                    courseData.clear();
                    courseData.addAll(courseEntities);

                    if(courseAdapter == null) {
                        courseAdapter = new CourseAdapter(courseData, TermDetailsActivity.this, RecyclerContext.CHILD, this);
                        rvCourse.setAdapter(courseAdapter);
                    }
                    else {
                        courseAdapter.notifyDataSetChanged();
                    }
                };

        final Observer<List<Course>> unassignedCourseObserver =
                courseEntities -> {
                    unassignedCourses.clear();
                    unassignedCourses.addAll(courseEntities);
                };

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            termID = extras.getInt(TERM_ID_KEY);
            editorVM.loadTerm(termID);
        }
        else {
            finish();
        }

        editorVM.getCoursesInTerm(termID).observe(this, courseObserver);
        editorVM.getUnassignedCourses().observe(this, unassignedCourseObserver);
    }

    private void initRecyclerView() {
        rvCourse.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvCourse.setLayoutManager(layoutManager);
    }

    @OnClick(R.id.fab_add_course)
    public void addCourseButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a new or an existing course?");
        builder.setMessage("Would you like to add an existing course to this term or create a new course?");
        builder.setIcon(R.drawable.ic_add);
        builder.setPositiveButton("New", (dialog, id) -> {
            dialog.dismiss();
            Intent intent = new Intent(this, CourseEditActivity.class);
            intent.putExtra(TERM_ID_KEY, termID);
            this.startActivity(intent);
        });
        builder.setNegativeButton("Existing", (dialog, id) -> {
            //Check if there are any unassigned courses
            if(unassignedCourses.size() >= 1) {
                final CourseDropdown menu = new CourseDropdown(this, unassignedCourses);
                menu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                menu.setWidth(getPxFromDp());
                menu.setOutsideTouchable(true);
                menu.setFocusable(true);
                menu.showAsDropDown(fabAddCourse);
                menu.setCourseListener((position, course) -> {
                    menu.dismiss();
                    course.setTermId(termID);
                    editorVM.overwriteCourse(course, termID);
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "There are no unassigned courses. Please create a new course.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.fab_edit_term)
    public void editTerm() {
        Intent intent = new Intent(this, TermEditActivity.class);
        intent.putExtra(TERM_ID_KEY, termID);
        this.startActivity(intent);
        finish();
    }

    @Override
    public void onCourseSelected(int position, Course course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to remove this course?");
        builder.setMessage("This will not delete the course, only remove it from this term.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Continue", (dialog, id) -> {
            dialog.dismiss();
            editorVM.overwriteCourse(course, -1);
            courseAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private int getPxFromDp() {
        return (int) (200 * getResources().getDisplayMetrics().density);
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }
}