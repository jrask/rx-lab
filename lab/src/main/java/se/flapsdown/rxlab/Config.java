package se.flapsdown.rxlab;


import java.util.List;

import static java.util.Arrays.asList;

public class Config {

    public static String URLS = "https://www.google.se,https://www.aftonbladet.se,http://www.handelsbanken.se";

    public static List<String> urls() {
        return asList(URLS.split(","));
    }
}
