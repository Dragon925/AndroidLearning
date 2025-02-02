package com.github.dragon925.androidlearning.classes.task_6;

public class Grade {

    private final int score;
    private final Teacher teacher;

    public Grade(int score, Teacher teacher) {
        this.score = score;
        this.teacher = teacher;
    }

    public int getScore() {
        return score;
    }

    public Teacher getTeacher() {
        return teacher;
    }
}
