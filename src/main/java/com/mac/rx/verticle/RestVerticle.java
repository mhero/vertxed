package com.mac.rx.verticle;

import com.mac.rx.movie.MovieDAO;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class RestVerticle extends AbstractVerticle {

	public static final String COLLECTION = "movies";
	private MongoClient mongo;
	private MovieDAO movieDao;
	private final Integer port;
	private final JsonObject mongoConfig;

	public RestVerticle(Integer port, JsonObject mongoConfig) {
		this.port = port;
		this.mongoConfig = mongoConfig;
	}

	@Override
	public void start(Promise<Void> promise) {

		mongo = MongoClient.createShared(vertx, mongoConfig);
		movieDao = new MovieDAO(mongo);

		movieDao.createFixture((nothing) -> startWebApp((http) -> completeStartup(http, promise)), promise);
	}

	private void startWebApp(Handler<AsyncResult<HttpServer>> next) {

		Routes routes = new Routes();
		vertx.createHttpServer().requestHandler(routes.createRoutes(vertx, movieDao))
				.listen(config().getInteger("http.port", this.port), next::handle);
	}

	private void completeStartup(AsyncResult<HttpServer> http, Promise<Void> fut) {
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

}
