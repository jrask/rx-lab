package se.flapsdown.rxlab;


import java.util.List;

import static java.util.Arrays.asList;

public class Config {


    public enum Service {

        LOCALHOST  ("http://localhost",        3_000),
        SHB        ("http://handelsbanken.se", 2_000),
        GOOGLE     ("http://google.com",       1_000);

        public String url;
        public long delay;

        Service(String s, long i) {
            url = s;
            delay = i;
        }
    }


    public static List<Service> services() {
        return asList(Service.values());
    }
}
