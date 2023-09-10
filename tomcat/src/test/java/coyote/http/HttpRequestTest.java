package coyote.http;

import org.apache.coyote.http.HttpMethod;
import org.apache.coyote.http.HttpRequest;
import org.apache.coyote.http.HttpRequestParser;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.apache.coyote.http.HttpMethod.GET;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpRequestTest {

    @Test
    void getMethod() throws IOException {
        //given
        HttpRequest httpRequest = createHttpRequest(RequestFixture.REQUEST);

        //when
        HttpMethod method = httpRequest.getMethod();

        //then
        assertEquals(GET, method);
    }

    private HttpRequest createHttpRequest(String request) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        HttpRequestParser httpRequestParser = new HttpRequestParser();
        return httpRequestParser.convertToHttpRequest(inputStream);
    }

    @Test
    void getPath() throws IOException {
        //given
        HttpRequest httpRequest = createHttpRequest(RequestFixture.REQUEST);

        //when
        String path = httpRequest.getPath();

        //then
        assertEquals("/index.html", path);
    }

    @Test
    void getProtocol() throws IOException {
        //given
        HttpRequest httpRequest = createHttpRequest(RequestFixture.REQUEST);

        //when
        String protocol = httpRequest.getProtocol().getName();

        //then
        assertEquals("HTTP/1.1", protocol);
    }

    @Test
    void getQueryStrings() throws IOException {
        //given
        HttpRequest httpRequest = createHttpRequest(RequestFixture.REQUEST_WITH_QUERY_STRING);

        //when
        Map<String, String> queryStrings = httpRequest.getQueryStrings();

        //then
        assertAll(
                () -> assertEquals("account", queryStrings.get("account")),
                () -> assertEquals("password", queryStrings.get("password"))
        );
    }
}