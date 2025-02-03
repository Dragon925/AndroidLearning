package com.github.dragon925.androidlearning.classes.task6;

import java.util.Objects;

public class Applicant {

    private final String firstName;
    private final String lastName;
    private final String middleName;

    public Applicant(String firstName, String lastName, String middleName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Applicant applicant = (Applicant) o;
        return Objects.equals(firstName, applicant.firstName)
                && Objects.equals(lastName, applicant.lastName)
                && Objects.equals(middleName, applicant.middleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, middleName);
    }
}
