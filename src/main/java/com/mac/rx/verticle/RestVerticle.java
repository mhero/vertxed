package com.mac.rx.verticle;

import com.mac.rx.movie.MovieRepository;
import com.mac.rx.movie.MovieFixture;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;

public class RestVerticle extends AbstractVerticle {

	private MongoClient mongo;
	private MovieRepository movieRepository;
	private final Integer port;
	private final JsonObject mongoConfig;
	private final ConfigRetriever configRetriever;

	public RestVerticle(Integer port, JsonObject mongoConfig, ConfigRetriever configRetriever) {
		this.port = port;
		this.mongoConfig = mongoConfig;
		this.configRetriever = configRetriever;
	}

	@Override
	public void start(Promise<Void> promise) {

		mongo = MongoClient.createShared(vertx, mongoConfig);
		movieRepository = new MovieRepository(mongo);

		MovieFixture.createMovie(mongo, nothing -> startWebApp(http -> completeStartup(http, promise)), promise);
	}

	private void startWebApp(Handler<AsyncResult<HttpServer>> next) {

		Router router = Router.router(vertx);
		JWTAuth authProvider = new JwtProvider(vertx, configRetriever).createJwtProvider();

		Routes routes = new Routes(router, authProvider, configRetriever);
		vertx.createHttpServer().requestHandler(routes.createRoutes(movieRepository))
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
	public void stop() {
		mongo.close();
	}

}
