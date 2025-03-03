package com.github.dragon925.androidlearning;


import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import com.github.dragon925.androidlearning.exceptions.ExpectedException;
import com.github.dragon925.androidlearning.rx.RxCompletableTraining;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.TestScheduler;

/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 20.11.18
 */
public class RxCompletableTrainingTest {

    private final RxCompletableTraining mRxCompletableTraining = Mockito.spy(new RxCompletableTraining());
    private TestScheduler mTestScheduler;

    @Before
    public void setUp() {
        reset(mRxCompletableTraining);
        mTestScheduler = new TestScheduler();
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> mTestScheduler);
    }

    @Test
    public void callFunction() {
        TestObserver<Void> testObserver = mRxCompletableTraining
                .callFunction()
                .test();

        testObserver.assertComplete();
        testObserver.assertNoErrors();

        verify(mRxCompletableTraining).callFunction();
    }

    @Test
    public void completeWhenTrue_true() {

        TestObserver<Void> testObserver = mRxCompletableTraining
                .completeWhenTrue(Single.just(true))
                .test();

        testObserver.assertComplete();
        testObserver.assertNoErrors();
    }

    @Test
    public void completeWhenTrue_false() {

        TestObserver<Void> testObserver = mRxCompletableTraining
                .completeWhenTrue(Single.just(false))
                .test();

        testObserver.assertNotComplete();
        testObserver.assertError(ExpectedException.class);
    }
}