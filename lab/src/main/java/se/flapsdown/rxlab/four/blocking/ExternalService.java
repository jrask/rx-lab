package se.flapsdown.rxlab.four.blocking;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.flapsdown.rxlab.four.Config;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static se.flapsdown.rxlab.util.GoodStuff.delay;

public class ExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalService.class);

    private static final OkHttpClient client = new OkHttpClient();

    private final Config.Service endpoint;

    public ExternalService(Config.Service endpoint) {
        this.endpoint = endpoint;
    }


    // Q: How can we implement this to return an observable instead?
    //    Two ways,
    //     - One simple that wraps the sync flow
    //     - One more advanced that uses the async nature of okhttp
    //
    public String get() {
        LOG.info("get() {}", endpoint.url);

        Request request = new Request.Builder()
                .url(endpoint.url)
                .get()
                .build();
        try {
            delay(endpoint.delay, TimeUnit.MILLISECONDS);
            return client.newCall(request).execute().body().string();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
