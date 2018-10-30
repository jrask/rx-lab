package se.flapsdown.rxlab.blocking;

import static spark.Spark.*;


public class RestService {


    Application application = new Application();

    {
        get("/imperative", (req, res) -> "Total bytes " + application.imperativeGet());


        get("/stream", (req, res) ->  "Total bytes " + application.functionalGet());
    }


    public static void main(String[] args) {
        new RestService();
    }

}
