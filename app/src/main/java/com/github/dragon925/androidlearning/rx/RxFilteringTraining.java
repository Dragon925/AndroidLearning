package com.github.dragon925.androidlearning.rx;


import com.github.dragon925.androidlearning.exceptions.NotImplementedException;

import io.reactivex.rxjava3.core.Observable;

/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 14.11.18
 */
public class RxFilteringTraining {

    /* Тренировочные методы */

    /**
     * Только положительные числа
     *
     * @param intValues {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} только с положительными числами, отрицательные должны быть
     * отфильтрованы
     */
    public Observable<Integer> onlyPositiveNumbers(Observable<Integer> intValues) {
        throw new NotImplementedException();
    }

    /**
     * Эммит только последних значений
     *
     * @param count     Количество последних элементов, которые нужно эммитить
     * @param intValues {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит последние значения
     */
    public Observable<Integer> onlyLastValues(int count, Observable<Integer> intValues) {
        throw new NotImplementedException();
    }

    /**
     * Эммит только первых значений
     *
     * @param count     Количество первых элементов, которые нужно эммитить
     * @param intValues {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит первые значения
     */
    public Observable<Integer> onlyFirstValues(int count, Observable<Integer> intValues) {
        throw new NotImplementedException();
    }

    /**
     * Отфильтровать первые значения
     *
     * @param count     Количество первых элементов, которые нужно отфильтровать
     * @param intValues {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит значения из {@code intValues} кроме первых
     * {@code count} значений
     */
    public Observable<Integer> ignoreFirstValues(int count, Observable<Integer> intValues) {
        throw new NotImplementedException();
    }

    /**
     * Только последний элемент из всех элементов во временном периоде
     *
     * @param periodMills Период в миллисекундах, за который необходимо произвести выборку
     *                    последнего элемента
     * @param intValues   {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит максимум 1 значения за интервал
     * {@code periodMills}
     */
    public Observable<Integer> onlyLastPerInterval(int periodMills, Observable<Integer> intValues) {
        throw new NotImplementedException();
    }

    /**
     * Ошибка при длительном ожидании элементов
     *
     * @param timeMills Время ожидания в миллисекундах
     * @param intValues {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит значения {@code intValues}, или выдаёт ошибку,
     * если время ожидания превышает {@code timeMills}
     */
    public Observable<Integer> errorIfLongWait(int timeMills, Observable<Integer> intValues) {
        throw new NotImplementedException();
    }

    /**
     * Значения без повторений
     *
     * @param intValues {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит значения {@code intValues}, но без повторяющихся
     * значений
     */
    public Observable<Integer> ignoreDuplicates(Observable<Integer> intValues) {
        throw new NotImplementedException();
    }

    /**
     * Игноритуются повторяющиеся элементы, которые идут подряд
     *
     * @param intValues {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит значения {@code intValues}, но если новое значение
     * повторяет предыдущее, оно пропускается
     */
    public Observable<Integer> onlyChangedValues(Observable<Integer> intValues) {
        throw new NotImplementedException();
    }

}
