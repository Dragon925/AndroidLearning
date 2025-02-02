package com.github.dragon925.androidlearning.training;

/**
 * Набор тренингов по работе со строками в java.
 * <p>
 * Задания определены в комментариях методов.
 * <p>
 * Проверка может быть осуществлена запуском тестов.
 * <p>
 * Доступна проверка тестированием @see StringsTrainingTest.
 */
public class StringsTraining {

    /**
     * Метод по созданию строки,
     * состоящей из нечетных символов
     * входной строки в том же порядке
     * (нумерация символов идет с нуля)
     *
     * @param text строка для выборки
     * @return новая строка из нечетных
     * элементов строки text
     */
    public String getOddCharacterString(String text) {
        StringBuilder result = new StringBuilder(text.length() / 2);
        for (int i = 0; i < text.length(); i++) {
            if (i % 2 != 0) {
                result.append(text.charAt(i));
            }
        }

        return result.toString();
    }

    /**
     * Метод для определения количества
     * символов, идентичных последнему
     * в данной строке
     *
     * @param text строка для выборки
     * @return массив с номерами символов,
     * идентичных последнему. Если таких нет,
     * вернуть пустой массив
     */
    public int[] getArrayLastSymbol(String text) {
        if (text == null || text.isEmpty()) return new int[0];

        int[] symbolIndexes = new int[text.length() - 1];
        int lastSymbol = text.charAt(text.length() - 1);
        int count = 0;
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) == lastSymbol) {
                symbolIndexes[count] = i;
                count++;
            }
        }

        int[] result = new int[count];
        System.arraycopy(symbolIndexes, 0, result, 0, count);

        return result;
    }

    /**
     * Метод по получению количества
     * цифр в строке
     *
     * @param text строка для выборки
     * @return количество цифр в строке
     */
    public int getNumbersCount(String text) {
        String numbers = text.replaceAll("[^0-9]", "");
        return numbers.length();
    }

    /**
     * Дан текст. Заменить все цифры
     * соответствующими словами.
     *
     * @param text текст для поиска и замены
     * @return текст, где цифры заменены словами
     */
    public String replaceAllNumbers(String text) {
        String[] numbers = {
                "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
        };
        for (int i = 0; i < numbers.length; i++) {
            text = text.replace(String.valueOf(i), numbers[i]);
        }

        return text;
    }

    /**
     * Метод должен заменить заглавные буквы
     * на прописные, а прописные на заглавные
     *
     * @param text строка для изменения
     * @return измененная строка
     */
    public String capitalReverse(String text) {
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isUpperCase(chars[i])) {
                chars[i] = Character.toLowerCase(chars[i]);
            } else {
                chars[i] = Character.toUpperCase(chars[i]);
            }
        }

        return new String(chars);
    }

}
