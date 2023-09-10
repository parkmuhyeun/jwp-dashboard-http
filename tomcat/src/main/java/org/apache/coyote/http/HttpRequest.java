package org.apache.coyote.http;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {

    private static final String KEY_VALUE_DELIMITER = "=";
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;

    private StartLine startLine;
    private Map<String, String> header;
    private Map<String, String> cookies;
    private String messageBody;

    public HttpRequest(StartLine startLine, Map<String, String> header, String messageBody) {
        this.startLine = startLine;
        this.header = header;
        cookies = findCookies();
        this.messageBody = messageBody;
    }

    private Map<String, String> findCookies() {
        return header.entrySet().stream()
                .filter(entry -> entry.getKey().equals("Cookie"))
                .map(entry -> entry.getValue().split("; "))
                .flatMap(Arrays::stream)
                .map(line -> line.split(KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(line -> line[KEY_INDEX], line -> line[VALUE_INDEX]));
    }

    public boolean containsCookie(String key) {
        return cookies.containsKey(key);
    }

    public String getCookie(String key) {
        return cookies.get(key);
    }

    public void addHeader(String key, String value) {
        header.put(key, value);
    }

    public HttpMethod getMethod() {
        return startLine.getMethod();
    }

    public String getPath() {
        return startLine.getPath();
    }

    public Map<String, String> getQueryStrings() {
        return startLine.getQueryStrings();
    }

    public HttpProtocol getProtocol() {
        return startLine.getProtocol();
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getMessageBody() {
        return messageBody;
    }
}
