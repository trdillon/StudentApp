package com.tdillon.studentapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdillon.studentapp.R;
import com.tdillon.studentapp.model.Mentor;

import java.util.List;

public class MentorDropdown extends PopupWindow {

    private Context currContext;
    private List<Mentor> currMentors;
    private MentorPopup mentorPopup;

    public MentorDropdown(Context currContext, List<Mentor> currMentors) {
        super(currContext);
        this.currContext = currContext;
        this.currMentors = currMentors;
        setupView();
    }

    private void setupView() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(currContext).inflate(R.layout.popup_view, null);

        RecyclerView rv = view.findViewById(R.id.rv_popup);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(currContext, LinearLayoutManager.VERTICAL, false));
        rv.addItemDecoration(new DividerItemDecoration(currContext, LinearLayoutManager.VERTICAL));

        mentorPopup = new MentorPopup(currMentors);
        rv.setAdapter(mentorPopup);

        setContentView(view);
    }

    public void setMentorListener(MentorPopup.MentorListener currMentorListener) {
        mentorPopup.setMentorListener(currMentorListener);
    }
}