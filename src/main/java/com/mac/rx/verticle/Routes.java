package com.mac.rx.verticle;

import com.mac.rx.movie.MovieRepository;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class Routes {
	public Router createRoutes(Vertx vertx, MovieRepository movieRepository) {
		Router router = Router.router(vertx);

		router.route("/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "text/html").end("<h1>Hello Vert.x 3!</h1>");
		});

		router.route("/assets/*").handler(StaticHandler.create("assets"));

		router.route("/api/movies*").handler(BodyHandler.create());
		router.route("/api/movies*").handler(CorsHandler.create("*").allowedHeaders(Headers.allowedHeaders)
				.allowedMethods(HttpMethods.allowedMethods));
		router.get("/api/movies").handler(movieRepository::getAll);
		router.post("/api/movies").handler(movieRepository::addOne);
		router.get("/api/movies/:id").handler(movieRepository::getOne);
		router.put("/api/movies/:id").handler(movieRepository::updateOne);
		router.delete("/api/movies/:id").handler(movieRepository::deleteOne);
		return router;
	}
}
