package com.tdillon.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tdillon.studentapp.viewmodel.EditorVM;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tdillon.studentapp.util.Constants.COURSE_ID_KEY;
import static com.tdillon.studentapp.util.Constants.EDITING_KEY;
import static com.tdillon.studentapp.util.Constants.MENTOR_ID_KEY;

public class MentorEditActivity extends AppCompatActivity {

    private EditorVM editorVM;
    private boolean isEditing;
    private int courseID = -1;

    @BindView(R.id.mentor_edit_name)
    EditText tvMentorName;

    @BindView(R.id.mentor_edit_email)
    EditText tvMentorEmail;

    @BindView(R.id.mentor_edit_phone)
    EditText tvMentorPhone;

    @Override
    public void onBackPressed() {
        addMentor();
    }

    @OnClick(R.id.fab_save_mentor)
    public void handleSaveBtn(View view) {
        addMentor();
    }

    @OnClick(R.id.button_home)
    public void showHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initViewModel() {
        editorVM = new ViewModelProvider(this).get(EditorVM.class);

        editorVM.vmLiveMentor.observe(this, mentor -> {
            if(mentor != null && !isEditing) {
                tvMentorName.setText(mentor.getName());
                tvMentorEmail.setText(mentor.getEmail());
                tvMentorPhone.setText(mentor.getPhone());
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            setTitle(getString(R.string.new_mentor));
        }
        else if (extras.containsKey(COURSE_ID_KEY)) {
            courseID = extras.getInt(COURSE_ID_KEY);
            setTitle(getString(R.string.new_mentor));
        }
        else {
            setTitle(getString(R.string.edit_mentor));
            int mentorId = extras.getInt(MENTOR_ID_KEY);
            editorVM.loadMentor(mentorId);
        }
    }

    public void addMentor() {
        editorVM.addMentor(tvMentorName.getText().toString(), tvMentorEmail.getText().toString(), tvMentorPhone.getText().toString(), courseID);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_edit);
        ButterKnife.bind(this);

        if(savedInstanceState != null) {
            isEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        initViewModel();
    }
}