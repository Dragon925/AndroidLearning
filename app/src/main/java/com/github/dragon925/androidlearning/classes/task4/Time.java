package com.github.dragon925.androidlearning.classes.task4;


/**
 * Составить описание класса для представления времени.
 * <p>
 * Предусмотреть возможности установки времени и изменения его отдельных полей
 * (час, минута, секунда) с проверкой допустимости вводимых значений.
 * <p>
 * В случае недопустимых значений полей выбрасываются исключения.
 * <p>
 * Создать методы изменения времени на заданное количество часов, минут и секунд.
 */
public class Time {

    private int hour;
    private int minute;
    private int second;

    public Time(int hour, int minute, int second) throws IllegalArgumentException {
        setHour(hour);
        setMinute(minute);
        setSecond(second);
    }

    public Time(int minute, int second) throws IllegalArgumentException {
        this(0, minute, second);
    }

    public Time(int second) throws IllegalArgumentException {
        this(0, 0, second);
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) throws IllegalArgumentException {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Time is not valid");
        }

        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) throws IllegalArgumentException {
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Time is not valid");
        }

        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) throws IllegalArgumentException {
        if (second < 0 || second > 59) {
            throw new IllegalArgumentException("Time is not valid");
        }

        this.second = second;
    }

    public void addHours(int hours) {
        this.hour = Math.floorMod(this.hour + hours, 24);
    }

    public void addMinutes(int minutes) {
        int totalMinutes = this.minute + minutes;
        this.minute = Math.floorMod(totalMinutes, 60);
        addHours(Math.floorDiv(totalMinutes, 60));
    }

    public void addSeconds(int seconds) {
        int totalSeconds = this.second + seconds;
        this.second = Math.floorMod(totalSeconds, 60);
        addMinutes(Math.floorDiv(totalSeconds, 60));
    }
}
