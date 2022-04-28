package com.mac.rx.movie;

import org.apache.http.HttpStatus;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class Response {

	protected static final String CONTENT_TYPE = "content-type";
	protected static final String APPLICATION_JSON = "application/json; charset=utf-8";

	protected void badRequest(RoutingContext routingContext) {
		routingContext.response().setStatusCode(HttpStatus.SC_BAD_REQUEST).end();
	}

	protected void notFound(RoutingContext routingContext) {
		routingContext.response().setStatusCode(HttpStatus.SC_NOT_FOUND).end();
	}

	protected void succesful(RoutingContext routingContext, Object oject) {
		routingContext.response().setStatusCode(HttpStatus.SC_OK).putHeader(CONTENT_TYPE, APPLICATION_JSON)
				.end(Json.encodePrettily(oject));
	}

}
