package com.github.dragon925.androidlearning.rx;


import com.github.dragon925.androidlearning.exceptions.NotImplementedException;
import io.reactivex.rxjava3.core.Observable;
import java.util.Collections;
import java.util.List;


/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 15.11.18
 */
public class RxCombiningTraining {

    /* Тренировочные методы */

    /**
     * Суммирование элементов двух последовательностей.
     *
     * @param integerObservable1 {@link Observable} с произвольным количеством рандомных чисел
     * @param integerObservable2 {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит числа, где i-й элемент равен сумме i-го элемента
     * {@code integerObservable1} и i-го элемента {@code integerObservable2}. Если в одной из
     * входящих последовательностей сработает  {@code onComplete или  {@code onError} то и в
     * результирующей последовательности тоже сработает этот метод.
     */
    public Observable<Integer> summation(Observable<Integer> integerObservable1, Observable<Integer> integerObservable2) {
        throw new NotImplementedException();
    }

    /**
     * Поиск элементов по выбранной строке и категории
     *
     * @param searchObservable   Последовательность поисковых строк (в приложении может быть
     *                           введёнными строками в поисковую строку)
     * @param categoryObservable Последовательность категорий, которые необходимо отобразить
     * @return {@link Observable}  который эммитит списки элементов, с учётом поисковой строки из
     * {@code searchObservable} и выбранной категории из {@code categoryObservable}
     * @see #searchItems(String searchString, int categoryId)
     */
    public Observable<List<String>> requestItems(Observable<String> searchObservable,
                                                 Observable<Integer> categoryObservable) {
        throw new NotImplementedException();
    }

    /**
     * Композиция потоков, обращение с несколькими объектами {@link Observable}, как с одним.
     *
     * @param intObservable1 {@link Observable} с произвольным количеством рандомных чисел
     * @param intObservable2 {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит элементы из {@code intObservable1} и
     * {@code intObservable2}
     */
    public Observable<Integer> composition(Observable<Integer> intObservable1,
                                           Observable<Integer> intObservable2) {
        throw new NotImplementedException();
    }

    /**
     * Дополнительный элемент перед всеми элементами потока
     *
     * @param firstItem     Первый элемент, который необходимо добавить
     * @param intObservable {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который сначала эммитит элемент {@code firstItem}, а потом все
     * элементы последовательности {@code intObservable}
     */
    public Observable<Integer> additionalFirstItem(int firstItem, Observable<Integer> intObservable) {
        throw new NotImplementedException();
    }

    /* Вспомогательные методы */

    /**
     * Поиск по строкам и вывод
     *
     * @param searchString Строка поиска
     * @param categoryId   Категория
     * @return вывод некоторых элементов с учётом поисковой строки и выбранной категории
     */
    public List<String> searchItems(String searchString, int categoryId) {
        // Поиск и выборка
        return Collections.emptyList();
    }

}
