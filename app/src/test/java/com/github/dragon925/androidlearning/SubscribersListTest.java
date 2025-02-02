package com.github.dragon925.androidlearning;

import com.github.dragon925.androidlearning.classes.task_5.Subscriber;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SubscribersListTest {

    private static List<Subscriber> subscribers;

    @BeforeClass
    public static void init() {
        subscribers = Arrays.asList(
                new Subscriber(1, "Ivanov", "Ivan", "Ivanovich", "Address 1", "1234567890123456", 100.0, 50.0, 120000, 30000),
                new Subscriber(2, "Petrov", "Petr", "Petrovich", "Address 2", "2345678901234567", 200.0, 100.0, 180000, 60000),
                new Subscriber(3, "Sidorov", "Sidor", "Sidorovich", "Address 3", "3456789012345678", 300.0, 150.0, 240000, 0),
                new Subscriber(4, "Kuznetsov", "Kuzma", "Kuzmich", "Address 4", "4567890123456789", 400.0, 200.0, 300000, 120000),
                new Subscriber(5, "Popov", "Pop", "Popovich", "Address 5", "5678901234567890", 500.0, 250.0, 360000, 150000),
                new Subscriber(6, "Sokolov", "Sokol", "Sokolovich", "Address 6", "6789012345678901", 600.0, 300.0, 420000, 0),
                new Subscriber(7, "Morozov", "Moroz", "Morozovich", "Address 7", "7890123456789012", 700.0, 350.0, 480000, 210000),
                new Subscriber(8, "Kuzmin", "Kuzma", "Kuzminich", "Address 8", "8901234567890123", 800.0, 400.0, 540000, 240000),
                new Subscriber(9, "Pavlov", "Pavlo", "Pavlovich", "Address 9", "9012345678901234", 900.0, 450.0, 600000, 270000),
                new Subscriber(10, "Smirnov", "Smirn", "Smirnovich", "Address 10", "0123456789012345", 1000.0, 500.0, 660000, 0)
        );
    }

    @Test
    public void subscriber_cityCallTimeExceeding() {
        long cityCallTimeThreshold = 200000;
        System.out.println("Subscribers with city call time exceeding " + cityCallTimeThreshold + " milliseconds:");
        subscribers.stream()
                .filter(subscriber -> subscriber.getCityCallTime() > cityCallTimeThreshold)
                .forEach(Subscriber::printInfo);
    }

    @Test
    public void subscriber_withLongDistanceCalls() {
        System.out.println("Subscribers with long distance calls:");
        subscribers.stream()
                .filter(subscriber -> subscriber.getLongDistanceCallTime() > 0)
                .forEach(Subscriber::printInfo);
    }

    @Test
    public void subscriber_inAlphabeticalOrder() {
        System.out.println("Subscribers in alphabetical order:");
        subscribers.stream()
                .sorted(Comparator.comparing(Subscriber::getLastName)
                        .thenComparing(Subscriber::getFirstName)
                        .thenComparing(Subscriber::getMiddleName))
                .forEach(Subscriber::printInfo);
    }
}
