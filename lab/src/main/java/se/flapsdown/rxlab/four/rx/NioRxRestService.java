package se.flapsdown.rxlab.four.rx;

import io.reactivex.schedulers.Schedulers;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

public class NioRxRestService extends AbstractVerticle {

    RxApplication application = new RxApplication();

    @Override
    public void start() {
        Router router = Router.router(vertx);

        router.get("/niorx").handler(rc -> {

            HttpServerResponse httpServerResponse = rc.response()
                .setChunked(true)
                .putHeader("content-type", "text/plain");

            application.rxAsyncGet()
                .subscribeOn(Schedulers.io())
                .map(httpResult -> httpResult.toString() + "\n")

                .subscribe(
                    httpServerResponse::write,
                    Throwable::printStackTrace,
                    httpServerResponse::end);
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    public static void main(String args[]) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new NioRxRestService());
    }

}
