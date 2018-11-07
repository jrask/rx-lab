package se.flapsdown.rxlab.four;

public class HttpResult {


    public final Config.Service service;
    public final String data;
    public final long ms;


    public HttpResult(Config.Service service, String data, long ms) {
        this.service = service;
        this.data = data;
        this.ms = ms;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
            "service=" + service.url +
            ", bytes='" + data.length() + '\'' +
            ", ms=" + ms +
            '}';
    }
}
