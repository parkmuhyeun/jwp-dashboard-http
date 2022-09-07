package org.apache.coyote.http11.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.coyote.http11.request.Request;
import org.apache.coyote.http11.request.RequestBody;
import org.apache.coyote.http11.request.RequestHeaders;
import org.apache.coyote.http11.request.StartLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResponseTest {

    private StartLine startLine = new StartLine("GET /eden HTTP/1.1 ");
    private RequestHeaders requestHeaders = RequestHeaders.of(List.of("JSESSIONID: eden"));
    private RequestBody requestBody = RequestBody.of("name=eden&nickName=king");
    private Request request;

    @BeforeEach
    void setUp() {
        request = new Request(startLine, requestHeaders, requestBody);
    }

    @Test
    void html_파일을_받을_수_있다() throws URISyntaxException, IOException {
        // given
        String body = "index.html";
        ResponseEntity responseEntity = ResponseEntity.body(body);

        // when
        URI uri = getClass().getClassLoader().getResource("static/index.html").toURI();
        String expectedBody = new String(Files.readAllBytes(Paths.get(uri)));
        Response response = Response.of(request, responseEntity);

        // then
        String expectedContentType = "Content-Type: text/" + "html" + ";charset=utf-8 ";
        String expectedContentLength = "Content-Length: " + expectedBody.getBytes().length + " ";
        assertThat(response.asString()).contains(List.of(expectedContentType, expectedContentLength, expectedBody));
    }

    @Test
    void css_컨텐츠_타입을_받을_수_있다() throws URISyntaxException, IOException {
        // given
        String body = "/css/styles.css";
        ResponseEntity responseEntity = ResponseEntity.body(body);

        // when
        URI uri = getClass().getClassLoader().getResource("static/css/styles.css").toURI();
        String expectedBody = new String(Files.readAllBytes(Paths.get(uri)));
        Response response = Response.of(request, responseEntity);

        // then
        String expectedContentType = "Content-Type: text/" + "css" + ";charset=utf-8 ";
        assertThat(response.asString()).contains(List.of(expectedContentType, expectedBody));
    }

    @Test
    void js_컨텐츠_타입을_받을_수_있다() throws URISyntaxException, IOException {
        // given
        String body = "/js/scripts.js";
        ResponseEntity responseEntity = ResponseEntity.body(body);

        // when
        URI uri = getClass().getClassLoader().getResource("static/js/scripts.js").toURI();
        String expectedBody = new String(Files.readAllBytes(Paths.get(uri)));
        Response response = Response.of(request, responseEntity);

        // then
        String expectedContentType = "Content-Type: text/" + "javascript" + ";charset=utf-8 ";
        assertThat(response.asString()).contains(List.of(expectedContentType, expectedBody));
    }

    @Test
    void 문자열을_받을_수_있다() {
        // given
        String body = "eden king";
        ResponseEntity responseEntity = ResponseEntity.body(body);

        // when
        Response response = Response.of(request, responseEntity);

        // then
        String expectedStartLine = "HTTP/1.1 " + 200 + " " + "OK" + " ";
        String expectedCookieHeader = "Set-Cookie: JSESSIONID=";
        assertThat(response.asString()).contains(expectedStartLine, expectedCookieHeader, body);
    }

    @Test
    void redirect인_경우_302를_받을_수_있다() {
        // given
        String body = "redirect:index.html";
        ResponseEntity responseEntity = ResponseEntity.body(body).status(HttpStatus.REDIRECT);

        // when
        Response response = Response.of(request, responseEntity);
        String expected = String.join("\r\n",
                "HTTP/1.1 " + 302 + " " + "FOUND" + " ",
                "Location: index.html ",
                "",
                ""
        );

        // then
        String expectedStartLine = "HTTP/1.1 " + 302 + " " + "FOUND" + " ";
        String expectedLocationHeader = "Location: index.html ";
        assertThat(response.asString()).contains(List.of(expectedStartLine, expectedLocationHeader));
    }
}