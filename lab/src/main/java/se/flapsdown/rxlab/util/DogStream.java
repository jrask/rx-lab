package se.flapsdown.rxlab.util;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

import static java.util.Arrays.asList;

public class DogStream {

    public static List<String> DOGS = asList(
        "Bella",
        "Buster",
        "Pepsi",
        "Daisy",
        "Rocky"
    );

    public static Observable<String> dogs() {
        return  Observable.fromIterable(DOGS);
    }

    public static Single<String> dog() {
        return Single.just("Bella");
    }

    public static Flowable<String> flowDogs() {
        return Flowable.fromIterable(DOGS);
    }

    public Maybe<String> withName(String name) {
        return dogs()
            .filter( n -> n.equals(name))
            .firstElement();
    }
}
