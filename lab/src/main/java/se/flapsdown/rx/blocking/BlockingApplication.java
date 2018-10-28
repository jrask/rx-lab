package se.flapsdown.rx.blocking;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.flapsdown.rx.Config;

import java.util.Optional;

public class BlockingApplication {

    private static final Logger LOG = LoggerFactory.getLogger(BlockingApplication.class);

    // Q: How can we run these in parallel?
    // Q: How can we handle error and continue nicely?
    // Q: How can we prevent long running connection

    static String imperativeGet() {

        long start = System.currentTimeMillis();
        long bytes = 0;
        for (String url : Config.urls()) {
            ExternalService externalService = new ExternalService(url);
            String data = externalService.get();
            bytes += data.length();
        }
        return  "Total bytes = " + bytes + " in " + (System.currentTimeMillis() - start) + " ms\n";
    }


    static String functionalGet() {

        long start = System.currentTimeMillis();
        Optional<Integer> bytes = Config.urls()
            .stream()
            .map(ExternalService::new)
            .map(ExternalService::get)
            .map(String::length)
            .reduce((integer, integer2) -> integer + integer2);

        return  "Total bytes = " + bytes.get() + " in " + (System.currentTimeMillis() - start) + " ms\n";
    }

    static String rxSyncGet() {
        long start = System.currentTimeMillis();

        Maybe<Integer> reduce = Observable.fromIterable(Config.urls())
            .map(ExternalService::new)
            .map(externalService -> externalService.get())
            .map(String::length)
            .reduce((integer, integer2) -> integer + integer2);

        return  "Total bytes = " + reduce.blockingGet() + " in " + (System.currentTimeMillis() - start) + " ms\n";

    }

    static String rxAsyncGet() {
        

        long start = System.currentTimeMillis();

        Maybe<Integer> reduce = Observable.fromIterable(Config.urls())
            .map(ExternalService::new)
            .flatMap(externalService ->
                externalService.rxGet()
                    .subscribeOn(Schedulers.io()))
            .map(String::length)
            .reduce((integer, integer2) -> integer + integer2);

        return  "Total bytes = " + reduce.blockingGet() + " in " + (System.currentTimeMillis() - start) + " ms\n";

    }
}
