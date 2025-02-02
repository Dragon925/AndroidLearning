package com.github.dragon925.androidlearning.collections;

import androidx.annotation.NonNull;

public class Grade {

    private final String subject;
    private final double score;

    public Grade(String subject, double score) {
        this.subject = subject;
        this.score = score;
    }

    public String getSubject() {
        return subject;
    }

    public double getScore() {
        return score;
    }

    @NonNull
    @Override
    public String toString() {
        return "Grade{" +
                "subject='" + subject + '\'' +
                ", score=" + score +
                '}';
    }
}
