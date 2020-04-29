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
import com.tdillon.studentapp.AssessmentDetailsActivity;
import com.tdillon.studentapp.AssessmentEditActivity;
import com.tdillon.studentapp.R;
import com.tdillon.studentapp.model.Assessment;
import com.tdillon.studentapp.util.TextFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tdillon.studentapp.util.Constants.ASSESSMENT_ID_KEY;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.ViewHolder> {

    private final Context currContext;
    private final RecyclerContext rContext;
    private final List<Assessment> currAssessments;
    private AssessmentListener currAssessmentListener;

    public AssessmentAdapter(List<Assessment> currAssessments, Context currContext,
                             RecyclerContext rContext, AssessmentListener currAssessmentListener) {
        this.currAssessments = currAssessments;
        this.currContext = currContext;
        this.rContext = rContext;
        this.currAssessmentListener = currAssessmentListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.view_assessment_title)
        TextView tvTitle;
        @BindView(R.id.view_assessment_dates)
        TextView tvDate;
        @BindView(R.id.view_assessment_fab)
        FloatingActionButton asmtFab;
        @BindView(R.id.btn_assessment_details)
        ImageButton asmtImageBtn;
        AssessmentListener currAssessmentListener;

        public ViewHolder(View itemView, AssessmentListener currAssessmentListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.currAssessmentListener = currAssessmentListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            currAssessmentListener.onAssessmentSelected(getAdapterPosition(),
                    currAssessments.get(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.assessment_list_view, parent, false);
        return new ViewHolder(view, currAssessmentListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.ViewHolder holder, int position) {
        final Assessment assessment = currAssessments.get(position);
        holder.tvTitle.setText(assessment.getTitle());
        holder.tvDate.setText(TextFormatter.getDateFormatted(assessment.getDate()));

        switch(rContext) {
            case MAIN:
                holder.asmtFab.setImageDrawable(ContextCompat.getDrawable(currContext, R.drawable.ic_edit));
                holder.asmtImageBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(currContext, AssessmentDetailsActivity.class);
                    intent.putExtra(ASSESSMENT_ID_KEY, assessment.getId());
                    currContext.startActivity(intent);
                });

                holder.asmtFab.setOnClickListener(v -> {
                    Intent intent = new Intent(currContext, AssessmentEditActivity.class);
                    intent.putExtra(ASSESSMENT_ID_KEY, assessment.getId());
                    currContext.startActivity(intent);
                });
                break;
            case CHILD:
                holder.asmtFab.setImageDrawable(ContextCompat.getDrawable(currContext, R.drawable.ic_delete));
                holder.asmtFab.setOnClickListener(v -> {
                    if(currAssessmentListener != null) {
                        currAssessmentListener.onAssessmentSelected(position, assessment);
                    }
                });
                break;
        }
    }

    public interface AssessmentListener {
        void onAssessmentSelected(int position, Assessment assessment);
    }
    
    @Override
    public int getItemCount() {
        return currAssessments.size();
    }
}