package com.github.dragon925.androidlearning;

import com.github.dragon925.androidlearning.collections.Grade;
import com.github.dragon925.androidlearning.collections.Student;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentListTest {

    private static List<Student> students;

    @BeforeClass
    public static void init() {
        students = new ArrayList<>();

        students.add(new Student("Иванов", "Иван", "Иванович", 2000, 2, "ГРП1",
                List.of(new Grade("Математика", 5), new Grade("Физика", 5), new Grade("Химия", 5), new Grade("История", 5), new Grade("Литература", 5))));

        students.add(new Student("Петров", "Петр", "Петрович", 2001, 1, "ГРП1",
                List.of(new Grade("Математика", 4), new Grade("Физика", 4), new Grade("Химия", 4), new Grade("История", 4), new Grade("Литература", 4))));

        students.add(new Student("Сидоров", "Алексей", "Николаевич", 1999, 2, "ГРП2",
                List.of(new Grade("Математика", 3), new Grade("Физика", 4), new Grade("Химия", 5), new Grade("История", 3), new Grade("Литература", 4))));

        students.add(new Student("Смирнова", "Ольга", "Сергеевна", 2002, 1, "ГРП2",
                List.of(new Grade("Математика", 5), new Grade("Физика", 5), new Grade("Химия", 4), new Grade("История", 5), new Grade("Литература", 5))));

        students.add(new Student("Кузнецов", "Сергей", "Владимирович", 2000, 3, "ГРП3",
                List.of(new Grade("Математика", 3), new Grade("Физика", 3), new Grade("Химия", 3), new Grade("История", 3), new Grade("Литература", 3))));
    }

    @Test
    public void studentList_sortList() {
        var sortedList = students.stream()
                .sorted(Comparator.comparing(Student::getCourse).thenComparing(Student::getFullName))
                .collect(Collectors.toList());
        sortedList.forEach(System.out::println);
    }

    @Test
    public void studentList_getAverageByGroup() {
        Map<String, List<Grade>> groupSubjectAverages = new HashMap<>();
        var studentsByGroup = students.stream().collect(Collectors.groupingBy(Student::getGroup));

        studentsByGroup.forEach((group, groupStudents) -> {
            var gradesBySubject = groupStudents.stream()
                    .flatMap(student -> student.getGrades().stream())
                    .collect(Collectors.groupingBy(Grade::getSubject));

            gradesBySubject.forEach((subject, grades) -> {
                var average = grades.stream().mapToDouble(Grade::getScore).average().orElse(0.0);
                groupSubjectAverages.computeIfAbsent(group, k -> new ArrayList<>())
                        .add(new Grade(subject, average));
            });
        });

        groupSubjectAverages.forEach((group, subjectsAverage) -> {
            System.out.println(group);
            subjectsAverage.forEach(subjectAverage -> {
                System.out.println("\t"  + subjectAverage.getSubject() + ": " + subjectAverage.getScore());
            });
        });
    }

    @Test
    public void studentList_getOldest() {
        var oldest = students.stream()
                .min(Comparator.comparing(Student::getBirthYear))
                .orElse(null);
        System.out.println(oldest);
    }

    @Test
    public void studentList_getYoungest() {
        var youngest = students.stream()
                .max(Comparator.comparing(Student::getBirthYear))
                .orElse(null);
        System.out.println(youngest);
    }

    @Test
    public void studentList_getBestInGroup() {
        Map<String, Student> bestInGroup = new HashMap<>();
        var studentsByGroup = students.stream().collect(Collectors.groupingBy(Student::getGroup));
        studentsByGroup.forEach((group, groupStudents) -> {
            var bestStudent = groupStudents.stream()
                    .max(Comparator.comparingDouble(student -> student.getGrades().stream()
                            .mapToDouble(Grade::getScore)
                            .average()
                            .orElse(0.0)))
                    .orElse(null);
            bestInGroup.put(group, bestStudent);
        });

        bestInGroup.forEach((group, student) -> System.out.println(group + ": " + student));
    }
}
