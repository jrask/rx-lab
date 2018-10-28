package se.flapsdown.rx.blocking;

public class LongRunningTask {


    public String runFor(long ms) {
        try {
            System.out.println(String.format("sleep %s on %s", ms, Thread.currentThread()));
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return String.valueOf(ms);
    }
}
