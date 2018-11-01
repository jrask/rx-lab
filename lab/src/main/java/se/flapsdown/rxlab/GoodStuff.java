package se.flapsdown.rxlab;

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
