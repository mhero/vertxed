package com.mac.rx.verticle;

import io.vertx.core.http.HttpMethod;

import java.util.HashSet;
import java.util.Set;

public class HttpMethods {
    static final Set<HttpMethod> allowedMethods = new HashSet<>();

    static {
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.DELETE);
        allowedMethods.add(HttpMethod.PATCH);
        allowedMethods.add(HttpMethod.PUT);

    }
}
