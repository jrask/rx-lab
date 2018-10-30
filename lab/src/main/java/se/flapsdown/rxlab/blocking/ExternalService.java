package se.flapsdown.rxlab.blocking;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ExternalService {

   private static final OkHttpClient client = new OkHttpClient();

    private final String endpoint;

    static Random random = new Random();

    public ExternalService(String endpoint) {
        this.endpoint = endpoint;
    }

    public String get() {
        return get(random.nextInt(3000));
    }

    public String get(long sleep) {
        System.out.println("get(" + endpoint + ") on " + Thread.currentThread().getName());
        Request request = new Request.Builder()
                .url(endpoint)
                .get()
                .build();
        try {
            TimeUnit.MILLISECONDS.sleep(1000);

            return client.newCall(request).execute().body().string();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted");
        }
    }

}
