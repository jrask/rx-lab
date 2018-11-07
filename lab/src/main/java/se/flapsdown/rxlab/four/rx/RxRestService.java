package se.flapsdown.rxlab.four.rx;

import static spark.Spark.get;


public class RxRestService {


    public static void main(String[] args) {

        RxApplication application = new RxApplication();


        get("/rxasync", (req, res) ->  {
            long start = System.currentTimeMillis();

            return "Total bytes " + application.rxAsyncGet().map(httpRes -> httpRes.data.length())
                .reduce(Math::addExact).blockingGet() + "\n" +
                    "Total time " + (System.currentTimeMillis() - start) + "ms\n";
        });

        get("/rxsync", (req, res) -> {
            long start = System.currentTimeMillis();

            return "Total bytes " + application.rxSyncGet().map(httpRes -> httpRes.data.length()).reduce(Math::addExact).blockingGet() + "\n" +
                    "Total time " + (System.currentTimeMillis() - start) + "ms\n";
        });
    }



}
