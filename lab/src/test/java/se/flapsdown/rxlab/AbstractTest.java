package se.flapsdown.rxlab;

public class AbstractTest {

    void print(String s) {
        System.out.println("[" + Thread.currentThread() + "] : " + s);
    }
}
