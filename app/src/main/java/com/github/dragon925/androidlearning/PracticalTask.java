package com.github.dragon925.androidlearning;

public class PracticalTask {

    // Задание 3 (Вывод в файле PracticalTaskTest: practicalTask_Task3())

    public static final Runnable myClosure = () -> System.out.println("I love Java");

    public static void repeatTask (int times, Runnable task) {
        for (int i = 0; i < times; i++) {
            task.run();
        }
    }

    // Задание 4 (Вывод в файле PracticalTaskTest: practicalTask_Task4())

    public enum Directions {
        UP, DOWN, LEFT, RIGHT
    }

    public static int[] move(int x, int y, Directions direction) {
        switch (direction) {
            case UP:
                y++;
                break;
            case DOWN:
                y--;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
        }
        return new int[]{x, y};
    }

    public static void move() {
        int[] location = {0, 0};
        Directions[] directions = {Directions.UP, Directions.UP, Directions.LEFT,
                Directions.DOWN, Directions.LEFT, Directions.DOWN, Directions.DOWN,
                Directions.RIGHT, Directions.RIGHT, Directions.DOWN, Directions.RIGHT};

        for (var direction : directions) {
            location = move(location[0], location[1], direction);
            System.out.println(location[0] + " " + location[1]);
        }
    }

    // Задание 5

    interface Shape {
        double getPerimeter();
        double getArea();
    }

    static class Rectangle implements Shape {

        private final double width;
        private final double height;

        public Rectangle(double width, double height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public double getPerimeter() {
            return (width + height) * 2;
        }

        @Override
        public double getArea() {
            return width * height;
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }
    }

    static class Square implements Shape {

        private final double side;

        public Square(double side) {
            this.side = side;
        }

        @Override
        public double getPerimeter() {
            return side * 4;
        }

        @Override
        public double getArea() {
            return side * side;
        }

        public double getSide() {
            return side;
        }
    }

    static class Circle implements Shape {

        private final double diameter;

        public Circle(double diameter) {
            this.diameter = diameter;
        }

        @Override
        public double getPerimeter() {
            return Math.PI * diameter;
        }

        @Override
        public double getArea() {
            return 0.25 * Math.PI * diameter * diameter;
        }
    }

}
