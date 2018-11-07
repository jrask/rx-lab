package se.flapsdown.rxlab;

import se.flapsdown.rxlab.util.Streams;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public class LongRunningTask {


    private final long delay;


    public LongRunningTask(long delay) {
        this.delay = delay;
    }


    public String toUpperCase(String s) {


        toUpperCase(s, s1 -> System.out.println(s1));

        Streams.print("sleep " + delay);
        Streams.delay(delay, TimeUnit.MILLISECONDS);
        return s.toUpperCase();
    }

    public void toUpperCase(String s, Consumer<String> function) {
        Streams.print("sleep " + delay);
        Streams.delay(delay, TimeUnit.MILLISECONDS);
        function.accept(s.toUpperCase());
    }
}
