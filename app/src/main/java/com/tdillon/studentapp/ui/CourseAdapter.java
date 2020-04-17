package com.tdillon.studentapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tdillon.studentapp.CourseDetailsActivity;
import com.tdillon.studentapp.CourseEditActivity;
import com.tdillon.studentapp.R;
import com.tdillon.studentapp.model.Course;
import com.tdillon.studentapp.util.TextFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tdillon.studentapp.util.Constants.COURSE_ID_KEY;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private final Context uContext;
    private final RecyclerContext rContext;
    private final List<Course> uCourses;
    private CourseListener currCourseListener;

    public CourseAdapter(List<Course> uCourses, Context uContext, RecyclerContext rContext, CourseListener currCourseListener) {
        this.uCourses = uCourses;
        this.uContext = uContext;
        this.rContext = rContext;
        this.currCourseListener = currCourseListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.view_course_title)
        TextView tvTitle;
        @BindView(R.id.view_course_fab)
        FloatingActionButton courseFab;
        @BindView(R.id.view_course_dates)
        TextView tvDates;
        @BindView(R.id.btn_course_details)
        ImageButton courseImageBtn;
        CourseListener currCourseListener;

        public ViewHolder(View itemView, CourseListener currCourseListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.currCourseListener = currCourseListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            currCourseListener.onCourseSelected(getAdapterPosition(), uCourses.get(getAdapterPosition()));
        }
    }

    public interface CourseListener {
        void onCourseSelected(int position, Course course);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.course_list_view, parent, false);
        return new ViewHolder(view, currCourseListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, int position) {
        final Course course = uCourses.get(position);
        holder.tvTitle.setText(course.getTitle());
        String startAndEnd = TextFormatter.cardDateFormat.format(course.getStartDate()) +
                " to " + TextFormatter.cardDateFormat.format(course.getExpectedEndDate());
        holder.tvDates.setText(startAndEnd);

        switch(rContext) {
            case MAIN:
                Log.v("rContext", "rContext is " + rContext.name());
                holder.courseFab.setImageDrawable(ContextCompat.getDrawable(uContext, R.drawable.ic_edit));
                holder.courseImageBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(uContext, CourseDetailsActivity.class);
                    intent.putExtra(COURSE_ID_KEY, course.getId());
                    uContext.startActivity(intent);
                });

                holder.courseFab.setOnClickListener(v -> {
                    Intent intent = new Intent(uContext, CourseEditActivity.class);
                    intent.putExtra(COURSE_ID_KEY, course.getId());
                    uContext.startActivity(intent);
                });
                break;
            case CHILD:
                holder.courseFab.setImageDrawable(ContextCompat.getDrawable(uContext, R.drawable.ic_delete));
                holder.courseFab.setOnClickListener(v -> {
                    if(currCourseListener != null){
                        currCourseListener.onCourseSelected(position, course);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return uCourses.size();
    }
}