package com.tdillon.studentapp.model;

import androidx.annotation.NonNull;

public enum CourseStatus {

    PLAN_TO_TAKE {
        @NonNull
        @Override
        public String toString() {
            return "Plan to take";
        }
    },

    IN_PROGRESS {
        @NonNull
        @Override
        public String toString() {
            return "In progress";
        }
    },

    PASSED {
        @NonNull
        @Override
        public String toString() {
            return "Passed";
        }
    },

    DROPPED {
        @NonNull
        @Override
        public String toString() {
            return "Dropped";
        }
    }
}