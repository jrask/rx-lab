package se.flapsdown.rx.blocking;

import io.reactivex.Observable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ExternalServiceExample {


    String urls = "https://www.google.se,https://www.aftonbladet.se";
    String urlsWithError = "https://www.google.se,https://www.aftonbladet.se,http://localhost";

    @Test
    public void test_get_single_url() {

        Optional<Integer> reduce = Arrays.asList(urls.split(","))
            .stream()
                .map(ExternalService::new)
                .map(ExternalService::get)
                .map(String::length)
                .reduce((integer, integer2) -> integer + integer2);

        System.out.println(reduce.get() + " bytes");

    }

    @Test
    public void test_get_single_url_rx() throws InterruptedException {

        RxJavaPlugins.setErrorHandler(e -> { });

        CountDownLatch latch = new CountDownLatch(1);

        long start = System.currentTimeMillis();
        rxFetchUrls()
            .map(String::length)
            .doOnComplete(() -> latch.countDown())
            .subscribe(System.out::println);

        latch.await();
        System.out.println("Total: " + (System.currentTimeMillis() - start));
        //Thread.sleep(10_000);
    }


    private Observable<String> rxFetchUrls() {
        return Observable.fromArray(urlsWithError.split(","))
                .observeOn(Schedulers.io())
                .map(ExternalService::new)
                .flatMap(externalService ->
                    Observable.fromCallable(() -> externalService.get())
                            .subscribeOn(Schedulers.io())
                            .timeout(2, TimeUnit.SECONDS)
                            .onErrorReturnItem(""),
            10);

    }
}
