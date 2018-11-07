package se.flapsdown.rxlab.four.blocking;

import static spark.Spark.*;


public class RestService {


    Application application = new Application();

    {
        get("/imperative", (req, res) -> {
                long start = System.currentTimeMillis();

                return "Total bytes " + application.imperativeGet() + "\n" +
                    "Total time " + (System.currentTimeMillis() - start) + "ms\n";
        }
        );

        get("/stream", (req, res) ->
        {
            long start = System.currentTimeMillis();

            return "Total bytes " + application.functionalGet() + "\n" +
                "Total time " + (System.currentTimeMillis() - start) + "ms\n";
        });
    }


    public static void main(String[] args) {
        new RestService();
    }

}
