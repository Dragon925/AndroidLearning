package com.github.dragon925.androidlearning.classes.task2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


/**
 * Создать класс, содержащий динамический массив и количество элементов в нем.
 * <p>
 * Добавить конструктор, который выделяет память под заданное количество элементов.
 * <p>
 * Добавить методы, позволяющие заполнять массив случайными числами,
 * переставлять в данном массиве элементы в случайном порядке, находить количество
 * различных элементов в массиве, выводить массив на экран.
 */
public class Task2 {

    private final ArrayList<Integer> numbers = new ArrayList<>();
    private int length;

    public Task2(int length) {
        numbers.ensureCapacity(Math.max(0, length));
        this.length = length;
    }

    public void fillRandom(int min, int max) {
        for (int i = 0; i < length; i++) {
            numbers.add((int) Math.round(Math.random() * (max - min) + min));
        }
    }

    public void shuffle() {
        Collections.shuffle(numbers);
    }

    public ArrayList<Integer> getNumbers() {
        return new ArrayList<>(numbers);
    }

    public int countUnique() {
        return (new HashSet<>(numbers)).size();
//        return numbers.stream().distinct().count(); Альтернатива
    }

    public int getCount(int number) {
        int count = 0;
        for (int num : numbers) {
            if (num == number) {
                count++;
            }
        }
        return count;
//        return numbers.stream().filter(num -> num == number).count(); Альтернатива
    }

    public void print() {
        for (int i = 0; i < numbers.size(); i++) {
            System.out.print(numbers.get(i) + " ");
        }
        System.out.println();
    }

    public void setNumbers(ArrayList<Integer> numbers) {
        this.numbers.clear();
        this.numbers.addAll(numbers);
        this.numbers.trimToSize();
        this.length = numbers.size();
    }

    public int getLength() {
        return length;
    }
}
