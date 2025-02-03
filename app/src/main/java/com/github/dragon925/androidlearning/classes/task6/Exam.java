package com.github.dragon925.androidlearning.classes.task6;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Exam {
    private final String subject;
    private final Map<Applicant, Grade> passed = new HashMap<>();

    public Exam(String subject) {
        this.subject = subject;
    }

    public void addApplicant(@NonNull Applicant applicant) {
        if (Objects.isNull(applicant) || passed.containsKey(applicant)) return;

        passed.put(applicant, null);
    }

    public void setGrade(@NonNull Applicant applicant, @NonNull Grade grade) {
        Objects.requireNonNull(applicant, "applicant is null");
        Objects.requireNonNull(grade, "grade is null");
        passed.put(applicant, grade);
    }

    public Map<Applicant, Grade> getPassed() {
        return new HashMap<>(passed);
    }

    public String getSubject() {
        return subject;
    }
}
