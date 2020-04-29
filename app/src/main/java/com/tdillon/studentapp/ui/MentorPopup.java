package com.tdillon.studentapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdillon.studentapp.R;
import com.tdillon.studentapp.model.Mentor;

import java.util.List;

public class MentorPopup extends RecyclerView.Adapter<MentorPopup.MentorViewHolder> {

    private List<Mentor> currMentors;
    private MentorPopup.MentorListener currMentorListener;

    MentorPopup(List<Mentor> currMentors) {
        super();
        this.currMentors = currMentors;
    }

    static class MentorViewHolder extends RecyclerView.ViewHolder {
        TextView tvMentorName;
        ImageView ivIcon;

        MentorViewHolder(View itemView) {
            super(itemView);
            tvMentorName = itemView.findViewById(R.id.tv_mentor_name);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }

    @NonNull
    @Override
    public MentorPopup.MentorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MentorPopup.MentorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mentor_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MentorPopup.MentorViewHolder holder, final int position) {
        final Mentor mentor = currMentors.get(position);
        holder.tvMentorName.setText(mentor.getName());
        holder.itemView.setOnClickListener(view -> {
            if(currMentorListener != null) {
                currMentorListener.onMentorSelected(position, mentor);
            }
        });
    }

    public interface MentorListener {
        void onMentorSelected(int position, Mentor mentor);
    }

    void setMentorListener(MentorPopup.MentorListener currMentorListener) {
        this.currMentorListener = currMentorListener;
    }

    @Override
    public int getItemCount() {
        return currMentors.size();
    }
}