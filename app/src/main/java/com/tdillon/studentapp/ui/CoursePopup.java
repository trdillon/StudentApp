package com.tdillon.studentapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdillon.studentapp.R;
import com.tdillon.studentapp.model.Course;

import java.util.List;

public class CoursePopup extends RecyclerView.Adapter<CoursePopup.CourseViewHolder> {

    private List<Course> uCourses;
    private CourseListener currCourseListener;

    public CoursePopup(List<Course> uCourses){
        super();
        this.uCourses = uCourses;
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseTitle;
        ImageView ivIcon;

        public CourseViewHolder(View itemView) {
            super(itemView);
            tvCourseTitle = itemView.findViewById(R.id.tv_course_title);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }

    public interface CourseListener {
        void onCourseSelected(int position, Course course);
    }

    public void setCourseListener(CoursePopup.CourseListener currCourseListener) {
        this.currCourseListener = currCourseListener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, final int position) {
        final Course course = uCourses.get(position);
        holder.tvCourseTitle.setText(course.getTitle());
        holder.itemView.setOnClickListener(view -> {
            if(currCourseListener != null){
                currCourseListener.onCourseSelected(position, course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return uCourses.size();
    }
}