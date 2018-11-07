package se.flapsdown.rxlab.four.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import se.flapsdown.rxlab.four.Config;
import se.flapsdown.rxlab.four.HttpResult;
import se.flapsdown.rxlab.four.blocking.ExternalService;
import se.flapsdown.rxlab.util.Streams;

import java.util.concurrent.TimeUnit;

public class RxApplication {


    static {
        RxJavaPlugins.setErrorHandler(Throwable::printStackTrace);
    }

     Observable<HttpResult> rxSyncGet() {

        return Observable.fromIterable(Config.services())
            .map(ExternalService::new)
            .map(ExternalService::get);

    }

    Observable<HttpResult> rxAsyncGet() {

        return Observable.fromIterable(Config.services())
            .map(RxExternalService::new)
            .flatMap(rxExternalService ->
                rxExternalService.rxGet()
                    .subscribeOn(Schedulers.io())
                    .timeout(6, TimeUnit.SECONDS)
                    .retryWhen(withLinearBackoff(3, 1))
                    .onErrorReturnItem(new HttpResult(rxExternalService.endpoint, "", -1))
            );

    }

    private Function<? super Observable<Throwable>, ? extends ObservableSource<?>> withLinearBackoff(int retries, int backoffTime) {
        return (Function<Observable<Throwable>, ObservableSource<?>>) errors ->
            errors

                // Use a count
                .map(error -> 1)

                // "Sum" the number of retries
                .scan((integer, integer2) -> integer + integer2)

                .doOnNext(errorCnt -> Streams.print("Retry attempt " + errorCnt))

                // This will cause an error, which means that we can use onErrorReturn*
                .flatMap(errorCount -> errorCount >= retries ? Observable.error(RuntimeException::new) : Observable.just(errorCount))

                // Limit the maximum number of retries.
                // This will simply skip retry and not throw an error
                //.takeWhile(errorCount -> errorCount < retries)



                // Signal resubscribe event after some delay.
                .flatMap(errorCount -> Observable.timer(errorCount * backoffTime, TimeUnit.SECONDS));
    }
}
