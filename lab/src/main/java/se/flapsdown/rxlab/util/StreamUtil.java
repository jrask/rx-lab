package se.flapsdown.rxlab.util;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class StreamUtil {

    static class QuietObserver implements Observer {

        @Override
        public void onSubscribe(Disposable disposable) {

        }

        @Override
        public void onNext(Object o) {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete() {

        }
    }

    public static class PrintErrorObserver extends QuietObserver {

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static Observer subscribeQuiet() {
        return new QuietObserver();
    }


    // Useful as onSuccess() callback
    public static void success() {
        System.out.println("Alles gut!");
    }

    public static void print(Object o) {
        System.out.println(o);
    }

    public static class Pair<T1, T2> {

        public final T1 t1;
        public final T2 t2;

        public static <T1,T2> Pair of (T1 t1, T2 t2) {
            return new Pair(t1,t2);
        }

        private Pair(T1 t1, T2 t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        @Override
        public String toString() {
            return t1.toString() + " - " + t2.toString();
        }
    }
}
