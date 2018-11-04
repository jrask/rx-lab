package se.flapsdown.rxlab.util;

import java.util.concurrent.TimeUnit;

public abstract class GoodStuff {


    public static void delay(long delay, TimeUnit unit) {
        try {
            unit.sleep(delay);
        } catch (InterruptedException e) {
            System.out.println("we are interrupted");
            Thread.currentThread().interrupt();
        }
    }

}
