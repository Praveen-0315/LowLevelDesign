package com.praveendocoding.lowleveldesign.patterns.builder;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    // Required
    private final String url;

    // Optional
    private final String method;
    private final Map<String, String> headers;
    private final Map<String, String> queryParams;
    private final String body;
    private final int timeout;

    // private constructor
    private HttpRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = Map.copyOf(builder.headers);
        this.queryParams = Map.copyOf(builder.queryParams);
        this.body = builder.body;
        this.timeout = builder.timeout;
    }

    public String getUrl() { return url; }
    public String getMethod() { return method; }
    public Map<String, String> getHeaders() { return headers; }
    public Map<String, String> getQueryParams() { return queryParams; }
    public String getBody() { return body; }
    public int getTimeout() { return timeout; }

    @Override
    public String toString() {
        return "HttpRequest {url='" + url + "', method='" + method + "', headers=" + headers + ", queryParams=" + queryParams
                + ", body=" + body + ", timeout=" + timeout + "}";
    }

    public static class Builder {
        // Required
        private final String url;

        // Optional
        private String method = "GET";
        private Map<String, String> headers = new HashMap<>();
        private Map<String, String> queryParams = new HashMap<>();
        private String body;
        private int timeout = 30000;

        public Builder(String url) {
            this.url = url;
        }

        public Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder setHeaders(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder setQueryParams(String key, String value) {
            this.queryParams.put(key, value);
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Builder setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public HttpRequest build(){
            return new HttpRequest(this);
        }
    }
}


class Main{
    public static void main(String[] args) {
        HttpRequest get = new HttpRequest.Builder("https://api.example.com/users")
                .build();

        System.out.println(get);

        HttpRequest post = new HttpRequest.Builder("https://api.example.com/users")
                .setMethod("POST")
                .setHeaders("Content-Type", "application/json")
                .setBody("{\\\"name\\\":\\\"Alice\\\",\\\"email\\\":\\\"alice@example.com\\\"}")
                .setTimeout(50000)
                .build();

        System.out.println(post);

        HttpRequest put = new HttpRequest.Builder("https://api.example.com/config")
                .setMethod("PUT")
                .setHeaders("Content-Type", "application/json")
                .setHeaders("Authorization", "Basic YTpi")
                .setQueryParams("env", "production")
                .setQueryParams("version", "2")
                .setBody("{\"feature_flag\":true}")
                .setTimeout(40000)
                .build();


        System.out.println(put);
    }
}