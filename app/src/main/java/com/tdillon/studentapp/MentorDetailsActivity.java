package com.tdillon.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.tdillon.studentapp.viewmodel.EditorVM;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tdillon.studentapp.util.Constants.MENTOR_ID_KEY;

public class MentorDetailsActivity extends AppCompatActivity {

    @BindView(R.id.mentor_detail_email)
    TextView tvMentorEmail;

    @BindView(R.id.mentor_detail_phone)
    TextView tvMentorPhone;

    private Toolbar toolbar;
    private int mentorId;
    private EditorVM aViewModel;

    private void initViewModel() {
        aViewModel = new ViewModelProvider(this).get(EditorVM.class);

        aViewModel.vmLiveMentor.observe(this, mentor -> {
            tvMentorEmail.setText(mentor.getEmail());
            tvMentorPhone.setText(mentor.getPhone());
            toolbar.setTitle(mentor.getName());
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mentorId = extras.getInt(MENTOR_ID_KEY);
            aViewModel.loadMentor(mentorId);
        } else {
            finish();
        }
    }

    @OnClick(R.id.fab_edit_mentor)
    public void openEditActivity() {
        Intent intent = new Intent(this, MentorEditActivity.class);
        intent.putExtra(MENTOR_ID_KEY, mentorId);
        this.startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initViewModel();
    }
}