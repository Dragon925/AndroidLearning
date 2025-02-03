package com.github.dragon925.androidlearning;

import static org.junit.Assert.assertEquals;

import com.github.dragon925.androidlearning.classes.task4.Time;

import org.junit.BeforeClass;
import org.junit.Test;

public class TimeTest {

    private static Time time;

    @BeforeClass
    public static void init() {
        time = new Time(0, 0, 0);
    }

    @Test
    public void time_minusSecond() {
        time.setHour(0);
        time.setMinute(0);
        time.setSecond(0);
        time.addSeconds(-1);
        var actualValue = String.format("%02d:%02d:%02d", time.getHour(), time.getMinute(), time.getSecond());
        var expectedValue = "23:59:59";

        assertEquals(expectedValue, actualValue);
    }
}
