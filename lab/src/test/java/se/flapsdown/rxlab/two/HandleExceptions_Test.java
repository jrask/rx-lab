package se.flapsdown.rxlab.two;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static se.flapsdown.rxlab.util.DogStream.dogs;
import static se.flapsdown.rxlab.util.Streams.print;
import static se.flapsdown.rxlab.util.Streams.subscribeQuiet;

public class HandleExceptions_Test {

    // Catch, Retries, Defaults etc

//https://github.com/ReactiveX/RxJava/wiki/Error-Handling-Operators


    Observable<String> source =  Observable.interval(0, 1, TimeUnit.SECONDS)
        .zipWith(dogs(),(aLong, s) -> s)
        .flatMap(name -> {
            if (name.equals("Pepsi")) return Observable.error(new IOException("Something went wrong!"));
            if (name.equals("Rocky")) return Observable.error(new RuntimeException("Something went wrong!"));
            else return Observable.just(name);
        });


    @Test
    public void test_retry_with() {

        // use  Observable.retry(cnt) so that the assertion is correct
        // Run the test first to see what happens

        AtomicInteger cnt = new AtomicInteger(0);

        source
            .doOnNext(s -> cnt.incrementAndGet())
            .retry(3)
            .blockingSubscribe(
                x -> print("onNext: " + x),
                error -> print("onError: " + error.getMessage()));

        assertThat(cnt.get()).isEqualTo(8);
    }

    @Test
    public void test_retry_with_count() {

        // Use Observable.retry(retryCount, Throwable) -> ...)
        AtomicInteger cnt = new AtomicInteger(0);

        source
            .doOnNext(s -> cnt.incrementAndGet())
            .retry((retryCount, error) -> retryCount <= 3)
        .blockingSubscribe(
            x -> System.out.println("onNext: " + x),
            error -> System.err.println("onError: " + error.getMessage()));

        assertThat(cnt.get()).isEqualTo(8);
    }


    @Test
    public void test_retry_when() {

        // This is finished, try to understand what it does

        source.retryWhen(errors ->

            // Return this observable until you do not want to have more retries
            // Perform any operations as you might required
            errors

                // Use a count
                .map(error -> 1)

                // "Sum" the number of retries
                .scan((integer, integer2) -> integer + integer2)

                .doOnNext(errorCount -> System.out.println("No. of errors: " + errorCount))

                // Limit the maximum number of retries.
                .takeWhile(errorCount -> errorCount < 3)

                // Signal resubscribe event after some delay.
                .flatMap(errorCount -> Observable.timer(errorCount, TimeUnit.SECONDS))
        )
        .blockingSubscribe(
            x -> System.out.println("onNext: " + x),
            Throwable::printStackTrace,
            () -> System.out.println("onComplete"));

    }

    @Test
    public void test_return_other_dogname() {

        // Make this test successful returning a default dogname on error
        // It is good enough if the default dogname is printed once
        dogs()
            .map(this::checkDogName)
            .onErrorReturnItem("Buster")
            .doOnNext(System.out::println)
            .subscribe(subscribeQuiet());
    }

    @Test
    public void test_return_other_dogname_for_each_failed() {

        // Make this test successful returning a default dogname for each dog name
        // that fails after invoking checkDogName()
        // it should print the default dog name once for each original dog name
        // Hint: Separate streams! onErrorResume*



        dogs()
            .flatMap(s ->
                Observable.just(s)
                    .map(this::checkDogName)
                    .onErrorResumeNext(Observable.just("Buster")))
            .doOnNext(System.out::println)
            .subscribe();
    }


    public String checkDogName(String s) {
        throw new RuntimeException();
    }
}
