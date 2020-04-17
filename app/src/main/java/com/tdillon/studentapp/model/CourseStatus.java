package com.tdillon.studentapp.model;

public enum CourseStatus {

    PLAN_TO_TAKE {
        @Override
        public String toString() {
            return "Plan to take";
        }
    },

    IN_PROGRESS {
        @Override
        public String toString() {
            return "In progress";
        }
    },

    PASSED {
        @Override
        public String toString() {
            return "Passed";
        }
    },

    DROPPED {
        @Override
        public String toString() {
            return "Dropped";
        }
    }
}