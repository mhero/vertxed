package com.mac.rx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import java.util.HashSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MyFirstVerticle extends AbstractVerticle {

    public static final String COLLECTION = "movies";
    private MongoClient mongo;

    @Override
    public void start(Future<Void> fut) {

        mongo = MongoClient.createShared(vertx, config());

        createSomeData(
                (nothing) -> startWebApp(
                        (http) -> completeStartup(http, fut)
                ), fut);
    }

    private void startWebApp(Handler<AsyncResult<HttpServer>> next) {
        Router router = Router.router(vertx);

        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);
        allowMethods.add(HttpMethod.PUT);

        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1>Hello Vert.x 3!</h1>");
        });

        router.route("/assets/*").handler(StaticHandler.create("assets"));

        router.route("/api/movies*").handler(BodyHandler.create());
        router.route("/api/movies*").handler(CorsHandler.create("*")
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));
        router.get("/api/movies").handler(this::getAll);
        router.post("/api/movies").handler(this::addOne);
        router.get("/api/movies/:id").handler(this::getOne);
        router.put("/api/movies/:id").handler(this::updateOne);
        router.delete("/api/movies/:id").handler(this::deleteOne);

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        config().getInteger("http.port", 8080),
                        next::handle
                );
    }

    private void completeStartup(AsyncResult<HttpServer> http, Future<Void> fut) {
        if (http.succeeded()) {
            fut.complete();
        } else {
            fut.fail(http.cause());
        }
    }

    @Override
    public void stop() throws Exception {
        mongo.close();
    }

    private void addOne(RoutingContext routingContext) {
        final Movie whisky = Json.decodeValue(routingContext.getBodyAsString(),
                Movie.class);

        mongo.insert(COLLECTION, whisky.toJson(), r
                -> routingContext.response()
                        .setStatusCode(201)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(whisky.setId(r.result()))));
    }

    private void getOne(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            mongo.findOne(COLLECTION, new JsonObject().put("_id", id), null, ar -> {
                if (ar.succeeded()) {
                    if (ar.result() == null) {
                        routingContext.response().setStatusCode(404).end();
                        return;
                    }
                    Movie whisky = new Movie(ar.result());
                    routingContext.response()
                            .setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(whisky));
                } else {
                    routingContext.response().setStatusCode(404).end();
                }
            });
        }
    }

    private void updateOne(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        JsonObject json = routingContext.getBodyAsJson();
        if (id == null || json == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            mongo.update(COLLECTION,
                    new JsonObject().put("_id", id),
                    new JsonObject()
                            .put("$set", json),
                    v -> {
                        if (v.failed()) {
                            routingContext.response().setStatusCode(404).end();
                        } else {
                            routingContext.response()
                                    .putHeader("content-type", "application/json; charset=utf-8")
                                    .end(Json.encodePrettily(new Movie(id, json.getString("name"), json.getString("rate"))));
                        }
                    });
        }
    }

    private void deleteOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            mongo.removeOne(COLLECTION, new JsonObject().put("_id", id),
                    ar -> routingContext.response().setStatusCode(204).end());
        }
    }

    private void getAll(RoutingContext routingContext) {
        mongo.find(COLLECTION, new JsonObject(), results -> {
            List<JsonObject> objects = results.result();
            List<Movie> movies = objects.stream().map(Movie::new).collect(Collectors.toList());
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(movies));
        });
    }

    private void createSomeData(Handler<AsyncResult<Void>> next, Future<Void> fut) {
        Movie bowmore = new Movie("The Shawshank Redemption", "9.3");
        Movie talisker = new Movie("The Godfather", "9.2");
        System.out.println(bowmore.toJson());

        mongo.count(COLLECTION, new JsonObject(), count -> {
            if (count.succeeded()) {
                if (count.result() == 0) {

                    mongo.insert(COLLECTION, bowmore.toJson(), ar -> {
                        if (ar.failed()) {
                            fut.fail(ar.cause());
                        } else {
                            mongo.insert(COLLECTION, talisker.toJson(), ar2 -> {
                                if (ar2.failed()) {
                                    fut.failed();
                                } else {
                                    next.handle(Future.<Void>succeededFuture());
                                }
                            });
                        }
                    });
                } else {
                    next.handle(Future.<Void>succeededFuture());
                }
            } else {
                // report the error
                fut.fail(count.cause());
            }
        });
    }
}
