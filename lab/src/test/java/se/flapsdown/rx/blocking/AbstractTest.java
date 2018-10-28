package se.flapsdown.rx.blocking;

public class AbstractTest {

    void print(String s) {
        System.out.println("[" + Thread.currentThread() + "] : " + s);
    }
}
