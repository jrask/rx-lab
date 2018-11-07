package se.flapsdown.rxlab.four.blocking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.flapsdown.rxlab.four.Config;
import se.flapsdown.rxlab.four.HttpResult;

import java.util.Optional;


/**
 *
 *
 */
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);


    // Q: How can we run these in parallel?
    //    Total time should be ~~ service with max time
    // Q: How can we handle error and continue nicely?
    // Q: How can we handle retries?
    // Q: How can we prevent long running connection
    long imperativeGet() {

        long bytes = 0;
        for (Config.Service service : Config.services()) {
            try {
                ExternalService externalService = new ExternalService(service);
                String data = externalService.get().data;
                bytes += data.length();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return  bytes;
    }


    // Q; Same as above. Which is simplest to fix?

    long functionalGet() {

        Optional<Integer> bytes = Config.services()
            .stream()
            .map(ExternalService::new)
            .map(ExternalService::get)
            .map(httpResult -> httpResult.data)
            .map(String::length)
            .reduce((integer, integer2) -> integer + integer2);

        return  bytes.isPresent() ? bytes.get() : 0;
    }

}
