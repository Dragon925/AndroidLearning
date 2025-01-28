package com.github.dragon925.androidlearning;

import com.github.dragon925.androidlearning.training.ElementaryTraining;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


public class ElementaryTrainingTest {

    private static ElementaryTraining elementaryTraining;

    @BeforeClass
    public static void init() {
        elementaryTraining = new ElementaryTraining();
    }

    @Test
    public void averageValue_realValues() {
        double averageValue = elementaryTraining.averageValue(10, 20);
        int expectedValue = 15;
        assertEquals(expectedValue, averageValue, 0.1);
    }

    @Test
    public void averageValue_checkDoubleResult() {
        double averageValue = elementaryTraining.averageValue(11, 20);
        double expectedValue = 15.5;
        assertEquals(expectedValue, averageValue, 0.1);
    }

    @Test
    public void averageValue_emptyValues() {
        double averageValue = elementaryTraining.averageValue(0, 0);
        int expectedValue = 0;
        assertEquals(expectedValue, averageValue, 0.1);
    }

    @Test
    public void complicatedAmount_realValues() {
        double amount = elementaryTraining.complicatedAmount(5, -7, 5);
        int expectedValue = 25;
        assertEquals(expectedValue, amount, 0.1);
    }

    @Test
    public void complicatedAmount_emptyValues() {
        double amount = elementaryTraining.complicatedAmount(0, 0, 0);
        int expectedValue = -3;
        assertEquals(expectedValue, amount, 0.1);
    }

    @Test
    public void changeValue_valueTwo() {
        int changedValue = elementaryTraining.changeValue(2);
        int expectedValue = -8;
        assertEquals(expectedValue, changedValue);
    }

    @Test
    public void changeValue_valueZero() {
        int changedValue = elementaryTraining.changeValue(0);
        int expectedValue = -10;
        assertEquals(expectedValue, changedValue);
    }

    @Test
    public void changeValue_valueTen() {
        int changedValue = elementaryTraining.changeValue(10);
        int expectedValue = 20;
        assertEquals(expectedValue, changedValue);
    }

    @Test
    public void swapNumbers_fiveDigitNumber() {
        int changedValue = elementaryTraining.swapNumbers(54301);
        int expectedValue = 14305;
        assertEquals(expectedValue, changedValue);

        int changedValueForZeroDigit = elementaryTraining.swapNumbers(54300);
        int expectedValueForZeroDigit = 4305;
        assertEquals(expectedValueForZeroDigit, changedValueForZeroDigit);
    }

    @Test
    public void swapNumbers_twoDigitNumber() {
        int changedValue = elementaryTraining.swapNumbers(25);
        int expectedValue = 52;
        assertEquals(expectedValue, changedValue);

        int changedValueForZeroDigit = elementaryTraining.swapNumbers(20);
        int expectedValueForZeroDigit = 2;
        assertEquals(expectedValueForZeroDigit, changedValueForZeroDigit);
    }

    @Test
    public void swapNumbers_oneDigitNumber() {
        int changedValue = elementaryTraining.swapNumbers(5);
        int expectedValue = 5;
        assertEquals(expectedValue, changedValue);
    }

    @Test
    public void zeroEvenNumber_fiveDigitNumber() {
        int changedValue = elementaryTraining.zeroEvenNumber(54301);
        int expectedValue = 50301;
        assertEquals(expectedValue, changedValue);
    }

    @Test
    public void zeroEvenNumber_twoDigitNumber() {
        int changedValue = elementaryTraining.zeroEvenNumber(25);
        int expectedValue = 5;
        assertEquals(expectedValue, changedValue);

        int changedValueForZeroDigit = elementaryTraining.zeroEvenNumber(20);
        int expectedValueForZeroDigit = 0;
        assertEquals(expectedValueForZeroDigit, changedValueForZeroDigit);
    }

    @Test
    public void zeroEvenNumber_oneDigitNumber() {
        int changedValue = elementaryTraining.zeroEvenNumber(5);
        int expectedValue = 5;
        assertEquals(expectedValue, changedValue);
    }
}