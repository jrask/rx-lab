package se.flapsdown.rxlab.three;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Before;
import org.junit.Test;
import se.flapsdown.rxlab.LongRunningTask;
import se.flapsdown.rxlab.util.Streams;

import static se.flapsdown.rxlab.util.DogStream.dogs;

public class ConcurrencyTest {



    Streams.PrintObserver observer = new Streams.PrintObserver();

    @Before
    public void prepare() {
        observer = new Streams.PrintObserver();
    }


    @Test
    public void test_subscribeon_and_observeon() {
        // Fix so that, When this runs,
        // 1. messages generate in the create() method, should be performed
        //    on the io thread
        // 2. other messages should be executed on the computation thread

        Observable.create(observableEmitter -> {
                Streams.print("Starting emitting data");
                observableEmitter.onNext("Johan");
                observableEmitter.onNext("Rask");
                observableEmitter.onComplete();
            })
            //.observeOn(Schedulers.computation())
            .doOnNext(Streams::print)  // computation
            //.subscribeOn(Schedulers.io())
            .subscribe(observer);      // computation

        observer.awaitCompletion();
    }

    @Test
    public void test_run_tasks_in_parallel() {

        // Instead of sequentially, run tasks in parallel.
        // Aka, all invocations to longRunningTask.toUpperCase should be performed
        // at the same time

//        Expected result (order of names is non deterministic though)
//
//        16:12:14.915 [SomeThreadPool-3] INFO rx-lab - sleep 1000
//        16:12:14.915 [SomeThreadPool-1] INFO rx-lab - sleep 1000
//        16:12:14.915 [SomeThreadPool-5] INFO rx-lab - sleep 1000
//        16:12:14.915 [SomeThreadPool-2] INFO rx-lab - sleep 1000
//        16:12:14.915 [SomeThreadPool-4] INFO rx-lab - sleep 1000
//        16:12:15.921 [SomeThreadPool-5] INFO rx-lab - Subscriber.onNext() ROCKY
//        16:12:15.924 [SomeThreadPool-5] INFO rx-lab - Subscriber.onNext() BELLA
//        16:12:15.925 [SomeThreadPool-5] INFO rx-lab - Subscriber.onNext() BUSTER
//        16:12:15.925 [SomeThreadPool-5] INFO rx-lab - Subscriber.onNext() PEPSI
//        16:12:15.925 [SomeThreadPool-5] INFO rx-lab - Subscriber.onNext() DAISY
//        16:12:15.926 [SomeThreadPool-5] INFO rx-lab - Subscriber.onComplete()

        final LongRunningTask longRunningTask = new LongRunningTask(1000);

        dogs()
            //.map(longRunningTask::toUpperCase)
            .flatMap(name ->
                Observable.fromCallable(() -> longRunningTask.toUpperCase(name))
                    .subscribeOn(Schedulers.io()))
            .subscribe(observer);

        observer.awaitCompletion();

    }
}
