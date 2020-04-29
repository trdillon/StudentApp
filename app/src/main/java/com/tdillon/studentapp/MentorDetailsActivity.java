package com.tdillon.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tdillon.studentapp.viewmodel.EditorVM;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tdillon.studentapp.util.Constants.MENTOR_ID_KEY;

public class MentorDetailsActivity extends AppCompatActivity {

    private int mentorID;

    @BindView(R.id.mentor_detail_email)
    TextView tvMentorEmail;

    @BindView(R.id.mentor_detail_phone)
    TextView tvMentorPhone;

    @OnClick(R.id.button_home)
    public void showHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initViewModel() {
        EditorVM editorVM = new ViewModelProvider(this).get(EditorVM.class);

        editorVM.vmLiveMentor.observe(this, mentor -> {
            tvMentorEmail.setText(mentor.getEmail());
            tvMentorPhone.setText(mentor.getPhone());
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mentorID = extras.getInt(MENTOR_ID_KEY);
            editorVM.loadMentor(mentorID);
        } else {
            finish();
        }
    }

    @OnClick(R.id.fab_edit_mentor)
    public void editMentor() {
        Intent intent = new Intent(this, MentorEditActivity.class);
        intent.putExtra(MENTOR_ID_KEY, mentorID);
        this.startActivity(intent);
        finish();
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_details);
        ButterKnife.bind(this);
        initViewModel();
    }
}