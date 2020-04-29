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

    private final Context currContext;
    private final RecyclerContext rContext;
    private final List<Course> currCourses;
    private CourseListener currCourseListener;

    public CourseAdapter(List<Course> currCourses, Context currContext, RecyclerContext rContext, CourseListener currCourseListener) {
        this.currCourses = currCourses;
        this.currContext = currContext;
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
            currCourseListener.onCourseSelected(getAdapterPosition(), currCourses.get(getAdapterPosition()));
        }
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
        final Course course = currCourses.get(position);
        holder.tvTitle.setText(course.getTitle());
        String startAndEnd = TextFormatter.getDateFormatted(course.getStartDate()) +
                " to " + TextFormatter.getDateFormatted(course.getExpectedEndDate());
        holder.tvDates.setText(startAndEnd);

        switch(rContext) {
            case MAIN:
                holder.courseFab.setImageDrawable(ContextCompat.getDrawable(currContext, R.drawable.ic_edit));
                holder.courseImageBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(currContext, CourseDetailsActivity.class);
                    intent.putExtra(COURSE_ID_KEY, course.getId());
                    currContext.startActivity(intent);
                });

                holder.courseFab.setOnClickListener(v -> {
                    Intent intent = new Intent(currContext, CourseEditActivity.class);
                    intent.putExtra(COURSE_ID_KEY, course.getId());
                    currContext.startActivity(intent);
                });
                break;
            case CHILD:
                holder.courseFab.setImageDrawable(ContextCompat.getDrawable(currContext, R.drawable.ic_delete));
                holder.courseFab.setOnClickListener(v -> {
                    if(currCourseListener != null){
                        currCourseListener.onCourseSelected(position, course);
                    }
                });
                break;
        }
    }

    public interface CourseListener {
        void onCourseSelected(int position, Course course);
    }

    @Override
    public int getItemCount() {
        return currCourses.size();
    }
}