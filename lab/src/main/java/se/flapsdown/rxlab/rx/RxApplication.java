package se.flapsdown.rxlab.rx;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import se.flapsdown.rxlab.Config;
import se.flapsdown.rxlab.blocking.ExternalService;

public class RxApplication {


     Observable<Integer> rxSyncGet() {

        return Observable.fromIterable(Config.services())
            .map(ExternalService::new)
            .map(ExternalService::get)
            .map(String::length);

    }

    Observable<Integer> rxAsyncGet() {

        return Observable.fromIterable(Config.services())
            .map(RxExternalService::new)
            .flatMap(rxExternalService ->
                rxExternalService.rxGet()
                    .subscribeOn(Schedulers.io()))
            .map(String::length);
    }
}
