package se.flapsdown.rxlab.four.blocking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.flapsdown.rxlab.four.Config;

import java.util.Optional;

public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    // Q: How can we run these in parallel?
    // Q: How can we handle error and continue nicely?
    // Q: How can we prevent long running connection
    long imperativeGet() {

        long bytes = 0;
        for (Config.Service service : Config.services()) {
            ExternalService externalService = new ExternalService(service);
            String data = externalService.get();
            bytes += data.length();
        }
        return  bytes;
    }


    // Q: How can we translate this into Observable instead of Stream
    //    Single thread is good enough
    long functionalGet() {

        Optional<Integer> bytes = Config.services()
            .stream()
            .map(ExternalService::new)
            .map(ExternalService::get)
            .map(String::length)
            .reduce((integer, integer2) -> integer + integer2);

        return  bytes.isPresent() ? bytes.get() : 0;
    }

}
