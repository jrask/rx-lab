package se.flapsdown.rxlab.four.rx;

import io.reactivex.Observable;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.flapsdown.rxlab.four.Config;
import se.flapsdown.rxlab.four.HttpResult;
import se.flapsdown.rxlab.four.blocking.ExternalService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static se.flapsdown.rxlab.util.Streams.delay;

public class RxExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(RxExternalService.class);

    private static final OkHttpClient client = new OkHttpClient();

    public final Config.Service endpoint;


    public RxExternalService(Config.Service endpoint) {
        this.endpoint = endpoint;
    }


    public Observable<HttpResult> rxGet() {

        LOG.info("get() {}", endpoint.url);

        Request request = new Request.Builder()
            .url(endpoint.url)
            .get()
            .build();

        return Observable.create(emitter -> {

            long start = System.currentTimeMillis();
            delay(endpoint.delay, TimeUnit.MILLISECONDS);

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    emitter.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    emitter.onNext(new HttpResult(endpoint, response.body().string(), (System.currentTimeMillis() - start)));
                    emitter.onComplete();
                }
            });
        });
    }

    public Observable<HttpResult> rxGetWrap() {
        return Observable.fromCallable(() -> new ExternalService(endpoint).get());
    }
}
