package se.flapsdown.rxlab.four.rx;

import static spark.Spark.get;


public class RxRestService {


    public static void main(String[] args) {

        RxApplication application = new RxApplication();

        get("/rxasync", (req, res) -> "Total bytes " +
            application.rxAsyncGet().reduce((integer, integer2) -> integer + integer2));

        get("/rxsync", (req, res) ->  "Total bytes " +
            application.rxSyncGet().reduce((integer, integer2) -> integer + integer2));

    }



}
