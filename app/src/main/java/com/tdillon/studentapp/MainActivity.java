package com.tdillon.studentapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.tdillon.studentapp.model.Assessment;
import com.tdillon.studentapp.model.Course;
import com.tdillon.studentapp.model.Mentor;
import com.tdillon.studentapp.model.Term;
import com.tdillon.studentapp.ui.RecyclerContext;
import com.tdillon.studentapp.ui.TermAdapter;
import com.tdillon.studentapp.util.Alerter;
import com.tdillon.studentapp.viewmodel.MainVM;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private List<Term> termData = new ArrayList<>();
    private List<Course> courseData = new ArrayList<>();
    private List<Assessment> assessmentData = new ArrayList<>();
    private TermAdapter aAdapter;
    private MainVM aViewModel;

    private void initViewModel() {
        // Term observer
        final Observer<List<Term>> termObserver =
                termEntities -> {
                    termData.clear();
                    termData.addAll(termEntities);
                    if (aAdapter == null) {
                        aAdapter = new TermAdapter(termData, MainActivity.this, RecyclerContext.MAIN);
                    }
                    else {
                        aAdapter.notifyDataSetChanged();
                    }
                };
        // Course observer
        final Observer<List<Course>> courseObserver =
                courseEntities -> {
                    courseData.clear();
                    courseData.addAll(courseEntities);
                    handleAlerts();
                };

        // Assessment observer
        final Observer<List<Assessment>> assessmentObserver =
                assessmentEntities -> {
                    assessmentData.clear();
                    assessmentData.addAll(assessmentEntities);
                    handleAlerts();
                };

        // Mentor observer
        final Observer<List<Mentor>> mentorObserver =
                mentorEntities -> {
                };
        aViewModel = new ViewModelProvider(this).get(MainVM.class);
        aViewModel.vmTerms.observe(this, termObserver);
        aViewModel.vmCourses.observe(this, courseObserver);
        aViewModel.vmAssessments.observe(this, assessmentObserver);
        aViewModel.vmMentors.observe(this, mentorObserver);
    }

    @OnClick(R.id.btn_terms)
    public void showTerms(View view) {
        Intent intent = new Intent(this, TermActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_courses)
    public void showCourses(View view) {
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_assessments)
    public void showAssessments(View view) {
        Intent intent = new Intent(this, AssessmentActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_mentors)
    public void showMentors(View view) {
        Intent intent = new Intent(this, MentorActivity.class);
        startActivity(intent);
    }

    private void handleAlerts() {
        Log.v("TAYBUG", "We are handling alerts");
        ArrayList<String> alerts = new ArrayList<>();

        Log.v("TAYBUG", "Courses: " + courseData.size() + "\nAssessments: " + assessmentData.size());

        // Loop through Courses to find start and end dates.
        for(Course course: courseData) {
            Log.v("TAYBUG", "We are looping through courses to find due dates");
            if(DateUtils.isToday(course.getStartDate().getTime())) {
                Log.v("DEBUG", "Start date is today.");
                alerts.add("Course " + course.getTitle() + " begins today!");
            } else if(DateUtils.isToday(course.getExpectedEndDate().getTime())) {
                Log.v("DEBUG", "End date is today!");
                alerts.add("Course" + course.getTitle() + " ends today!");
            }
        }

        // Loop through assessments to find start dates
        for(Assessment assessment: assessmentData) {
            Log.v("TAYBUG", "We are looping through assessments to find due dates");
            if(DateUtils.isToday(assessment.getDate().getTime())) {
                Log.v("DEBUG", "Assessment due date is today");
                alerts.add("Assessment " + assessment.getTitle() + " is due today!");
            }
        }
        // Toast the alerts one at a time
        if(alerts.size() > 0) {
            for(String alert: alerts) {
                AlarmManager alarm = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
                Alerter alerting = new Alerter();
                IntentFilter filter = new IntentFilter("ALARM_ACTION");
                registerReceiver(alerting, filter);

                Intent intent = new Intent("ALARM_ACTION");
                intent.putExtra("param", alert);
                PendingIntent operation = PendingIntent.getBroadcast(this, 0, intent, 0);
                alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ Toast.LENGTH_SHORT, operation);
            }
        }
    }

    /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_sample_data) {
            addSampleData();
            return true;
        } else if (id == R.id.action_delete_all) {
            deleteAllData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    } */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        initViewModel();
    }
}