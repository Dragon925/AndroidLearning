package com.github.dragon925.androidlearning.collections;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final int birthYear;
    private final int course;
    private final String group;
    private final List<Grade> grades;

    public Student(String firstName,
                   String lastName,
                   String middleName,
                   int birthYear,
                   int course,
                   String group,
                   List<Grade> grades) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthYear = birthYear;
        this.course = course;
        this.group = group;
        this.grades = new ArrayList<>(grades);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getFullName() {
        return String.format("%s %s %s", firstName, lastName, middleName);
    }

    public int getBirthYear() {
        return birthYear;
    }

    public int getCourse() {
        return course;
    }

    public String getGroup() {
        return group;
    }

    public List<Grade> getGrades() {
        return new ArrayList<>(grades);
    }

    @NonNull
    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthYear=" + birthYear +
                ", course=" + course +
                ", groupNumber='" + group + '\'' +
                ", grades=" + grades +
                '}';
    }
}
