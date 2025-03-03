package com.github.dragon925.androidlearning;


import static org.mockito.Mockito.reset;

import com.github.dragon925.androidlearning.rx.RxFilteringTraining;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.TestScheduler;

/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 14.11.18
 */
public class RxFilteringTrainingTest {

    private final RxFilteringTraining mRxFilteringTraining = Mockito.spy(new RxFilteringTraining());
    private TestScheduler mTestScheduler;

    @Before
    public void setUp() {
        reset(mRxFilteringTraining);
        mTestScheduler = new TestScheduler();
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> mTestScheduler);
    }

    @Test
    public void onlyPositiveNumbers() {
        TestObserver<Integer> testObserver = mRxFilteringTraining
                .onlyPositiveNumbers(Observable.fromArray(-20, 0, Integer.MIN_VALUE, 10, Integer.MAX_VALUE))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(10, Integer.MAX_VALUE);
    }

    @Test
    public void onlyLastValues_countMoreThenValues() {
        TestObserver<Integer> testObserver = mRxFilteringTraining
                .onlyLastValues(100, Observable.fromArray(1, 2, 3, 4, 5))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(1, 2, 3, 4, 5);

    }

    @Test
    public void onlyLastValues_countLessThenValues() {
        TestObserver<Integer> testObserver = mRxFilteringTraining
                .onlyLastValues(2, Observable.fromArray(1, 2, 3, 4, 5))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(4, 5);
    }

    @Test
    public void onlyFirstValues_countMoreThenValues() {
        TestObserver<Integer> testObserver = mRxFilteringTraining
                .onlyFirstValues(100, Observable.fromArray(1, 2, 3, 4, 5))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(1, 2, 3, 4, 5);
    }

    @Test
    public void onlyFirstValues_countLessThenValues() {
        TestObserver<Integer> testObserver = mRxFilteringTraining
                .onlyFirstValues(2, Observable.fromArray(1, 2, 3, 4, 5))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(1, 2);
    }

    @Test
    public void ignoreFirstValues_countMoreThenValues() {
        TestObserver<Integer> testObserver = mRxFilteringTraining
                .ignoreFirstValues(100, Observable.fromArray(1, 2, 3, 4, 5))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertNoValues();
    }

    @Test
    public void ignoreFirstValues_countLessThenValues() {
        TestObserver<Integer> testObserver = mRxFilteringTraining
                .ignoreFirstValues(2, Observable.fromArray(1, 2, 3, 4, 5))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(3, 4, 5);
    }

    @Test
    public void onlyLastPerInterval() {
        final int periodMills = 500;
        TestObserver<Integer> testObserver = mRxFilteringTraining
                .onlyLastPerInterval(periodMills, Observable.create(emitter -> {
                    emitter.onNext(1);
                    mTestScheduler.advanceTimeBy(periodMills, TimeUnit.MILLISECONDS);
                    emitter.onNext(2);
                    emitter.onNext(3);
                    emitter.onNext(4);
                    mTestScheduler.advanceTimeBy(periodMills, TimeUnit.MILLISECONDS);
                    emitter.onNext(5);
                    mTestScheduler.advanceTimeBy(periodMills, TimeUnit.MILLISECONDS);
                    mTestScheduler.advanceTimeBy(periodMills, TimeUnit.MILLISECONDS);
                    emitter.onNext(6);
                    emitter.onNext(7);
                    emitter.onComplete();
                }))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(1, 4, 5);
    }

    @Test
    public void errorIfLongWait_withoutError() {
        final int periodMills = 500;
        TestObserver<Integer> testObserver = mRxFilteringTraining
                .errorIfLongWait(periodMills, Observable.create(emitter -> {
                    emitter.onNext(1);
                    mTestScheduler.advanceTimeBy(periodMills - 1, TimeUnit.MILLISECONDS);
                    emitter.onNext(2);
                    emitter.onNext(3);
                    emitter.onNext(4);
                    mTestScheduler.advanceTimeBy(periodMills - 1, TimeUnit.MILLISECONDS);
                    emitter.onNext(5);
                    mTestScheduler.advanceTimeBy(periodMills - 1, TimeUnit.MILLISECONDS);
                    emitter.onNext(6);
                    emitter.onNext(7);
                    emitter.onComplete();
                }))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(1, 2, 3, 4, 5, 6, 7);
    }

    @Test
    public void errorIfLongWait_withError() {
        final int periodMills = 500;
        TestObserver<Integer> testObserver = mRxFilteringTraining
                .errorIfLongWait(periodMills, Observable.create(emitter -> {
                    emitter.onNext(1);
                    mTestScheduler.advanceTimeBy(periodMills - 1, TimeUnit.MILLISECONDS);
                    emitter.onNext(2);
                    emitter.onNext(3);
                    emitter.onNext(4);
                    mTestScheduler.advanceTimeBy(periodMills - 1, TimeUnit.MILLISECONDS);
                    emitter.onNext(5);
                    mTestScheduler.advanceTimeBy(periodMills, TimeUnit.MILLISECONDS);
                    emitter.onNext(6);
                    emitter.onNext(7);
                    emitter.onComplete();
                }))
                .test();

        testObserver.assertNotComplete();
        testObserver.assertValues(1, 2, 3, 4, 5);
        testObserver.assertError(TimeoutException.class);
    }

    @Test
    public void ignoreDuplicates() {
        TestObserver<Integer> testObserver = mRxFilteringTraining
                .ignoreDuplicates(Observable.fromArray(2, 1, 2, 3, 6, 4, 5, 5, 6))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(2, 1, 3, 6, 4, 5);
    }

    @Test
    public void onlyChangedValues() {
        TestObserver<Integer> testObserver = mRxFilteringTraining
                .onlyChangedValues(Observable.fromArray(2, 1, 1, 2, 3, 6, 4, 5, 5, 6))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(2, 1, 2, 3, 6, 4, 5, 6);
    }
}