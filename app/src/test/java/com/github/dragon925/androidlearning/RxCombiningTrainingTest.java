package com.github.dragon925.androidlearning;


import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.dragon925.androidlearning.rx.RxCombiningTraining;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.TestScheduler;

/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 15.11.18
 */
public class RxCombiningTrainingTest {

    private final RxCombiningTraining mRxCombiningTraining = Mockito.spy(new RxCombiningTraining());
    private TestScheduler mTestScheduler;

    @Before
    public void setUp() {
        reset(mRxCombiningTraining);
        mTestScheduler = new TestScheduler();
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> mTestScheduler);
    }

    @Test
    public void summation() {
        TestObserver<Integer> testObserver = mRxCombiningTraining.summation(
                Observable.fromArray(1, 2, 3, 4, 5),
                Observable.fromArray(10, 20, 30, 40, 50)
        )
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(11, 22, 33, 44, 55);
    }

    @Test
    public void requestItems() {
        final long period = 1;
        final TimeUnit unit = TimeUnit.MINUTES;
        final String[] searchStrings = {"a", "ab", "abc"};
        final Integer[] selectedCategories = {1, 2, 3, 4, 5};

        TestObserver<List<String>> testObserver = mRxCombiningTraining.requestItems(
                Observable.interval(period, period * 2, unit)
                        .take(searchStrings.length)
                        .map(aLong -> searchStrings[aLong.intValue()]),
                Observable.interval(0, period * 2, unit)
                        .take(selectedCategories.length)
                        .map(aLong -> selectedCategories[aLong.intValue()])
        )
                .test();

        mTestScheduler.advanceTimeBy(period, unit);
        testObserver.assertValueCount(1);
        verify(mRxCombiningTraining).searchItems(eq(searchStrings[0]), eq(selectedCategories[0]));
        reset(mRxCombiningTraining);

        mTestScheduler.advanceTimeBy(period, unit);
        testObserver.assertValueCount(2);
        verify(mRxCombiningTraining).searchItems(eq(searchStrings[0]), eq(selectedCategories[1]));
        reset(mRxCombiningTraining);

        mTestScheduler.advanceTimeBy(period, unit);
        testObserver.assertValueCount(3);
        verify(mRxCombiningTraining).searchItems(eq(searchStrings[1]), eq(selectedCategories[1]));
        reset(mRxCombiningTraining);

        mTestScheduler.advanceTimeBy(period, unit);
        testObserver.assertValueCount(4);
        verify(mRxCombiningTraining).searchItems(eq(searchStrings[1]), eq(selectedCategories[2]));
        reset(mRxCombiningTraining);

        mTestScheduler.advanceTimeBy(period, unit);
        testObserver.assertValueCount(5);
        verify(mRxCombiningTraining).searchItems(eq(searchStrings[2]), eq(selectedCategories[2]));
        reset(mRxCombiningTraining);

        mTestScheduler.advanceTimeBy(period, unit);
        testObserver.assertValueCount(6);
        verify(mRxCombiningTraining).searchItems(eq(searchStrings[2]), eq(selectedCategories[3]));
        reset(mRxCombiningTraining);

        mTestScheduler.advanceTimeBy(period, unit);
        testObserver.assertValueCount(6);
        verify(mRxCombiningTraining, times(0)).searchItems(anyString(), anyInt());
        reset(mRxCombiningTraining);

        mTestScheduler.advanceTimeBy(period, unit);
        testObserver.assertValueCount(7);
        verify(mRxCombiningTraining).searchItems(eq(searchStrings[2]), eq(selectedCategories[4]));
        reset(mRxCombiningTraining);

        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void composition() {
        TestObserver<Integer> testObserver = mRxCombiningTraining.composition(
                Observable.interval(0, 2, TimeUnit.MINUTES)
                        .take(3)
                        .map(aLong -> aLong.intValue() * 2),
                Observable.interval(1, 2, TimeUnit.MINUTES)
                        .take(3)
                        .map(aLong -> aLong.intValue() * 2 + 1))
                .test();

        mTestScheduler.advanceTimeBy(6, TimeUnit.MINUTES);
        testObserver.assertValues(0, 1, 2, 3, 4, 5);
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void additionalFirstItem() {
        TestObserver<Integer> testObserver = mRxCombiningTraining
                .additionalFirstItem(0, Observable.fromArray(1, 2, 3))
                .test();
        testObserver.assertValues(0, 1, 2, 3);
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

}