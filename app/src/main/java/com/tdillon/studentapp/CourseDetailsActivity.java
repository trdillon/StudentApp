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
import com.tdillon.studentapp.model.Assessment;
import com.tdillon.studentapp.model.Mentor;
import com.tdillon.studentapp.ui.AssessmentAdapter;
import com.tdillon.studentapp.ui.AssessmentDropdown;
import com.tdillon.studentapp.ui.MentorAdapter;
import com.tdillon.studentapp.ui.MentorDropdown;
import com.tdillon.studentapp.ui.RecyclerContext;
import com.tdillon.studentapp.util.TextFormatter;
import com.tdillon.studentapp.viewmodel.EditorVM;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tdillon.studentapp.util.Constants.COURSE_ID_KEY;

public class CourseDetailsActivity extends AppCompatActivity implements AssessmentAdapter.AssessmentListener, MentorAdapter.MentorListener {

    @BindView(R.id.course_detail_start)
    TextView tvCourseStartDate;

    @BindView(R.id.course_detail_end)
    TextView tvCourseEndDate;

    @BindView(R.id.rview_course_detail_assessments)
    RecyclerView aAsmtRecyclerView;

    @BindView(R.id.rview_course_detail_mentors)
    RecyclerView aMentorsRecyclerView;

    @BindView(R.id.course_detail_status)
    TextView tvCourseStatus;

    @BindView(R.id.course_detail_note)
    TextView tvCourseNote;

    @BindView(R.id.fab_add_assessment)
    FloatingActionButton fabAddAssessment;

    @BindView(R.id.fab_add_mentor)
    FloatingActionButton fabAddMentor;

    private List<Assessment> assessmentData = new ArrayList<>();
    private List<Mentor> mentorData = new ArrayList<>();
    private List<Assessment> unassignedAssessments = new ArrayList<>();
    private List<Mentor> unassignedMentors = new ArrayList<>();
    private int courseId;
    private AssessmentAdapter aAssessmentAdapter;
    private MentorAdapter aMentorAdapter;
    private EditorVM aViewModel;

    private void initViewModel() {
        aViewModel = new ViewModelProvider(this).get(EditorVM.class);

        aViewModel.vmLiveCourse.observe(this, course -> {
            tvCourseStartDate.setText(TextFormatter.getDateFormatted(course.getStartDate()));
            tvCourseEndDate.setText(TextFormatter.getDateFormatted(course.getExpectedEndDate()));
            tvCourseStatus.setText(course.getCourseStatus().toString());
            tvCourseNote.setText(course.getNote());
        });

        // Assessments
        final Observer<List<Assessment>> assessmentObserver =
                assessmentEntities -> {
                    assessmentData.clear();
                    assessmentData.addAll(assessmentEntities);

                    if(aAssessmentAdapter == null) {
                        aAssessmentAdapter = new AssessmentAdapter(assessmentData, CourseDetailsActivity.this, RecyclerContext.CHILD, this);
                        aAsmtRecyclerView.setAdapter(aAssessmentAdapter);
                    }
                    else {
                        aAssessmentAdapter.notifyDataSetChanged();
                    }
                };

        // Mentors
        final Observer<List<Mentor>> mentorObserver =
                mentorEntities -> {
                    mentorData.clear();
                    mentorData.addAll(mentorEntities);

                    if(aMentorAdapter == null) {
                        aMentorAdapter = new MentorAdapter(mentorData, CourseDetailsActivity.this, RecyclerContext.CHILD, this);
                        aMentorsRecyclerView.setAdapter(aMentorAdapter);
                    }
                    else {
                        aMentorAdapter.notifyDataSetChanged();
                    }
                };

        // Load and observe unassigned assessments to enable adding them
        final Observer<List<Assessment>> unassignedAssessmentObserver =
                assessmentEntities -> {
                    unassignedAssessments.clear();
                    unassignedAssessments.addAll(assessmentEntities);
                };

        // Load and observe unassigned mentors to enable adding them
        final Observer<List<Mentor>> unassignedMentorObserver =
                mentorEntities -> {
                    unassignedMentors.clear();
                    unassignedMentors.addAll(mentorEntities);
                };

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            courseId = extras.getInt(COURSE_ID_KEY);
            aViewModel.loadCourse(courseId);
        }
        else {
            finish();
        }

        aViewModel.getAssessmentsInCourse(courseId).observe(this, assessmentObserver);
        aViewModel.getMentorsInCourse(courseId).observe(this, mentorObserver);
        aViewModel.getUnassignedAssessments().observe(this, unassignedAssessmentObserver);
        aViewModel.getUnassignedMentors().observe(this, unassignedMentorObserver);
    }

    private void initRecyclerView() {
        aAsmtRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        aAsmtRecyclerView.setLayoutManager(layoutManager);

        aMentorsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        aMentorsRecyclerView.setLayoutManager(layoutManager1);
    }
//TODO - add alert options for course start & end date
    @OnClick(R.id.fab_add_assessment)
    public void addAssessment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new or existing Assessment?");
        builder.setMessage("Would you like to add an existing assessment to this course or create a new assessment?");
        builder.setIcon(R.drawable.ic_add);
        builder.setPositiveButton("New", (dialog, id) -> {
            dialog.dismiss();
            Intent intent = new Intent(this, AssessmentEditActivity.class);
            intent.putExtra(COURSE_ID_KEY, courseId);
            this.startActivity(intent);
        });
        builder.setNegativeButton("Existing", (dialog, id) -> {
            // Ensure at least once unassigned assessment is available
            if(unassignedAssessments.size() >= 1) {
                final AssessmentDropdown menu = new AssessmentDropdown(this, unassignedAssessments);
                menu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                menu.setWidth(getPxFromDp(200));
                menu.setOutsideTouchable(true);
                menu.setFocusable(true);
                menu.showAsDropDown(fabAddAssessment);
                menu.setAssessmentListener((position, assessment) -> {
                    menu.dismiss();
                    assessment.setCourseId(courseId);
                    aViewModel.overwriteAssessment(assessment, courseId);
                });
            } else { // No unassigned courses.  Notify user.
                Toast.makeText(getApplicationContext(), "There are no unassigned assessments.  Create a new assessment.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.fab_add_mentor)
    public void addMentor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new or existing Mentor?");
        builder.setMessage("Would you like to add an existing mentor to this course or add a new mentor?");
        builder.setIcon(R.drawable.ic_add);
        builder.setPositiveButton("New", (dialog, id) -> {
            dialog.dismiss();
            Intent intent = new Intent(this, MentorEditActivity.class);
            intent.putExtra(COURSE_ID_KEY, courseId);
            this.startActivity(intent);
        });
        builder.setNegativeButton("Existing", (dialog, id) -> {
            // Ensure at least once unassigned mentor is available
            if(unassignedMentors.size() >= 1) {
                final MentorDropdown menu = new MentorDropdown(this, unassignedMentors);
                menu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                menu.setWidth(getPxFromDp(200));
                menu.setOutsideTouchable(true);
                menu.setFocusable(true);
                menu.showAsDropDown(fabAddMentor);
                menu.setMentorListener((position, mentor) -> {
                    menu.dismiss();
                    mentor.setCourseId(courseId);
                    aViewModel.overwriteMentor(mentor, courseId);
                });
            } else { // No unassigned courses.  Notify user.
                Toast.makeText(getApplicationContext(), "There are no unassigned mentors.  Create a new mentor.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.fab_edit_course)
    public void editCourse() {
        Intent intent = new Intent(this, CourseEditActivity.class);
        intent.putExtra(COURSE_ID_KEY, courseId);
        this.startActivity(intent);
        finish();
    }

    @Override
    public void onAssessmentSelected(int position, Assessment assessment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to remove this assessment?");
        builder.setMessage("This will not delete the assessment, only remove it from this course.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Continue", (dialog, id) -> {
            dialog.dismiss();
            aViewModel.overwriteAssessment(assessment, -1);
            aAssessmentAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onMentorSelected(int position, Mentor mentor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to remove this mentor?");
        builder.setMessage("This will not delete the mentor, only remove them from this course.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Continue", (dialog, id) -> {
            dialog.dismiss();
            aViewModel.overwriteMentor(mentor, -1);
            aMentorAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.course_detail_share_fab)
    public void shareNote() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody = tvCourseNote.getText().toString();
        String shareSub = "Notes for course: " + getTitle();
        intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "Share using"));
    }

    @OnClick(R.id.course_detail_share_fab)
    public void handleShareBtn(View view) {
        shareNote();
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
        setContentView(R.layout.activity_course_details);
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }
}