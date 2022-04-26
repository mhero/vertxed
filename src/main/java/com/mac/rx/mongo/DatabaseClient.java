package com.mac.rx.mongo;

import org.apache.http.HttpStatus;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class DatabaseClient {
	protected MongoClient mongo;
	protected static final String CONTENT_TYPE = "content-type";
	protected static final String APPLICATION_JSON = "application/json; charset=utf-8";

	protected JsonObject findById(String id) {
		return new JsonObject().put("_id", id);
	}

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

	protected String getId(RoutingContext routingContext) {
		return routingContext.request().getParam("id");
	}
}
