package com.github.dragon925.androidlearning.classes.task6;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Faculty {

    private final String name;
    private final double passingScore;
    private final List<String> requiredExams;

    private final Set<Applicant> applicants = new HashSet<>();

    public Faculty(@NonNull String name, double passingScore, @NonNull List<String> requiredExams) {
        this.name = Objects.requireNonNull(name, "name is null");
        this.passingScore = passingScore;
        this.requiredExams = Objects.requireNonNull(requiredExams, "requiredExams is null");
    }

    public void addApplicant(@NonNull Applicant applicant) {
        applicants.add(Objects.requireNonNull(applicant, "applicant is null"));
    }

    public Set<Applicant> getApplicants() {
        return new HashSet<>(applicants);
    }

    public String getName() {
        return name;
    }

    public double getPassingScore() {
        return passingScore;
    }

    public List<String> getRequiredExams() {
        return new ArrayList<>(requiredExams);
    }
}
