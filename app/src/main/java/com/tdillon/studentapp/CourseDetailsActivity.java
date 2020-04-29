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

    private List<Assessment> assessmentData = new ArrayList<>();
    private List<Mentor> mentorData = new ArrayList<>();
    private List<Assessment> unassignedAssessments = new ArrayList<>();
    private List<Mentor> unassignedMentors = new ArrayList<>();
    private int courseID;
    private AssessmentAdapter assessmentAdapter;
    private MentorAdapter mentorAdapter;
    private EditorVM editorVM;

    @BindView(R.id.course_detail_start)
    TextView tvCourseStartDate;

    @BindView(R.id.course_detail_end)
    TextView tvCourseEndDate;

    @BindView(R.id.rview_course_detail_assessments)
    RecyclerView rvAssessment;

    @BindView(R.id.rview_course_detail_mentors)
    RecyclerView rvMentor;

    @BindView(R.id.course_detail_status)
    TextView tvCourseStatus;

    @BindView(R.id.course_detail_note)
    TextView tvCourseNote;

    @BindView(R.id.fab_add_assessment)
    FloatingActionButton fabAddAssessment;

    @BindView(R.id.fab_add_mentor)
    FloatingActionButton fabAddMentor;


    @OnClick(R.id.course_detail_share_fab)
    public void handleShareBtn(View view) {
        shareNote();
    }

    @OnClick(R.id.button_home)
    public void showHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initViewModel() {
        editorVM = new ViewModelProvider(this).get(EditorVM.class);

        editorVM.vmLiveCourse.observe(this, course -> {
            tvCourseStartDate.setText(TextFormatter.getDateFormatted(course.getStartDate()));
            tvCourseEndDate.setText(TextFormatter.getDateFormatted(course.getExpectedEndDate()));
            tvCourseStatus.setText(course.getCourseStatus().toString());
            tvCourseNote.setText(course.getNote());
        });

        //Setup assessments
        final Observer<List<Assessment>> assessmentObserver =
                assessmentEntities -> {
                    assessmentData.clear();
                    assessmentData.addAll(assessmentEntities);

                    if(assessmentAdapter == null) {
                        assessmentAdapter = new AssessmentAdapter(assessmentData, CourseDetailsActivity.this, RecyclerContext.CHILD, this);
                        rvAssessment.setAdapter(assessmentAdapter);
                    }
                    else {
                        assessmentAdapter.notifyDataSetChanged();
                    }
                };

        //Setup mentors
        final Observer<List<Mentor>> mentorObserver =
                mentorEntities -> {
                    mentorData.clear();
                    mentorData.addAll(mentorEntities);

                    if(mentorAdapter == null) {
                        mentorAdapter = new MentorAdapter(mentorData, CourseDetailsActivity.this, RecyclerContext.CHILD, this);
                        rvMentor.setAdapter(mentorAdapter);
                    }
                    else {
                        mentorAdapter.notifyDataSetChanged();
                    }
                };

        //Get unassigned courses for adding
        final Observer<List<Assessment>> unassignedAssessmentObserver =
                assessmentEntities -> {
                    unassignedAssessments.clear();
                    unassignedAssessments.addAll(assessmentEntities);
                };

        //Get unassigned mentors for adding
        final Observer<List<Mentor>> unassignedMentorObserver =
                mentorEntities -> {
                    unassignedMentors.clear();
                    unassignedMentors.addAll(mentorEntities);
                };

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            courseID = extras.getInt(COURSE_ID_KEY);
            editorVM.loadCourse(courseID);
        }
        else {
            finish();
        }

        editorVM.getAssessmentsInCourse(courseID).observe(this, assessmentObserver);
        editorVM.getMentorsInCourse(courseID).observe(this, mentorObserver);
        editorVM.getUnassignedAssessments().observe(this, unassignedAssessmentObserver);
        editorVM.getUnassignedMentors().observe(this, unassignedMentorObserver);
    }

    private void initRecyclerView() {
        rvAssessment.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvAssessment.setLayoutManager(layoutManager);

        rvMentor.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        rvMentor.setLayoutManager(layoutManager1);
    }

    @OnClick(R.id.fab_add_assessment)
    public void addAssessment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new or existing Assessment?");
        builder.setMessage("Would you like to add an existing assessment to this course or create a new assessment?");
        builder.setIcon(R.drawable.ic_add);
        builder.setPositiveButton("New", (dialog, id) -> {
            dialog.dismiss();
            Intent intent = new Intent(this, AssessmentEditActivity.class);
            intent.putExtra(COURSE_ID_KEY, courseID);
            this.startActivity(intent);
        });
        builder.setNegativeButton("Existing", (dialog, id) -> {
            //Check if there are any unassigned assessments
            if(unassignedAssessments.size() >= 1) {
                final AssessmentDropdown menu = new AssessmentDropdown(this, unassignedAssessments);
                menu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                menu.setWidth(getPxFromDp());
                menu.setOutsideTouchable(true);
                menu.setFocusable(true);
                menu.showAsDropDown(fabAddAssessment);
                menu.setAssessmentListener((position, assessment) -> {
                    menu.dismiss();
                    assessment.setCourseId(courseID);
                    editorVM.overwriteAssessment(assessment, courseID);
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "There are no unassigned assessments. Please create a new assessment.", Toast.LENGTH_SHORT).show();
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
            intent.putExtra(COURSE_ID_KEY, courseID);
            this.startActivity(intent);
        });
        builder.setNegativeButton("Existing", (dialog, id) -> {
            //Check if there are any unassigned mentors
            if(unassignedMentors.size() >= 1) {
                final MentorDropdown menu = new MentorDropdown(this, unassignedMentors);
                menu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                menu.setWidth(getPxFromDp());
                menu.setOutsideTouchable(true);
                menu.setFocusable(true);
                menu.showAsDropDown(fabAddMentor);
                menu.setMentorListener((position, mentor) -> {
                    menu.dismiss();
                    mentor.setCourseId(courseID);
                    editorVM.overwriteMentor(mentor, courseID);
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "There are no unassigned mentors. Please create a new mentor.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.fab_edit_course)
    public void editCourse() {
        Intent intent = new Intent(this, CourseEditActivity.class);
        intent.putExtra(COURSE_ID_KEY, courseID);
        this.startActivity(intent);
        finish();
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

    @Override
    public void onAssessmentSelected(int position, Assessment assessment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to remove this assessment?");
        builder.setMessage("This will not delete the assessment, only remove it from this course.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Continue", (dialog, id) -> {
            dialog.dismiss();
            editorVM.overwriteAssessment(assessment, -1);
            assessmentAdapter.notifyDataSetChanged();
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
            editorVM.overwriteMentor(mentor, -1);
            mentorAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_course_details);
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }
}