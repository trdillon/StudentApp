package com.tdillon.studentapp.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tdillon.studentapp.MentorDetailsActivity;
import com.tdillon.studentapp.MentorEditActivity;
import com.tdillon.studentapp.R;
import com.tdillon.studentapp.model.Mentor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tdillon.studentapp.util.Constants.MENTOR_ID_KEY;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.ViewHolder> {

    private final Context currContext;
    private final RecyclerContext rContext;
    private final List<Mentor> currMentors;
    private MentorListener currMentorListener;

    public MentorAdapter(List<Mentor> currMentors, Context currContext, RecyclerContext rContext, MentorListener currMentorListener) {
        this.currMentors = currMentors;
        this.currContext = currContext;
        this.rContext = rContext;
        this.currMentorListener = currMentorListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.view_mentor_name)
        TextView tvName;
        @BindView(R.id.view_mentor_fab)
        FloatingActionButton mentorFab;
        @BindView(R.id.view_mentor_email)
        TextView tvEmail;
        @BindView(R.id.btn_mentor_details)
        ImageButton mentorImageBtn;
        MentorListener currMentorListener;

        public ViewHolder(View itemView, MentorListener currMentorListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.currMentorListener = currMentorListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            currMentorListener.onMentorSelected(getAdapterPosition(), currMentors.get(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.mentor_list_view, parent, false);
        return new ViewHolder(view, currMentorListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorAdapter.ViewHolder holder, int position) {
        final Mentor mentor = currMentors.get(position);
        holder.tvName.setText(mentor.getName());
        holder.tvEmail.setText(mentor.getEmail());

        switch(rContext) {
            case MAIN:
                holder.mentorFab.setImageDrawable(ContextCompat.getDrawable(currContext, R.drawable.ic_edit));
                holder.mentorImageBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(currContext, MentorDetailsActivity.class);
                    intent.putExtra(MENTOR_ID_KEY, mentor.getId());
                    currContext.startActivity(intent);
                });

                holder.mentorFab.setOnClickListener(v -> {
                    Intent intent = new Intent(currContext, MentorEditActivity.class);
                    intent.putExtra(MENTOR_ID_KEY, mentor.getId());
                    currContext.startActivity(intent);
                });
                break;
            case CHILD:
                holder.mentorFab.setImageDrawable(ContextCompat.getDrawable(currContext, R.drawable.ic_delete));
                holder.mentorFab.setOnClickListener(v -> {
                    if(currMentorListener != null) {
                        currMentorListener.onMentorSelected(position, mentor);
                    }
                });
                break;
        }
    }

    public interface MentorListener {
        void onMentorSelected(int position, Mentor mentor);
    }

    @Override
    public int getItemCount() {
        return currMentors.size();
    }
}