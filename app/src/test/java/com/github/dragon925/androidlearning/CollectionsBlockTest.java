package com.github.dragon925.androidlearning;

import com.github.dragon925.androidlearning.collections.CollectionsBlock;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;


public class CollectionsBlockTest {

    private static CollectionsBlock<Integer> collectionsBlock;

    @BeforeClass
    public static void init() {
        collectionsBlock = new CollectionsBlock<>();
    }

    @Test
    public void collectionTask0_emptyLists() {
        List<Integer> averageValue = collectionsBlock.collectionTask0(
                Collections.<Integer>emptyList(),
                Collections.<Integer>emptyList()
        );
        List<Integer> expectedValue = Collections.emptyList();
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask0_notEmptyFirstList() {
        List<Integer> firstList = Arrays.asList(3, 2, 1);
        List<Integer> secondList = Collections.emptyList();
        List<Integer> averageValue = collectionsBlock.collectionTask0(firstList, secondList);
        List<Integer> expectedValue = Arrays.asList(3, 2, 1);
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask0_notEmptySecondList() {
        List<Integer> secondList = Arrays.asList(3, 2, 1);
        List<Integer> firstList = Collections.emptyList();
        List<Integer> averageValue = collectionsBlock.collectionTask0(firstList, secondList);
        List<Integer> expectedValue = Arrays.asList(3, 2, 1);
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask0_notEmptyLists() {
        List<Integer> secondList = Arrays.asList(3, 2, 1);
        List<Integer> firstList = Arrays.asList(-4, -5, -6);
        List<Integer> averageValue = collectionsBlock.collectionTask0(firstList, secondList);
        List<Integer> expectedValue = Arrays.asList(3, 2, 1, -4, -5, -6);
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask0_mergeNotEmptyLists() {
        List<Integer> secondList = Arrays.asList(3, 1, -5);
        List<Integer> firstList = Arrays.asList(2, -4, -6);
        List<Integer> averageValue = collectionsBlock.collectionTask0(firstList, secondList);
        List<Integer> expectedValue = Arrays.asList(3, 2, 1, -4, -5, -6);
        assertEquals(expectedValue, averageValue);
    }

    @Test(expected = NullPointerException.class)
    public void collectionTask0_throwsNPE() {
        collectionsBlock.collectionTask0(null, null);
    }

    @Test
    public void collectionTask1_emptyList() {
        List<Integer> inputList = Collections.emptyList();
        List<Integer> averageValue = collectionsBlock.collectionTask1(inputList);
        List<Integer> expectedValue = Collections.emptyList();
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask1_notEmptyList() {
        List<Integer> inputList = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> averageValue = collectionsBlock.collectionTask1(inputList);
        List<Integer> expectedValue = Arrays.asList(1, 2, 1, 3, 1, 2, 4, 1, 2, 3, 5, 1, 2, 3, 4);
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask2_emptyFirstList() {
        List<Integer> firstList = Collections.emptyList();
        List<Integer> secondList = Arrays.asList(3, 2, 1);
        boolean averageValue = collectionsBlock.collectionTask2(firstList, secondList);
        boolean expectedValue = false;
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask2_emptyLists() {
        List<Integer> firstList = Collections.emptyList();
        List<Integer> secondList = Collections.emptyList();
        boolean averageValue = collectionsBlock.collectionTask2(firstList, secondList);
        boolean expectedValue = true;
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask2_setsAreEqual() {
        List<Integer> firstList = Arrays.asList(4, 1, 3, 2);
        List<Integer> secondList = Arrays.asList(3, 2, 1, 4, 2, 1);
        boolean averageValue = collectionsBlock.collectionTask2(firstList, secondList);
        boolean expectedValue = true;
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask2_setsAreNotEqual() {
        List<Integer> firstList = Arrays.asList(4, 1, 3, 2, 5);
        List<Integer> secondList = Arrays.asList(3, 2, 1, 4, 2, 1);
        boolean averageValue = collectionsBlock.collectionTask2(firstList, secondList);
        boolean expectedValue = false;
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask3_emptyList() {
        List<Integer> inputList = Collections.emptyList();
        int n = 3;
        List<Integer> averageValue = collectionsBlock.collectionTask3(inputList, n);
        List<Integer> expectedValue = Collections.emptyList();
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask3_notEmptyList_rightShift() {
        List<Integer> inputList = Arrays.asList(4, 3, 2, 1);
        int n = 3;
        List<Integer> averageValue = collectionsBlock.collectionTask3(inputList, n);
        List<Integer> expectedValue = Arrays.asList(3, 2, 1, 4);
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask3_notEmptyList_leftShift() {
        List<Integer> inputList = Arrays.asList(4, 3, 2, 1);
        int n = -3;
        List<Integer> averageValue = collectionsBlock.collectionTask3(inputList, n);
        List<Integer> expectedValue = Arrays.asList(1, 4, 3, 2);
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask3_notEmptyList_largeLeftShift() {
        List<Integer> inputList = Arrays.asList(4, 3, 2, 1);
        int n = -2_000_000_003;
        List<Integer> averageValue = collectionsBlock.collectionTask3(inputList, n);
        List<Integer> expectedValue = Arrays.asList(1, 4, 3, 2);
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask4_emptyList() {
        List<String> inputList = Collections.emptyList();
        String a = "Hello";
        String b = "Hi";
        List<String> averageValue = collectionsBlock.collectionTask4(inputList, a, b);
        List<String> expectedValue = Collections.emptyList();
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask4_notEmptyList() {
        List<String> inputList = Arrays.asList("Hello", ",", " ", "world");
        String a = "Hello";
        String b = "Hi";
        List<String> averageValue = collectionsBlock.collectionTask4(inputList, a, b);
        List<String> expectedValue = Arrays.asList("Hi", ",", " ", "world");
        assertEquals(expectedValue, averageValue);
    }

    @Test
    public void collectionTask4_notEmptyListMultipleReplace() {
        List<String> inputList = Arrays.asList("Hello", "Hello", ",", " ", "world", "Hello");
        String a = "Hello";
        String b = "Hi";
        List<String> averageValue = collectionsBlock.collectionTask4(inputList, a, b);
        List<String> expectedValue = Arrays.asList("Hi", "Hi", ",", " ", "world", "Hi");
        assertEquals(expectedValue, averageValue);
    }

    @Test(expected = NullPointerException.class)
    public void collectionTask4_checkNull() {
        List<String> inputList = null;
        String a = "Hello";
        String b = "Hi";
        List<String> averageValue = collectionsBlock.collectionTask4(inputList, a, b);
        List<String> expectedValue = Arrays.asList("Hi", ",", " ", "world");
        assertEquals(expectedValue, averageValue);
    }

    @Test(expected = NullPointerException.class)
    public void collectionTask4_checkNullB() {
        List<String> inputList = Collections.emptyList();
        String a = "Hello";
        collectionsBlock.collectionTask4(inputList, a, null);
    }
}
