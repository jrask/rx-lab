package se.flapsdown.rx.blocking;

import static spark.Spark.*;


public class RestService {


    public static void main(String[] args) {
        get("/imperative", (req, res) -> "Total bytes " + BlockingApplication.imperativeGet());
        get("/stream", (req, res) ->  "Total bytes " + BlockingApplication.functionalGet());
        get("/rxsync", (req, res) ->  "Total bytes " + BlockingApplication.rxSyncGet());
        get("/rxasync", (req, res) ->  "Total bytes " + BlockingApplication.rxAsyncGet());
    }



}
