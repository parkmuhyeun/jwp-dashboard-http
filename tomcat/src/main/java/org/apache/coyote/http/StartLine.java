package org.apache.coyote.http;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class StartLine {

    private static final String START_LINE_DELIMITER = " ";
    private static final String QUERY_PARAMETER_DELIMITER_REGEX = "\\?";
    private static final String QUERY_PARAMETER_DELIMITER = "?";
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;
    private static final String KEY_VALUE_DELIMITER = "=";

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> queryStrings;
    private final HttpProtocol protocol;

    public StartLine(String startLine) {
        this.method = HttpMethod.from(divideMethod(startLine));
        String pathWithQueryString = dividePathWithQueryString(startLine);
        this.path = dividePath(pathWithQueryString);
        this.queryStrings = divideQueryString(pathWithQueryString);
        this.protocol = HttpProtocol.from(divideProtocol(startLine));
    }

    private static String dividePathWithQueryString(String startLine) {
        return startLine.split(START_LINE_DELIMITER)[1];
    }

    private String divideMethod(String startLine) {
        return startLine.split(START_LINE_DELIMITER)[0];
    }

    public String dividePath(String pathWithQueryString) {
        if (!pathWithQueryString.contains(QUERY_PARAMETER_DELIMITER)) {
            return pathWithQueryString;
        }
        return pathWithQueryString.split(QUERY_PARAMETER_DELIMITER_REGEX)[0];
    }

    private Map<String, String> divideQueryString(String pathWithQueryString) {
        if (!pathWithQueryString.contains(QUERY_PARAMETER_DELIMITER)) {
            return Collections.emptyMap();
        }
        String queryString = pathWithQueryString.split(QUERY_PARAMETER_DELIMITER_REGEX)[1];
        return Arrays.stream(queryString.split("&"))
                .map(line -> line.split(KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(line -> line[KEY_INDEX], line -> line[VALUE_INDEX]));
    }

    private String divideProtocol(String startLine) {
        return startLine.split(START_LINE_DELIMITER)[2];
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryStrings() {
        return queryStrings;
    }

    public HttpProtocol getProtocol() {
        return protocol;
    }
}
