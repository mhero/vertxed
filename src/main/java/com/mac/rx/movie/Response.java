package com.mac.rx.movie;

import org.apache.http.HttpStatus;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;


public class Response {

    private static final String APPLICATION_JSON = "application/json; charset=utf-8";

    public void badRequest(RoutingContext routingContext, String message) {
        routingContext.response()
            .setStatusCode(HttpStatus.SC_BAD_REQUEST)
            .putHeader(HttpHeaders.CONTENT_TYPE, "text/plain; charset=utf-8")
            .end(message != null ? message : "Bad Request");
    }

    public void notFound(RoutingContext routingContext, String message) {
        routingContext.response()
            .setStatusCode(HttpStatus.SC_NOT_FOUND)
            .putHeader(HttpHeaders.CONTENT_TYPE, "text/plain; charset=utf-8")
            .end(message != null ? message : "Not Found");
    }

    public void succesful(RoutingContext routingContext, Object object) {
        routingContext.response()
            .setStatusCode(HttpStatus.SC_OK)
            .putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
            .end(Json.encodePrettily(object));
    }

    public void failed(RoutingContext routingContext, int statusCode, String message) {
        routingContext.response()
            .setStatusCode(statusCode)
            .putHeader(HttpHeaders.CONTENT_TYPE, "text/plain; charset=utf-8")
            .end(message != null ? message : "An error occurred");
    }
}
