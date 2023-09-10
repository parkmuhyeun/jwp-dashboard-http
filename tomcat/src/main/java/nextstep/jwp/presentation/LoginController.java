package nextstep.jwp.presentation;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.UserNotFoundException;
import nextstep.jwp.model.User;
import org.apache.coyote.http.HttpRequest;
import org.apache.coyote.http.HttpResponse;
import org.apache.coyote.http.HttpResponseBuilder;
import org.apache.coyote.http.SessionManager;

import java.io.IOException;
import java.util.UUID;

import static org.apache.coyote.http.HttpMethod.GET;
import static org.apache.coyote.http.HttpMethod.POST;

public class LoginController implements Controller {

    private static final String SESSION_ID = "JSESSIONID";
    private static final String KEY_VALUE_SEPARATOR = "=";

    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (httpRequest.getMethod() == GET) {
            doGet(httpRequest, httpResponse);
            return;
        }
        if (httpRequest.getMethod() == POST) {
            doPost(httpRequest, httpResponse);
            return;
        }
        throw new IllegalArgumentException("지원하지 않는 HTTP Method 입니다.");
    }

    private void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (httpRequest.containsCookie(SESSION_ID) && SessionManager.isAlreadyLogin(httpRequest.getCookie(SESSION_ID))) {
            HttpResponseBuilder.buildStaticFileRedirectResponse(httpRequest, httpResponse, "/index.html");
            return;
        }
        HttpResponseBuilder.buildStaticFileOkResponse(httpRequest, httpResponse, "/login.html");
    }

    private void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String[] splitRequestBody = httpRequest.getMessageBody().split("&");
        String account = splitRequestBody[0].split(KEY_VALUE_SEPARATOR)[1];
        String password = splitRequestBody[1].split(KEY_VALUE_SEPARATOR)[1];
        try {
            User user = InMemoryUserRepository.findByAccount(account).orElseThrow(UserNotFoundException::new);
            addSession(user, httpRequest, httpResponse);
            redirect(password, user, httpRequest, httpResponse);
        } catch (UserNotFoundException e) {
            HttpResponseBuilder.buildStaticFileRedirectResponse(httpRequest, httpResponse, "/401.html");
        }
    }

    private void addSession(User user, HttpRequest httpRequest, HttpResponse httpResponse) {
        String jSessionId = UUID.randomUUID().toString();
        if (!httpRequest.containsCookie(SESSION_ID)) {
            httpResponse.addCookie(SESSION_ID, jSessionId);
        }
        SessionManager.add(jSessionId, user);
    }

    private void redirect(String password, User user, HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (user.checkPassword(password)) {
            HttpResponseBuilder.buildStaticFileRedirectResponse(httpRequest, httpResponse, "/index.html");
            return;
        }
        HttpResponseBuilder.buildStaticFileRedirectResponse(httpRequest, httpResponse, "/401.html");
    }
}
