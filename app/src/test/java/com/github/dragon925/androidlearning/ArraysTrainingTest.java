package com.github.dragon925.androidlearning;

import com.github.dragon925.androidlearning.training.ArraysTraining;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class ArraysTrainingTest {

    private static ArraysTraining arraysTraining;

    @BeforeClass
    public static void init() {
        arraysTraining = new ArraysTraining();
    }

    @Test
    public void sort_twelveItems() {
        int[] array = {100, 15, 21, 45, 30, 57, 48, 0, -2, 4, 80, -11};
        int[] arraySorted = {-11, -2, 0, 4, 15, 21, 30, 45, 48, 57, 80, 100};
        int[] sortedArrayFunc = arraysTraining.sort(array);
        assertArrayEquals(arraySorted, sortedArrayFunc);
    }

    @Test
    public void sort_zeroItems() {
        int[] array = {};
        int[] arraySorted = {};
        int[] sortedArrayFunc = arraysTraining.sort(array);
        assertArrayEquals(arraySorted, sortedArrayFunc);
    }

    @Test
    public void sort_oneItems() {
        int[] array = {5};
        int[] arraySorted = {5};
        int[] sortedArrayFunc = arraysTraining.sort(array);
        assertArrayEquals(arraySorted, sortedArrayFunc);
    }

    @Test
    public void maxValue_emptyItems() {
        int expectedResultIfValuesIsEmpty = 0;
        int result = arraysTraining.maxValue();
        assertEquals(expectedResultIfValuesIsEmpty, result);
    }

    @Test
    public void maxValue_oneItems() {
        int value = 10;
        int result = arraysTraining.maxValue(value);
        assertEquals(value, result);
    }

    @Test
    public void maxValue_tenItems() {
        int[] values = {-2, 7, 10, -20};
        int maxValue = 10;
        int result = arraysTraining.maxValue(values);
        assertEquals(maxValue, result);
    }

    @Test
    public void reverse_emptyItems() {
        int[] values = {};
        int[] expectedResult = {};
        int[] actualResult = arraysTraining.reverse(values);
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void reverse_tenItems() {
        int[] values = {-2, 7, 10, -20};
        int[] expectedResult = {-20, 10, 7, -2};
        int[] actualResult = arraysTraining.reverse(values);
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void fibonacciNumbers_emptyItems() {
        int value = 0;
        int[] expectedResult = {};
        int[] actualResult = arraysTraining.fibonacciNumbers(value);
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test(timeout = 100)
    public void fibonacciNumbers_fortyItems() {
        int value = 40;
        int[] expectedResult = {
                1, 1, 2, 3, 5, 8, 13, 21, 34, 55,
                89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765,
                10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040,
                1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169, 63245986, 102334155};
        int[] actualResult = arraysTraining.fibonacciNumbers(value);
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void fibonacciNumbers_negativeValue() {
        int value = -10;
        int[] expectedResult = {};
        int[] actualResult = arraysTraining.fibonacciNumbers(value);
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void maxCountSymbol_emptyArray() {
        int[] value = {};
        int expectedResult = 0;
        int actualResult = arraysTraining.maxCountSymbol(value);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void maxCountSymbol_notEmptyArray() {
        int[] value = {3, 1, 4, 2, -9, 1, 4, 3, 1, 4, 1};
        int expectedResult = 4;
        int actualResult = arraysTraining.maxCountSymbol(value);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void maxCountSymbol_allDifferent() {
        int[] value = {3, 1, 4, 2};
        int expectedResult = 1;
        int actualResult = arraysTraining.maxCountSymbol(value);
        assertEquals(expectedResult, actualResult);
    }
}
