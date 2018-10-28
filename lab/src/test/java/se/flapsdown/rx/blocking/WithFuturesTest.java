package se.flapsdown.rx.blocking;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.*;

import static org.awaitility.Awaitility.await;

public class WithFuturesTest extends AbstractTest {

    static ExecutorService executorService;

    @BeforeClass
    public static void setup() {
        executorService = Executors.newCachedThreadPool();
    }

    @AfterClass
    public static void close() throws InterruptedException {
        executorService.shutdownNow();
        executorService.awaitTermination(2, TimeUnit.SECONDS);
    }


    @Test
    public void test_with_1future() throws ExecutionException, InterruptedException {
        Future<String> res = executorService.submit(() -> new LongRunningTask().runFor(1_000));
        print(res.get()); // blocks
    }

    @Test
    public void test_with_2completable_future() throws ExecutionException, InterruptedException {

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> new LongRunningTask().runFor(1_000))
                .thenApply(s -> "Completed Future Sleep was " + s)
                .thenCompose(s -> CompletableFuture.completedFuture(""))
                .thenAccept(this::print);

        print("This will be printed before result of operation");

        voidCompletableFuture.get(); // Only for test not to end before result is finished

    }


}
