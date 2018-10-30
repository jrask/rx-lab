package se.flapsdown.rxlab;

import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class WithRxTest extends AbstractTest {


    CountDownLatch latch = new CountDownLatch(1);

    @Before
    public void prepare() {
        latch = new CountDownLatch(1);
    }


    @Test
    public void test_with_rx_blocking() throws InterruptedException {

        String res = longRunningTask()
                .doOnNext(s -> print("doOnNext() " + s))
                .blockingFirst();
        print(res);

    }
    

    @Test
    public void test_with_rx() throws InterruptedException {

        longRunningTask()
                .doOnNext(s -> print("doOnNext() " + s))
                .subscribe(this::done);

        latch.await();
    }

    
    Observable<String> longRunningTask() {
        return Observable
                .fromCallable(() -> new LongRunningTask().runFor(1_000));
    }

    private void done(String s) {
        print(s);
        latch.countDown();
    }
}
