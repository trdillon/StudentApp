package com.tdillon.studentapp.ui;

import androidx.annotation.NonNull;

public enum RecyclerContext {

    MAIN {
        @NonNull
        @Override
        public String toString() {
            return "Parent";
        }
    },

    CHILD {
        @NonNull
        @Override
        public String toString() {
            return "Child";
        }
    },

    ADD {
        @NonNull
        @Override
        public String toString() {
            return "Add";
        }
    }
}