package se.flapsdown.rxlab.samples;

import io.reactivex.Observable;
import org.junit.Test;
import se.flapsdown.rxlab.util.Streams;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class RegisterTwiceTest {


    @Test
    public void test_subscribe_twice() {

        final AtomicInteger count = new AtomicInteger();

        Observable<String> johan = Observable.just("Johan")
            .map(name -> name.toUpperCase())
            .doOnNext(name -> count.incrementAndGet())
            .doOnNext(Streams::print);

        johan.subscribe();
        johan.subscribe();

        assertThat(count.get()).isEqualTo(2);

    }
}
