package com.tdillon.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdillon.studentapp.model.Mentor;
import com.tdillon.studentapp.ui.MentorAdapter;
import com.tdillon.studentapp.ui.RecyclerContext;
import com.tdillon.studentapp.viewmodel.MentorVM;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MentorActivity extends AppCompatActivity implements MentorAdapter.MentorListener {

    private List<Mentor> mentorData = new ArrayList<>();
    private MentorAdapter mentorAdapter;

    @BindView(R.id.mentor_recycler_view)
    RecyclerView rvMentor;

    @OnClick(R.id.mentor_add_btn)
    void handleAddBtn() {
        Intent intent = new Intent(this, MentorEditActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_home)
    public void showHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initViewModel() {
        final Observer<List<Mentor>> mentorObserver =
                mentorEntities -> {
                    mentorData.clear();
                    mentorData.addAll(mentorEntities);

                    if(mentorAdapter == null) {
                        mentorAdapter = new MentorAdapter(mentorData, MentorActivity.this, RecyclerContext.MAIN, this);
                        rvMentor.setAdapter(mentorAdapter);
                    } else {
                        mentorAdapter.notifyDataSetChanged();
                    }
                };

        MentorVM mentorVM = new ViewModelProvider(this).get(MentorVM.class);
        mentorVM.vmMentors.observe(this, mentorObserver);
    }

    private void initRecyclerView() {
        rvMentor.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMentor.setLayoutManager(layoutManager);
    }

    @Override
    public void onMentorSelected(int position, Mentor mentor) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_main);
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }
}