package se.flapsdown.rxlab.blocking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.flapsdown.rxlab.Config;

import java.util.Optional;

public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    // Q: How can we run these in parallel?
    // Q: How can we handle error and continue nicely?
    // Q: How can we prevent long running connection

    String imperativeGet() {

        long start = System.currentTimeMillis();
        long bytes = 0;
        for (String url : Config.urls()) {
            ExternalService externalService = new ExternalService(url);
            String data = externalService.get();
            bytes += data.length();
        }
        return  "Total bytes = " + bytes + " in " + (System.currentTimeMillis() - start) + " ms\n";
    }


    String functionalGet() {

        long start = System.currentTimeMillis();
        Optional<Integer> bytes = Config.urls()
            .stream()
            .map(ExternalService::new)
            .map(ExternalService::get)
            .map(String::length)
            .reduce((integer, integer2) -> integer + integer2);

        return  "Total bytes = " + bytes.get() + " in " + (System.currentTimeMillis() - start) + " ms\n";
    }

}
