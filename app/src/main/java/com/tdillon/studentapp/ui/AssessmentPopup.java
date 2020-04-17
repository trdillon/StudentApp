package com.tdillon.studentapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdillon.studentapp.R;
import com.tdillon.studentapp.model.Assessment;

import java.util.List;

public class AssessmentPopup extends RecyclerView.Adapter<AssessmentPopup.AssessmentViewHolder> {

    private List<Assessment> uAssessments;
    private AssessmentListener currAssessmentListener;

    public AssessmentPopup(List<Assessment> uAssessments) {
        super();
        this.uAssessments = uAssessments;
    }

    static class AssessmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvAssessmentTitle;
        ImageView ivIcon;

        public AssessmentViewHolder(View itemView) {
            super(itemView);
            tvAssessmentTitle = itemView.findViewById(R.id.tv_assessment_title);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }

    public interface AssessmentListener {
        void onAssessmentSelected(int position, Assessment assessment);
    }

    public void setAssessmentListener(AssessmentPopup.AssessmentListener currAssessmentListener) {
        this.currAssessmentListener = currAssessmentListener;
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssessmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.assessment_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, final int position) {
        final Assessment assessment = uAssessments.get(position);
        holder.tvAssessmentTitle.setText(assessment.getTitle());
        holder.itemView.setOnClickListener(view -> {
            if(currAssessmentListener != null) {
                currAssessmentListener.onAssessmentSelected(position, assessment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return uAssessments.size();
    }
}