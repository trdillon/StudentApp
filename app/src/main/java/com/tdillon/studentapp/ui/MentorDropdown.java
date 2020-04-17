package com.tdillon.studentapp.ui;

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

    private Context uContext;
    private RecyclerView rPopup;
    private List<Mentor> uMentors;
    private MentorPopup mentorPopup;

    public MentorDropdown(Context uContext, List<Mentor> uMentors) {
        super(uContext);
        this.uContext = uContext;
        this.uMentors = uMentors;
        setupView();
    }

    private void setupView() {
        View view = LayoutInflater.from(uContext).inflate(R.layout.popup_view, null);

        rPopup = view.findViewById(R.id.rv_popup);
        rPopup.setHasFixedSize(true);
        rPopup.setLayoutManager(new LinearLayoutManager(uContext, LinearLayoutManager.VERTICAL, false));
        rPopup.addItemDecoration(new DividerItemDecoration(uContext, LinearLayoutManager.VERTICAL));

        mentorPopup = new MentorPopup(uMentors);
        rPopup.setAdapter(mentorPopup);

        setContentView(view);
    }

    public void setMentorListener(MentorPopup.MentorListener currMentorListener) {
        mentorPopup.setMentorListener(currMentorListener);
    }
}