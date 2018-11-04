package se.flapsdown.rxlab;


import io.reactivex.Observable;

public class Observables {


    public static <T> Observable<T> fromIterable(Iterable<T> iterable) {
        return Observable.create(emitter -> {

            for (T t : iterable) {
                emitter.onNext(t);
            }
            emitter.onComplete();
        });
    }

}
