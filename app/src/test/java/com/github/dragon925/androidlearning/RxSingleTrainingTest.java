package com.github.dragon925.androidlearning;


import static org.mockito.Mockito.reset;

import com.github.dragon925.androidlearning.exceptions.ExpectedException;
import com.github.dragon925.androidlearning.rx.RxSingleTraining;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.TestScheduler;

/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 20.11.18
 */
public class RxSingleTrainingTest {

    private final RxSingleTraining mRxSingleTraining = Mockito.spy(new RxSingleTraining());
    private TestScheduler mTestScheduler;

    @Before
    public void setUp() {
        reset(mRxSingleTraining);
        mTestScheduler = new TestScheduler();
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> mTestScheduler);
    }

    @Test
    public void onlyOneElement_success() {
        TestObserver<Integer> testObserver = mRxSingleTraining
                .onlyOneElement(1)
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(1);
    }

    @Test
    public void onlyOneElement_error() {
        TestObserver<Integer> testObserver = mRxSingleTraining
                .onlyOneElement(-1)
                .test();

        testObserver.assertNoValues();
        testObserver.assertError(ExpectedException.class);
    }

    @Test
    public void onlyOneElementOfSequence_success() {
        TestObserver<Integer> testObserver = mRxSingleTraining
                .onlyOneElementOfSequence(Observable.fromArray(1, 2, 3))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(1);
    }

    @Test
    public void onlyOneElementOfSequence_error() {
        TestObserver<Integer> testObserver = mRxSingleTraining
                .onlyOneElementOfSequence(Observable.<Integer>empty())
                .test();

        testObserver.assertNoValues();
        testObserver.assertError(NoSuchElementException.class);
    }

    @Test
    public void calculateSumOfValues() {
        TestObserver<Integer> testObserver = mRxSingleTraining
                .calculateSumOfValues(Observable.fromArray(1, 2, 3))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(6);
    }

    @Test
    public void collectionOfValues() {
        TestObserver<List<Integer>> testObserver = mRxSingleTraining
                .collectionOfValues(Observable.fromArray(1, 2, 3))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(Arrays.asList(1, 2, 3));
    }

    @Test
    public void allElementsIsPositive_true() {
        TestObserver<Boolean> testObserver = mRxSingleTraining
                .allElementsIsPositive(Observable.fromArray(1, 2, 3))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(true);
    }

    @Test
    public void allElementsIsPositive_false() {
        TestObserver<Boolean> testObserver = mRxSingleTraining
                .allElementsIsPositive(Observable.fromArray(1, 2, -3))
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(false);
    }
}