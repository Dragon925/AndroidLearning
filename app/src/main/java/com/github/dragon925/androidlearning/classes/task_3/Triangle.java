package com.github.dragon925.androidlearning.classes.task_3;

/**
 * Описать класс, представляющий треугольник.
 * <p>
 * Предусмотреть методы для создания объектов,
 * вычисления площади, периметра и точки пересечения медиан.
 * <p>
 * Описать свойства для получения состояния объекта.
 */
public class Triangle {

    private final Point pointA;
    private final Point pointB;
    private final Point pointC;
    private final double sideA;
    private final double sideB;
    private final double sideC;

    public Triangle(Point pointA, Point pointB, Point pointC) throws IllegalArgumentException {

        this.pointA = pointA;
        this.pointB = pointB;
        this.pointC = pointC;

        this.sideA = getSideLength(pointA, pointB);
        this.sideB = getSideLength(pointB, pointC);
        this.sideC = getSideLength(pointC, pointA);

        if (sideA + sideB <= sideC || sideB + sideC <= sideA || sideC + sideA <= sideB) {
            throw new IllegalArgumentException("Triangle can't be created");
        }
    }

    private double getSideLength(Point a, Point b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    public Point getPointA() {
        return pointA;
    }

    public Point getPointB() {
        return pointB;
    }

    public Point getPointC() {
        return pointC;
    }

    public double getSideA() {
        return sideA;
    }

    public double getSideB() {
        return sideB;
    }

    public double getSideC() {
        return sideC;
    }

    public double getPerimeter() {
        return sideA + sideB + sideC;
    }

    public double getArea() {
        double p = getPerimeter() / 2;
        return Math.sqrt(p * (p - sideA) * (p - sideB) * (p - sideC));
    }

    public Point getMedianIntersectionPoint() {
        double x = (pointA.getX() + pointB.getX() + pointC.getX()) / 3;
        double y = (pointA.getY() + pointB.getY() + pointC.getY()) / 3;
        return new Point(x, y);
    }
}
