/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.rx.movie;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

import static com.mac.rx.verticle.RestVerticle.COLLECTION;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.HttpStatus;

/**
 *
 * @author marco
 */
@SuppressWarnings("unused")
public class MovieRepository {

	private final MongoClient mongo;
	public static final String CONTENT_TYPE = "content-type";
	public static final String APPLICATION_JSON = "application/json; charset=utf-8";

	public MovieRepository(MongoClient mongo) {
		this.mongo = mongo;
	}

	public void addOne(RoutingContext routingContext) {
		final Movie movie = Json.decodeValue(routingContext.getBodyAsString(), Movie.class);

		mongo.insert(COLLECTION, movie.toJson(), r -> routingContext.response().setStatusCode(HttpStatus.SC_CREATED)
				.putHeader(CONTENT_TYPE, APPLICATION_JSON).end(Json.encodePrettily(movie.setId(r.result()))));
	}

	public void getOne(RoutingContext routingContext) {
		final String id = routingContext.request().getParam("id");
		if (id == null) {
			routingContext.response().setStatusCode(HttpStatus.SC_BAD_REQUEST).end();
		} else {
			mongo.findOne(COLLECTION, new JsonObject().put("_id", id), null, ar -> {
				if (ar.succeeded()) {
					if (ar.result() == null) {
						routingContext.response().setStatusCode(HttpStatus.SC_NOT_FOUND).end();
						return;
					}
					Movie movie = new Movie(ar.result());
					routingContext.response().setStatusCode(HttpStatus.SC_OK).putHeader(CONTENT_TYPE, APPLICATION_JSON)
							.end(Json.encodePrettily(movie));
				} else {
					routingContext.response().setStatusCode(HttpStatus.SC_NOT_FOUND).end();
				}
			});
		}
	}

	public void updateOne(RoutingContext routingContext) {
		final String id = routingContext.request().getParam("id");
		JsonObject json = routingContext.getBodyAsJson();
		if (id == null || json == null) {
			routingContext.response().setStatusCode(HttpStatus.SC_BAD_REQUEST).end();
		} else {
			mongo.updateCollection(COLLECTION, new JsonObject().put("_id", id), new JsonObject().put("$set", json),
					v -> {
						if (v.failed()) {
							routingContext.response().setStatusCode(HttpStatus.SC_NOT_FOUND).end();
						} else {
							routingContext.response().putHeader(CONTENT_TYPE, APPLICATION_JSON).end(
									Json.encodePrettily(new Movie(id, json.getString("name"), json.getString("rate"))));
						}
					});
		}
	}

	public void deleteOne(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		if (id == null) {
			routingContext.response().setStatusCode(HttpStatus.SC_BAD_REQUEST).end();
		} else {
			mongo.removeDocument(COLLECTION, new JsonObject().put("_id", id),
					ar -> routingContext.response().setStatusCode(HttpStatus.SC_NO_CONTENT).end());
		}
	}

	public void getAll(RoutingContext routingContext) {
		mongo.find(COLLECTION, new JsonObject(), results -> {
			List<JsonObject> objects = results.result();
			List<Movie> movies = objects.stream().map(Movie::new).collect(Collectors.toList());
			routingContext.response().putHeader(CONTENT_TYPE, APPLICATION_JSON).end(Json.encodePrettily(movies));
		});
	}

}
