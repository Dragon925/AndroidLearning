package com.github.dragon925.androidlearning;

import com.github.dragon925.androidlearning.training.StringsTraining;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class StringsTrainingTest {

    private static StringsTraining stringsTraining;

    @BeforeClass
    public static void init() {
        stringsTraining = new StringsTraining();
    }

    @Test
    public void capitalReverse_isValidEng() {
        String text = "Hello World_123";
        String verificationText = "hELLO wORLD_123";
        String capitalReversedText = stringsTraining.capitalReverse(text);
        assertEquals(capitalReversedText, verificationText);
    }

    @Test
    public void capitalReverse_isValidRus() {
        String text = "Здравствуй, Мир!";
        String verificationText = "зДРАВСТВУЙ, мИР!";
        String capitalReversedText = stringsTraining.capitalReverse(text);
        assertEquals(capitalReversedText, verificationText);
    }

    @Test
    public void getOddCharacterString_emptyParam() {
        String text = "";
        String verificationResult = "";
        String actualResult = stringsTraining.getOddCharacterString(text);
        assertEquals(actualResult, verificationResult);
    }

    @Test
    public void getOddCharacterString_notEmptyParam() {
        String text = "Hello World!";
        String verificationResult = "el ol!";
        String actualResult = stringsTraining.getOddCharacterString(text);
        assertEquals(actualResult, verificationResult);
    }

    @Test
    public void getArrayLastSymbol_notEmptyParam_notEmptyResult() {
        String text = "Hello Hell";
        int[] verificationResult = {2, 3, 8};
        int[] actualResult = stringsTraining.getArrayLastSymbol(text);
        assertArrayEquals(actualResult, verificationResult);
    }

    @Test
    public void getArrayLastSymbol_notEmptyParam_emptyResult() {
        String text = "Hello World!";
        int[] verificationResult = {};
        int[] actualResult = stringsTraining.getArrayLastSymbol(text);
        assertArrayEquals(actualResult, verificationResult);
    }

    @Test
    public void getArrayLastSymbol_emptyParam() {
        String text = "";
        int[] verificationResult = {};
        int[] actualResult = stringsTraining.getArrayLastSymbol(text);
        assertArrayEquals(actualResult, verificationResult);
    }

    @Test
    public void getNumbersCount_emptyParam() {
        String text = "";
        int verificationResult = 0;
        int actualResult = stringsTraining.getNumbersCount(text);
        assertEquals(actualResult, verificationResult);
    }

    @Test
    public void getNumbersCount_notEmptyParam_notExistNumbers() {
        String text = "Hello world";
        int verificationResult = 0;
        int actualResult = stringsTraining.getNumbersCount(text);
        assertEquals(actualResult, verificationResult);
    }

    @Test
    public void getNumbersCount_notEmptyParam_existNumbers() {
        String text = "He110 w0r1d";
        int verificationResult = 5;
        int actualResult = stringsTraining.getNumbersCount(text);
        assertEquals(actualResult, verificationResult);
    }

    @Test
    public void replaceAllNumbers_emptyText() {
        String text = "";
        String verificationResult = "";
        String actualResult = stringsTraining.replaceAllNumbers(text);
        assertEquals(actualResult, verificationResult);
    }

    @Test
    public void replaceAllNumbers_notEmptyText_numbersDoNotExist() {
        String text = "Hello World!";
        String verificationResult = "Hello World!";
        String actualResult = stringsTraining.replaceAllNumbers(text);
        assertEquals(actualResult, verificationResult);
    }

    @Test
    public void replaceAllNumbers_notEmptyText_existNumbers() {
        String text = "He110 w0r1d";
        String verificationResult = "Heoneonezero wzeroroned";
        String actualResult = stringsTraining.replaceAllNumbers(text);
        assertEquals(actualResult, verificationResult);
    }
}
