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

    @BindView(R.id.term_detail_start)
    TextView tvTermStartDate;

    @BindView(R.id.term_detail_end)
    TextView tvTermEndDate;

    @BindView(R.id.rview_term_detail_course)
    RecyclerView aCourseRecyclerView;

    @BindView(R.id.fab_add_course)
    FloatingActionButton fabAddCourse;

    private List<Course> courseData = new ArrayList<>();
    private List<Course> unassignedCourses = new ArrayList<>();
    private int termId;
    private CourseAdapter aCourseAdapter;
    private EditorVM aViewModel;

    private void initViewModel() {
        aViewModel = new ViewModelProvider(this).get(EditorVM.class);

        aViewModel.vmLiveTerm.observe(this, term -> {
            tvTermStartDate.setText(TextFormatter.getDateFormatted(term.getStartDate()));
            tvTermEndDate.setText(TextFormatter.getDateFormatted(term.getEndDate()));
        });

        final Observer<List<Course>> courseObserver =
                courseEntities -> {
                    courseData.clear();
                    courseData.addAll(courseEntities);

                    if(aCourseAdapter == null) {
                        aCourseAdapter = new CourseAdapter(courseData, TermDetailsActivity.this, RecyclerContext.CHILD, this);
                        aCourseRecyclerView.setAdapter(aCourseAdapter);
                    }
                    else {
                        aCourseAdapter.notifyDataSetChanged();
                    }
                };

        final Observer<List<Course>> unassignedCourseObserver =
                courseEntities -> {
                    unassignedCourses.clear();
                    unassignedCourses.addAll(courseEntities);
                };

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            termId = extras.getInt(TERM_ID_KEY);
            aViewModel.loadTerm(termId);
        }
        else {
            finish();
        }

        aViewModel.getCoursesInTerm(termId).observe(this, courseObserver);
        aViewModel.getUnassignedCourses().observe(this, unassignedCourseObserver);
    }

    private void initRecyclerView() {
        aCourseRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        aCourseRecyclerView.setLayoutManager(layoutManager);
    }
    //TODO - fix the overlapping course items on details screen
    @OnClick(R.id.fab_add_course)
    public void addCourseButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a new or an existing course?");
        builder.setMessage("Would you like to add an existing course to this term or create a new course?");
        builder.setIcon(R.drawable.ic_add);
        builder.setPositiveButton("New", (dialog, id) -> {
            dialog.dismiss();
            Intent intent = new Intent(this, CourseEditActivity.class);
            intent.putExtra(TERM_ID_KEY, termId);
            this.startActivity(intent);
        });
        builder.setNegativeButton("Existing", (dialog, id) -> {
            // Ensure at least once unassigned course is available
            if(unassignedCourses.size() >= 1) {
                final CourseDropdown menu = new CourseDropdown(this, unassignedCourses);
                menu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                menu.setWidth(getPxFromDp(200));
                menu.setOutsideTouchable(true);
                menu.setFocusable(true);
                menu.showAsDropDown(fabAddCourse);
                menu.setCourseListener((position, course) -> {
                    menu.dismiss();
                    course.setTermId(termId);
                    aViewModel.overwriteCourse(course, termId);
                });
            }
            // No unassigned courses.  Notify user.
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
        intent.putExtra(TERM_ID_KEY, termId);
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
            aViewModel.overwriteCourse(course, -1);
            aCourseAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private int getPxFromDp(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    @OnClick(R.id.button_home)
    public void showHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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