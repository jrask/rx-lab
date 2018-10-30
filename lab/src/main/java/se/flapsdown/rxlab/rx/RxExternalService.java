package se.flapsdown.rxlab.rx;

import io.reactivex.Observable;
import okhttp3.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RxExternalService {

    private static final OkHttpClient client = new OkHttpClient();

    private final String endpoint;

    static Random random = new Random();

    public RxExternalService(String endpoint) {
        this.endpoint = endpoint;
    }

    public Observable<String> rxGet() {
        System.out.println("get(" + endpoint + ") on " + Thread.currentThread().getName());
        Request request = new Request.Builder()
            .url(endpoint)
            .get()
            .build();

        return Observable.create(emitter -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        emitter.onNext(response.body().string());
                        emitter.onComplete();
                    }
                });
            }  catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
