package com.mac.rx.movie;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import static com.mac.rx.movie.MovieRepository.COLLECTION;

public class MovieFixture {

	public static void createMovie(MongoClient mongo, Handler<AsyncResult<Void>> next, Promise<Void> fut) {
		Movie shawshank = new Movie("The Shawshank Redemption", "9.3");
		Movie godfather = new Movie("The Godfather", "9.2");

		mongo.count(COLLECTION, new JsonObject()).compose(count -> {
			if (count == 0) {
				return mongo.insert(COLLECTION, shawshank.toJson())
						.compose(_ -> mongo.insert(COLLECTION, godfather.toJson())).mapEmpty(); // Returns Future<Void>
			} else {
				return Future.succeededFuture();
			}
		}).onSuccess(_ -> next.handle(Future.succeededFuture())).onFailure(err -> fut.fail(err));
	}
}
