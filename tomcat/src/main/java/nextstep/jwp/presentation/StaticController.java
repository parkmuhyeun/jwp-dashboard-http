package nextstep.jwp.presentation;

import org.apache.coyote.http.HttpRequest;
import org.apache.coyote.http.HttpResponse;
import org.apache.coyote.http.HttpResponseBuilder;

import java.io.IOException;

import static org.apache.coyote.http.HttpMethod.GET;

public class StaticController implements Controller {

    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (httpRequest.getMethod() == GET) {
            doGet(httpRequest, httpResponse);
            return;
        }
        throw new IllegalArgumentException("지원하지 않는 HTTP Method 입니다.");
    }

    private void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        HttpResponseBuilder.buildStaticFileOkResponse(httpRequest, httpResponse, httpRequest.getPath());
    }
}
