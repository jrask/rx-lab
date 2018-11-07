package se.flapsdown.rxlab.util;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Streams {

    public static final Logger LOG = LoggerFactory.getLogger("rx-lab");

    public static void delay(long delay, TimeUnit unit) {
        try {
            unit.sleep(delay);
        } catch (InterruptedException e) {
            System.out.println("we are interrupted");
            Thread.currentThread().interrupt();
        }
    }

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

    public static class PrintObserver extends QuietObserver {

        CountDownLatch latch = new CountDownLatch(1);

        public void awaitCompletion() {
            try {
                latch.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onNext(Object o) {
            LOG.info("Subscriber.onNext() {}", o.toString());
        }

        @Override
        public void onComplete() {
            LOG.info("Subscriber.onComplete()");
            latch.countDown();
        }
    }

    public static Observer subscribeQuiet() {
        return new QuietObserver();
    }


    public static Logger logger() {
        return LOG;
    }

    // Useful as onSuccess() callback
    public static void success() {
        System.out.println("Alles gut!");
    }

    public static void print(Object o) {
        LOG.info(o.toString());
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
