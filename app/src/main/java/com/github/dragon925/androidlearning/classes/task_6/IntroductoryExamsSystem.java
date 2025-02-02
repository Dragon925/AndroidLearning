package com.github.dragon925.androidlearning.classes.task_6;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Задача на взаимодействие между классами. Разработать систему «Вступительные экзамены».
 * <p>
 * Абитуриент регистрируется на Факультет, сдает Экзамены.
 * <p>
 * Преподаватель выставляет Оценку.
 * <p>
 * Система подсчитывает средний бал и определяет Абитуриента, зачисленного в учебное заведение.
 */
public class IntroductoryExamsSystem {

    private final Map<String, Faculty> nameToFaculty = new HashMap<>();
    private final Map<String, Exam> nameToExam = new HashMap<>();

    public IntroductoryExamsSystem(@NonNull List<Faculty> faculties) {
        Objects.requireNonNullElse(faculties, new ArrayList<Faculty>()).stream()
                .filter(Objects::nonNull)
                .forEach(faculty -> nameToFaculty.put(faculty.getName(), faculty));

        faculties.stream()
                .flatMap(faculty -> faculty.getRequiredExams().stream())
                .distinct()
                .forEach(name -> nameToExam.put(name, new Exam(name)));
    }

    public List<String> registerApplicantToFaculty(@NonNull Applicant applicant, @NonNull String facultyName) {
        var faculty = nameToFaculty.get(Objects.requireNonNull(facultyName, "facultyName is null"));
        if (faculty == null) {
            throw new IllegalArgumentException("faculty is not registered");
        }
        Objects.requireNonNull(faculty, "faculty is not registered")
                .addApplicant(Objects.requireNonNull(applicant, "applicant is null"));

        return faculty.getRequiredExams();
    }

    public void passExams(@NonNull Applicant applicant, @NonNull List<String> exams) {
        Objects.requireNonNull(applicant, "applicant is null");
        Objects.requireNonNull(exams, "exams is null").stream()
                .filter(nameToExam::containsKey)
                .forEach(examName -> nameToExam.get(examName).addApplicant(applicant));
    }

    public void assignGrade(@NonNull Applicant applicant, @NonNull Teacher teacher, @NonNull String examName, int score) {
        if (!nameToExam.containsKey(examName)) {
            throw new IllegalArgumentException("exam is not exist");
        }

        var exam = nameToExam.get(examName);
        if (!exam.getPassed().containsKey(Objects.requireNonNull(applicant, "applicant is null"))) {
            throw new IllegalArgumentException("applicant is not passed exam");
        }

        var grade = new Grade(score, Objects.requireNonNull(teacher, "teacher is null"));
        exam.setGrade(applicant, grade);
    }

    public Map<String, List<Applicant>> getFacultiesWithEnrolledApplicants() {
        Map<String, List<Applicant>> result = new HashMap<>();
        for (var faculty : nameToFaculty.values()) {
            var applicants = faculty.getApplicants().stream()
                    .filter(applicant -> getAverageScore(applicant, faculty) >= faculty.getPassingScore())
                    .collect(Collectors.toList());
            result.put(faculty.getName(), applicants);
        }

        return result;
    }

    private double getAverageScore(Applicant applicant, Faculty faculty) {
        var requiredExams = faculty.getRequiredExams();
        return requiredExams.stream()
                .map(nameToExam::get)
                .filter(Objects::nonNull)
                .map(exam -> exam.getPassed().get(applicant))
                .filter(Objects::nonNull)
                .map(grade -> grade.getScore())
                .reduce(Integer::sum)
                .map(sum -> sum / (double) requiredExams.size())
                .orElse(0.0);
    }
}
