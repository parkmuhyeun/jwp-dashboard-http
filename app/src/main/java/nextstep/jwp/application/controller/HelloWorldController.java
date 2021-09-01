package nextstep.jwp.application.controller;

import nextstep.jwp.webserver.AbstractController;
import nextstep.jwp.webserver.HttpRequest;
import nextstep.jwp.webserver.HttpResponse;
import nextstep.jwp.webserver.StatusCode;

public class HelloWorldController extends AbstractController {

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) throws Exception {
        response.setStatusCode(StatusCode._200_OK);
        response.setBody("Hello world!");
    }
}
