package com.github.dragon925.androidlearning;

import static org.mockito.Mockito.reset;

import com.github.dragon925.androidlearning.rx.RxMaybeTraining;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.TestScheduler;

/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 20.11.18
 */
public class RxMaybeTrainingTest {


    private final RxMaybeTraining mRxMaybeTraining = Mockito.spy(new RxMaybeTraining());
    private TestScheduler mTestScheduler;

    @Before
    public void setUp() {
        reset(mRxMaybeTraining);
        mTestScheduler = new TestScheduler();
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> mTestScheduler);
    }

    @Test
    public void positiveOrEmpty_positiveValue() {
        TestObserver<Integer> testObserver = mRxMaybeTraining
                .positiveOrEmpty(1)
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(1);
    }

    @Test
    public void positiveOrEmpty_negativeValue() {
        TestObserver<Integer> testObserver = mRxMaybeTraining
                .positiveOrEmpty(-1)
                .test();

        testObserver.assertNoValues();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void positiveOrEmpty_positiveSingle() {
        TestObserver<Integer> testObserver = mRxMaybeTraining
                .positiveOrEmpty(Single.just(1).blockingGet())
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValues(1);
    }

    @Test
    public void positiveOrEmpty_negativeSingle() {
        TestObserver<Integer> testObserver = mRxMaybeTraining
                .positiveOrEmpty(Single.just(-1).blockingGet())
                .test();

        testObserver.assertNoValues();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void calculateSumOfValues_hasValues() {
        TestObserver<Integer> testObserver = mRxMaybeTraining
                .calculateSumOfValues(Observable.fromArray(1,2,3))
                .test();

        testObserver.assertValues(6);
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void calculateSumOfValues_noValues() {
        TestObserver<Integer> testObserver = mRxMaybeTraining
                .calculateSumOfValues(Observable.<Integer>empty())
                .test();

        testObserver.assertNoValues();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void leastOneElement_hasValues() {
        TestObserver<Integer> testObserver = mRxMaybeTraining
                .leastOneElement(Maybe.just(1), 2)
                .test();

        testObserver.assertValues(1);
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void leastOneElement_noValues() {
        TestObserver<Integer> testObserver = mRxMaybeTraining
                .leastOneElement(Maybe.<Integer>empty(), 2)
                .test();

        testObserver.assertValues(2);
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }
}