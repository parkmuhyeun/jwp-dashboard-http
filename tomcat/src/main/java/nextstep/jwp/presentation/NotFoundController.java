package nextstep.jwp.presentation;

import org.apache.coyote.http.HttpRequest;
import org.apache.coyote.http.HttpResponse;
import org.apache.coyote.http.HttpResponseBuilder;

import java.io.IOException;

public class NotFoundController implements Controller {

    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        HttpResponseBuilder.buildStaticFileNotFoundResponse(httpRequest, httpResponse);
    }
}
