package com.github.dragon925.androidlearning;


import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import com.github.dragon925.androidlearning.exceptions.ExpectedException;
import com.github.dragon925.androidlearning.rx.RxCreatingTraining;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.TestScheduler;

/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 13.11.18
 */
public class RxCreatingTrainingTest {

    private static final int VALUE_INT = 0;
    private static final String[] ARRAY_STRINGS = {"1", "2", "3"};
    private static final int DELAY = 100;
    private static final int PERIOD = 200;

    private final RxCreatingTraining mRxCreatingTraining = Mockito.spy(new RxCreatingTraining());
    private TestScheduler mTestScheduler;

    @Before
    public void setUp() {
        reset(mRxCreatingTraining);
        mTestScheduler = new TestScheduler();
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> mTestScheduler);
    }

    @Test
    public void valueToObservable() {
        TestObserver<Integer> testObserver = new TestObserver<>();
        mRxCreatingTraining.valueToObservable(VALUE_INT)
                .subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValue(VALUE_INT);
    }

    @Test
    public void arrayToObservable() {
        TestObserver<String> testObserver = new TestObserver<>();
        mRxCreatingTraining.arrayToObservable(ARRAY_STRINGS)
                .subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(ARRAY_STRINGS);
    }

    @Test
    public void expensiveMethodResult() {
        TestObserver<Integer> testObserver = new TestObserver<>();
        Observable<Integer> observable = mRxCreatingTraining.expensiveMethodResult();
        verify(mRxCreatingTraining, never()).expensiveMethod();
        observable.subscribe(testObserver);
        verify(mRxCreatingTraining).expensiveMethod();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValueCount(1);
    }

    @Test
    public void increasingSequenceWithDelays() {
        TestObserver<Long> testObserver = new TestObserver<>();
        mRxCreatingTraining.increasingSequenceWithDelays(DELAY, PERIOD)
                .subscribe(testObserver);

        testObserver.assertNoValues();

        mTestScheduler.triggerActions();

        testObserver.assertNoValues();

        mTestScheduler.advanceTimeBy(DELAY, TimeUnit.MILLISECONDS);

        testObserver.assertValue(0L);

        mTestScheduler.advanceTimeBy(PERIOD, TimeUnit.MILLISECONDS);

        testObserver.assertValues(0L, 1L);

        mTestScheduler.advanceTimeBy(PERIOD, TimeUnit.MILLISECONDS);

        testObserver.assertValues(0L, 1L, 2L);

        testObserver.dispose();

        testObserver.assertNoErrors();
        testObserver.assertNotComplete();
        testObserver.assertValueCount(3);
    }

    @Test
    public void delayedZero() {
        TestObserver<Long> testObserver = new TestObserver<>();
        mRxCreatingTraining.delayedZero(DELAY)
                .subscribe(testObserver);

        testObserver.assertNoValues();

        mTestScheduler.triggerActions();

        testObserver.assertNoValues();

        mTestScheduler.advanceTimeBy(DELAY - 1L, TimeUnit.MILLISECONDS);

        testObserver.assertNoValues();

        mTestScheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS);

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValue(0L);
    }

    @Test
    public void combinationExpensiveMethods_withError() {
        TestObserver<Integer> testObserver = new TestObserver<>();
        Observable<Integer> observable = mRxCreatingTraining.combinationExpensiveMethods(true);

        verify(mRxCreatingTraining, never()).expensiveMethod();
        verify(mRxCreatingTraining, never()).alternativeExpensiveMethod();
        verify(mRxCreatingTraining, never()).unstableMethod(anyBoolean());

        observable.subscribe(testObserver);

        verify(mRxCreatingTraining).expensiveMethod();
        verify(mRxCreatingTraining).alternativeExpensiveMethod();
        verify(mRxCreatingTraining).unstableMethod(anyBoolean());

        testObserver.assertValueCount(2);
        testObserver.assertError(ExpectedException.class);
        testObserver.assertNotComplete();
    }

    @Test
    public void combinationExpensiveMethods_withoutError() {
        TestObserver<Integer> testObserver = new TestObserver<>();
        Observable<Integer> observable = mRxCreatingTraining.combinationExpensiveMethods(false);

        verify(mRxCreatingTraining, never()).expensiveMethod();
        verify(mRxCreatingTraining, never()).alternativeExpensiveMethod();
        verify(mRxCreatingTraining, never()).unstableMethod(anyBoolean());

        observable.subscribe(testObserver);

        verify(mRxCreatingTraining).expensiveMethod();
        verify(mRxCreatingTraining).alternativeExpensiveMethod();
        verify(mRxCreatingTraining).unstableMethod(anyBoolean());

        testObserver.assertValueCount(3);
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void withoutAnyEvents() {
        TestObserver<Integer> testObserver = new TestObserver<>();
        mRxCreatingTraining.withoutAnyEvents()
                .subscribe(testObserver);

        mTestScheduler.triggerActions();

        testObserver.assertNoErrors();
        testObserver.assertNotComplete();
        testObserver.assertNoValues();
    }

    @Test
    public void onlyComplete() {
        TestObserver<Integer> testObserver = new TestObserver<>();
        mRxCreatingTraining.onlyComplete()
                .subscribe(testObserver);

        mTestScheduler.triggerActions();

        testObserver.assertNoErrors();
        testObserver.assertNoValues();
        testObserver.assertComplete();
    }

    @Test
    public void onlyError() {
        TestObserver<Integer> testObserver = new TestObserver<>();
        mRxCreatingTraining.onlyError()
                .subscribe(testObserver);

        mTestScheduler.triggerActions();

        testObserver.assertNotComplete();
        testObserver.assertNoValues();
        testObserver.assertError(ExpectedException.class);
    }
}