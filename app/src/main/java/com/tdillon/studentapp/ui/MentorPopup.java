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

    private List<Mentor> uMentors;
    private MentorPopup.MentorListener currMentorListener;

    public MentorPopup(List<Mentor> uMentors) {
        super();
        this.uMentors = uMentors;
    }

    static class MentorViewHolder extends RecyclerView.ViewHolder {
        TextView tvMentorName;
        ImageView ivIcon;

        public MentorViewHolder(View itemView) {
            super(itemView);
            tvMentorName = itemView.findViewById(R.id.tv_mentor_name);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }

    public interface MentorListener {
        void onMentorSelected(int position, Mentor mentor);
    }

    public void setMentorListener(MentorPopup.MentorListener currMentorListener) {
        this.currMentorListener = currMentorListener;
    }

    @NonNull
    @Override
    public MentorPopup.MentorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MentorPopup.MentorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mentor_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MentorPopup.MentorViewHolder holder, final int position) {
        final Mentor mentor = uMentors.get(position);
        holder.tvMentorName.setText(mentor.getName());
        holder.itemView.setOnClickListener(view -> {
            if(currMentorListener != null) {
                currMentorListener.onMentorSelected(position, mentor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return uMentors.size();
    }
}