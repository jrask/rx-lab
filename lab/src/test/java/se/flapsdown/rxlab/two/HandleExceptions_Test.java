package se.flapsdown.rxlab.two;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static se.flapsdown.rxlab.util.DogStream.dogs;
import static se.flapsdown.rxlab.util.StreamUtil.subscribeQuiet;

public class HandleExceptions_Test {

    // Catch, Retries, Defaults etc

//https://github.com/ReactiveX/RxJava/wiki/Error-Handling-Operators#retryuntil


    @Test
    public void test_retry_when() {

        Observable<Long> source = Observable.interval(0, 1, TimeUnit.SECONDS)
            .flatMap(x -> {
                if (x >= 2) return Observable.error(new IOException("Something went wrong!"));
                else return Observable.just(x);
            });

        source.retryWhen(errors ->

            errors.map(error -> 1)

                // Count the number of errors.
                .scan((integer, integer2) -> integer + integer2)

                .doOnNext(errorCount -> System.out.println("No. of errors: " + errorCount))

                // Limit the maximum number of retries.
                .takeWhile(errorCount -> errorCount < 3)

                // Signal resubscribe event after some delay.
                .flatMapSingle(errorCount -> Single.timer(errorCount, TimeUnit.SECONDS))
        ).blockingSubscribe(
            x -> System.out.println("onNext: " + x),
            Throwable::printStackTrace,
            () -> System.out.println("onComplete"));


    }

    @Test
    public void test_return_other_dogname() {

        // Make this test successful returning a default dogname on error
        // It should print the dog name once
        dogs()
            .map(this::throwEx)
         //   .onErrorReturnItem("Buster")
            .doOnNext(System.out::println)
           // .doOnError(Throwable::printStackTrace)
            .subscribe(subscribeQuiet());
    }

    @Test
    public void test_return_other_dogname_for_each_failed() {

        // Make this test successful returning a default dogname for each dog name
        // it should print the default dog name once for each original dog name
        // Hint: Separate streams!

        dogs()
            //.flatMap(s ->
            //    Observable.just(s)
            //        .map(this::throwEx)
            //        .onErrorResumeNext(Observable.just("Buster")))
            .doOnNext(System.out::println)
            .subscribe();
    }


    public String throwEx(String s) {
        throw new RuntimeException();
    }

}
