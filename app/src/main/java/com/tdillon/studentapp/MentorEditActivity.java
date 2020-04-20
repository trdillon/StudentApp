package com.tdillon.studentapp;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.tdillon.studentapp.viewmodel.EditorVM;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tdillon.studentapp.util.Constants.COURSE_ID_KEY;
import static com.tdillon.studentapp.util.Constants.EDITING_KEY;
import static com.tdillon.studentapp.util.Constants.MENTOR_ID_KEY;

public class MentorEditActivity extends AppCompatActivity {

    @BindView(R.id.mentor_edit_name)
    EditText tvMentorName;

    @BindView(R.id.mentor_edit_email)
    EditText tvMentorEmail;

    @BindView(R.id.mentor_edit_phone)
    EditText tvMentorPhone;

    private EditorVM aViewModel;
    private boolean aNewMentor, aEditing;
    private int courseId = -1;

    private void initViewModel() {
        aViewModel = new ViewModelProvider(this).get(EditorVM.class);

        aViewModel.vmLiveMentor.observe(this, mentor -> {
            if(mentor != null && !aEditing) {
                tvMentorName.setText(mentor.getName());
                tvMentorEmail.setText(mentor.getEmail());
                tvMentorPhone.setText(mentor.getPhone());
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            setTitle(getString(R.string.new_mentor));
            aNewMentor = true;
        } else if (extras.containsKey(COURSE_ID_KEY)) {
            courseId = extras.getInt(COURSE_ID_KEY);
            setTitle(getString(R.string.new_mentor));
        } else {
            setTitle(getString(R.string.edit_mentor));
            int mentorId = extras.getInt(MENTOR_ID_KEY);
            aViewModel.loadMentor(mentorId);
        }
    }

    public void addMentor() {
        aViewModel.addMentor(tvMentorName.getText().toString(), tvMentorEmail.getText().toString(), tvMentorPhone.getText().toString(), courseId);
        finish();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!aNewMentor) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            addMentor();
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
        addMentor();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mentor_edit);
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