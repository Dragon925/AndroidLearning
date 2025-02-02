package com.github.dragon925.androidlearning.classes.task_1;

import androidx.annotation.NonNull;

/**
 * Создать класс с двумя переменными. Добавить функцию вывода на экран
 * и функцию изменения этих переменных. Добавить функцию, которая находит
 * сумму значений этих переменных, и функцию, которая находит наибольшее
 * значение из этих двух переменных.
 */
public class Task1 {

    private int first;
    private int second;

    public Task1(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void print() {
        System.out.println(this);
    }

    public int sum() {
        return first + second;
    }

    public int max() {
        return Math.max(first, second);
    }

    @NonNull
    @Override
    public String toString() {
        return "Task1{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
